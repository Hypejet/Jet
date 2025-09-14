package net.hypejet.jet.server.network.codec.game.world.coordinate.floats;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.world.coordinate.floats.FloatQuaternion;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer} of {@linkplain FloatQuaternion float quaternions}.
 *
 * @since 1.0
 * @see FloatQuaternion
 * @see NetworkWriter
 */
public final class FloatQuaternionNetworkWriter implements NetworkWriter<FloatQuaternion> {
    /**
     * An instance of the {@linkplain FloatQuaternionNetworkWriter float quaternion network writer}.
     *
     * @since 1.0
     */
    public static final FloatQuaternionNetworkWriter INSTANCE = new FloatQuaternionNetworkWriter();

    private FloatQuaternionNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull FloatQuaternion object) {
        buf.writeFloat(object.x());
        buf.writeFloat(object.y());
        buf.writeFloat(object.z());
        buf.writeFloat(object.w());
    }
}