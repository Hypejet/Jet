package net.hypejet.jet.server.network.codec.game.key;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.CombinedNetworkCodec;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkReader;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

/**
 * Represents {@linkplain NetworkCodec a network codec}, which reads and writes {@linkplain Key a key}
 * from/to a single {@linkplain String string}.
 *
 * @since 1.0
 * @author Codestech
 * @see Key
 * @see String
 * @see NetworkCodec
 */
public final class PackedKeyNetworkCodec implements NetworkCodec<Key> {

    /**
     * An instance of {@linkplain PackedKeyNetworkCodec a packed key network codec}.
     *
     * @since 1.0
     */
    public static final PackedKeyNetworkCodec INSTANCE = new PackedKeyNetworkCodec();

    /**
     * An instance of {@linkplain NetworkCodec a network codec}, which reads and writes
     * {@linkplain Collection collections} of {@linkplain Key keys} using {@linkplain PackedKeyNetworkCodec a packed
     * key network codec}.
     *
     * @since 1.0
     */
    public static final NetworkCodec<Collection<Key>> COLLECTION_CODEC = new CombinedNetworkCodec<>(
            new CollectionNetworkReader<>(INSTANCE), new CollectionNetworkWriter<>(INSTANCE)
    );

    private PackedKeyNetworkCodec() {}

    @Override
    public @NonNull Key read(@NonNull ByteBuf buf) {
        return Key.key(StringNetworkCodec.INSTANCE.read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Key object) {
        StringNetworkCodec.INSTANCE.write(buf, object.asString());
    }
}