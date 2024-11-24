package net.hypejet.jet.server.network.protocol.codecs.link;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.link.ServerLink;
import net.hypejet.jet.link.label.BuiltinLabel;
import net.hypejet.jet.link.label.ComponentLabel;
import net.hypejet.jet.link.label.ServerLinkLabel;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.component.ComponentNetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.enums.EnumVarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain ServerLink a server link}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerLink
 * @see NetworkWriter
 */
public final class ServerLinkNetworkWriter implements NetworkWriter<ServerLink> {

    private static final EnumVarIntNetworkCodec<BuiltinLabel> BUILT_IN_LABEL_CODEC = EnumVarIntNetworkCodec
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

    /**
     * An instance of {@linkplain ServerLinkNetworkWriter a server link network writer}.
     *
     * @since 1.0
     */
    public static final ServerLinkNetworkWriter INSTANCE = new ServerLinkNetworkWriter();

    /**
     * An instance of {@linkplain CollectionNetworkWriter a collection network writer}, which writes elements
     * with a type of {@linkplain ServerLink server link}.
     *
     * @since 1.0
     */
    public static final CollectionNetworkWriter<ServerLink> COLLECTION_WRITER
            = new CollectionNetworkWriter<>(INSTANCE);

    private ServerLinkNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerLink object) {
        ServerLinkLabel label = object.label();
        buf.writeBoolean(label instanceof BuiltinLabel);

        switch (label) {
            case BuiltinLabel builtinLabel -> BUILT_IN_LABEL_CODEC.write(buf, builtinLabel);
            case ComponentLabel (Component component) -> ComponentNetworkWriter.INSTANCE.write(buf, component);
        }

        StringNetworkCodec.INSTANCE.write(buf, object.url());
    }
}