package net.hypejet.jet.server.network.codec.game.world.coordinate.relative;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.util.math.MathUtil;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ChunkRelativeBlockPosition a chunk-relative block position}.
 *
 * @since 1.0
 * @see ChunkRelativeBlockPosition
 * @see NetworkWriter
 */
public final class ChunkRelativeBlockPositionNetworkWriter implements NetworkWriter<ChunkRelativeBlockPosition> {
    /**
     * An instance of the
     * {@linkplain ChunkRelativeBlockPositionNetworkWriter chunk-relative block position network writer}.
     *
     * @since 1.0
     */
    public static final ChunkRelativeBlockPositionNetworkWriter
            INSTANCE = new ChunkRelativeBlockPositionNetworkWriter();

    private ChunkRelativeBlockPositionNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf,
                      @NonNull JetRegistryManager registryManager,
                      @NonNull ChunkRelativeBlockPosition object) {
        int bitCount = MathUtil.bitCount(ChunkPaletteType.BLOCK_STATE.maximumCoordinateValue());

        byte packedXZ = (byte) (object.relativeX() << bitCount);
        packedXZ |= object.relativeZ();

        buf.writeByte(packedXZ);
        buf.writeShort(object.absoluteY());
    }
}