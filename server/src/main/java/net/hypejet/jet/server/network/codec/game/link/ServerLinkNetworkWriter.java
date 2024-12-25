package net.hypejet.jet.server.network.codec.game.link;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.data.codecs.util.mapper.Mapper;
import net.hypejet.jet.link.ServerLink;
import net.hypejet.jet.link.label.BuiltinLabel;
import net.hypejet.jet.link.label.ComponentLabel;
import net.hypejet.jet.link.label.ServerLinkLabel;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.codec.game.component.ComponentNetworkWriter;
import net.hypejet.jet.server.network.codec.mapper.MapperNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain ServerLink a server link}.
 *
 * @since 1.0
 * @see ServerLink
 * @see NetworkWriter
 */
public final class ServerLinkNetworkWriter implements NetworkWriter<ServerLink> {

    private static final MapperNetworkCodec<BuiltinLabel, Integer> BUILT_IN_LABEL_CODEC = new MapperNetworkCodec<>(
            Mapper.builder(BuiltinLabel.class, int.class)
                    .register(BuiltinLabel.BUG_REPORT, 0)
                    .register(BuiltinLabel.COMMUNITY_GUIDELINES, 1)
                    .register(BuiltinLabel.SUPPORT, 2)
                    .register(BuiltinLabel.STATUS, 3)
                    .register(BuiltinLabel.FEEDBACK, 4)
                    .register(BuiltinLabel.COMMUNITY, 5)
                    .register(BuiltinLabel.WEBSITE, 6)
                    .register(BuiltinLabel.FORUMS, 7)
                    .register(BuiltinLabel.NEWS, 8)
                    .register(BuiltinLabel.ANNOUNCEMENTS, 9)
                    .build(),
            VarIntNetworkCodec.INSTANCE
    );

    /**
     * An instance of the {@linkplain ServerLinkNetworkWriter server link network writer}.
     *
     * @since 1.0
     */
    public static final ServerLinkNetworkWriter INSTANCE = new ServerLinkNetworkWriter();

    /**
     * An instance of the {@linkplain CollectionNetworkWriter collection network writer}, which writes elements
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