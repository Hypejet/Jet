package net.hypejet.jet.server.network.codec.game.world.coordinate.chunk.section;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.util.math.MathUtil;
import net.hypejet.jet.server.world.coordinate.chunk.section.ChunkSectionPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer} of {@linkplain ChunkSectionPosition a chunk section position}.
 *
 * @since 1.0
 * @see ChunkSectionPosition
 * @see NetworkWriter
 */
public final class ChunkSectionPositionNetworkWriter implements NetworkWriter<ChunkSectionPosition> {
    /**
     * An instance of the {@linkplain ChunkSectionPositionNetworkWriter chunk section position network writer}.
     *
     * @since 1.0
     * @see ChunkSectionPositionNetworkWriter
     */
    public static final ChunkSectionPositionNetworkWriter INSTANCE = new ChunkSectionPositionNetworkWriter();

    private static final int HORIZONTAL_BIT_COUNT = 22;
    private static final int VERTICAL_BIT_COUNT = 20;

    private static final int HORIZONTAL_MAX_VALUE = MathUtil.power(2, HORIZONTAL_BIT_COUNT) - 1;
    private static final int VERTICAL_MAX_VALUE = MathUtil.power(2, VERTICAL_BIT_COUNT) - 1;

    private ChunkSectionPositionNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf,
                      @NonNull JetRegistryManager registryManager,
                      @NonNull ChunkSectionPosition object) {
        long packedValue = object.sectionY() & VERTICAL_MAX_VALUE;
        packedValue |= (long) (object.sectionZ() & HORIZONTAL_MAX_VALUE) << VERTICAL_BIT_COUNT;
        packedValue |= (long) (object.sectionX() & HORIZONTAL_MAX_VALUE) << HORIZONTAL_BIT_COUNT + VERTICAL_BIT_COUNT;
        buf.writeLong(packedValue);
    }
}