package net.hypejet.jet.server.network.protocol.codecs.identifier;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.CombinedNetworkCodec;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.collection.CollectionNetworkReader;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain Key key}
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
     * An instance of {@linkplain PackedKeyNetworkCodec a packed identifier network codec}.
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
        return Key.key(StringNetworkCodec.instance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Key object) {
        StringNetworkCodec.instance().write(buf, object.asString());
    }
}