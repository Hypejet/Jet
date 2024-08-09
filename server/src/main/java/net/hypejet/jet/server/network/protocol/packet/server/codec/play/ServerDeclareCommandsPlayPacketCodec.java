package net.hypejet.jet.server.network.protocol.packet.server.codec.play;

import com.mojang.brigadier.arguments.ArgumentType;
import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.play.ServerDeclareCommandsPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerDeclareCommandsPlayPacket.ArgumentNode;
import net.hypejet.jet.protocol.packet.server.play.ServerDeclareCommandsPlayPacket.ChildrenInitializer;
import net.hypejet.jet.protocol.packet.server.play.ServerDeclareCommandsPlayPacket.LiteralNode;
import net.hypejet.jet.protocol.packet.server.play.ServerDeclareCommandsPlayPacket.Node;
import net.hypejet.jet.protocol.packet.server.play.ServerDeclareCommandsPlayPacket.RootNode;
import net.hypejet.jet.protocol.packet.server.play.ServerDeclareCommandsPlayPacket.SuggestionsType;
import net.hypejet.jet.server.command.argument.ArgumentCodec;
import net.hypejet.jet.server.command.argument.ArgumentCodecRegistry;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.arrays.VarIntArrayNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.enums.EnumIdentifierCodec;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.server.util.bytes.ByteUtil;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ServerDeclareCommandsPlayPacket declare commands play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerDeclareCommandsPlayPacket
 * @see PacketCodec
 */
public final class ServerDeclareCommandsPlayPacketCodec extends PacketCodec<ServerDeclareCommandsPlayPacket> {

    private static final int BEGINNING_NODE_INDEX = 0;

    private static final byte LITERAL_TYPE = 0x01;
    private static final byte ARGUMENT_TYPE = 0x02;

    private static final byte EXECUTABLE = 0x04;
    private static final byte HAS_REDIRECT = 0x08;
    private static final byte HAS_SUGGESTIONS_TYPE = 0x10;

    private static final EnumIdentifierCodec<SuggestionsType> SUGGESTIONS_TYPE_CODEC = EnumIdentifierCodec
            .builder(SuggestionsType.class)
            .add(SuggestionsType.ASK_SERVER, Key.key("ask_server"))
            .add(SuggestionsType.ALL_RECIPES, Key.key("all_recipes"))
            .add(SuggestionsType.AVAILABLE_SOUNDS, Key.key("available_sounds"))
            .add(SuggestionsType.SUMMONABLE_ENTITIES, Key.key("summonable_entities"))
            .build();

    /**
     * Constructs the {@linkplain ServerDeclareCommandsPlayPacketCodec declare commands play packet codec}.
     *
     * @since 1.0
     */
    public ServerDeclareCommandsPlayPacketCodec() {
        super(ServerPacketIdentifiers.PLAY_DECLARE_COMMANDS, ServerDeclareCommandsPlayPacket.class);
    }

    @Override
    public @NonNull ServerDeclareCommandsPlayPacket read(@NonNull ByteBuf buf) {
        int nodeCount = VarIntNetworkCodec.instance().read(buf);
        List<RawNode> rawNodes = new ArrayList<>();

        for (int i = 0; i < nodeCount; i++) {
            byte flags = buf.readByte();
            int[] children = VarIntArrayNetworkCodec.instance().read(buf);

            Integer redirect = null;
            String name = null;
            ArgumentType<?> argumentType = null;
            SuggestionsType suggestionsType = null;

            if (ByteUtil.isBitMaskEnabled(flags, HAS_REDIRECT))
                redirect = VarIntNetworkCodec.instance().read(buf);
            if (ByteUtil.isBitMaskEnabled(flags, LITERAL_TYPE) || ByteUtil.isBitMaskEnabled(flags, ARGUMENT_TYPE))
                name = StringNetworkCodec.instance().read(buf);

            if (ByteUtil.isBitMaskEnabled(flags, ARGUMENT_TYPE)) {
                int parserId = VarIntNetworkCodec.instance().read(buf);
                ArgumentCodec<?> codec = ArgumentCodecRegistry.codec(parserId);

                if (codec == null) {
                    throw new IllegalArgumentException("Could not find an argument codec for parser with " +
                            "identifier of " + parserId);
                }

                argumentType = codec.read(buf);

                if (ByteUtil.isBitMaskEnabled(flags, HAS_SUGGESTIONS_TYPE))
                    suggestionsType = SUGGESTIONS_TYPE_CODEC.read(buf);
            }

            rawNodes.add(new RawNode(flags, children, redirect, name, argumentType, suggestionsType));
        }

        int rootNodeIndex = VarIntNetworkCodec.instance().read(buf);

        List<Node> nodes = new ArrayList<>(rawNodes.size());
        for (int index = 0; index < rawNodes.size(); index++)
            deserializedNode(index, rawNodes, nodes);

        Node node = nodes.get(rootNodeIndex);
        if (!(node instanceof RootNode rootNode))
            throw new IllegalArgumentException("The root node index does not reference to a node with type of root");

        return new ServerDeclareCommandsPlayPacket(rootNode);
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerDeclareCommandsPlayPacket object) {
        Deque<Node> nodeQueue = new ArrayDeque<>(Collections.singleton(object.rootNode()));

        Map<Node, Integer> identifiedNodes = new IdentityHashMap<>();
        List<Node> order = new ArrayList<>();

        int currentIndex = BEGINNING_NODE_INDEX;

        while (!nodeQueue.isEmpty()) {
            Node node = nodeQueue.poll();
            if (identifiedNodes.containsKey(node)) continue;

            identifiedNodes.put(node, currentIndex++);
            order.add(node);

            nodeQueue.addAll(node.children());

            Node redirect = node.redirect();
            if (redirect != null) nodeQueue.add(redirect);
        }

        VarIntNetworkCodec.instance().write(buf, identifiedNodes.size());
        for (Node node : order)
            serializeNode(identifiedNodes, node, buf);

        VarIntNetworkCodec.instance().write(buf, BEGINNING_NODE_INDEX);
    }

    private static void serializeNode(@NonNull Map<Node, Integer> identifiedNodes, @NonNull Node node,
                                      @NonNull ByteBuf buf) {
        byte flags = 0;

        if (node.executable()) {
            flags |= EXECUTABLE;
        }

        Node redirect = node.redirect();
        if (redirect != null) {
            flags |= HAS_REDIRECT;
        }

        switch (node) {
            case RootNode ignored -> {}
            case LiteralNode ignored -> flags |= LITERAL_TYPE;
            case ArgumentNode argumentNode -> {
                flags |= ARGUMENT_TYPE;
                if (argumentNode.suggestionsType() != null)
                    flags |= HAS_SUGGESTIONS_TYPE;
            }
        }

        buf.writeByte(flags);

        Collection<Node> children = node.children();
        int[] identifiers = new int[children.size()];

        int currentIndex = 0;

        for (Node child : children) {
            identifiers[currentIndex++] = identifiedNodes.get(child);
        }

        VarIntArrayNetworkCodec.instance().write(buf, identifiers);
        if (redirect != null)
            VarIntNetworkCodec.instance().write(buf, identifiedNodes.get(redirect));

        switch (node) {
            case LiteralNode literalNode -> StringNetworkCodec.instance().write(buf, literalNode.name());
            case ArgumentNode argumentNode -> {
                StringNetworkCodec.instance().write(buf, argumentNode.name());

                ArgumentType<?> argumentType = argumentNode.argumentType();
                Class<?> argumentTypeClass = argumentType.getClass();
                ArgumentCodec<?> codec = ArgumentCodecRegistry.codec(argumentTypeClass);

                if (codec == null) {
                    throw new IllegalArgumentException("Could not find a codec for argument type: "
                            + argumentTypeClass.getSimpleName());
                }

                VarIntNetworkCodec.instance().write(buf, codec.getParserId());
                write(argumentType, buf, codec);

                SuggestionsType suggestionsType = argumentNode.suggestionsType();
                if (suggestionsType != null) SUGGESTIONS_TYPE_CODEC.write(buf, suggestionsType);
            }
            default -> {}
        }
    }

    private static @NonNull Node deserializedNode(int index, @NonNull List<RawNode> rawNodes,
                                                  @NonNull List<Node> nodes) {
        if (nodes.size() > index) {
            Node deserializedNode = nodes.get(index);
            if (deserializedNode != null)
                return deserializedNode;
        }

        RawNode rawNode = rawNodes.get(index);

        byte flags = rawNode.flags();
        boolean executable = ByteUtil.isBitMaskEnabled(flags, EXECUTABLE);

        Node redirect = null;

        Integer redirectId = rawNode.redirect();
        if (redirectId != null)
            redirect = deserializedNode(rawNode.redirect(), rawNodes, nodes);

        String name = rawNode.name();

        ChildrenInitializer childrenInitializer = node -> {
            // Add the node to the list before children initialization to allow them referencing to it
            nodes.add(index, node);

            int[] childrenIds = rawNode.children();
            List<Node> children = new ArrayList<>(childrenIds.length);

            for (int child : childrenIds)
                children.add(deserializedNode(child, rawNodes, nodes));
            return children;
        };

        if (ByteUtil.isBitMaskEnabled(flags, LITERAL_TYPE))
            return new LiteralNode(childrenInitializer, redirect, executable,
                    Objects.requireNonNull(name, "The name must not be null"));

        if (ByteUtil.isBitMaskEnabled(flags, ARGUMENT_TYPE))
            return new ArgumentNode(childrenInitializer, redirect, executable,
                    Objects.requireNonNull(name, "The name must not be null"),
                    Objects.requireNonNull(rawNode.argumentType(), "The argument type must not be null"),
                    rawNode.suggestionsType());

        return new RootNode(childrenInitializer, redirect, executable);
    }

    private static <A extends ArgumentType<?>> void write(@NonNull ArgumentType<?> type, @NonNull ByteBuf buf,
                                                          @NonNull ArgumentCodec<A> codec) {
        codec.write(buf, codec.getArgumentTypeClass().cast(type));
    }

    private record RawNode(byte flags, int @NonNull [] children, @Nullable Integer redirect, @Nullable String name,
                           @Nullable ArgumentType<?> argumentType, @Nullable SuggestionsType suggestionsType) {}
}