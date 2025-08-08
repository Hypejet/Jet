package net.hypejet.jet.server.network.codec.game.registry.feature;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.registry.feature.KnownPack;
import net.hypejet.jet.server.network.codec.CombinedNetworkCodec;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkReader;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

/**
 * A {@linkplain NetworkCodec network codec} of {@linkplain KnownPack known packs}.
 *
 * @since 1.0
 * @see KnownPack
 * @see NetworkCodec
 */
public final class KnownPackNetworkCodec implements NetworkCodec<KnownPack> {

    /**
     * An instance of the {@linkplain KnownPackNetworkCodec known pack network-codec}.
     *
     * @since 1.0
     */
    public static final KnownPackNetworkCodec INSTANCE = new KnownPackNetworkCodec();

    /**
     * A {@linkplain NetworkCodec network codec} of {@linkplain Collection collections}
     * of {@linkplain KnownPack known packs}.
     *
     * @since 1.0
     * @see Collection
     */
    public static final NetworkCodec<Collection<KnownPack>> COLLECTION_CODEC = new CombinedNetworkCodec<>(
            new CollectionNetworkReader<>(INSTANCE),
            new CollectionNetworkWriter<>(INSTANCE)
    );

    private KnownPackNetworkCodec() {}

    @Override
    public @NonNull KnownPack read(@NonNull ByteBuf buf) {
        return new KnownPack(
                StringNetworkCodec.INSTANCE.read(buf),
                StringNetworkCodec.INSTANCE.read(buf),
                StringNetworkCodec.INSTANCE.read(buf)
        );
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull KnownPack object) {
        StringNetworkCodec.INSTANCE.write(buf, object.namespace());
        StringNetworkCodec.INSTANCE.write(buf, object.path());
        StringNetworkCodec.INSTANCE.write(buf, object.version());
    }
}