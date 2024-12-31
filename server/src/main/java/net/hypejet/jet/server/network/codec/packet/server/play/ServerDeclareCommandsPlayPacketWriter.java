package net.hypejet.jet.server.network.codec.packet.server.play;

import com.mojang.brigadier.arguments.ArgumentType;
import io.netty.buffer.ByteBuf;
import net.hypejet.jet.data.codecs.util.mapper.Mapper;
import net.hypejet.jet.server.command.argument.ArgumentWriter;
import net.hypejet.jet.server.command.argument.ArgumentWriterRegistry;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.array.varint.VarIntArrayNetworkWriter;
import net.hypejet.jet.server.network.codec.game.key.PackedKeyNetworkCodec;
import net.hypejet.jet.server.network.codec.mapper.MapperNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerDeclareCommandsPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerDeclareCommandsPlayPacket.ArgumentNode;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerDeclareCommandsPlayPacket.LiteralNode;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerDeclareCommandsPlayPacket.Node;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerDeclareCommandsPlayPacket.RootNode;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerDeclareCommandsPlayPacket.SuggestionsType;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerDeclareCommandsPlayPacket a declare commands play packet}.
 *
 * @since 1.0
 * @see ServerDeclareCommandsPlayPacket
 * @see NetworkWriter
 */
public final class ServerDeclareCommandsPlayPacketWriter implements NetworkWriter<ServerDeclareCommandsPlayPacket> {

    /**
     * An instance of the {@linkplain ServerDeclareCommandsPlayPacketWriter server declare commands play packet
     * writer}.
     *
     * @since 1.0
     */
    public static final ServerDeclareCommandsPlayPacketWriter INSTANCE = new ServerDeclareCommandsPlayPacketWriter();

    private static final int BEGINNING_NODE_INDEX = 0;

    private static final byte LITERAL_TYPE = 0x01;
    private static final byte ARGUMENT_TYPE = 0x02;

    private static final byte EXECUTABLE = 0x04;
    private static final byte HAS_REDIRECT = 0x08;
    private static final byte HAS_SUGGESTIONS_TYPE = 0x10;

    private static final MapperNetworkCodec<SuggestionsType, Key> SUGGESTIONS_TYPE_CODEC = new MapperNetworkCodec<>(
            Mapper.builder(SuggestionsType.class, Key.class)
                    .register(SuggestionsType.ASK_SERVER, Key.key("ask_server"))
                    .register(SuggestionsType.ALL_RECIPES, Key.key("all_recipes"))
                    .register(SuggestionsType.AVAILABLE_SOUNDS, Key.key("available_sounds"))
                    .register(SuggestionsType.SUMMONABLE_ENTITIES, Key.key("summonable_entities"))
                    .build(),
            PackedKeyNetworkCodec.INSTANCE
    );

    private ServerDeclareCommandsPlayPacketWriter() {}

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
            if (redirect != null)
                nodeQueue.add(redirect);
        }

        VarIntNetworkCodec.INSTANCE.write(buf, identifiedNodes.size());
        for (Node node : order)
            serializeNode(identifiedNodes, node, buf);

        VarIntNetworkCodec.INSTANCE.write(buf, BEGINNING_NODE_INDEX);
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

        VarIntArrayNetworkWriter.INSTANCE.write(buf, identifiers);
        if (redirect != null)
            VarIntNetworkCodec.INSTANCE.write(buf, identifiedNodes.get(redirect));

        switch (node) {
            case LiteralNode literalNode -> StringNetworkCodec.INSTANCE.write(buf, literalNode.name());
            case ArgumentNode argumentNode -> {
                StringNetworkCodec.INSTANCE.write(buf, argumentNode.name());

                ArgumentType<?> argumentType = argumentNode.argumentType();
                Class<?> argumentTypeClass = argumentType.getClass();
                ArgumentWriter<?> writer = ArgumentWriterRegistry.writer(argumentTypeClass);

                if (writer == null) {
                    throw new IllegalArgumentException(String.format(
                            "Could not find a network writer for argument type with class name of %s",
                            argumentTypeClass.getSimpleName()
                    ));
                }

                VarIntNetworkCodec.INSTANCE.write(buf, writer.parserId());
                write(argumentType, buf, writer);

                SuggestionsType suggestionsType = argumentNode.suggestionsType();
                if (suggestionsType != null) SUGGESTIONS_TYPE_CODEC.write(buf, suggestionsType);
            }
            case RootNode ignored -> {}
        }
    }

    private static <A extends ArgumentType<?>> void write(@NonNull ArgumentType<?> type, @NonNull ByteBuf buf,
                                                          @NonNull ArgumentWriter<A> codec) {
        codec.write(buf, codec.argumentTypeClass().cast(type));
    }
}