package net.hypejet.jet.server.network.codec.game.link;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.codec.game.component.ComponentNetworkWriter;
import net.hypejet.jet.server.network.codec.index.IndexNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.hypejet.jet.util.game.link.ServerLink;
import net.hypejet.jet.util.game.link.label.BuiltinLabel;
import net.hypejet.jet.util.game.link.label.ComponentLabel;
import net.hypejet.jet.util.game.link.label.ServerLinkLabel;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain ServerLink a server link}.
 *
 * @since 1.0
 * @see ServerLink
 * @see NetworkWriter
 */
public final class ServerLinkNetworkWriter implements NetworkWriter<ServerLink> {

    private static final IndexNetworkCodec<BuiltinLabel, Integer> BUILT_IN_LABEL_CODEC = new IndexNetworkCodec<>(
            IndexUtil.fromMap(Map.of(
                    0, BuiltinLabel.BUG_REPORT,
                    1, BuiltinLabel.COMMUNITY_GUIDELINES,
                    2, BuiltinLabel.SUPPORT,
                    3, BuiltinLabel.STATUS,
                    4, BuiltinLabel.FEEDBACK,
                    5, BuiltinLabel.COMMUNITY,
                    6, BuiltinLabel.WEBSITE,
                    7, BuiltinLabel.FORUMS,
                    8, BuiltinLabel.NEWS,
                    9, BuiltinLabel.ANNOUNCEMENTS
            )),
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
            default -> throw new IllegalStateException(String.format("Unknown server link label: %s", label));
        }

        StringNetworkCodec.INSTANCE.write(buf, object.url());
    }
}