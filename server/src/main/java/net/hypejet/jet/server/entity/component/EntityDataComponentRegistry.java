package net.hypejet.jet.server.entity.component;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.EntityType;
import net.hypejet.jet.entity.component.EntityDataComponent;
import net.hypejet.jet.registry.keys.EntityTypeKeys;
import net.hypejet.jet.server.entity.enderdragon.EnderDragonPhaseRegistry;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.util.game.entity.EntityTypePredicate;
import net.hypejet.jet.server.util.number.ByteUtil;
import net.hypejet.jet.server.world.block.state.JetBlockState;
import net.hypejet.jet.world.block.state.BlockState;
import net.hypejet.jet.world.coordinate.BlockPosition;
import net.hypejet.jet.world.coordinate.rotation.Rotations;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
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
                    /* ---------------- Entity components applicable to all kind of entities ---------------- */
                    .putBitFlag(EntityDataComponent.ON_FIRE, 0, 0)
                    .putBitFlag(EntityDataComponent.SNEAKING, 0, 1)
                    .putBitFlag(EntityDataComponent.SPRINTING, 0, 3)
                    .putBitFlag(EntityDataComponent.SWIMMING, 0, 4)
                    .putBitFlag(EntityDataComponent.INVISIBLE, 0, 5)
                    .putBitFlag(EntityDataComponent.GLOWING, 0, 6)
                    .putBitFlag(EntityDataComponent.GLIDING, 0, 7)
                    .putInt(EntityDataComponent.AIR_SUPPLY, 1, EntityTypePredicate.TRUE)
                    .put(
                            EntityDataComponent.CUSTOM_NAME, 2, EntityMetadataValue.OptionalComponentValue.class,
                            EntityTypePredicate.TRUE, EntityMetadataValue.OptionalComponentValue::value,
                            (ignored, value) -> new EntityMetadataValue.OptionalComponentValue(value)
                    )
                    .putBoolean(EntityDataComponent.CUSTOM_NAME_VISIBLE, 3, EntityTypePredicate.TRUE)
                    .putBoolean(EntityDataComponent.SILENT, 4, EntityTypePredicate.TRUE)
                    .putBoolean(EntityDataComponent.NO_GRAVITY, 5, EntityTypePredicate.TRUE)
                    .put(
                            EntityDataComponent.POSE, 6, EntityMetadataValue.PoseValue.class,
                            EntityTypePredicate.TRUE, EntityMetadataValue.PoseValue::value,
                            (ignored, value) -> new EntityMetadataValue.PoseValue(Objects.requireNonNull(value))
                    )
                    .putInt(EntityDataComponent.TICKS_FROZEN, 7, EntityTypePredicate.TRUE)

                    /* ---------------- Entity components applicable to living entities ---------------- */
                    .putBitFlag(EntityDataComponent.USING_ITEM, 8, 0, EntityTypePredicate.LIVING)
                    .putBitFlag(
                            EntityDataComponent.USED_ITEM_HAND, 8, 1, EntityTypePredicate.LIVING,
                            value -> value ? Entity.InteractionHand.OFFHAND : Entity.InteractionHand.MAIN_HAND,
                            value -> value == Entity.InteractionHand.OFFHAND
                    )
                    .putBitFlag(EntityDataComponent.AUTO_SPIN_ATTACK, 8, 2, EntityTypePredicate.LIVING)
                    .putFloat(EntityDataComponent.HEALTH, 9, EntityTypePredicate.LIVING)
                    .put(
                            EntityDataComponent.POTION_PARTICLES, 10, EntityMetadataValue.ParticleList.class,
                            EntityTypePredicate.LIVING, EntityMetadataValue.ParticleList::value,
                            (ignored, value) -> new EntityMetadataValue.ParticleList(Objects.requireNonNull(value))
                    )
                    .putBoolean(EntityDataComponent.REDUCE_POTION_PARTICLES, 11, EntityTypePredicate.LIVING)
                    .putInt(EntityDataComponent.ARROW_COUNT, 12, EntityTypePredicate.LIVING)
                    .putInt(EntityDataComponent.STINGER_COUNT, 13, EntityTypePredicate.LIVING)
                    .putOptionalBlockPos(EntityDataComponent.SLEEPING_POSITION, 14, EntityTypePredicate.LIVING)

                    /* ---------------- Entity components applicable to mob entities ---------------- */
                    .putBitFlag(EntityDataComponent.NO_AI, 15, 0, EntityTypePredicate.MOB)
                    .putBitFlag(EntityDataComponent.LEFT_HANDED, 15, 1, EntityTypePredicate.MOB)
                    .putBitFlag(EntityDataComponent.AGGRESSIVE, 15, 2, EntityTypePredicate.MOB)

                    /* ---------------- Entity components applicable to ghast entities ---------------- */
                    .putBoolean(
                            EntityDataComponent.FIREBALL_CHARGING, 16,
                            EntityTypePredicate.typed(EntityTypeKeys.GHAST)
                    )

                    /* ---------- Entity components applicable to phantom, slime and magma cube entities ---------- */
                    .putInt(
                            EntityDataComponent.SIZE, 16,
                            EntityTypePredicate.typed(
                                    EntityTypeKeys.PHANTOM,
                                    EntityTypeKeys.SLIME,
                                    EntityTypeKeys.MAGMA_CUBE
                            )
                    )

                    /* ---------------- Entity components applicable to end crystal entities ---------------- */
                    .putOptionalBlockPos(
                            EntityDataComponent.BEAM_TARGET, 8,
                            EntityTypePredicate.typed(EntityTypeKeys.END_CRYSTAL)
                    )
                    .putBoolean(
                            EntityDataComponent.SHOW_BOTTOM, 9,
                            EntityTypePredicate.typed(EntityTypeKeys.END_CRYSTAL)
                    )

                    /* ---------------- Entity components applicable to area effect cloud entities ---------------- */
                    .putFloat(
                            EntityDataComponent.EFFECT_RADIUS, 8,
                            EntityTypePredicate.typed(EntityTypeKeys.AREA_EFFECT_CLOUD)
                    )
                    .putBoolean(
                            EntityDataComponent.EFFECT_WAITING, 9,
                            EntityTypePredicate.typed(EntityTypeKeys.AREA_EFFECT_CLOUD)
                    )
                    .put(
                            EntityDataComponent.EFFECT_PARTICLE, 10,
                            EntityMetadataValue.ParticleValue.class,
                            EntityTypePredicate.typed(EntityTypeKeys.AREA_EFFECT_CLOUD),
                            EntityMetadataValue.ParticleValue::value,
                            (currentMetadataValue, value) ->
                                    new EntityMetadataValue.ParticleValue(Objects.requireNonNull(value))
                    )

                    /* ---------------- Entity components applicable to armor stand entities ---------------- */
                    .putBitFlag(
                            EntityDataComponent.SMALL, 15, 0,
                            EntityTypePredicate.typed(EntityTypeKeys.ARMOR_STAND)
                    )
                    .putBitFlag(
                            EntityDataComponent.SHOW_ARMS, 15, 2,
                            EntityTypePredicate.typed(EntityTypeKeys.ARMOR_STAND)
                    )
                    .putBitFlag(
                            EntityDataComponent.HIDE_BASE_PLATE, 15, 3,
                            EntityTypePredicate.typed(EntityTypeKeys.ARMOR_STAND)
                    )
                    .putBitFlag(
                            EntityDataComponent.MARKER, 15, 4,
                            EntityTypePredicate.typed(EntityTypeKeys.ARMOR_STAND)
                    )
                    .putRotations(
                            EntityDataComponent.HEAD_POSE, 16,
                            EntityTypePredicate.typed(EntityTypeKeys.ARMOR_STAND)
                    )
                    .putRotations(
                            EntityDataComponent.BODY_POSE, 17,
                            EntityTypePredicate.typed(EntityTypeKeys.ARMOR_STAND)
                    )
                    .putRotations(
                            EntityDataComponent.LEFT_ARM_POSE, 18,
                            EntityTypePredicate.typed(EntityTypeKeys.ARMOR_STAND)
                    )
                    .putRotations(
                            EntityDataComponent.RIGHT_ARM_POSE, 19,
                            EntityTypePredicate.typed(EntityTypeKeys.ARMOR_STAND)
                    )
                    .putRotations(
                            EntityDataComponent.LEFT_LEG_POSE, 20,
                            EntityTypePredicate.typed(EntityTypeKeys.ARMOR_STAND)
                    )
                    .putRotations(
                            EntityDataComponent.RIGHT_LEG_POSE, 21,
                            EntityTypePredicate.typed(EntityTypeKeys.ARMOR_STAND)
                    )

                    /* ---------------- Entity components applicable to ender dragon entities ---------------- */
                    .put(
                            EntityDataComponent.ENDER_DRAGON_PHASE, 16, EntityMetadataValue.Int.class,
                            EntityTypePredicate.typed(EntityTypeKeys.ENDER_DRAGON),
                            metadataValue -> EnderDragonPhaseRegistry.phaseById(metadataValue.value()),
                            (currentMetadataValue, value) -> new EntityMetadataValue.Int(
                                    EnderDragonPhaseRegistry.phaseId(Objects.requireNonNull(value))
                            )
                    )

                    /* ---------------- Entity components applicable to allay entities ---------------- */
                    .putBoolean(EntityDataComponent.DANCING, 16, EntityTypePredicate.typed(EntityTypeKeys.ALLAY))
                    .putBoolean(EntityDataComponent.CAN_DUPLICATE, 17, EntityTypePredicate.typed(EntityTypeKeys.ALLAY))

                    /* ---------------- Entity components applicable to bat entities ---------------- */
                    .putBitFlag(EntityDataComponent.RESTING, 16, 0, EntityTypePredicate.typed(EntityTypeKeys.BAT))

                    /* ---------------- Entity components applicable to creaking entities ---------------- */
                    .putBoolean(EntityDataComponent.CAN_MOVE, 16, EntityTypePredicate.typed(EntityTypeKeys.CREAKING))
                    .putBoolean(EntityDataComponent.ACTIVE, 17, EntityTypePredicate.typed(EntityTypeKeys.CREAKING))
                    .putBoolean(
                            EntityDataComponent.TEARING_DOWN, 18,
                            EntityTypePredicate.typed(EntityTypeKeys.CREAKING)
                    ).putOptionalBlockPos(
                            EntityDataComponent.CREAKING_HEART_POSITION, 19,
                            EntityTypePredicate.typed(EntityTypeKeys.CREAKING)
                    )

                    /* ---------------- Entity components applicable to blaze and vex entities ---------------- */
                    .putBitFlag(
                            EntityDataComponent.CHARGED, 16, 0,
                            EntityTypePredicate.typed(EntityTypeKeys.BLAZE, EntityTypeKeys.VEX)
                    )

                    /* ---------------- Entity components applicable to spider entities ---------------- */
                    .putBitFlag(
                            EntityDataComponent.CLIMBING, 16, 0,
                            EntityTypePredicate.typed(EntityTypeKeys.SPIDER, EntityTypeKeys.CAVE_SPIDER)
                    )

                    /* ---------------- Entity components applicable to warden entities ---------------- */
                    .putInt(EntityDataComponent.ANGER_LEVEL, 16, EntityTypePredicate.typed(EntityTypeKeys.WARDEN))

                    /* ---------------- Entity components applicable to enderman entities ---------------- */
                    .putOptionalBlockState(
                            EntityDataComponent.CARRIED_BLOCK, 16,
                            EntityTypePredicate.typed(EntityTypeKeys.ENDERMAN)
                    )
                    .putBoolean(EntityDataComponent.SCREAMING, 17, EntityTypePredicate.typed(EntityTypeKeys.ENDERMAN))
                    .putBoolean(EntityDataComponent.STARED_AT, 18, EntityTypePredicate.typed(EntityTypeKeys.ENDERMAN))

                    /* ---------------- Entity components applicable to piglin entities ---------------- */
                    .putBoolean(
                            EntityDataComponent.IMMUNE_TO_ZOMBIFICATION, 16,
                            EntityTypePredicate.typed(EntityTypeKeys.PIGLIN, EntityTypeKeys.PIGLIN_BRUTE)
                    )

                    /* ----------- Entity components applicable to entities with baby variants ----------- */
                    .putBoolean(
                            EntityDataComponent.BABY, 16,
                            EntityTypePredicate.and(
                                    EntityTypePredicate.typed(EntityTypeKeys.ZOGLIN),
                                    EntityTypePredicate.ZOMBIE
                            )
                    )

                    /* -------------- Entity components applicable to zombie-like entities -------------- */
                    // 17 index is reserved for legacy type field, but in modern versions it remains 0, therefore there is no need for a component
                    .putBoolean(EntityDataComponent.CONVERTING_TO_DROWNED, 18, EntityTypePredicate.ZOMBIE)

                    /* -------------- Entity components applicable to zombie villager entities -------------- */
                    .putBoolean(
                            EntityDataComponent.RECOVERING, 19,
                            EntityTypePredicate.typed(EntityTypeKeys.ZOMBIE_VILLAGER)
                    )
                    .put(
                            EntityDataComponent.VILLAGER_TYPE, 20, EntityMetadataValue.VillagerData.class,
                            EntityTypePredicate.typed(EntityTypeKeys.ZOMBIE_VILLAGER),
                            EntityMetadataValue.VillagerData::type,
                            (currentMetadataValue, value) -> new EntityMetadataValue.VillagerData(
                                    Objects.requireNonNull(value),
                                    currentMetadataValue.profession(),
                                    currentMetadataValue.level()
                            )
                    )
                    .put(
                            EntityDataComponent.VILLAGER_PROFESSION, 20, EntityMetadataValue.VillagerData.class,
                            EntityTypePredicate.typed(EntityTypeKeys.ZOMBIE_VILLAGER),
                            EntityMetadataValue.VillagerData::profession,
                            (currentMetadataValue, value) -> new EntityMetadataValue.VillagerData(
                                    currentMetadataValue.type(),
                                    Objects.requireNonNull(value),
                                    currentMetadataValue.level()
                            )
                    )
                    .put(
                            EntityDataComponent.VILLAGER_LEVEL, 20, EntityMetadataValue.VillagerData.class,
                            EntityTypePredicate.typed(EntityTypeKeys.ZOMBIE_VILLAGER),
                            EntityMetadataValue.VillagerData::level,
                            (currentMetadataValue, value) -> new EntityMetadataValue.VillagerData(
                                    currentMetadataValue.type(),
                                    currentMetadataValue.profession(),
                                    Objects.requireNonNull(value)
                            )
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
                EntityTypePredicate entityTypePredicate,
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
         * <p>The component is going to be backed by
         * a {@linkplain EntityMetadataValue.Boolean boolean entity metadata value}.</p>
         *
         * @param component the entity data component to register, must not be nullable
         * @param metadataIndex entity metadata index where entity metadata values that are
         *                      associated with the specified entity data component should be put at
         * @param entityTypePredicate a predicate that should check whether entities with entity type provided during
         *                            predicate testing should support the specified entity data component
         * @return this builder
         * @throws IllegalArgumentException if the specified entity data component is nullable
         * @since 1.0
         */
        private RegistrationsBuilder putBoolean(EntityDataComponent<Boolean> component, int metadataIndex,
                                                EntityTypePredicate entityTypePredicate) {
            if (component.nullable())
                throw new IllegalArgumentException("The entity data component must not be nullable");

            return this.put(
                    component, metadataIndex, EntityMetadataValue.Boolean.class,
                    entityTypePredicate, EntityMetadataValue.Boolean::value,
                    (ignored, value) -> new EntityMetadataValue.Boolean(Objects.requireNonNull(value))
            );
        }

        /**
         * Registers the specified {@linkplain Integer integer} {@linkplain EntityDataComponent entity data component}.
         *
         * <p>The component is going to be backed by
         * an {@linkplain EntityMetadataValue.Int int entity metadata value}.</p>
         *
         * @param component the entity data component to register, must not be nullable
         * @param metadataIndex entity metadata index where entity metadata values that are
         *                      associated with the specified entity data component should be put at
         * @param entityTypePredicate a predicate that should check whether entities with entity type provided during
         *                            predicate testing should support the specified entity data component
         * @return this builder
         * @throws IllegalArgumentException if the specified entity data component is nullable
         * @since 1.0
         */
        private RegistrationsBuilder putInt(EntityDataComponent<Integer> component, int metadataIndex,
                                            EntityTypePredicate entityTypePredicate) {
            if (component.nullable())
                throw new IllegalArgumentException("The entity data component must not be nullable");

            return this.put(
                    component, metadataIndex, EntityMetadataValue.Int.class,
                    entityTypePredicate, EntityMetadataValue.Int::value,
                    (ignored, value) -> new EntityMetadataValue.Int(Objects.requireNonNull(value))
            );
        }

        /**
         * Registers the specified {@linkplain Float float} {@linkplain EntityDataComponent entity data component}.
         *
         * <p>The component is going to be backed by
         * an {@linkplain EntityMetadataValue.Float float entity metadata value}.</p>
         *
         * @param component the entity data component to register, must not be nullable
         * @param metadataIndex entity metadata index where entity metadata values that are
         *                      associated with the specified entity data component should be put at
         * @param entityTypePredicate a predicate that should check whether entities with entity type provided during
         *                            predicate testing should support the specified entity data component
         * @return this builder
         * @throws IllegalArgumentException if the specified entity data component is nullable
         * @since 1.0
         */
        private RegistrationsBuilder putFloat(EntityDataComponent<Float> component, int metadataIndex,
                                              EntityTypePredicate entityTypePredicate) {
            if (component.nullable())
                throw new IllegalArgumentException("The entity data component must not be nullable");

            return this.put(
                    component, metadataIndex, EntityMetadataValue.Float.class,
                    entityTypePredicate, EntityMetadataValue.Float::value,
                    (ignored, value) -> new EntityMetadataValue.Float(Objects.requireNonNull(value))
            );
        }

        /**
         * Registers the specified nullable {@linkplain BlockPosition block-position}
         * {@linkplain EntityDataComponent entity data component}.
         *
         * <p>The component is going to be backed by
         * an {@linkplain EntityMetadataValue.OptionalBlockPositionValue optional
         * block position entity metadata value}.</p>
         *
         * @param component the entity data component to register, must be nullable
         * @param metadataIndex entity metadata index where entity metadata values that are
         *                      associated with the specified entity data component should be put at
         * @param entityTypePredicate a predicate that should check whether entities with entity type provided during
         *                            predicate testing should support the specified entity data component
         * @return this builder
         * @throws IllegalArgumentException if the specified entity data component is <strong>NOT</strong> nullable
         * @since 1.0
         */
        private RegistrationsBuilder putOptionalBlockPos(EntityDataComponent<BlockPosition> component,
                                                         int metadataIndex, EntityTypePredicate entityTypePredicate) {
            if (!component.nullable())
                throw new IllegalArgumentException("The entity data component must be nullable");

            return this.put(
                    component, metadataIndex, EntityMetadataValue.OptionalBlockPositionValue.class,
                    entityTypePredicate, EntityMetadataValue.OptionalBlockPositionValue::value,
                    (currentMetadataValue, value) -> new EntityMetadataValue.OptionalBlockPositionValue(value)
            );
        }

        /**
         * Registers the specified nullable {@linkplain BlockState block-state}
         * {@linkplain EntityDataComponent entity data component}.
         *
         * <p>The component is going to be backed by
         * an {@linkplain EntityMetadataValue.OptionalBlockStateValue optional
         * block state entity metadata value}.</p>
         *
         * @param component the entity data component to register, must be nullable
         * @param metadataIndex entity metadata index where entity metadata values that are
         *                      associated with the specified entity data component should be put at
         * @param entityTypePredicate a predicate that should check whether entities with entity type provided during
         *                            predicate testing should support the specified entity data component
         * @return this builder
         * @throws IllegalArgumentException if the specified entity data component is <strong>NOT</strong> nullable
         * @since 1.0
         */
        private RegistrationsBuilder putOptionalBlockState(
                EntityDataComponent<BlockState> component,
                int metadataIndex, EntityTypePredicate entityTypePredicate
        ) {
            if (!component.nullable())
                throw new IllegalArgumentException("The entity data component must be nullable");

            return this.put(
                    component, metadataIndex, EntityMetadataValue.OptionalBlockStateValue.class,
                    entityTypePredicate, EntityMetadataValue.OptionalBlockStateValue::value,
                    (currentMetadataValue, value) -> {
                        JetBlockState validatedBlockState;

                        if (value == null) {
                            validatedBlockState = null;
                        } else if (value instanceof JetBlockState) {
                            validatedBlockState = (JetBlockState) value;
                        } else {
                            throw new IllegalArgumentException("The specified block state is not a valid block state");
                        }

                        return new EntityMetadataValue.OptionalBlockStateValue(validatedBlockState);
                    }
            );
        }

        /**
         * Registers the specified {@linkplain Rotations rotations}
         * {@linkplain EntityDataComponent entity data component}.
         *
         * <p>The component is going to be backed by
         * an {@linkplain EntityMetadataValue.RotationsValue rotations entity metadata value}.</p>
         *
         * @param component the entity data component to register, must not be nullable
         * @param metadataIndex entity metadata index where entity metadata values that are
         *                      associated with the specified entity data component should be put at
         * @param entityTypePredicate a predicate that should check whether entities with entity type provided during
         *                            predicate testing should support the specified entity data component
         * @return this builder
         * @throws IllegalArgumentException if the specified entity data component is nullable
         * @since 1.0
         */
        private RegistrationsBuilder putRotations(EntityDataComponent<Rotations> component,
                                                  int metadataIndex, EntityTypePredicate entityTypePredicate) {
            if (component.nullable())
                throw new IllegalArgumentException("The entity data component must not be nullable");

            return this.put(
                    component, metadataIndex, EntityMetadataValue.RotationsValue.class,
                    entityTypePredicate, EntityMetadataValue.RotationsValue::value,
                    (ignored, value) -> new EntityMetadataValue.RotationsValue(Objects.requireNonNull(value))
            );
        }

        /**
         * Registers the specified {@linkplain EntityDataComponent entity data component}.
         *
         * <p>Updating the specified component will update a single bit of
         * a {@linkplain EntityMetadataValue.Byte byte entity metadata value}.
         * The component value is converted to/from a {@code boolean}, which represents whether the bit is set.</p>
         *
         * @param component the entity data component to register, must not be nullable
         * @param metadataIndex entity metadata index where entity metadata values that are
         *                      associated with the specified entity data component should be put at
         * @param flagIndex the index of the bit that the entity data component should update,
         *                  where {@code 0} is the least significant bit
         * @param entityTypePredicate a predicate that should check whether entities with entity type provided during
         *                            predicate testing should support the specified entity data component
         * @param flagToValueFunction a function converting states of the bit to values
         *                            compatible with the specified entity data component
         * @param valueToFlagFunction a function converting values compatible with the specified
         *                            entity data component to states that the bit should have
         * @return this builder
         * @param <V> the value type of the specified entity data component
         * @throws IllegalArgumentException if the specified entity data component is nullable
         * @since 1.0
         */
        private <V> RegistrationsBuilder putBitFlag(EntityDataComponent<V> component, int metadataIndex,
                                                    int flagIndex, EntityTypePredicate entityTypePredicate,
                                                    Function<Boolean, V> flagToValueFunction,
                                                    Predicate<V> valueToFlagFunction) {
            if (component.nullable())
                throw new IllegalArgumentException("Nullable components cannot be registered as bit flag components");

            return this.put(
                    component, metadataIndex, EntityMetadataValue.Byte.class, entityTypePredicate,
                    metadataValue -> flagToValueFunction.apply(ByteUtil.bitSet(metadataValue.value(), flagIndex)),
                    (metadataValue, value) -> new EntityMetadataValue.Byte(ByteUtil.withBit(
                            metadataValue.value(), flagIndex,
                            valueToFlagFunction.test(Objects.requireNonNull(value))
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
         * @param component the entity data component to register, must not be nullable
         * @param metadataIndex entity metadata index where entity metadata values that are
         *                      associated with the specified entity data component should be put at
         * @param flagIndex the index of the bit that the entity data component should update,
         *                  where {@code 0} is the least significant bit
         * @param entityTypePredicate a predicate that should check whether entities with entity type provided during
         *                            predicate testing should support the specified entity data component
         * @return this builder
         * @throws IllegalArgumentException if the specified entity data component is nullable
         * @since 1.0
         */
        private RegistrationsBuilder putBitFlag(EntityDataComponent<Boolean> component, int metadataIndex,
                                                int flagIndex, EntityTypePredicate entityTypePredicate) {
            return this.putBitFlag(
                    component, metadataIndex, flagIndex, entityTypePredicate,
                    Function.identity(), Boolean::booleanValue
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
            return this.putBitFlag(component, metadataIndex, flagIndex, EntityTypePredicate.TRUE);
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