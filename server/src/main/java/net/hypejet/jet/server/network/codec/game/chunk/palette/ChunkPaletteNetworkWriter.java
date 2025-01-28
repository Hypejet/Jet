package net.hypejet.jet.server.network.codec.game.chunk.palette;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.array.longs.LongArrayNetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.array.varint.VarIntArrayNetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.world.chunk.palette.ChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.DirectChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.IndirectChunkPalette;
import net.hypejet.jet.server.world.chunk.palette.SingleValuedChunkPalette;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain ChunkPalette a chunk palette}.
 *
 * @since 1.0
 * @see ChunkPalette
 * @see NetworkWriter
 */
public final class ChunkPaletteNetworkWriter implements NetworkWriter<ChunkPalette<?>> {
    /**
     * An instance of the {@linkplain ChunkPaletteNetworkWriter chunk palette network writer}.
     *
     * @since 1.0
     */
    public static final ChunkPaletteNetworkWriter INSTANCE = new ChunkPaletteNetworkWriter();

    private ChunkPaletteNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ChunkPalette<?> object) {
        buf.writeByte(object.bitsPerElement());

        switch (object) {
            case DirectChunkPalette<?> ignoredPalette -> {}
            case IndirectChunkPalette<?> palette ->
                    VarIntArrayNetworkWriter.INSTANCE.write(buf, palette.registryIndices());
            case SingleValuedChunkPalette<?> palette ->
                    VarIntNetworkCodec.INSTANCE.write(buf, palette.elementIdentifier());
        }

        LongArrayNetworkWriter.INSTANCE.write(buf, object.data());
    }
}