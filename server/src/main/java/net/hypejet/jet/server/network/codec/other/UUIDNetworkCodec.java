package net.hypejet.jet.server.network.codec.other;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

/**
 * Represents {@linkplain NetworkCodec a network codec}, which reads and writes {@linkplain UUID a unique identifier}.
 *
 * @since 1.0
 * @author Codestech
 * @see UUID
 * @see NetworkCodec
 */
public final class UUIDNetworkCodec implements NetworkCodec<UUID> {

    /**
     * An instance of the {@linkplain UUIDNetworkCodec unique identifier network codec}.
     *
     * @since 1.0
     */
    public static final UUIDNetworkCodec INSTANCE = new UUIDNetworkCodec();

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
}