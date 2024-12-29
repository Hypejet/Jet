package net.hypejet.jet.server.registry.writers.number;

import net.hypejet.jet.data.model.api.number.IntegerProvider;
import net.hypejet.jet.server.util.codec.Writer;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain Writer a writer}, which writes
 * {@linkplain IntegerProvider.WeightedList.Entry a weighted list entry} into
 * {@linkplain CompoundBinaryTag a compound binary tag}.
 *
 * @since 1.0
 * @see net.hypejet.jet.data.model.api.number.IntegerProvider.WeightedList
 * @see CompoundBinaryTag
 * @see Writer
 */
public final class WeightedListEntryBinaryTagWriter
        implements Writer<IntegerProvider.WeightedList.Entry, CompoundBinaryTag> {
    /**
     * An instance of the {@linkplain WeightedListEntryBinaryTagWriter weighted list entry binary tag writer}.
     *
     * @since 1.0
     */
    public static final WeightedListEntryBinaryTagWriter INSTANCE = new WeightedListEntryBinaryTagWriter();

    private static final String DATA = "data";
    private static final String WEIGHT = "weight";

    private WeightedListEntryBinaryTagWriter() {}

    @Override
    public @NonNull CompoundBinaryTag write(IntegerProvider.WeightedList.@NonNull Entry object) {
        return CompoundBinaryTag.builder()
                .put(DATA, IntegerProviderBinaryTagWriter.INSTANCE.write(object.source()))
                .putInt(WEIGHT, object.weight())
                .build();
    }
}