package net.hypejet.jet.server.network.codec.game.chunk.section;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.chunk.palette.ChunkPaletteNetworkWriter;
import net.hypejet.jet.server.world.chunk.section.ChunkSection;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@link ChunkSection chunk section}.
 *
 * @since 1.0
 * @see ChunkSection
 * @see NetworkWriter
 */
public final class ChunkSectionNetworkWriter implements NetworkWriter<ChunkSection> {
    /**
     * An instance of the {@linkplain ChunkSectionNetworkWriter chunk section network writer}.
     *
     * @since 1.0
     */
    public static final ChunkSectionNetworkWriter INSTANCE = new ChunkSectionNetworkWriter();

    private ChunkSectionNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ChunkSection object) {
        buf.writeShort(object.blockCount());
        ChunkPaletteNetworkWriter.INSTANCE.write(buf, object.blockPalette());
        ChunkPaletteNetworkWriter.INSTANCE.write(buf, object.biomePalette());
    }
}