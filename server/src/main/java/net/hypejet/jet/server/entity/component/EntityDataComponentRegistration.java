package net.hypejet.jet.server.entity.component;

import net.hypejet.jet.entity.EntityType;
import net.hypejet.jet.entity.component.EntityDataComponent;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * A data of an {@linkplain EntityDataComponent entity data component} registration
 * in the {@linkplain EntityDataComponentRegistry entity data component registry}.
 *
 * @param metadataIndex entity metadata index where entity metadata values that are associated
 *                      with the registered entity data component are put at
 * @param metadataValueClass the class of entity metadata values that can be
 *                           associated with the registered entity data component
 * @param entityTypePredicate a predicate checking whether entities with entity type provided during
 *                            predicate testing should support the registered entity data component
 * @param componentValueDecoder a function converting entity metadata values to
 *                              values supported by the registered entity data component
 * @param componentValueEncoder a function providing a value that the current entity metadata value should be
 *                              replaced with when the value of the registered entity data component gets updated,
 *                              the function accepts the new entity data component value and the current entity
 *                              metadata value
 * @param <V> the type of values that the entity data component associated with this registration supports
 * @param <MV> the type of entity metadata values that can contain values of the registered entity data component
 * @since 1.0
 * @see EntityDataComponent
 * @see EntityDataComponentRegistry
 */
@NullMarked
public record EntityDataComponentRegistration<V, MV extends EntityMetadataValue>(
        int metadataIndex, Class<MV> metadataValueClass,
        Predicate<Holder.Reference<EntityType>> entityTypePredicate,
        ComponentValueDecoder<MV, V> componentValueDecoder,
        ComponentValueEncoder<MV, V> componentValueEncoder
) {
    /**
     * Constructs the {@linkplain EntityDataComponentRegistration entity data component registration}.
     *
     * @param metadataIndex entity metadata index where entity metadata values that are associated
     *                      with the registered entity data component should be put at
     * @param metadataValueClass the class of entity metadata values that should be able
     *                           to be associated with the registered entity data component
     * @param entityTypePredicate a predicate that should check whether entities with entity type provided during
     *                            predicate testing should support the entity data component that is going
     *                            to be associated with the registration which is being constructed
     * @param componentValueDecoder a function that should convert entity metadata values to values
     *                              supported by entity data component that is going to be associated
     *                              with the registration which is being constructed
     * @param componentValueEncoder a function that should provide a value that the current entity metadata value
     *                              should be replaced with when the value of the registered entity data component
     *                              gets updated, the function accepts the new entity data component value
     *                              and the current entity metadata value
     * @since 1.0
     */
    public EntityDataComponentRegistration {
        Objects.requireNonNull(metadataValueClass, "metadata value class");
        Objects.requireNonNull(entityTypePredicate, "entity type predicate");
        Objects.requireNonNull(componentValueDecoder, "component value decoder");
        Objects.requireNonNull(componentValueEncoder, "component value encoder");
    }
}