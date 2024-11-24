package net.hypejet.jet.server.network.protocol.codecs.key;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkCodec a network codec}, which reads and writes {@linkplain Key a key}
 * from/to two {@linkplain String strings}.
 *
 * @since 1.0
 * @author Codestech
 * @see Key
 * @see String
 * @see NetworkCodec
 */
public final class KeyNetworkCodec implements NetworkCodec<Key> {

    /**
     * An instance of {@linkplain KeyNetworkCodec a key network codec}.
     *
     * @since 1.0
     */
    public static final KeyNetworkCodec INSTANCE = new KeyNetworkCodec();

    private KeyNetworkCodec() {}

    @Override
    public @NonNull Key read(@NonNull ByteBuf buf) {
        return Key.key(StringNetworkCodec.INSTANCE.read(buf), StringNetworkCodec.INSTANCE.read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Key object) {
        StringNetworkCodec.INSTANCE.write(buf, object.namespace());
        StringNetworkCodec.INSTANCE.write(buf, object.value());
    }
}