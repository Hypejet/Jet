package net.hypejet.jet.data.json.model.item;

import net.kyori.adventure.nbt.api.BinaryTagHolder;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * A data component of Minecraft item.
 *
 * @since 1.0
 */
@NullMarked
public sealed interface JsonItemComponent {
    /**
     * An {@linkplain JsonItemComponent item component} with bound serialized value.
     *
     * @param value the serialized value, as binary tag holder
     * @since 1.0
     * @see JsonItemComponent
     */
    record Valued(BinaryTagHolder value) implements JsonItemComponent {
        /**
         * Constructs the {@linkplain Valued valued item component}.
         *
         * @param value the serialized value, as binary tag holder
         * @since 1.0
         */
        public Valued {
            Objects.requireNonNull(value, "value");
        }
    }

    /**
     * An {@linkplain JsonItemComponent item component} that is not supported anymore.
     *
     * @since 1.0
     * @see JsonItemComponent
     */
    final class Removed implements JsonItemComponent {
        private static final Removed INSTANCE = new Removed();
        private Removed() {}
    }

    /**
     * Gets an instance of the {@linkplain Removed removed item component}.
     *
     * @return the removed item component instance
     * @since 1.0
     */
    static Removed removed() {
        return Removed.INSTANCE;
    }
}