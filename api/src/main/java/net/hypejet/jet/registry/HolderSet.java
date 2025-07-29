package net.hypejet.jet.registry;

import net.hypejet.concurrency.primitive.booleans.BooleanAcquisition;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A set of {@linkplain Holder holders}.
 *
 * <p>This interface is not sealed since it depends on Minecraft.
 * Adding new implementations could break switch cases for example.</p>
 *
 * @param <V> a value type of holders that this holder set store
 * @since 1.0
 */
@ApiStatus.NonExtendable
public interface HolderSet<V> {
    /**
     * Gets contents of this {@linkplain HolderSet holder set}.
     *
     * @return the contents
     * @since 1.0
     */
    @NonNull List<Holder<V>> contents();

    /**
     * A {@linkplain HolderSet holder set} directly storing the holders in a {@linkplain List list}.
     *
     * @param contents the list storing the holders
     * @param <V> a value type of holders that this holder set store
     * @since 1.0
     */
    record Direct<V>(@NonNull List<Holder<V>> contents) implements HolderSet<V> {
        /**
         * Constructs the {@linkplain Direct direct holder set}.
         *
         * @param contents the list storing the holders
         * @since 1.0
         */
        public Direct {
            Objects.requireNonNull(contents, "contents");
        }
    }

    /**
     * A {@linkplain HolderSet holder set} referencing to all {@linkplain RegistryEntry registry entries}
     * bound to the specified tag.
     *
     * @param registry a registry whose registry entries should be referenced
     * @param tagKey the key of the tag
     * @param <V> a value type of holders that this holder set store
     * @since 1.0
     */
    record Named<V>(@NonNull MinecraftRegistry<V> registry, @NonNull Key tagKey) implements HolderSet<V> {
        /**
         * Constructs the {@linkplain Named named holder set}.
         *
         * @param registry a registry whose registry entries should be referenced
         * @param tagKey the key of the tag
         * @since 1.0
         */
        public Named {
            Objects.requireNonNull(registry, "registry");
            Objects.requireNonNull(tagKey, "tag key");
        }

        @Override
        public @NonNull List<Holder<V>> contents() {
            List<Holder<V>> holders = new ArrayList<>();

            for (RegistryEntry<V> entry : this.registry.entries()) {
                try (BooleanAcquisition acquisition = this.registry.hasTag(entry, this.tagKey)) {
                    if (!acquisition.get()) continue;
                    holders.add(entry);
                }
            }

            return List.copyOf(holders);
        }
    }
}