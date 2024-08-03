package net.hypejet.jet.server.network.protocol.codecs.link;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.link.ServerLink;
import net.hypejet.jet.link.label.BuiltinLabel;
import net.hypejet.jet.link.label.ComponentLabel;
import net.hypejet.jet.link.label.ServerLinkLabel;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.component.ComponentNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.enums.EnumVarIntCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

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

    private static final EnumVarIntCodec<BuiltinLabel> BUILT_IN_LABEL_CODEC = EnumVarIntCodec
            .builder(BuiltinLabel.class)
            .add(BuiltinLabel.BUG_REPORT, 0)
            .add(BuiltinLabel.COMMUNITY_GUIDELINES, 1)
            .add(BuiltinLabel.SUPPORT, 2)
            .add(BuiltinLabel.STATUS, 3)
            .add(BuiltinLabel.FEEDBACK, 4)
            .add(BuiltinLabel.COMMUNITY, 5)
            .add(BuiltinLabel.WEBSITE, 6)
            .add(BuiltinLabel.FORUMS, 7)
            .add(BuiltinLabel.NEWS, 8)
            .add(BuiltinLabel.ANNOUNCEMENTS, 9)
            .build();

    private ServerLinkNetworkCodec() {}

    @Override
    public @NonNull ServerLink read(@NonNull ByteBuf buf) {
        ServerLinkLabel label;

        if (buf.readBoolean()) {
            label = BUILT_IN_LABEL_CODEC.read(buf);
        } else {
            label = new ComponentLabel(ComponentNetworkCodec.instance().read(buf));
        }

        return new ServerLink(label, StringNetworkCodec.instance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerLink object) {
        ServerLinkLabel label = object.label();
        buf.writeBoolean(label instanceof BuiltinLabel);

        switch (label) {
            case BuiltinLabel builtinLabel -> BUILT_IN_LABEL_CODEC.write(buf, builtinLabel);
            case ComponentLabel (Component component) -> ComponentNetworkCodec.instance().write(buf, component);
        }

        StringNetworkCodec.instance().write(buf, object.url());
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