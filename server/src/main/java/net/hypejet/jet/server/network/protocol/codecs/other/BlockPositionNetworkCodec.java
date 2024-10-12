package net.hypejet.jet.server.network.protocol.codecs.other;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.data.model.api.coordinate.BlockPosition;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain BlockPosition block
 * position}.
 *
 * @since 1.0
 * @author Codestech
 * @see BlockPosition
 * @see NetworkCodec
 */
public final class BlockPositionNetworkCodec implements NetworkCodec<BlockPosition> {

    private static final BlockPositionNetworkCodec INSTANCE = new BlockPositionNetworkCodec();

    private BlockPositionNetworkCodec() {}

    @Override
    public @NonNull BlockPosition read(@NonNull ByteBuf buf) {
        long encodedPosition = buf.readLong();

        int x = (int) (encodedPosition >> 38);
        int y = (int) (encodedPosition << 52 >> 52);
        int z = (int) (encodedPosition << 26 >> 38);

        return new BlockPosition(x, y, z);
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull BlockPosition object) {
        long encodedLong = (long) (object.blockX() & 0x3FFFFFF) << 38;
        encodedLong |= (long) (object.blockZ() & 0x3FFFFFF) << 12;
        encodedLong |= object.blockY() & 0xFFF;
        buf.writeLong(encodedLong);
    }

    /**
     * Gets an instance of the {@linkplain BlockPositionNetworkCodec block position network codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull BlockPositionNetworkCodec instance() {
        return INSTANCE;
    }
}