package net.hypejet.jet.server.entity.component;

import net.hypejet.jet.entity.component.EntityDataComponent;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * A function decoding an {@linkplain EntityDataComponent entity data component}
 * value from an {@linkplain EntityMetadataValue entity metadata value}.
 *
 * @param <MV> the type of entity metadata values that this function decodes component values from
 * @param <V> the type of entity data component values that this function decodes entity metadata values to
 * @since 1.0
 * @see EntityDataComponent
 * @see EntityMetadataValue
 */
@FunctionalInterface
public interface ComponentValueDecoder<MV extends EntityMetadataValue, V> {
    /**
     * Decodes the specified {@linkplain EntityMetadataValue entity metadata value}
     * to an {@linkplain EntityDataComponent entity data component} value.
     *
     * @param metadataValue the entity metadata value to decode
     * @return the entity data component value, which can be {@code null} only
     *         if the entity data component associated with the returned value is nullable
     * @since 1.0
     */
    @Nullable V decode(@NonNull MV metadataValue);
}