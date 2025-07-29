package net.hypejet.jet.registry.holder;

import net.hypejet.jet.registry.MinecraftRegistry;
import net.hypejet.jet.registry.RegistryEntry;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * A holder of registry value.
 *
 * <p>This interface is not sealed since it depends on Minecraft.
 * Adding new implementations could break switch cases for example.</p>
 *
 * @param <V> the type of the registry value
 * @since 1.0
 */
@ApiStatus.NonExtendable
public interface Holder<V> {
    /**
     * Gets the registry value.
     *
     * @param registry a registry that should provide the value if this holder does not store it directly
     * @return the value, {@code null} if this holder does not provide the value
     *         directly and the specified registry does not contain the value
     * @since 1.0
     */
    @Nullable V value(@NonNull MinecraftRegistry<? extends V> registry);

    /**
     * A {@linkplain Holder holder} directly holding the registry value.
     *
     * @param value the registry value that the holder holds
     * @param <V> the type of the registry value
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

        @Override
        public @NonNull V value(@NonNull MinecraftRegistry<? extends V> registry) {
            return this.value;
        }
    }

    /**
     * A {@linkplain Holder holder} holding a {@linkplain MinecraftRegistry registry} reference to the registry value.
     *
     * @param key the key of the registry entry that the holder references to
     * @param <V> the type of the registry value
     * @since 1.0
     */
    record Reference<V>(@NonNull Key key) implements Holder<V> {
        /**
         * Constructs the {@linkplain Reference reference holder}.
         *
         * @param key the key of the registry entry that the holder references to
         * @since 1.0
         */
        public Reference {
            Objects.requireNonNull(key, "key");
        }

        @Override
        public @Nullable V value(@NonNull MinecraftRegistry<? extends V> registry) {
            RegistryEntry<? extends V> entry = registry.get(this.key);
            if (entry == null) return null;
            return entry.value();
        }
    }
}