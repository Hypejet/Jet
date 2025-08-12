package net.hypejet.jet.server.network.codec.game.world.coordinate;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.world.coordinate.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkCodec network codec} of {@linkplain Vector vectors}.
 *
 * @since 1.0
 * @see Vector
 * @see NetworkCodec
 */
public final class VectorNetworkCodec implements NetworkCodec<Vector> {

    /**
     * An instance of the {@linkplain VectorNetworkCodec vector network codec}.
     *
     * @since 1.0
     */
    public static final VectorNetworkCodec INSTANCE = new VectorNetworkCodec();

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
}