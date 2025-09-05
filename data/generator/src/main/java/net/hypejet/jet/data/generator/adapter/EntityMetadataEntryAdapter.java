package net.hypejet.jet.data.generator.adapter;

import net.hypejet.jet.data.json.model.entity.metadata.JsonEntityMetadataEntry;
import net.hypejet.jet.data.json.model.entity.metadata.JsonEntityMetadataValue;
import net.hypejet.jet.data.json.model.item.JsonItemComponent;
import net.hypejet.jet.data.json.model.position.JsonBlockPosition;
import net.hypejet.jet.data.json.model.position.JsonGlobalPosition;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Something converting entries of metadata of {@linkplain Entity entities} to Jet data equivalents.
 *
 * @since 1.0
 * @see Entity
 */
@NullMarked
public final class EntityMetadataEntryAdapter {

    private static final Field INITIAL_VALUE_FIELD;

    private static final Map<EntityDataSerializer<?>, Converter<?>> CONVERSIONS = new ConversionsBuilder()
            .register(EntityDataSerializers.BYTE, JsonEntityMetadataValue.Byte::new)
            .register(EntityDataSerializers.INT, JsonEntityMetadataValue.Int::new)
            .register(EntityDataSerializers.LONG, JsonEntityMetadataValue.Long::new)
            .register(EntityDataSerializers.FLOAT, JsonEntityMetadataValue.Float::new)
            .register(EntityDataSerializers.STRING, JsonEntityMetadataValue.StringValue::new)
            .register(EntityDataSerializers.BOOLEAN, JsonEntityMetadataValue.Boolean::new)
            .register(EntityDataSerializers.ITEM_STACK, EntityMetadataEntryAdapter::convertItemStack)
            .register(EntityDataSerializers.PARTICLES, EntityMetadataEntryAdapter::convertParticleList)
            .register(EntityDataSerializers.DIRECTION, EntityMetadataEntryAdapter::convertDirection)
            .register(EntityDataSerializers.POSE, EntityMetadataEntryAdapter::convertPose)
            .register(EntityDataSerializers.ARMADILLO_STATE, EntityMetadataEntryAdapter::convertArmadilloState)
            .register(EntityDataSerializers.SNIFFER_STATE, EntityMetadataEntryAdapter::convertSnifferState)
            .register(EntityDataSerializers.COMPOUND_TAG, EntityMetadataEntryAdapter::convertCompoundBinaryTag)
            .register(EntityDataSerializers.VILLAGER_DATA, EntityMetadataEntryAdapter::convertVillagerData)
            .register(
                    EntityDataSerializers.COMPONENT,
                    value -> new JsonEntityMetadataValue.Component(serializeComponent(value))
            )
            .register(
                    EntityDataSerializers.BLOCK_STATE,
                    value -> new JsonEntityMetadataValue.BlockState(Block.getId(value))
            )
            .register(
                    EntityDataSerializers.PARTICLE,
                    value -> new JsonEntityMetadataValue.Particle(serializeParticle(value))
            )
            .register(
                    EntityDataSerializers.ROTATIONS,
                    value -> new JsonEntityMetadataValue.Rotations(value.x(), value.y(), value.z())
            )
            .register(
                    EntityDataSerializers.BLOCK_POS,
                    value -> new JsonEntityMetadataValue.BlockPosition(toJsonEquivalent(value))
            )
            .register(
                    EntityDataSerializers.VECTOR3,
                    value -> new JsonEntityMetadataValue.Vector(value.x(), value.y(), value.z())
            )
            .register(
                    EntityDataSerializers.QUATERNION,
                    value -> new JsonEntityMetadataValue.Quaternion(value.w(), value.x(), value.y(), value.z())
            )
            .register(
                    EntityDataSerializers.OPTIONAL_UNSIGNED_INT,
                    value -> new JsonEntityMetadataValue.OptionalUnsignedInt(value.isEmpty() ? null : value.getAsInt())
            )
            .register(
                    EntityDataSerializers.OPTIONAL_COMPONENT,
                    value -> convertOptionalComponent(value.orElse(null))
            )
            .register(
                    EntityDataSerializers.OPTIONAL_BLOCK_POS,
                    value -> convertOptionalBlockPos(value.orElse(null))
            )
            .register(
                    EntityDataSerializers.OPTIONAL_BLOCK_STATE,
                    value -> convertOptionalBlockState(value.orElse(null))
            )
            .register(
                    EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE,
                    value -> convertOptionalEntity(value.orElse(null))
            )
            .register(
                    EntityDataSerializers.OPTIONAL_GLOBAL_POS,
                    value -> convertOptionalGlobalPosition(value.orElse(null))
            )
            .registerVariant(
                    EntityDataSerializers.CAT_VARIANT,
                    Registries.CAT_VARIANT,
                    JsonEntityMetadataValue.CatVariant::new
            )
            .registerVariant(
                    EntityDataSerializers.CHICKEN_VARIANT,
                    Registries.CHICKEN_VARIANT,
                    JsonEntityMetadataValue.ChickenVariant::new
            )
            .registerVariant(
                    EntityDataSerializers.COW_VARIANT,
                    Registries.COW_VARIANT,
                    JsonEntityMetadataValue.CowVariant::new
            )
            .registerVariant(
                    EntityDataSerializers.WOLF_VARIANT,
                    Registries.WOLF_VARIANT,
                    JsonEntityMetadataValue.WolfVariant::new
            )
            .registerVariant(
                    EntityDataSerializers.WOLF_SOUND_VARIANT,
                    Registries.WOLF_SOUND_VARIANT,
                    JsonEntityMetadataValue.WolfSoundVariant::new
            )
            .registerVariant(
                    EntityDataSerializers.FROG_VARIANT,
                    Registries.FROG_VARIANT,
                    JsonEntityMetadataValue.FrogVariant::new
            )
            .registerVariant(
                    EntityDataSerializers.PIG_VARIANT,
                    Registries.PIG_VARIANT,
                    JsonEntityMetadataValue.PigVariant::new
            )
            .registerVariant(
                    EntityDataSerializers.PAINTING_VARIANT,
                    Registries.PAINTING_VARIANT,
                    JsonEntityMetadataValue.PaintingVariant::new
            )
            .build();

    static {
        try {
            INITIAL_VALUE_FIELD = SynchedEntityData.DataItem.class.getDeclaredField("initialValue");
            INITIAL_VALUE_FIELD.setAccessible(true);
        } catch (NoSuchFieldException exception) {
            throw new RuntimeException(exception);
        }
    }

    private EntityMetadataEntryAdapter() {}

    /**
     * Converts the specified {@linkplain Entity entity} metadata entry to a Jet data equivalent.
     *
     * @param dataItem the entity metadata entry to convert
     * @param registryAccess access to all Minecraft registries
     * @return the converted entity metadata entry
     * @param <T> the value type of the metadata entry to convert
     * @since 1.0
     */
    public static <T> JsonEntityMetadataEntry convert(SynchedEntityData.DataItem<T> dataItem,
                                                      RegistryAccess registryAccess) {
        return new JsonEntityMetadataEntry(
                (short) dataItem.getAccessor().id(),
                convertValue(dataItem, registryAccess)
        );
    }

    private static <T> JsonEntityMetadataValue convertValue(SynchedEntityData.DataItem<T> dataItem,
                                                            RegistryAccess registryAccess) {
        try {
            EntityDataSerializer<T> serializer = dataItem.getAccessor().serializer();
            // noinspection unchecked ; the initial value field also uses generics
            T initialValue = (T) INITIAL_VALUE_FIELD.get(dataItem);

            // noinspection unchecked ; the conversion map was created safely with a builder
            Converter<T> converter = (Converter<T>) CONVERSIONS.get(serializer);
            if (converter == null) {
                throw new IllegalArgumentException(
                        "Could not find an entity data conversion for: "
                                + initialValue.getClass().getSimpleName()
                );
            }

            return converter.convert(initialValue, registryAccess);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static JsonEntityMetadataValue convertOptionalComponent(@Nullable Component component) {
        return new JsonEntityMetadataValue.OptionalComponent(component == null ? null : serializeComponent(component));
    }

    private static JsonEntityMetadataValue convertItemStack(ItemStack itemStack, RegistryAccess registryAccess) {
        Registry<DataComponentType<?>> componentTypes = registryAccess.lookupOrThrow(Registries.DATA_COMPONENT_TYPE);
        Map<Key, JsonItemComponent> components = new HashMap<>();

        for (Map.Entry<DataComponentType<?>, Optional<?>> entry : itemStack.getComponentsPatch().entrySet()) {
            DataComponentType<?> componentType = entry.getKey();
            if (componentType.isTransient()) continue;

            ResourceLocation componentTypeLocation = componentTypes.getKey(componentType);
            if (componentTypeLocation == null)
                throw new IllegalStateException("The data component type was not registered");

            Object value = entry.getValue().orElse(null);
            components.put(
                    KeyAdapter.convert(componentTypeLocation),
                    value == null
                            ? JsonItemComponent.removed()
                            : new JsonItemComponent.Valued(serializeItemComponent(componentType, value))
            );
        }

        return new JsonEntityMetadataValue.ItemStack(
                itemStack.getCount(),
                registryAccess.lookupOrThrow(Registries.ITEM)
                        .getResourceKey(itemStack.getItem())
                        .map(KeyAdapter::convert)
                        .orElseThrow(),
                components
        );
    }

    private static JsonEntityMetadataValue convertOptionalBlockState(@Nullable BlockState blockState) {
        return new JsonEntityMetadataValue.OptionalBlockState(blockState == null ? null : Block.getId(blockState));
    }

    private static JsonEntityMetadataValue convertParticleList(List<ParticleOptions> particleList) {
        List<BinaryTagHolder> serializedParticles = new ArrayList<>();
        particleList.forEach(particle -> serializedParticles.add(serializeParticle(particle)));
        return new JsonEntityMetadataValue.ParticleList(serializedParticles);
    }

    private static JsonEntityMetadataValue convertOptionalBlockPos(@Nullable BlockPos blockPos) {
        return new JsonEntityMetadataValue.OptionalBlockPosition(blockPos == null ? null : toJsonEquivalent(blockPos));
    }

    private static JsonEntityMetadataValue convertDirection(Direction direction) {
        return switch (direction) {
            case DOWN -> JsonEntityMetadataValue.Direction.DOWN;
            case UP -> JsonEntityMetadataValue.Direction.UP;
            case NORTH -> JsonEntityMetadataValue.Direction.NORTH;
            case SOUTH -> JsonEntityMetadataValue.Direction.SOUTH;
            case WEST -> JsonEntityMetadataValue.Direction.WEST;
            case EAST -> JsonEntityMetadataValue.Direction.EAST;
        };
    }

    private static JsonEntityMetadataValue convertOptionalEntity(@Nullable EntityReference<LivingEntity> reference) {
        return new JsonEntityMetadataValue.OptionalLivingEntityReference(
                reference == null ? null : reference.getUUID()
        );
    }

    private static JsonEntityMetadataValue convertOptionalGlobalPosition(@Nullable GlobalPos globalPos) {
        return new JsonEntityMetadataValue.OptionalGlobalPosition(
                globalPos == null ? null : new JsonGlobalPosition(
                        KeyAdapter.convert(globalPos.dimension()),
                        toJsonEquivalent(globalPos.pos())
                )
        );
    }

    private static JsonEntityMetadataValue convertCompoundBinaryTag(CompoundTag compoundTag) {
        return new JsonEntityMetadataValue.CompoundBinaryTag(BinaryTagHolder.binaryTagHolder(compoundTag.toString()));
    }

    private static JsonEntityMetadataValue convertVillagerData(VillagerData villagerData,
                                                               RegistryAccess registryAccess) {
        return new JsonEntityMetadataValue.VillagerData(
                holderToKey(villagerData.type(), Registries.VILLAGER_TYPE, registryAccess),
                holderToKey(villagerData.profession(), Registries.VILLAGER_PROFESSION, registryAccess),
                villagerData.level()
        );
    }

    private static JsonEntityMetadataValue convertPose(Pose pose) {
        return switch (pose) {
            case STANDING -> JsonEntityMetadataValue.Pose.STANDING;
            case FALL_FLYING -> JsonEntityMetadataValue.Pose.FALL_FLYING;
            case SLEEPING -> JsonEntityMetadataValue.Pose.SLEEPING;
            case SWIMMING -> JsonEntityMetadataValue.Pose.SWIMMING;
            case SPIN_ATTACK -> JsonEntityMetadataValue.Pose.SPIN_ATTACK;
            case CROUCHING -> JsonEntityMetadataValue.Pose.CROUCHING;
            case LONG_JUMPING -> JsonEntityMetadataValue.Pose.LONG_JUMPING;
            case DYING -> JsonEntityMetadataValue.Pose.DYING;
            case CROAKING -> JsonEntityMetadataValue.Pose.CROAKING;
            case USING_TONGUE -> JsonEntityMetadataValue.Pose.USING_TONGUE;
            case SITTING -> JsonEntityMetadataValue.Pose.SITTING;
            case ROARING -> JsonEntityMetadataValue.Pose.ROARING;
            case SNIFFING -> JsonEntityMetadataValue.Pose.SNIFFING;
            case EMERGING -> JsonEntityMetadataValue.Pose.EMERGING;
            case DIGGING -> JsonEntityMetadataValue.Pose.DIGGING;
            case SLIDING -> JsonEntityMetadataValue.Pose.SLIDING;
            case SHOOTING -> JsonEntityMetadataValue.Pose.SHOOTING;
            case INHALING -> JsonEntityMetadataValue.Pose.INHALING;
        };
    }

    private static JsonEntityMetadataValue convertArmadilloState(Armadillo.ArmadilloState state) {
        return switch (state) {
            case IDLE -> JsonEntityMetadataValue.ArmadilloState.IDLE;
            case ROLLING -> JsonEntityMetadataValue.ArmadilloState.ROLLING;
            case SCARED -> JsonEntityMetadataValue.ArmadilloState.SCARED;
            case UNROLLING -> JsonEntityMetadataValue.ArmadilloState.UNROLLING;
        };
    }

    private static JsonEntityMetadataValue convertSnifferState(Sniffer.State state) {
        return switch (state) {
            case IDLING -> JsonEntityMetadataValue.SnifferState.IDLING;
            case FEELING_HAPPY -> JsonEntityMetadataValue.SnifferState.FEELING_HAPPY;
            case SCENTING -> JsonEntityMetadataValue.SnifferState.SCENTING;
            case SNIFFING -> JsonEntityMetadataValue.SnifferState.SNIFFING;
            case SEARCHING -> JsonEntityMetadataValue.SnifferState.SEARCHING;
            case DIGGING -> JsonEntityMetadataValue.SnifferState.DIGGING;
            case RISING -> JsonEntityMetadataValue.SnifferState.RISING;
        };
    }

    private static <T> Key holderToKey(Holder<T> holder,
                                       ResourceKey<? extends Registry<T>> registryKey,
                                       RegistryAccess registryAccess) {
        return registryAccess.lookupOrThrow(registryKey)
                .getResourceKey(holder.value())
                .map(KeyAdapter::convert)
                .orElseThrow();
    }

    private static JsonBlockPosition toJsonEquivalent(BlockPos blockPos) {
        return new JsonBlockPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    private static <T> BinaryTagHolder serializeItemComponent(DataComponentType<T> componentType, Object value) {
        // noinspection unchecked ; the specified value is expected to be a value from the data component patch
        return BinaryTagHolder.binaryTagHolder(
                componentType.codecOrThrow()
                        .encodeStart(NbtOps.INSTANCE, (T) value)
                        .getOrThrow()
                        .toString()
        );
    }

    private static BinaryTagHolder serializeComponent(Component component) {
        return BinaryTagHolder.binaryTagHolder(
                ComponentSerialization.CODEC.encodeStart(NbtOps.INSTANCE, component)
                        .getOrThrow()
                        .toString()
        );
    }

    private static BinaryTagHolder serializeParticle(ParticleOptions options) {
        return BinaryTagHolder.binaryTagHolder(
                ParticleTypes.CODEC.encodeStart(NbtOps.INSTANCE, options)
                        .getOrThrow()
                        .toString()
        );
    }

    /**
     * A builder of a {@linkplain Map map} associating {@linkplain EntityDataSerializer entity data serializers}
     * with {@linkplain Converter converters} converting {@linkplain Entity entity} metadata values that these
     * {@linkplain EntityDataSerializer entity data serializers} can serialize.
     *
     * @since 1.0
     * @see EntityDataSerializer
     * @see Converter
     * @see Entity
     * @see Map
     */
    private static final class ConversionsBuilder {

        private final Map<EntityDataSerializer<?>, Converter<?>> conversions = new HashMap<>();

        /**
         * Registers the {@linkplain Entity entity} metadata value conversion.
         *
         * @param serializer the entity data serializer that can serialize the value that should be converted
         * @param converter the converter converting the entity metadata value
         * @return this builder
         * @param <T> the type of the entity metadata value that should be converted
         * @since 1.0
         */
        private <T> ConversionsBuilder register(EntityDataSerializer<T> serializer, Converter<T> converter) {
            this.conversions.put(serializer, converter);
            return this;
        }

        /**
         * Registers the {@linkplain Entity entity} metadata value conversion.
         *
         * @param serializer the entity data serializer that can serialize the value that should be converted
         * @param converter the function converting the entity metadata value
         * @return this builder
         * @param <T> the type of the entity metadata value that should be converted
         * @since 1.0
         */
        private <T> ConversionsBuilder register(EntityDataSerializer<T> serializer,
                                                Function<T, JsonEntityMetadataValue> converter) {
            return this.register(serializer, (value, registryAccess) -> converter.apply(value));
        }

        /**
         * Registers the {@linkplain Entity entity} metadata value conversion
         * for an {@linkplain Entity entity} variant holder.
         *
         * @param serializer the entity data serializer that can serialize
         *                   entity variant holders that should be converted
         * @param variantRegistryKey the resource key of the registry containing
         *                           the variants whose holders should be converted
         * @param valueFunction the function creating a converted entity metadata value
         *                      using the key of the entity variant of the holder
         * @return this builder
         * @param <T> the type of the entity metadata value that should be converted
         * @since 1.0
         */
        private <T> ConversionsBuilder registerVariant(EntityDataSerializer<Holder<T>> serializer,
                                                       ResourceKey<? extends Registry<T>> variantRegistryKey,
                                                       Function<Key, JsonEntityMetadataValue> valueFunction) {
            return this.register(
                    serializer,
                    (value, access) -> valueFunction.apply(holderToKey(value, variantRegistryKey, access))
            );
        }

        /**
         * Builds the {@linkplain Map map}.
         *
         * @return the created map
         * @since 1.0
         */
        private Map<EntityDataSerializer<?>, Converter<?>> build() {
            return Map.copyOf(this.conversions);
        }
    }

    /**
     * Something converting {@linkplain Entity entity} metadata values of a specific type to a Jet data equivalent.
     *
     * @param <T> the type of entity metadata values that this converter converts
     * @since 1.0
     * @see Entity
     */
    @FunctionalInterface
    private interface Converter<T> {
        /**
         * Converts the specified {@linkplain Entity entity} metadata value to a Jet data equivalent.
         *
         * @param value the value to convert
         * @param registryAccess access to all Minecraft registries
         * @return the converted entity metadata value
         * @since 1.0
         */
        JsonEntityMetadataValue convert(T value, RegistryAccess registryAccess);
    }
}