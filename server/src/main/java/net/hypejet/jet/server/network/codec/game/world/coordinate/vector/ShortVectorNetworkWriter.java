package net.hypejet.jet.server.network.codec.game.world.coordinate.vector;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.world.coordinate.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer} writing {@linkplain Vector vectors} as shorts.
 *
 * @since 1.0
 * @see Vector
 */
public final class ShortVectorNetworkWriter implements NetworkWriter<Vector> {
    /**
     * An instance of the {@linkplain ShortVectorNetworkWriter short vector network codec}.
     *
     * @since 1.0
     */
    public static final ShortVectorNetworkWriter INSTANCE = new ShortVectorNetworkWriter();

    private ShortVectorNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Vector object) {
        buf.writeShort(wrapAsShort(object.x()));
        buf.writeShort(wrapAsShort(object.y()));
        buf.writeShort(wrapAsShort(object.z()));
    }

    private static short wrapAsShort(double value) {
        return (short) (Math.clamp(value, -3.9, 3.9) * 8000);
    }
}