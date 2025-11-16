package net.hypejet.jet.server.entity.metadata;

import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.hypejet.jet.entity.EntityType;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.entity.JetEntity;
import org.jspecify.annotations.NullMarked;

import java.util.function.UnaryOperator;

/**
 * Metadata of an {@linkplain JetEntity entity}.
 *
 * @since 1.0
 * @see JetEntity
 */
@NullMarked
public final class EntityMetadata {

    private final IntObjectMap<EntityMetadataValue> values = new IntObjectHashMap<>();

    /**
     * Constructs the {@linkplain EntityMetadata entity metadata}.
     *
     * @param entityType the type of entity that the entity metadata is being constructed for
     * @since 1.0
     */
    public EntityMetadata(Holder.Reference<EntityType> entityType) {
        this.values.putAll(EntityMetadataDefaults.defaultsFor(entityType));
    }

    /**
     * Gets an {@linkplain EntityMetadataValue entity metadata value} set
     * at the specified index in this {@linkplain EntityMetadata entity metadata}.
     *
     * @param index the index of the entity metadata value that should be returned
     * @param valueType the expected class of the entity metadata value
     * @return the entity metadata value
     * @param <V> the expected type of the entity metadata value
     * @throws IllegalArgumentException if the entity metadata value is not of the specified expected class
     * @since 1.0
     */
    public <V extends EntityMetadataValue> V value(int index, Class<V> valueType) {
        EntityMetadataValue value = this.values.get(index);
        if (value == null) {
            throw new IllegalArgumentException(String.format(
                    "No entity metadata value was defined at %d index",
                    index
            ));
        } else if (!valueType.isInstance(value)) {
            throw new IllegalArgumentException(String.format(
                    "Entity metadata value at index %d is of %s type and cannot be cast to %s",
                    index, value.getClass().getSimpleName(), valueType.getSimpleName()
            ));
        }
        return valueType.cast(value);
    }

    /**
     * Replaces an {@linkplain EntityMetadataValue entity metadata value}
     * at the specified index in this {@linkplain EntityMetadata entity metadata}.
     *
     * @param index the index that the entity metadata value should be replaced at
     * @param valueType the expected class of the current entity metadata value,
     *                  new values should be of the same class
     * @param updatedValueProvider a unary operator consuming the current entity metadata value and providing
     *                             a new entity metadata value that should be present at the specified index
     * @param <V> the expected type of the current entity metadata value and the type of the new entity metadata value
     * @throws IllegalArgumentException if the current entity metadata value or the new entity
     *                                  metadata value is not of the specified expected class
     * @since 1.0
     */
    public <V extends EntityMetadataValue> void value(int index, Class<V> valueType,
                                                      UnaryOperator<V> updatedValueProvider) {
        V currentValue = this.value(index, valueType);
        V newValue = updatedValueProvider.apply(currentValue);
        if (!valueType.isInstance(newValue)) {
            throw new IllegalArgumentException(String.format(
                    "Entity metadata value at index %d expects %s type instead of %s",
                    index, valueType.getSimpleName(), newValue.getClass().getSimpleName()
            ));
        }
        this.values.put(index, newValue);
    }
}