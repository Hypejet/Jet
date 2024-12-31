package net.hypejet.jet.server.network.codec.game.pack;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.data.model.api.pack.PackInfo;
import net.hypejet.jet.server.network.codec.CombinedNetworkCodec;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkReader;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.codec.game.key.KeyNetworkCodec;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

/**
 * Represents {@linkplain NetworkCodec a network codec}, which reads and writes {@linkplain PackInfo a pack info}.
 *
 * @since 1.0
 * @see PackInfo
 * @see NetworkCodec
 */
public final class PackInfoNetworkCodec implements NetworkCodec<PackInfo> {

    /**
     * An instance of the {@linkplain PackInfoNetworkCodec pack info network writer}.
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
        return new PackInfo(KeyNetworkCodec.INSTANCE.read(buf), StringNetworkCodec.INSTANCE.read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull PackInfo object) {
        KeyNetworkCodec.INSTANCE.write(buf, object.key());
        StringNetworkCodec.INSTANCE.write(buf, object.version());
    }
}