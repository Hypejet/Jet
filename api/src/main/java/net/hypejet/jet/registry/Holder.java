package net.hypejet.jet.registry;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A holder of registry value.
 *
 * @param <V> a type of the registry value
 * @since 1.0
 */
public sealed interface Holder<V> permits Holder.Direct, RegistryEntry {
    /**
     * Gets the registry value.
     *
     * @return the value
     * @since 1.0
     */
    @NonNull V value();

    /**
     * A {@linkplain Holder holder} directly holding the value.
     *
     * @param value the registry value that the holder holds
     * @param <V> a type of the registry value
     * @since 1.0
     */
    record Direct<V>(@NonNull V value) implements Holder<V> {
        /**
         * Constructs the {@linkplain Direct direct holder}.
         *
         * @param value the registry value that the holder holds
         * @since 1.0
         */
        public Direct {
            Objects.requireNonNull(value, "value");
        }
    }
}