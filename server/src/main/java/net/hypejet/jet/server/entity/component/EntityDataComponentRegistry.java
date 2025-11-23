package net.hypejet.jet.server.entity.component;

import net.hypejet.jet.entity.EntityType;
import net.hypejet.jet.entity.component.EntityDataComponent;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.util.number.ByteUtil;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * A registry of {@linkplain EntityDataComponent entity data components}.
 *
 * @since 1.0
 * @see EntityDataComponent
 * @see EntityDataComponentRegistration
 */
@NullMarked
public final class EntityDataComponentRegistry {

    private static final Map<EntityDataComponent<?>, EntityDataComponentRegistration<?, ?>> REGISTRATIONS =
            new RegistrationsBuilder()
                    // Shared entity flags
                    .putBitFlag(EntityDataComponent.ON_FIRE, 0, 0)
                    .putBitFlag(EntityDataComponent.SNEAKING, 0, 1)
                    .putBitFlag(EntityDataComponent.SPRINTING, 0, 3)
                    .putBitFlag(EntityDataComponent.SWIMMING, 0, 4)
                    .putBitFlag(EntityDataComponent.INVISIBLE, 0, 5)
                    .putBitFlag(EntityDataComponent.GLOWING, 0, 6)
                    .putBitFlag(EntityDataComponent.GLIDING, 0, 7)
                    // Other components that are used by all kind of entities
                    .put(
                            EntityDataComponent.AIR_SUPPLY, 1, EntityMetadataValue.Int.class,
                            entityType -> true, EntityMetadataValue.Int::value,
                            (currentMetadataValue, value) -> new EntityMetadataValue.Int(Objects.requireNonNull(value))
                    )
                    .build();

    private EntityDataComponentRegistry() {}

    /**
     * Gets a registration data of the specified {@linkplain EntityDataComponent entity data component}.
     *
     * @param component the entity data component whose registration data should be returned
     * @return the entity data component registration data
     * @param <V> the value type of the entity data component whose registration data should be returned
     * @throws IllegalArgumentException if the specified entity data component was not registered
     * @since 1.0
     */
    public static <V> EntityDataComponentRegistration<V, ?> registration(EntityDataComponent<V> component) {
        EntityDataComponentRegistration<?, ?> registration = REGISTRATIONS.get(component);
        if (registration == null)
            throw new IllegalArgumentException("Unregistered entity data component: " + component);
        // noinspection unchecked ; the registration was safely created via the registrations builder
        return (EntityDataComponentRegistration<V, ?>) registration;
    }

    /**
     * A builder of a {@linkplain Map map} associating {@linkplain EntityDataComponent entity data components}
     * with their {@linkplain EntityDataComponentRegistration registrations}.
     *
     * @since 1.0
     * @see EntityDataComponent
     * @see EntityDataComponentRegistration
     * @see Map
     */
    private static final class RegistrationsBuilder {

        private final Map<EntityDataComponent<?>, EntityDataComponentRegistration<?, ?>> registrations = new HashMap<>();

        /**
         * Associates the specified {@linkplain EntityDataComponent entity data component}
         * with an {@linkplain EntityDataComponentRegistration entity data component registration}
         * with the specified data.
         *
         * @param component the entity data component to be associated with the entity data component registration
         * @param metadataIndex entity metadata index where entity metadata values that are
         *                      associated with the specified entity data component should be put at
         * @param metadataValueClass the class of entity metadata values that should be able
         *                           to be associated with the specified entity data component
         * @param entityTypePredicate a predicate that should check whether entities with entity type provided during
         *                            predicate testing should support the specified entity data component
         * @param componentValueDecoder a function that should convert entity metadata values
         *                              to values supported by the specified entity data component
         * @param componentValueEncoder a function that should provide a value that the current entity metadata value
         *                              should be replaced with when the value of the specified entity data component
         *                              gets updated, the function accepts the new entity data component value
         *                              and the current entity metadata value
         * @return this builder
         * @param <V> the type of values that the specified entity data component supports
         * @param <MV> the type of entity metadata values that can contain
         *             values of the specified entity data component
         * @since 1.0
         */
        private <V, MV extends EntityMetadataValue> RegistrationsBuilder put(
                EntityDataComponent<V> component,
                int metadataIndex, Class<MV> metadataValueClass,
                Predicate<Holder.Reference<EntityType>> entityTypePredicate,
                ComponentValueDecoder<MV, V> componentValueDecoder,
                ComponentValueEncoder<MV, V> componentValueEncoder
        ) {
            this.registrations.put(component, new EntityDataComponentRegistration<>(
                    metadataIndex, metadataValueClass, entityTypePredicate,
                    componentValueDecoder, componentValueEncoder
            ));
            return this;
        }

        /**
         * Registers the specified {@linkplain Boolean boolean} {@linkplain EntityDataComponent entity data component}.
         *
         * <p>Updating the specified component will update a single bit of
         * a {@linkplain EntityMetadataValue.Byte byte entity metadata value} - {@code true} value
         * sets a bit, {@code false} value unsets a bit.</p>
         *
         * @param component the entity data component to register, must not be nullable
         * @param metadataIndex entity metadata index where entity metadata values that are
         *                      associated with the specified entity data component should be put at
         * @param entityTypePredicate a predicate that should check whether entities with entity type provided during
         *                            predicate testing should support the specified entity data component
         * @param flagIndex the index of the bit that the entity data component should update,
         *                  where {@code 0} is the least significant bit
         * @return this builder
         * @throws IllegalArgumentException if the specified entity data component is nullable
         * @since 1.0
         */
        private RegistrationsBuilder putBitFlag(EntityDataComponent<Boolean> component, int metadataIndex,
                                                Predicate<Holder.Reference<EntityType>> entityTypePredicate,
                                                int flagIndex) {
            if (component.nullable())
                throw new IllegalArgumentException("Nullable components cannot be registered as bit flag components");

            return this.put(
                    component, metadataIndex, EntityMetadataValue.Byte.class, entityTypePredicate,
                    metadataValue -> ByteUtil.bitSet(metadataValue.value(), flagIndex),
                    (metadataValue, value) -> new EntityMetadataValue.Byte(ByteUtil.withBit(
                            metadataValue.value(), flagIndex,
                            Objects.requireNonNull(value)
                    ))
            );
        }

        /**
         * Registers the specified {@linkplain Boolean boolean} {@linkplain EntityDataComponent entity data component}.
         *
         * <p>Updating the specified component will update a single bit of
         * a {@linkplain EntityMetadataValue.Byte byte entity metadata value} - {@code true} value
         * sets a bit, {@code false} value unsets a bit.</p>
         *
         * <p>The {@linkplain EntityDataComponent entity data component} is going
         * to be supported by all {@linkplain EntityType entity types}.</p>
         *
         * @param component the entity data component to register
         * @param metadataIndex entity metadata index where entity metadata values that are
         *                      associated with the specified entity data component should be put at
         * @param flagIndex the index of the bit that the entity data component should update,
         *                  where {@code 0} is the least significant bit
         * @return this builder
         * @since 1.0
         */
        private RegistrationsBuilder putBitFlag(EntityDataComponent<Boolean> component,
                                                int metadataIndex, int flagIndex) {
            return this.putBitFlag(component, metadataIndex, entityType -> true, flagIndex);
        }

        /**
         * Builds the {@linkplain EntityDataComponentRegistration entity data component registration}
         * {@linkplain Map map}.
         *
         * @return the created map
         * @since 1.0
         */
        private Map<EntityDataComponent<?>, EntityDataComponentRegistration<?, ?>> build() {
            return Map.copyOf(this.registrations);
        }
    }
}