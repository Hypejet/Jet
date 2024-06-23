package net.hypejet.jet.server.network.protocol.codecs.link;

import io.netty.buffer.ByteBuf;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.hypejet.jet.link.ServerLink;
import net.hypejet.jet.link.label.BuiltinLabel;
import net.hypejet.jet.link.label.ComponentLabel;
import net.hypejet.jet.link.label.ServerLinkLabel;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.EnumMap;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain ServerLink server link}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerLink
 * @see NetworkCodec
 */
public final class ServerLinkNetworkCodec implements NetworkCodec<ServerLink> {

    private static final ServerLinkNetworkCodec INSTANCE = new ServerLinkNetworkCodec();

    private static final EnumMap<BuiltinLabel, Integer> builtInLabelToIdMap = new EnumMap<>(BuiltinLabel.class);
    private static final IntObjectMap<BuiltinLabel> idToBuildInLabelMap = new IntObjectHashMap<>();

    static {
        builtInLabelToIdMap.put(BuiltinLabel.BUG_REPORT, 0);
        builtInLabelToIdMap.put(BuiltinLabel.COMMUNITY_GUIDELINES, 1);
        builtInLabelToIdMap.put(BuiltinLabel.SUPPORT, 2);
        builtInLabelToIdMap.put(BuiltinLabel.STATUS, 3);
        builtInLabelToIdMap.put(BuiltinLabel.FEEDBACK, 4);
        builtInLabelToIdMap.put(BuiltinLabel.COMMUNITY, 5);
        builtInLabelToIdMap.put(BuiltinLabel.WEBSITE, 6);
        builtInLabelToIdMap.put(BuiltinLabel.FORUMS, 7);
        builtInLabelToIdMap.put(BuiltinLabel.NEWS, 8);
        builtInLabelToIdMap.put(BuiltinLabel.ANNOUNCEMENTS, 9);
        builtInLabelToIdMap.forEach((label, id) -> idToBuildInLabelMap.put(id, label));
    }

    private ServerLinkNetworkCodec() {}

    @Override
    public @NonNull ServerLink read(@NonNull ByteBuf buf) {
        ServerLinkLabel label;

        if (buf.readBoolean()) {
            int buildInLabelId = NetworkUtil.readVarInt(buf);
            label = idToBuildInLabelMap.get(buildInLabelId);

            if (label == null) {
                throw new IllegalArgumentException("Unknown build-in label identifier: " + buildInLabelId);
            }
        } else {
            label = new ComponentLabel(NetworkUtil.readComponent(buf));
        }

        return new ServerLink(label, NetworkUtil.readString(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerLink object) {
        ServerLinkLabel label = object.label();
        buf.writeBoolean(label instanceof BuiltinLabel);

        switch (label) {
            case BuiltinLabel builtinLabel -> {
                Integer builtInLabelId = builtInLabelToIdMap.get(builtinLabel);
                if (builtInLabelId == null) {
                    throw new IllegalArgumentException("Could not find an identifier for label: " + builtinLabel);
                }
                NetworkUtil.writeVarInt(buf, builtInLabelId);
            }
            case ComponentLabel (Component component) -> NetworkUtil.writeComponent(buf, component);
        }

        NetworkUtil.writeString(buf, object.url());
    }

    /**
     * Gets an instance of the {@linkplain ServerLinkNetworkCodec server link codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull ServerLinkNetworkCodec instance() {
        return INSTANCE;
    }
}