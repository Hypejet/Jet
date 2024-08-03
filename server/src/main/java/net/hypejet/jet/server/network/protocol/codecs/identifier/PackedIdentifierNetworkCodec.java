package net.hypejet.jet.server.network.protocol.codecs.identifier;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.CollectionNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain Key identifier}
 * from/to a single {@linkplain String string}.
 *
 * @since 1.0
 * @author Codestech
 * @see Key
 * @see String
 * @see NetworkCodec
 */
public final class PackedIdentifierNetworkCodec implements NetworkCodec<Key> {

    private static final PackedIdentifierNetworkCodec INSTANCE = new PackedIdentifierNetworkCodec();
    private static final CollectionNetworkCodec<Key> COLLECTION_CODEC = CollectionNetworkCodec.create(INSTANCE);

    private PackedIdentifierNetworkCodec() {}

    @Override
    public @NonNull Key read(@NonNull ByteBuf buf) {
        return Key.key(StringNetworkCodec.instance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Key object) {
        StringNetworkCodec.instance().write(buf, object.asString());
    }

    /**
     * Gets an instance of the {@linkplain PackedIdentifierNetworkCodec identifier network codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull PackedIdentifierNetworkCodec instance() {
        return INSTANCE;
    }

    /**
     * Gets an instance of a {@linkplain CollectionNetworkCodec collection network codec}, which reads and writes
     * a {@linkplain java.util.Collection collection} of {@linkplain Key identifiers}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull CollectionNetworkCodec<Key> collectionCodec() {
        return COLLECTION_CODEC;
    }
}