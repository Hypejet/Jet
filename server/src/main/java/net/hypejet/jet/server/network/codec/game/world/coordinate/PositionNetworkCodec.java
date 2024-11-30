package net.hypejet.jet.server.network.codec.game.world.coordinate;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.data.model.api.coordinate.Position;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkCodec a network codec}, which reads and writes {@linkplain Position a position}.
 *
 * @since 1.0
 * @author Codestech
 * @see Position
 * @see NetworkCodec
 */
public final class PositionNetworkCodec implements NetworkCodec<Position> {

    /**
     * An instance of {@linkplain PositionNetworkCodec a position network codec}.
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