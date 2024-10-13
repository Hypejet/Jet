package net.hypejet.jet.server.network.protocol.codecs.coordinate;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.data.model.api.coordinate.Vector;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain Vector vector}.
 *
 * @since 1.0
 * @author Codestech
 * @see Vector
 * @see NetworkCodec
 */
public final class VectorNetworkCodec implements NetworkCodec<Vector> {

    private static final VectorNetworkCodec INSTANCE = new VectorNetworkCodec();

    private VectorNetworkCodec() {}

    @Override
    public @NonNull Vector read(@NonNull ByteBuf buf) {
        return new Vector(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Vector object) {
        buf.writeDouble(object.x());
        buf.writeDouble(object.y());
        buf.writeDouble(object.z());
    }

    /**
     * Gets an instance of the {@linkplain VectorNetworkCodec vector network codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull VectorNetworkCodec instance() {
        return INSTANCE;
    }
}