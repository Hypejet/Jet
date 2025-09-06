package net.hypejet.jet.server.entity.metadata;

import io.netty.util.collection.IntCollections;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.hypejet.jet.entity.metadata.EntityMetadata;
import net.hypejet.jet.entity.pose.Pose;
import net.hypejet.jet.server.entity.JetEntityType;
import net.hypejet.jet.server.util.collection.IntObjectMapBuilder;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * An implementation of the {@linkplain EntityMetadata entity metadata}.
 *
 * @since 1.0
 * @see EntityMetadata
 */
@NullMarked
public class JetEntityMetadata implements EntityMetadata {

    private static final int SHARED_FLAGS_INDEX = 0;
    private static final int AIR_SUPPLY_INDEX = 1;
    private static final int CUSTOM_NAME_INDEX = 2;
    private static final int CUSTOM_NAME_VISIBLE_INDEX = 3;
    private static final int SILENT_INDEX = 4;
    private static final int HAS_NO_GRAVITY_INDEX = 5;
    private static final int POSE_INDEX = 6;
    private static final int TICKS_FROZEN_INDEX = 7;

    private static final int ON_FIRE_FLAG_INDEX = 0;
    private static final int SNEAKING_FLAG_INDEX = 1;
    private static final int SPRINTING_FLAG_INDEX = 3;
    private static final int SWIMMING_FLAG_INDEX = 4;
    private static final int INVISIBLE_FLAG_INDEX = 5;
    private static final int GLOWING_FLAG_INDEX = 6;
    private static final int GLIDING_FLAG_INDEX = 7;

    private final IntObjectMap<EntityMetadataValue> values;

    /**
     * Constructs the {@linkplain JetEntityMetadata entity metadata} with default values.
     *
     * @param entityType the entity type of entity that the entity metadata is being constructed for
     * @since 1.0
     */
    public JetEntityMetadata(JetEntityType entityType) {
        IntObjectMapBuilder<EntityMetadataValue> valuesBuilder = new IntObjectMapBuilder<EntityMetadataValue>()
                .put(SHARED_FLAGS_INDEX, new EntityMetadataValue.Byte((byte) 0))
                .put(AIR_SUPPLY_INDEX, new EntityMetadataValue.Int(entityType.maxAirSupply()))
                .put(CUSTOM_NAME_VISIBLE_INDEX, new EntityMetadataValue.Boolean(false))
                .put(CUSTOM_NAME_INDEX, new EntityMetadataValue.OptionalComponentValue(null))
                .put(SILENT_INDEX, new EntityMetadataValue.Boolean(false))
                .put(HAS_NO_GRAVITY_INDEX, new EntityMetadataValue.Boolean(false))
                .put(POSE_INDEX, new EntityMetadataValue.PoseValue(Pose.STANDING))
                .put(TICKS_FROZEN_INDEX, new EntityMetadataValue.Int(0));
        this.defineDefaults(valuesBuilder);
        this.values = valuesBuilder.build();
    }

    /**
     * Constructs the {@linkplain JetEntityMetadata entity metadata} with values
     * from the specified {@linkplain Builder entity metadata builder}.
     *
     * @param builder the entity metadata builder whose values the entity metadata should have
     * @since 1.0
     */
    protected JetEntityMetadata(Builder builder) {
        IntObjectMap<EntityMetadataValue> values = new IntObjectHashMap<>(builder.values.size());
        values.putAll(builder.values);
        this.values = IntCollections.unmodifiableMap(values);
    }

    @Override
    public final boolean onFire() {
        return this.sharedFlag(ON_FIRE_FLAG_INDEX);
    }

    @Override
    public final boolean sneaking() {
        return this.sharedFlag(SNEAKING_FLAG_INDEX);
    }

    @Override
    public final boolean sprinting() {
        return this.sharedFlag(SPRINTING_FLAG_INDEX);
    }

    @Override
    public final boolean swimming() {
        return this.sharedFlag(SWIMMING_FLAG_INDEX);
    }

    @Override
    public final boolean invisible() {
        return this.sharedFlag(INVISIBLE_FLAG_INDEX);
    }

    @Override
    public final boolean glowing() {
        return this.sharedFlag(GLOWING_FLAG_INDEX);
    }

    @Override
    public final boolean gliding() {
        return this.sharedFlag(GLIDING_FLAG_INDEX);
    }

    @Override
    public final int airSupply() {
        return this.value(AIR_SUPPLY_INDEX, EntityMetadataValue.Int.class).value();
    }

    @Override
    public final boolean customNameVisible() {
        return this.value(CUSTOM_NAME_VISIBLE_INDEX, EntityMetadataValue.Boolean.class).value();
    }

    @Override
    public final @Nullable Component customName() {
        return this.value(CUSTOM_NAME_INDEX, EntityMetadataValue.OptionalComponentValue.class).value();
    }

    @Override
    public final boolean silent() {
        return this.value(SILENT_INDEX, EntityMetadataValue.Boolean.class).value();
    }

    @Override
    public final boolean hasNoGravity() {
        return this.value(HAS_NO_GRAVITY_INDEX, EntityMetadataValue.Boolean.class).value();
    }

    @Override
    public final Pose pose() {
        return this.value(POSE_INDEX, EntityMetadataValue.PoseValue.class).value();
    }

    @Override
    public final int ticksFrozen() {
        return this.value(TICKS_FROZEN_INDEX, EntityMetadataValue.Int.class).value();
    }

    @Override
    public EntityMetadata.Builder toBuilder() {
        return new Builder(this);
    }

    /**
     * Defines defaults of additional fields of this {@linkplain JetEntityMetadata entity metadata}.
     *
     * @param valuesBuilder the builder of the default values
     * @since 1.0
     */
    protected void defineDefaults(IntObjectMapBuilder<EntityMetadataValue> valuesBuilder) {}

    /**
     * Gets the {@linkplain EntityMetadataValue entity metadata value} set
     * in this {@linkplain JetEntityMetadata entity metadata} at the specified index.
     *
     * @param index the index that the metadata value is bound to
     * @param valueType the expected class of the metadata value
     * @return the entity metadata value
     * @param <T> the type of the expected entity metadata value
     * @since 1.0
     */
    protected final <T extends EntityMetadataValue> T value(int index, Class<T> valueType) {
        return value(index, valueType, this.values);
    }

    private boolean sharedFlag(int index) {
        byte sharedFlags = value(index, EntityMetadataValue.Byte.class, this.values).value();
        return (sharedFlags & (1 << index)) != 0;
    }

    private static <T extends EntityMetadataValue> T value(int index, Class<T> valueType,
                                                           IntObjectMap<EntityMetadataValue> values) {
        EntityMetadataValue value = values.get(index);
        // noinspection ConstantValue ; the value actually is nullable
        if (value == null)
            throw new IllegalStateException("No metadata value present at index: " + index);

        if (!valueType.isAssignableFrom(value.getClass())) {
            throw new IllegalArgumentException(String.format(
                    "Metadata value at index %d is not an instance of the %s metadata value class",
                    index, valueType.getSimpleName()
            ));
        }

        return valueType.cast(value);
    }

    /**
     * An implementation of the {@linkplain EntityMetadata.Builder entity metadata builder}.
     *
     * @since 1.0
     * @see EntityMetadata.Builder
     */
    public static class Builder implements EntityMetadata.Builder {

        private final IntObjectMap<EntityMetadataValue> values;

        /**
         * Constructs the {@linkplain Builder entity metadata builder}.
         *
         * @param entityMetadata the entity metadata that should be a base for the modified version
         * @since 1.0
         */
        protected Builder(JetEntityMetadata entityMetadata) {
            this.values = new IntObjectHashMap<>(entityMetadata.values.size());
            this.values.putAll(entityMetadata.values);
        }

        @Override
        public final EntityMetadata.Builder onFire(boolean value) {
            return this.updateSharedFlag(ON_FIRE_FLAG_INDEX, value);
        }

        @Override
        public final EntityMetadata.Builder sneaking(boolean value) {
            return this.updateSharedFlag(SNEAKING_FLAG_INDEX, value);
        }

        @Override
        public final EntityMetadata.Builder sprinting(boolean value) {
            return this.updateSharedFlag(SPRINTING_FLAG_INDEX, value);
        }

        @Override
        public final EntityMetadata.Builder swimming(boolean value) {
            return this.updateSharedFlag(SWIMMING_FLAG_INDEX, value);
        }

        @Override
        public final EntityMetadata.Builder invisible(boolean value) {
            return this.updateSharedFlag(INVISIBLE_FLAG_INDEX, value);
        }

        @Override
        public final EntityMetadata.Builder glowing(boolean value) {
            return this.updateSharedFlag(GLOWING_FLAG_INDEX, value);
        }

        @Override
        public final EntityMetadata.Builder gliding(boolean value) {
            return this.updateSharedFlag(GLIDING_FLAG_INDEX, value);
        }

        @Override
        public final EntityMetadata.Builder airSupply(int value) {
            return this.updateValue(AIR_SUPPLY_INDEX, new EntityMetadataValue.Int(value));
        }

        @Override
        public final EntityMetadata.Builder customNameVisible(boolean value) {
            return this.updateValue(CUSTOM_NAME_VISIBLE_INDEX, new EntityMetadataValue.Boolean(value));
        }

        @Override
        public final EntityMetadata.Builder customName(@Nullable Component value) {
            return this.updateValue(CUSTOM_NAME_INDEX, new EntityMetadataValue.OptionalComponentValue(value));
        }

        @Override
        public final EntityMetadata.Builder silent(boolean value) {
            return this.updateValue(SILENT_INDEX, new EntityMetadataValue.Boolean(value));
        }

        @Override
        public final EntityMetadata.Builder hasNoGravity(boolean value) {
            return this.updateValue(HAS_NO_GRAVITY_INDEX, new EntityMetadataValue.Boolean(value));
        }

        @Override
        public final EntityMetadata.Builder pose(Pose value) {
            return this.updateValue(POSE_INDEX, new EntityMetadataValue.PoseValue(value));
        }

        @Override
        public final EntityMetadata.Builder ticksFrozen(int value) {
            return this.updateValue(TICKS_FROZEN_INDEX, new EntityMetadataValue.Int(value));
        }

        @Override
        public EntityMetadata build() {
            return new JetEntityMetadata(this);
        }

        /**
         * Updates {@linkplain EntityMetadataValue entity metadata value} at the specified index.
         * 
         * @param index the index to update the entity metadata value at
         * @param value the new entity metadata value that should be at the specified index
         * @return this builder
         * @since 1.0
         */
        protected final EntityMetadata.Builder updateValue(int index, EntityMetadataValue value) {
            this.values.put(index, value);
            return this;
        }

        /**
         * Gets current {@linkplain EntityMetadataValue entity metadata value} set at the specified index.
         *
         * @param index the index that the metadata value is bound to
         * @param valueType the expected class of the metadata value
         * @return the entity metadata value
         * @param <T> the type of the expected entity metadata value
         * @since 1.0
         */
        protected final <T extends EntityMetadataValue> T currentValue(int index, Class<T> valueType) {
            return value(index, valueType, this.values);
        }

        private EntityMetadata.Builder updateSharedFlag(int index, boolean value) {
            byte sharedFlags = this.currentValue(index, EntityMetadataValue.Byte.class).value();
            if (value) {
                sharedFlags |= (byte) (1 << index);
            } else {
                sharedFlags &= (byte) ~(1 << index);
            }
            return this.updateValue(index, new EntityMetadataValue.Byte(sharedFlags));
        }
    }
}