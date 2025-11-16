package net.hypejet.jet.server.entity.component;

import net.hypejet.jet.entity.EntityType;
import net.hypejet.jet.entity.component.EntityDataComponent;
import net.hypejet.jet.entity.component.EntityDataComponentMap;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.entity.metadata.EntityMetadata;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * An implementation of the {@linkplain EntityDataComponentMap entity data component map}
 * backed by an {@linkplain EntityMetadata entity metadata}.
 *
 * @since 1.0
 * @see EntityMetadata
 * @see EntityDataComponentMap
 */
@NullMarked
public final class JetEntityDataComponentMap implements EntityDataComponentMap {

    private final Holder.Reference<EntityType> entityType;
    private final EntityMetadata entityMetadata;

    /**
     * Constructs the {@linkplain JetEntityDataComponentMap entity data component map implementation}.
     *
     * @param server the server of the entity that the entity data component map is being constructed for
     * @param entityType the entity type of the entity that the entity data component map is being constructed for
     * @since 1.0
     */
    public JetEntityDataComponentMap(JetMinecraftServer server, Holder.Reference<EntityType> entityType) {
        this.entityType = entityType;
        this.entityMetadata = new EntityMetadata(server, entityType);
    }

    @Override
    public <V> V value(EntityDataComponent<V> component) {
        Objects.requireNonNull(component, "component");
        return this.value(ensureSupported(this.entityType, component));
    }

    @Override
    public <V> void value(EntityDataComponent<V> component, V value) {
        Objects.requireNonNull(component, "component");
        Objects.requireNonNull(value, "value");
        this.value(ensureSupported(this.entityType, component), value);
    }

    private <V, MV extends EntityMetadataValue> V value(EntityDataComponentRegistration<V, MV> componentRegistration) {
        return componentRegistration.metadataValueToValueFunction().apply(this.entityMetadata.value(
                componentRegistration.metadataIndex(),
                componentRegistration.metadataValueClass()
        ));
    }

    private <V, MV extends EntityMetadataValue> void value(
            EntityDataComponentRegistration<V, MV> componentRegistration,
            V value
    ) {
        this.entityMetadata.value(
                componentRegistration.metadataIndex(),
                componentRegistration.metadataValueClass(),
                metadataValue -> componentRegistration.updatedMetadataValueFunction().apply(value, metadataValue)
        );
    }

    private static <V> EntityDataComponentRegistration<V, ?> ensureSupported(Holder.Reference<EntityType> entityType,
                                                                             EntityDataComponent<V> component) {
        EntityDataComponentRegistration<V, ?> registration = EntityDataComponentRegistry.registration(component);
        if (!registration.entityTypePredicate().test(entityType)) {
            throw new IllegalArgumentException(String.format(
                    "Entity type \"%s\" does not support \"%s\" entity data component",
                    entityType, component
            ));
        }
        return registration;
    }
}