package net.hypejet.jet.server.network.protocol.codecs.pack;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.data.model.api.pack.PackInfo;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.CollectionNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.identifier.IdentifierNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain PackInfo pack info}.
 *
 * @since 1.0
 * @author Codestech
 * @see PackInfo
 * @see NetworkCodec
 */
public final class PackInfoNetworkCodec implements NetworkCodec<PackInfo> {

    private static final PackInfoNetworkCodec INSTANCE = new PackInfoNetworkCodec();
    private static final CollectionNetworkCodec<PackInfo> COLLECTION_CODEC = CollectionNetworkCodec.create(INSTANCE);

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

    /**
     * Gets an instance of the {@linkplain PackInfoNetworkCodec pack info network codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull PackInfoNetworkCodec instance() {
        return INSTANCE;
    }

    /**
     * Gets an instance of a {@linkplain CollectionNetworkCodec collection network codec}, which reads and writes
     * {@linkplain java.util.Collection collections} of {@linkplain PackInfo pack infos}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull CollectionNetworkCodec<PackInfo> collectionCodec() {
        return COLLECTION_CODEC;
    }
}