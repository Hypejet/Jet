package net.hypejet.jet.data.generator.adpater;

import net.hypejet.jet.data.json.model.JsonHolder;
import net.hypejet.jet.data.json.model.JsonHolderSet;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Represents something converting {@linkplain JsonHolderSet holder sets} to a Jet data equivalent.
 *
 * @since 1.0
 * @see JsonHolderSet
 */
public final class HolderSetAdapter {

    private HolderSetAdapter() {}

    /**
     * Converts the specified {@linkplain HolderSet holder set} to a Jet data equivalent.
     *
     * @param holderSet the holder set to convert
     * @param valueAdapter a function converting value of holder
     * @return the converted holder set
     * @param <MV> a type of the unconverted holder value
     * @param <CV> a type of the converted holder value
     * @since 1.0
     */
    public static <MV, CV> @NonNull JsonHolderSet<CV> convert(@NonNull HolderSet<MV> holderSet,
                                                              @NonNull Function<MV, CV> valueAdapter) {
        return holderSet.unwrap().map(
                key -> new JsonHolderSet.Named<>(KeyAdapter.convert(key.location())),
                value -> {
                    List<JsonHolder<CV>> holders = new ArrayList<>();
                    for (Holder<MV> holder : value)
                        holders.add(HolderAdapter.convert(holder, valueAdapter));
                    return new JsonHolderSet.Direct<>(holders);
                }
        );
    }
}