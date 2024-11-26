package net.hypejet.jet.server.network.protocol.codecs.game.world.chunk;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.world.chunk.ChunkSection;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain ChunkSection a chunk section}.
 *
 * @since 1.0
 * @author Codestech
 * @see ChunkSection
 * @see NetworkWriter
 */
public final class ChunkSectionNetworkWriter implements NetworkWriter<ChunkSection> {

    /**
     * An instance of {@linkplain ChunkSectionNetworkWriter a chunk section network writer}.
     *
     * @since 1.0
     */
    public static final ChunkSectionNetworkWriter INSTANCE = new ChunkSectionNetworkWriter();

    private ChunkSectionNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ChunkSection object) {
        buf.writeShort(object.blockCount());
        PaletteNetworkWriter.BLOCK_INSTANCE.write(buf, object.chunkData());
        PaletteNetworkWriter.BIOME_INSTANCE.write(buf, object.biomeData());
    }
}