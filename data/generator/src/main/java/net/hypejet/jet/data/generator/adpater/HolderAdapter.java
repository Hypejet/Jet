package net.hypejet.jet.data.generator.adpater;

import net.hypejet.jet.data.json.model.JsonHolder;
import net.hypejet.jet.data.json.model.JsonSoundEvent;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;

/**
 * Represents something converting {@linkplain Holder holders} to a Jet data equivalent.
 *
 * @since 1.0
 * @see Holder
 */
public final class HolderAdapter {

    private HolderAdapter() {}

    /**
     * Converts the specified {@linkplain Holder holder} to a Jet data equivalent.
     *
     * @param holder the holder to convert
     * @param valueAdapter a function converting the holder value
     * @return the converted holder
     * @param <MV> the type of unconverted holder value
     * @param <CV> the type of converted holder value
     * @since 1.0
     */
    public static <MV, CV> @NonNull JsonHolder<CV> convert(@NonNull Holder<MV> holder,
                                                           @NonNull Function<MV, CV> valueAdapter) {
        return holder.unwrap().map(
                key -> new JsonHolder.Reference<>(KeyAdapter.convert(key.location())),
                value -> new JsonHolder.Direct<>(valueAdapter.apply(value))
        );
    }

    /**
     * Converts the specified {@linkplain SoundEvent sound event} {@linkplain Holder holder} to a Jet data equivalent.
     *
     * @param holder the sound event holder to convert
     * @return the converted sound event holder
     * @since 1.0
     */
    public static @NonNull JsonHolder<JsonSoundEvent> convertSoundEventHolder(@NonNull Holder<SoundEvent> holder) {
        return convert(holder, event -> new JsonSoundEvent(
                KeyAdapter.convert(event.location()),
                event.fixedRange().orElse(null)
        ));
    }
}