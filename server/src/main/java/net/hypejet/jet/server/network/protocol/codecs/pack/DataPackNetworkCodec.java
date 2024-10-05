package net.hypejet.jet.server.network.protocol.codecs.pack;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.data.model.pack.DataPack;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.CollectionNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.identifier.IdentifierNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain DataPack data pack}.
 *
 * @since 1.0
 * @author Codestech
 * @see DataPack
 * @see NetworkCodec
 */
public final class DataPackNetworkCodec implements NetworkCodec<DataPack> {

    private static final DataPackNetworkCodec INSTANCE = new DataPackNetworkCodec();
    private static final CollectionNetworkCodec<DataPack> COLLECTION_CODEC = CollectionNetworkCodec.create(INSTANCE);

    private DataPackNetworkCodec() {}

    @Override
    public @NonNull DataPack read(@NonNull ByteBuf buf) {
        return new DataPack(IdentifierNetworkCodec.instance().read(buf), StringNetworkCodec.instance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull DataPack object) {
        IdentifierNetworkCodec.instance().write(buf, object.key());
        StringNetworkCodec.instance().write(buf, object.version());
    }

    /**
     * Gets an instance of the {@linkplain DataPackNetworkCodec data pack network codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull DataPackNetworkCodec instance() {
        return INSTANCE;
    }

    /**
     * Gets an instance of a {@linkplain CollectionNetworkCodec collection network codec}, which reads and writes
     * {@linkplain java.util.Collection collections} of {@linkplain DataPack data packs}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull CollectionNetworkCodec<DataPack> collectionCodec() {
        return COLLECTION_CODEC;
    }
}