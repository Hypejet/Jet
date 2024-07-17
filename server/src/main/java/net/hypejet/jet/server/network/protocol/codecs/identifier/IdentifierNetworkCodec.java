package net.hypejet.jet.server.network.protocol.codecs.identifier;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain Key key}
 * from/to two {@linkplain String strings}.
 *
 * @since 1.0
 * @author Codestech
 * @see Key
 * @see String
 * @see NetworkCodec
 */
public final class IdentifierNetworkCodec implements NetworkCodec<Key> {

    private static final IdentifierNetworkCodec INSTANCE = new IdentifierNetworkCodec();

    private IdentifierNetworkCodec() {}

    @Override
    public @NonNull Key read(@NonNull ByteBuf buf) {
        return Key.key(StringNetworkCodec.instance().read(buf), StringNetworkCodec.instance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Key object) {
        StringNetworkCodec.instance().write(buf, object.namespace());
        StringNetworkCodec.instance().write(buf, object.value());
    }

    /**
     * Gets an instance of the {@linkplain IdentifierNetworkCodec identifier network codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull IdentifierNetworkCodec instance() {
        return INSTANCE;
    }
}