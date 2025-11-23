package net.hypejet.jet.server.entity.component;

import net.hypejet.jet.entity.component.EntityDataComponent;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * A function encoding an {@linkplain EntityDataComponent entity data component}
 * value as an {@linkplain EntityMetadataValue entity metadata value}.
 *
 * @param <MV> the type of entity metadata value that the component values should be encoded to
 * @param <V> the type of entity data component values that should be encoded to entity metadata values
 * @since 1.0
 * @see EntityDataComponent
 * @see EntityMetadataValue
 */
@FunctionalInterface
public interface ComponentValueEncoder<MV extends EntityMetadataValue, V> {
    /**
     * Encodes the specified {@linkplain EntityDataComponent entity data component}
     * value as an {@linkplain EntityMetadataValue entity metadata value}.
     *
     * @param currentMetadataValue the current entity metadata value that is an encoded value of
     *                             the entity data component associated with the specified component value
     * @param value the entity data component value to encode, it can be {@code null},
     *              but only if the component associated with it is nullable
     * @return the specified entity data component value encoded as an entity metadata value, if the specified
     *         current entity metadata value is an encoded representation of multiple entity data component
     *         values it is expected to keep values of the other components unchanged
     * @since 1.0
     */
    @NonNull MV encode(@NonNull MV currentMetadataValue, @Nullable V value);
}