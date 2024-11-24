package net.hypejet.jet.server.network.protocol.codecs.chunk;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.array.longs.LongArrayNetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.array.varint.VarIntArrayNetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.world.chunk.palette.DirectPalette;
import net.hypejet.jet.world.chunk.palette.IndirectPalette;
import net.hypejet.jet.world.chunk.palette.Palette;
import net.hypejet.jet.world.chunk.palette.SingleValuedPalette;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain Palette a palette}.
 *
 * @since 1.0
 * @author Codestech
 * @see Palette
 * @see NetworkWriter
 */
public final class PaletteNetworkWriter implements NetworkWriter<Palette> {

    /**
     * An instance of {@linkplain PaletteNetworkWriter a palette network writer}, which writes palettes
     * for {@linkplain ??? blocks}.
     *
     * @since 1.0
     */
    public static final PaletteNetworkWriter BLOCK_INSTANCE =
            new PaletteNetworkWriter((short) 4, (short) 8, (short) 15);

    /**
     * An instance {@linkplain PaletteNetworkWriter a palette network writer}, which writes palettes
     * for {@linkplain ??? biomes}.
     *
     * @since 1.0
     */
    public static final PaletteNetworkWriter BIOME_INSTANCE =
            new PaletteNetworkWriter((short) 1, (short) 3, (short) 6);

    private static final int SINGLE_VALUED_BITS_PER_ENTRY = 0;

    private final short indirectMin;
    private final short indirectMax;

    private final short direct;

    private PaletteNetworkWriter(short indirectMin, short indirectMax, short direct) {
        this.indirectMin = indirectMin;
        this.indirectMax = indirectMax;
        this.direct = direct;
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Palette object) {
        short bitsPerEntry = switch (object) {
            case DirectPalette ignored -> this.direct;
            case IndirectPalette indirect -> (short) Math.clamp(
                    indirect.bitsPerEntry(),
                    this.indirectMin,
                    this.indirectMax
            );
            case SingleValuedPalette ignored -> SINGLE_VALUED_BITS_PER_ENTRY;
        };

        buf.writeByte(bitsPerEntry);

        switch (object) {
            case IndirectPalette indirect -> VarIntArrayNetworkWriter.INSTANCE.write(buf, indirect.data());
            case SingleValuedPalette (int value) -> VarIntNetworkCodec.INSTANCE.write(buf, value);
            default -> {}
        }

        LongArrayNetworkWriter.INSTANCE.write(buf, object.entries());
    }
}