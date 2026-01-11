package net.hypejet.jet.server.network.codec.game.world.chunk.section;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.chunk.palette.ChunkPaletteNetworkWriter;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.world.chunk.section.JetChunkSection;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@link JetChunkSection chunk section}.
 *
 * @since 1.0
 * @see JetChunkSection
 * @see NetworkWriter
 */
public final class ChunkSectionNetworkWriter implements NetworkWriter<JetChunkSection> {
    /**
     * An instance of the {@linkplain ChunkSectionNetworkWriter chunk section network writer}.
     *
     * @since 1.0
     */
    public static final ChunkSectionNetworkWriter INSTANCE = new ChunkSectionNetworkWriter();

    private ChunkSectionNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf,
                      @NonNull JetRegistryManager registryManager,
                      @NonNull JetChunkSection object) {
        buf.writeShort(object.nonAirBlockCount());
        ChunkPaletteNetworkWriter.INSTANCE.write(buf, registryManager, object.blockStatePalette());
        ChunkPaletteNetworkWriter.INSTANCE.write(buf, registryManager, object.biomePalette());
    }
}