package net.hypejet.jet.server.network.protocol.codecs.chunk;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.world.chunk.ChunkSection;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes
 * a {@linkplain ChunkSection chunk section}.
 *
 * @since 1.0
 * @author Codestech
 * @see ChunkSection
 * @see NetworkCodec
 */
public final class ChunkSectionNetworkCodec implements NetworkCodec<ChunkSection> {

    private static final ChunkSectionNetworkCodec INSTANCE = new ChunkSectionNetworkCodec();

    private ChunkSectionNetworkCodec() {}

    @Override
    public @NonNull ChunkSection read(@NonNull ByteBuf buf) {
        return new ChunkSection(buf.readShort(),
                PaletteNetworkCodec.blockInstance().read(buf),
                PaletteNetworkCodec.biomeInstance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ChunkSection object) {
        buf.writeShort(object.blockCount());
        PaletteNetworkCodec.blockInstance().write(buf, object.chunkData());
        PaletteNetworkCodec.biomeInstance().write(buf, object.biomeData());
    }

    /**
     * Gets an instance of the {@linkplain ChunkSectionNetworkCodec chunk section network codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull ChunkSectionNetworkCodec instance() {
        return INSTANCE;
    }
}