package net.hypejet.jet.server.network.protocol.codecs.pack;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.data.model.api.pack.PackInfo;
import net.hypejet.jet.server.network.codec.CombinedNetworkCodec;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.collection.CollectionNetworkReader;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.identifier.IdentifierNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

/**
 * Represents {@linkplain NetworkCodec a network codec}, which reads and writes {@linkplain PackInfo a pack info}.
 *
 * @since 1.0
 * @author Codestech
 * @see PackInfo
 * @see NetworkCodec
 */
public final class PackInfoNetworkCodec implements NetworkCodec<PackInfo> {

    /**
     * An instance of {@linkplain PackInfoNetworkCodec a pack info network writer}.
     *
     * @since 1.0
     */
    public static final PackInfoNetworkCodec INSTANCE = new PackInfoNetworkCodec();

    /**
     * An instance of {@linkplain NetworkCodec a network codec}, which reads and writes elements with type
     * of {@linkplain Collection collections} of {@linkplain PackInfo a pack info}
     * using {@linkplain PackInfoNetworkCodec a pack info network codec}.
     *
     * @since 1.0
     */
    public static final NetworkCodec<Collection<PackInfo>> COLLECTION_CODEC = new CombinedNetworkCodec<>(
            new CollectionNetworkReader<>(INSTANCE), new CollectionNetworkWriter<>(INSTANCE)
    );

    private PackInfoNetworkCodec() {}

    @Override
    public @NonNull PackInfo read(@NonNull ByteBuf buf) {
        return new PackInfo(IdentifierNetworkCodec.instance().read(buf), StringNetworkCodec.instance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull PackInfo object) {
        IdentifierNetworkCodec.instance().write(buf, object.key());
        StringNetworkCodec.instance().write(buf, object.version());
    }
}