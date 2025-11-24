package net.hypejet.jet.server.entity.component;

import net.hypejet.jet.entity.EntityType;
import net.hypejet.jet.entity.component.EntityDataComponent;
import net.hypejet.jet.entity.component.EntityDataComponentMap;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.entity.metadata.EntityMetadata;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.util.game.entity.EntityTypePredicate;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

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
    private final JetMinecraftServer server;
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
        this.server = server;
        this.entityMetadata = new EntityMetadata(server, entityType);
    }

    @Override
    public <V> @Nullable V value(EntityDataComponent<V> component) {
        Objects.requireNonNull(component, "component");

        V value = this.value(this.ensureSupported(component));
        if (value == null && !component.nullable()) {
            throw new IllegalStateException(String.format(
                    "Registration for entity data component \"%s\" returned null while the component is not nullable",
                    component.name()
            ));
        }

        return value;
    }

    @Override
    public <V> void value(EntityDataComponent<V> component, @Nullable V value) {
        Objects.requireNonNull(component, "component");

        if (value == null && !component.nullable()) {
            throw new IllegalArgumentException(String.format(
                    "The specified value is null, but the specified entity data component \"%s\" is not nullable",
                    component.name()
            ));
        }

        this.value(this.ensureSupported(component), value);
    }

    private <V, MV extends EntityMetadataValue> @Nullable V value(
            EntityDataComponentRegistration<V, MV> componentRegistration
    ) {
        return componentRegistration.componentValueDecoder().decode(this.entityMetadata.value(
                componentRegistration.metadataIndex(),
                componentRegistration.metadataValueClass()
        ));
    }

    private <V, MV extends EntityMetadataValue> void value(
            EntityDataComponentRegistration<V, MV> componentRegistration,
            @Nullable V value
    ) {
        this.entityMetadata.value(
                componentRegistration.metadataIndex(),
                componentRegistration.metadataValueClass(),
                metadataValue -> componentRegistration.componentValueEncoder().encode(metadataValue, value)
        );
    }

    private <V> EntityDataComponentRegistration<V, ?> ensureSupported(EntityDataComponent<V> component) {
        EntityDataComponentRegistration<V, ?> registration = EntityDataComponentRegistry.registration(component);
        if (!EntityTypePredicate.test(registration.entityTypePredicate(), this.entityType, this.server)) {
            throw new IllegalArgumentException(String.format(
                    "Entity type \"%s\" does not support \"%s\" entity data component",
                    this.entityType, component
            ));
        }
        return registration;
    }
}