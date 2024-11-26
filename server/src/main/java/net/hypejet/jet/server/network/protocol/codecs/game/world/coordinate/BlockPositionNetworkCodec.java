package net.hypejet.jet.server.network.protocol.codecs.game.world.coordinate;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.world.coordinate.BlockPosition;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkCodec a network codec}, which reads and writes {@linkplain BlockPosition a block
 * position}.
 *
 * @since 1.0
 * @author Codestech
 * @see BlockPosition
 * @see NetworkCodec
 */
public final class BlockPositionNetworkCodec implements NetworkCodec<BlockPosition> {

    private static final byte Y_BITS = 12;
    private static final byte X_OR_Z_BITS = 26;
    private static final byte X_AND_Y_BITS = X_OR_Z_BITS + Y_BITS;

    private static final byte X_SHIFT = X_AND_Y_BITS;
    private static final byte Y_SHIFT = 2 * X_OR_Z_BITS;
    private static final byte Z_SHIFT = Y_BITS;

    private static final short Y_MASK = 0xFFF;
    private static final int X_OR_Z_MASK = 0x3FFFFFF;

    /**
     * An instance of {@linkplain BlockPositionNetworkCodec a block position network codec}.
     *
     * @since 1.0
     */
    public static final BlockPositionNetworkCodec INSTANCE = new BlockPositionNetworkCodec();

    private BlockPositionNetworkCodec() {}

    @Override
    public @NonNull BlockPosition read(@NonNull ByteBuf buf) {
        long value = buf.readLong();

        int x = (int) (value >> X_SHIFT);
        int y = (int) (value << Y_SHIFT >> Y_SHIFT);
        int z = (int) (value << X_OR_Z_BITS >> X_AND_Y_BITS);

        return BlockPosition.blockPosition(x, y, z);
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull BlockPosition object) {
        long value = ((long) object.blockX() & X_OR_Z_MASK) << X_SHIFT;
        value |= (long) object.blockY() & Y_MASK;
        value |= ((long) object.blockZ() & X_OR_Z_MASK) << Z_SHIFT;
        buf.writeLong(value);
    }
}