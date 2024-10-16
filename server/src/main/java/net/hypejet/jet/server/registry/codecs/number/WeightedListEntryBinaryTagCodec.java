package net.hypejet.jet.server.registry.codecs.number;

import net.hypejet.jet.data.model.api.number.IntegerProvider;
import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain BinaryTagCodec binary tag codec} which deserializes and serializes
 * an {@linkplain IntegerProvider.WeightedList.Entry entry of weighted list integer provider}.
 *
 * @since 1.0
 * @author Codestech
 * @see IntegerProvider.WeightedList.Entry
 * @see BinaryTagCodec
 */
public final class WeightedListEntryBinaryTagCodec implements BinaryTagCodec<IntegerProvider.WeightedList.Entry> {

    private static final WeightedListEntryBinaryTagCodec INSTANCE = new WeightedListEntryBinaryTagCodec();

    private static final String DATA = "data";
    private static final String WEIGHT = "weight";

    private WeightedListEntryBinaryTagCodec() {}

    @Override
    public IntegerProvider.WeightedList.@NonNull Entry read(@NonNull BinaryTag tag) {
        if (!(tag instanceof CompoundBinaryTag compound))
            throw new IllegalArgumentException("The binary tag specified must be a compound binary tag");

        BinaryTag dataBinaryTag = compound.get(DATA);
        if (dataBinaryTag == null)
            throw new IllegalArgumentException("The data binary tag was not specified");

        return new IntegerProvider.WeightedList.Entry(IntegerProviderBinaryTagCodec.instance().read(dataBinaryTag),
                compound.getInt(WEIGHT));
    }

    @Override
    public @NonNull BinaryTag write(IntegerProvider.WeightedList.@NonNull Entry object) {
        return CompoundBinaryTag.builder()
                .put(DATA, IntegerProviderBinaryTagCodec.instance().write(object.source()))
                .putInt(WEIGHT, object.weight())
                .build();
    }

    /**
     * Gets an instance of the {@linkplain WeightedListEntryBinaryTagCodec weighted list entry binary tag codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull WeightedListEntryBinaryTagCodec instance() {
        return INSTANCE;
    }
}