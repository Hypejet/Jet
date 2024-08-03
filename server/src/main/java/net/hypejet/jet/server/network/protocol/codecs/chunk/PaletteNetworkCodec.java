package net.hypejet.jet.server.network.protocol.codecs.chunk;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.data.biome.Biome;
import net.hypejet.jet.data.block.Block;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.arrays.LongArrayNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.arrays.VarIntArrayNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.world.chunk.palette.DirectPalette;
import net.hypejet.jet.world.chunk.palette.IndirectPalette;
import net.hypejet.jet.world.chunk.palette.Palette;
import net.hypejet.jet.world.chunk.palette.SingleValuedPalette;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain Palette palette}.
 *
 * @since 1.0
 * @author Codestech
 * @see Palette
 * @see NetworkCodec
 */
public final class PaletteNetworkCodec implements NetworkCodec<Palette> {

    private static final PaletteNetworkCodec BLOCK_INSTANCE = new PaletteNetworkCodec((short) 4, (short) 8, (short) 15);
    private static final PaletteNetworkCodec BIOME_INSTANCE = new PaletteNetworkCodec((short) 1, (short) 3, (short) 6);

    private static final int SINGLE_VALUED_BITS_PER_ENTRY = 0;

    private final short indirectMin;
    private final short indirectMax;

    private final short direct;

    private PaletteNetworkCodec(short indirectMin, short indirectMax, short direct) {
        this.indirectMin = indirectMin;
        this.indirectMax = indirectMax;
        this.direct = direct;
    }

    @Override
    public @NonNull Palette read(@NonNull ByteBuf buf) {
        short bitsPerEntry = buf.readUnsignedByte();

        if (bitsPerEntry == SINGLE_VALUED_BITS_PER_ENTRY) {
            int value = VarIntNetworkCodec.instance().read(buf);
            LongArrayNetworkCodec.instance().read(buf); // Read and ignore the entries array
            return new SingleValuedPalette(value);
        }

        if (bitsPerEntry <= this.indirectMax && bitsPerEntry != this.direct) {
            bitsPerEntry = (short) Math.clamp(bitsPerEntry, this.indirectMin, this.indirectMin);
            int[] data = VarIntArrayNetworkCodec.instance().read(buf);
            return new IndirectPalette(bitsPerEntry, data, LongArrayNetworkCodec.instance().read(buf));
        }

        return new DirectPalette(LongArrayNetworkCodec.instance().read(buf));
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
            case IndirectPalette indirect -> VarIntArrayNetworkCodec.instance().write(buf, indirect.data());
            case SingleValuedPalette (int value) -> VarIntNetworkCodec.instance().write(buf, value);
            default -> {}
        }

        LongArrayNetworkCodec.instance().write(buf, object.entries());
    }

    /**
     * Gets an instance {@linkplain PaletteNetworkCodec palette network codec}, which reads and writes palettes
     * for {@linkplain Block blocks}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull PaletteNetworkCodec blockInstance() {
        return BLOCK_INSTANCE;
    }

    /**
     * Gets an instance {@linkplain PaletteNetworkCodec palette network codec}, which reads and writes palettes
     * for {@linkplain Biome biomes}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull PaletteNetworkCodec biomeInstance() {
        return BIOME_INSTANCE;
    }
}