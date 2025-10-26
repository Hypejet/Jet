package net.hypejet.jet.server.network.codec.game.world.coordinate;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.util.math.MathUtil;
import net.hypejet.jet.world.coordinate.BlockPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkCodec a network codec}, which reads and writes {@linkplain BlockPosition a block
 * position}.
 *
 * @since 1.0
 * @see BlockPosition
 * @see NetworkCodec
 */
public final class BlockPositionNetworkCodec implements NetworkCodec<BlockPosition> {

    private static final byte Y_BITS = 12;
    private static final byte X_OR_Z_BITS = 26;

    private static final byte X_AND_Y_BITS = X_OR_Z_BITS + Y_BITS;
    private static final byte Y_DESERIALIZATION_SHIFT = 2 * X_OR_Z_BITS;

    private static final int Y_MASK = MathUtil.power(2, Y_BITS) - 1;
    private static final int X_OR_Z_MASK = MathUtil.power(2, X_OR_Z_BITS) - 1;

    /**
     * An instance of the {@linkplain BlockPositionNetworkCodec block position network codec}.
     *
     * @since 1.0
     */
    public static final BlockPositionNetworkCodec INSTANCE = new BlockPositionNetworkCodec();

    private BlockPositionNetworkCodec() {}

    @Override
    public @NonNull BlockPosition read(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager) {
        long value = buf.readLong();

        int x = (int) (value >>> X_AND_Y_BITS);
        int y = (int) (value << Y_DESERIALIZATION_SHIFT >> Y_DESERIALIZATION_SHIFT);
        int z = (int) (value << X_OR_Z_BITS >> X_AND_Y_BITS);

        return BlockPosition.blockPosition(x, y, z);
    }

    @Override
    public void write(@NonNull ByteBuf buf,
                      @NonNull JetRegistryManager registryManager,
                      @NonNull BlockPosition object) {
        long value = (long) (object.blockX() & X_OR_Z_MASK) << X_AND_Y_BITS;
        value |= object.blockY() & Y_MASK;
        value |= (long) (object.blockZ() & X_OR_Z_MASK) << Y_BITS;
        buf.writeLong(value);
    }
}