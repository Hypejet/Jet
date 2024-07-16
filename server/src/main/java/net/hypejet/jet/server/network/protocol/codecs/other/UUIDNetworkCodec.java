package net.hypejet.jet.server.network.protocol.codecs.other;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain UUID unique identifier}.
 *
 * @since 1.0
 * @author Codestech
 * @see UUID
 * @see NetworkCodec
 */
public final class UUIDNetworkCodec implements NetworkCodec<UUID> {

    private static final UUIDNetworkCodec INSTANCE = new UUIDNetworkCodec();

    private UUIDNetworkCodec() {}

    @Override
    public @NonNull UUID read(@NonNull ByteBuf buf) {
        return new UUID(buf.readLong(), buf.readLong());
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull UUID object) {
        buf.writeLong(object.getMostSignificantBits());
        buf.writeLong(object.getLeastSignificantBits());
    }

    /**
     * Gets an instance of the {@linkplain UUIDNetworkCodec unique identifier network codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull UUIDNetworkCodec instance() {
        return INSTANCE;
    }
}