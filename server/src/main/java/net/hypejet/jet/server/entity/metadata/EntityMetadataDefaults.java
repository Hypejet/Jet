package net.hypejet.jet.server.entity.metadata;

import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.hypejet.jet.entity.EntityType;
import net.hypejet.jet.entity.pose.Pose;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.registry.reference.RegistryReference;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.JetEntityType;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Something providing default {@linkplain EntityMetadataValue entity metadata values}
 * for different {@linkplain EntityType entity types}.
 *
 * @since 1.0
 * @see EntityType
 * @see EntityMetadataValue
 */
@NullMarked
public final class EntityMetadataDefaults {

    private static final Set<DefaultsEntry> ENTRIES = Set.of(
            new DefaultsEntry(0, new EntityMetadataValue.Byte((byte) 0)), // Shared entity flags
            new DefaultsEntry(1, (server, entityType) -> new EntityMetadataValue.Int(
                    JetEntityType.cast(
                            entityType.valueOrThrow(server.registryManager().registry(RegistryReference.ENTITY_TYPE))
                    ).maxAirSupply()
            )), // Air supply
            new DefaultsEntry(2, new EntityMetadataValue.OptionalComponentValue(null)), // Custom name
            new DefaultsEntry(3, new EntityMetadataValue.Boolean(false)), // Custom name visible
            new DefaultsEntry(4, new EntityMetadataValue.Boolean(false)), // Silent
            new DefaultsEntry(5, new EntityMetadataValue.Boolean(false)), // No gravity
            new DefaultsEntry(6, new EntityMetadataValue.PoseValue(Pose.STANDING)), // Pose
            new DefaultsEntry(7, new EntityMetadataValue.Int(0)) // Ticks frozen
    );

    private EntityMetadataDefaults() {}

    /**
     * Provides an {@linkplain IntObjectMap int-object map} associating {@linkplain EntityMetadata entity metadata}
     * indices with default {@linkplain EntityMetadataValue entity metadata values} that should be used
     * by an {@linkplain JetEntity entity} with the specified {@linkplain EntityType entity type}.
     *
     * @param server the server that the entity is on
     * @param entityType the entity type of the entity that is going to use the provided default entity metadata values
     * @return the int-object map containing default entity metadata values for the specified entity type
     * @since 1.0
     */
    public static IntObjectMap<EntityMetadataValue> defaultsFor(JetMinecraftServer server,
                                                                Holder.Reference<EntityType> entityType) {
        IntObjectMap<EntityMetadataValue> defaultValues = new IntObjectHashMap<>();
        for (DefaultsEntry entry : ENTRIES) {
            if (!entry.entityTypePredicate().test(entityType)) continue;
            defaultValues.put(entry.index(), entry.defaultValueProvider().provide(server, entityType));
        }
        return defaultValues;
    }

    /**
     * A definition of a default {@linkplain EntityMetadataValue entity metadata value} to be put
     * to {@linkplain EntityMetadata entity metadata} of {@linkplain JetEntity entities} in construction phase.
     *
     * @param index the entity metadata index that the value should be put at
     * @param defaultValueProvider a default value provider providing the default value
     * @param entityTypePredicate a predicate which checks whether entities with entity type
     *                            provided during predicate testing should use an entity metadata
     *                            value specified by this defaults entry
     * @since 1.0
     * @see EntityMetadataValue
     * @see EntityMetadata
     * @see JetEntity
     */
    private record DefaultsEntry(int index, DefaultValueProvider defaultValueProvider,
                                 Predicate<Holder.Reference<EntityType>> entityTypePredicate) {
        /**
         * Constructs the {@linkplain DefaultsEntry defaults entry} that is going
         * to be used by all kind of {@linkplain JetEntity entities}.
         *
         * @param index the entity metadata index that the value should be put at
         * @param defaultValue the default entity metadata value
         * @since 1.0
         */
        private DefaultsEntry(int index, EntityMetadataValue defaultValue) {
            // TODO: JDK 25 pre-"this" nullability check
            this(index, (server, entityType) -> defaultValue);
        }

        /**
         * Constructs the {@linkplain DefaultsEntry defaults entry}.
         *
         * @param index the entity metadata index that the value should be put at
         * @param defaultValue the default entity metadata value
         * @param entityTypePredicate a predicate that should check whether entities with entity type
         *                            provided during predicate testing should use the specified
         *                            entity metadata value
         * @since 1.0
         */
        private DefaultsEntry(int index, EntityMetadataValue defaultValue,
                              Predicate<Holder.Reference<EntityType>> entityTypePredicate) {
            // TODO: JDK 25 pre-"this" nullability check
            this(index, (server, entityType) -> defaultValue, entityTypePredicate);
        }

        /**
         * Constructs the {@linkplain DefaultsEntry defaults entry} that is going
         * to be used by all kind of {@linkplain JetEntity entities}.
         *
         * @param index the entity metadata index that the value should be put at
         * @param defaultValueProvider a default value provider that should provide the default value
         * @since 1.0
         */
        private DefaultsEntry(int index, DefaultValueProvider defaultValueProvider) {
            this(index, defaultValueProvider, entityType -> true);
        }

        /**
         * Constructs the {@linkplain DefaultsEntry defaults entry}.
         *
         * @param index the entity metadata index that the value should be put at
         * @param defaultValueProvider a default value provider that should provide the default value
         * @param entityTypePredicate a predicate that should check whether entities with entity type
         *                            provided during predicate testing should use the specified
         *                            entity metadata value
         * @since 1.0
         */
        private DefaultsEntry {
            Objects.requireNonNull(defaultValueProvider, "default value provider");
            Objects.requireNonNull(entityTypePredicate, "entity type predicate");
        }
    }

    /**
     * A function providing an {@linkplain EntityMetadataValue entity metadata value} that
     * an {@linkplain JetEntity entity} should use by default in their {@linkplain EntityMetadata entity metadata}.
     *
     * @since 1.0
     * @see EntityMetadataValue
     * @see EntityMetadata
     * @see JetEntity
     */
    @FunctionalInterface
    private interface DefaultValueProvider {
        /**
         * Provides the default {@linkplain EntityMetadataValue entity metadata value}
         * for an {@linkplain JetEntity entity}.
         *
         * @param server the server that the entity is on
         * @param entityType the entity type of the entity
         * @return the default entity metadata value
         * @since 1.0
         */
        EntityMetadataValue provide(JetMinecraftServer server, Holder.Reference<EntityType> entityType);
    }
}