package net.hypejet.jet.server.entity.metadata;

import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.hypejet.jet.entity.EntityType;
import net.hypejet.jet.entity.enderdragon.EnderDragonPhase;
import net.hypejet.jet.entity.pose.Pose;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.registry.keys.EntityTypeKeys;
import net.hypejet.jet.registry.keys.ParticleTypeKeys;
import net.hypejet.jet.registry.reference.RegistryReference;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.JetEntityType;
import net.hypejet.jet.server.entity.enderdragon.EnderDragonPhaseRegistry;
import net.hypejet.jet.server.util.game.entity.EntityTypePredicate;
import net.hypejet.jet.util.color.ARGBColor;
import net.hypejet.jet.world.coordinate.rotation.Rotations;
import net.hypejet.jet.world.particle.color.ColorParticle;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Objects;
import java.util.Set;

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
            /* --------------------- Defaults for all entities --------------------- */
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
            new DefaultsEntry(7, new EntityMetadataValue.Int(0)), // Ticks frozen

            /* --------------------- Defaults for living entities --------------------- */
            new DefaultsEntry(8, new EntityMetadataValue.Byte((byte) 0), EntityTypePredicate.LIVING), // Living entity flags
            new DefaultsEntry(9, new EntityMetadataValue.Float(1f), EntityTypePredicate.LIVING), // Health
            new DefaultsEntry(10, new EntityMetadataValue.ParticleList(List.of()), EntityTypePredicate.LIVING), // Potion particle effects
            new DefaultsEntry(11, new EntityMetadataValue.Boolean(false), EntityTypePredicate.LIVING), // Reduce potion particle effects
            new DefaultsEntry(12, new EntityMetadataValue.Int(0), EntityTypePredicate.LIVING), // Arrow count
            new DefaultsEntry(13, new EntityMetadataValue.Int(0), EntityTypePredicate.LIVING), // Stinger count
            new DefaultsEntry(14, new EntityMetadataValue.OptionalBlockPositionValue(null), EntityTypePredicate.LIVING), // Sleeping position

            /* --------------------- Defaults for mob entities --------------------- */
            new DefaultsEntry(15, new EntityMetadataValue.Byte((byte) 0), EntityTypePredicate.MOB), // Mob flags

            /* --------------------- Defaults for ghast entities --------------------- */
            new DefaultsEntry(16, new EntityMetadataValue.Boolean(false), EntityTypePredicate.typed(EntityTypeKeys.GHAST)), // Fireball charging

            /* --------------------- Defaults for phantom, slime and magma cube entities --------------------- */
            new DefaultsEntry(
                    16,
                    (server, entityType) -> new EntityMetadataValue.Int(entityType.key().equals(EntityTypeKeys.PHANTOM) ? 0 : 1),
                    EntityTypePredicate.typed(EntityTypeKeys.PHANTOM, EntityTypeKeys.SLIME, EntityTypeKeys.MAGMA_CUBE)
            ), // Size

            /* --------------------- Defaults for end crystal entities --------------------- */
            new DefaultsEntry(
                    8, new EntityMetadataValue.OptionalBlockPositionValue(null),
                    EntityTypePredicate.typed(EntityTypeKeys.END_CRYSTAL)
            ), // Beam target
            new DefaultsEntry(
                    9, new EntityMetadataValue.Boolean(true),
                    EntityTypePredicate.typed(EntityTypeKeys.END_CRYSTAL)
            ), // Show bottom

            /* --------------------- Defaults for area effect cloud entities --------------------- */
            new DefaultsEntry(
                    8, new EntityMetadataValue.Float(3f),
                    EntityTypePredicate.typed(EntityTypeKeys.AREA_EFFECT_CLOUD)
            ), // Radius
            new DefaultsEntry(
                    9, new EntityMetadataValue.Boolean(false),
                    EntityTypePredicate.typed(EntityTypeKeys.AREA_EFFECT_CLOUD)
            ), // Waiting
            new DefaultsEntry(
                    10,
                    new EntityMetadataValue.ParticleValue(new ColorParticle(
                            new Holder.Reference<>(ParticleTypeKeys.ENTITY_EFFECT),
                            ARGBColor.fromARGB(255, 255, 255, 255)
                    )),
                    EntityTypePredicate.typed(EntityTypeKeys.AREA_EFFECT_CLOUD)
            ), // Particle

            /* --------------------- Defaults for armor stand entities --------------------- */
            new DefaultsEntry(
                    15, new EntityMetadataValue.Byte((byte) 0),
                    EntityTypePredicate.typed(EntityTypeKeys.ARMOR_STAND)
            ), // Client flags
            new DefaultsEntry(
                    16, new EntityMetadataValue.RotationsValue(new Rotations(0f, 0f, 0f)),
                    EntityTypePredicate.typed(EntityTypeKeys.ARMOR_STAND)
            ), // Head pose
            new DefaultsEntry(
                    17, new EntityMetadataValue.RotationsValue(new Rotations(0f, 0f, 0f)),
                    EntityTypePredicate.typed(EntityTypeKeys.ARMOR_STAND)
            ), // Body pose
            new DefaultsEntry(
                    18, new EntityMetadataValue.RotationsValue(new Rotations(-10f, 0f, -10f)),
                    EntityTypePredicate.typed(EntityTypeKeys.ARMOR_STAND)
            ), // Left arm pose
            new DefaultsEntry(
                    19, new EntityMetadataValue.RotationsValue(new Rotations(-15f, 0f, 10f)),
                    EntityTypePredicate.typed(EntityTypeKeys.ARMOR_STAND)
            ), // Right arm pose
            new DefaultsEntry(
                    20, new EntityMetadataValue.RotationsValue(new Rotations(-1f, 0f, -1f)),
                    EntityTypePredicate.typed(EntityTypeKeys.ARMOR_STAND)
            ), // Left leg pose
            new DefaultsEntry(
                    21, new EntityMetadataValue.RotationsValue(new Rotations(1f, 0f, 1f)),
                    EntityTypePredicate.typed(EntityTypeKeys.ARMOR_STAND)
            ), // Right leg pose

            /* --------------------- Defaults for ender dragon entities --------------------- */
            new DefaultsEntry(
                    16, new EntityMetadataValue.Int(EnderDragonPhaseRegistry.phaseId(EnderDragonPhase.HOVERING)),
                    EntityTypePredicate.typed(EntityTypeKeys.ENDER_DRAGON)
            ), // Ender dragon phase

            /* --------------------- Defaults for allay entities --------------------- */
            new DefaultsEntry(
                    16, new EntityMetadataValue.Boolean(false),
                    EntityTypePredicate.typed(EntityTypeKeys.ALLAY)
            ), // Dancing
            new DefaultsEntry(
                    17, new EntityMetadataValue.Boolean(true),
                    EntityTypePredicate.typed(EntityTypeKeys.ALLAY)
            ), // Can duplicate

            /* --------------------- Defaults for bat entities --------------------- */
            new DefaultsEntry(
                    16, new EntityMetadataValue.Byte((byte) 0),
                    EntityTypePredicate.typed(EntityTypeKeys.BAT)
            ), // Bat flags

            /* --------------------- Defaults for creaking entities --------------------- */
            new DefaultsEntry(
                    16, new EntityMetadataValue.Boolean(true),
                    EntityTypePredicate.typed(EntityTypeKeys.CREAKING)
            ), // Can move
            new DefaultsEntry(
                    17, new EntityMetadataValue.Boolean(false),
                    EntityTypePredicate.typed(EntityTypeKeys.CREAKING)
            ), // Active
            new DefaultsEntry(
                    18, new EntityMetadataValue.Boolean(false),
                    EntityTypePredicate.typed(EntityTypeKeys.CREAKING)
            ), // Tearing down
            new DefaultsEntry(
                    19, new EntityMetadataValue.OptionalBlockPositionValue(null),
                    EntityTypePredicate.typed(EntityTypeKeys.CREAKING)
            ), // Creaking heart position

            /* --------------------- Defaults for blaze entities --------------------- */
            new DefaultsEntry(
                    16, new EntityMetadataValue.Byte((byte) 0),
                    EntityTypePredicate.typed(EntityTypeKeys.BLAZE)
            ), // Blaze flags

            /* --------------------- Defaults for spider entities --------------------- */
            new DefaultsEntry(
                    16, new EntityMetadataValue.Byte((byte) 0),
                    EntityTypePredicate.typed(EntityTypeKeys.SPIDER, EntityTypeKeys.CAVE_SPIDER)
            ), // Spider flags

            /* --------------------- Defaults for warden entities --------------------- */
            new DefaultsEntry(
                    16, new EntityMetadataValue.Int(0),
                    EntityTypePredicate.typed(EntityTypeKeys.WARDEN)
            )
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
            if (!EntityTypePredicate.test(entry.entityTypePredicate(), entityType, server)) continue;
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
                                 EntityTypePredicate entityTypePredicate) {
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
                              EntityTypePredicate entityTypePredicate) {
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
            this(index, defaultValueProvider, EntityTypePredicate.TRUE);
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