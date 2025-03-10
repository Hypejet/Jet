package net.hypejet.jet.server.network.codec.game.world.coordinate;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.util.math.MathUtil;
import net.hypejet.jet.server.world.coordinate.relative.ChunkRelativePosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ChunkRelativePosition a chunk-relative position}.
 *
 * @since 1.0
 * @see ChunkRelativePosition
 * @see NetworkWriter
 */
public final class ChunkRelativePositionNetworkWriter implements NetworkWriter<ChunkRelativePosition> {
    /**
     * An instance of the {@linkplain ChunkRelativePositionNetworkWriter chunk-relative position network writer}.
     *
     * @since 1.0
     */
    public static final ChunkRelativePositionNetworkWriter INSTANCE = new ChunkRelativePositionNetworkWriter();

    private ChunkRelativePositionNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ChunkRelativePosition object) {
        // Maximum value allowed is axis
        int bitCount = MathUtil.bitCount(object.paletteType().axisLength() - 1);

        byte packedXZ = (byte) (object.paletteRelativeX() << bitCount);
        packedXZ |= object.paletteRelativeZ();

        buf.writeByte(packedXZ);
        buf.writeShort(object.absoluteY());
    }
}