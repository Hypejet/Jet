package net.hypejet.jet.server.network.protocol.codecs.identifier;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain Key identifier}.
 *
 * @since 1.0
 * @author Codestech
 * @see Key
 * @see NetworkCodec
 */
public final class IdentifierNetworkCodec implements NetworkCodec<Key> {

    private static final IdentifierNetworkCodec INSTANCE = new IdentifierNetworkCodec();

    private IdentifierNetworkCodec() {}

    @Override
    public @NonNull Key read(@NonNull ByteBuf buf) {
        return Key.key(NetworkUtil.readString(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Key object) {
        NetworkUtil.writeString(buf, object.asString());
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