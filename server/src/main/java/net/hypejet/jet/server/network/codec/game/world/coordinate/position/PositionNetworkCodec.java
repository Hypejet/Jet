package net.hypejet.jet.server.network.codec.game.world.coordinate.position;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.world.coordinate.Position;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkCodec network codec} of {@linkplain Position positions}.
 *
 * @since 1.0
 * @see Position
 * @see NetworkCodec
 */
public final class PositionNetworkCodec implements NetworkCodec<Position> {

    /**
     * An instance of the {@linkplain PositionNetworkCodec position network codec}.
     *
     * @since 1.0
     */
    public static final PositionNetworkCodec INSTANCE = new PositionNetworkCodec();

    private PositionNetworkCodec() {}

    @Override
    public @NonNull Position read(@NonNull ByteBuf buf) {
        return new Position(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readFloat(), buf.readFloat());
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Position object) {
        buf.writeDouble(object.x());
        buf.writeDouble(object.y());
        buf.writeDouble(object.z());
        buf.writeFloat(object.yaw());
        buf.writeFloat(object.pitch());
    }
}