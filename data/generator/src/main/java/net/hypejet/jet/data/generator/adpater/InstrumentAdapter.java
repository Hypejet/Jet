package net.hypejet.jet.data.generator.adpater;

import net.hypejet.jet.data.json.model.instrument.JsonInstrument;
import net.minecraft.world.item.Instrument;
import org.jspecify.annotations.NonNull;

/**
 * Represents something converting {@linkplain Instrument instruments} to a Jet data equivalent.
 *
 * @since 1.0
 * @see Instrument
 */
public final class InstrumentAdapter {

    private InstrumentAdapter() {}

    /**
     * Converts the specified {@linkplain Instrument instrument} to a Jet data equivalent.
     *
     * @param instrument the instrument to convert
     * @return the converted instrument
     * @since 1.0
     */
    public static @NonNull JsonInstrument convert(@NonNull Instrument instrument) {
        return new JsonInstrument(
                HolderAdapter.convertSoundEventHolder(instrument.soundEvent()),
                instrument.useDuration(),
                instrument.range(),
                ComponentAdapter.convert(instrument.description())
        );
    }
}