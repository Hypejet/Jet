package net.hypejet.jet.server.network.codec.game.key;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.CombinedNetworkCodec;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkReader;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

/**
 * A {@linkplain NetworkCodec network codec} of {@linkplain Key keys}.
 *
 * @since 1.0
 * @see Key
 * @see NetworkCodec
 */
public final class KeyNetworkCodec implements NetworkCodec<Key> {

    /**
     * An instance of the {@linkplain KeyNetworkCodec key network codec}.
     *
     * @since 1.0
     */
    public static final KeyNetworkCodec INSTANCE = new KeyNetworkCodec();

    /**
     * An instance of a {@linkplain NetworkCodec network codec}
     * of {@linkplain Collection collections} of {@linkplain Key keys}.
     *
     * @since 1.0
     */
    public static final NetworkCodec<Collection<Key>> COLLECTION_CODEC = new CombinedNetworkCodec<>(
            new CollectionNetworkReader<>(INSTANCE),
            new CollectionNetworkWriter<>(INSTANCE)
    );

    private KeyNetworkCodec() {}

    @Override
    public @NonNull Key read(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager) {
        return Key.key(StringNetworkCodec.INSTANCE.read(buf, registryManager));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager, @NonNull Key object) {
        StringNetworkCodec.INSTANCE.write(buf, registryManager, object.asString());
    }
}