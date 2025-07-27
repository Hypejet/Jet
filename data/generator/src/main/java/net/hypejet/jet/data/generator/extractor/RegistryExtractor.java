package net.hypejet.jet.data.generator.extractor;

import net.hypejet.jet.data.json.entry.JsonRegistryEntry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import org.jspecify.annotations.NonNull;

import java.util.List;

/**
 * Represents something that extracts data from a {@linkplain Registry Minecraft registry} and converts it to
 * Jet equivalents.
 *
 * @param <T> a type of the Jet data equivalent
 * @since 1.0
 */
public interface RegistryExtractor<T> {
    /**
     * Extracts the data.
     *
     * @param registryAccess access to loaded Minecraft registries
     * @return registry entries of the extracted and converted data, as a list with preserved order
     * @since 1.0
     */
    @NonNull List<JsonRegistryEntry<T>> extract(@NonNull RegistryAccess registryAccess);

    /**
     * Gets a {@linkplain Class class} of the Jet equivalent of the registry data.
     *
     * @return the class
     * @since 1.0
     */
    @NonNull Class<T> valueClass();
}