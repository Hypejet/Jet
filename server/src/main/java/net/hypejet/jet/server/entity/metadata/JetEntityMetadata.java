package net.hypejet.jet.server.entity.metadata;

import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.hypejet.jet.entity.metadata.EntityMetadata;
import net.hypejet.jet.entity.pose.Pose;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerEntityMetadataPlayPacket;
import net.hypejet.jet.server.util.collection.IntObjectMapBuilder;
import net.hypejet.jet.server.util.number.ByteUtil;
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

    private final JetEntity entity;
    private final IntObjectMap<EntityMetadataValue> values = new IntObjectHashMap<>();

    /**
     * Constructs the {@linkplain JetEntityMetadata entity metadata} with default values.
     *
     * @param entity the entity that the entity metadata is being constructed for
     * @since 1.0
     */
    public JetEntityMetadata(JetEntity entity) {
        this.entity = entity;

        IntObjectMapBuilder<EntityMetadataValue> defaultsBuilder = new IntObjectMapBuilder<EntityMetadataValue>()
                .put(SHARED_FLAGS_INDEX, new EntityMetadataValue.Byte((byte) 0))
                .put(AIR_SUPPLY_INDEX, new EntityMetadataValue.Int(entity.entityTypeValue().maxAirSupply()))
                .put(CUSTOM_NAME_VISIBLE_INDEX, new EntityMetadataValue.Boolean(false))
                .put(CUSTOM_NAME_INDEX, new EntityMetadataValue.OptionalComponentValue(null))
                .put(SILENT_INDEX, new EntityMetadataValue.Boolean(false))
                .put(HAS_NO_GRAVITY_INDEX, new EntityMetadataValue.Boolean(false))
                .put(POSE_INDEX, new EntityMetadataValue.PoseValue(Pose.STANDING))
                .put(TICKS_FROZEN_INDEX, new EntityMetadataValue.Int(0));

        this.defineDefaults(defaultsBuilder);
        this.values.putAll(defaultsBuilder.build());
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
    public Update<?> createUpdateBuilder() {
        return new Update<>(this);
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
        T value = value(index, valueType, this.values);
        if (value == null)
            throw new IllegalStateException("No metadata value present at index: " + index);
        return value;
    }

    private boolean sharedFlag(int index) {
        byte sharedFlags = this.value(index, EntityMetadataValue.Byte.class).value();
        return ByteUtil.bitSet(sharedFlags, index);
    }

    private static <T extends EntityMetadataValue> @Nullable T value(int index, Class<T> valueType,
                                                                     IntObjectMap<EntityMetadataValue> values) {
        EntityMetadataValue value = values.get(index);
        if (value == null) return null;

        if (!valueType.isAssignableFrom(value.getClass())) {
            throw new IllegalArgumentException(String.format(
                    "Metadata value at index %d is not an instance of the %s metadata value class",
                    index, valueType.getSimpleName()
            ));
        }

        return valueType.cast(value);
    }

    /**
     * An implementation of the {@linkplain EntityMetadata.Update entity metadata update}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see EntityMetadata.Update
     */
    public static class Update<U extends Update<U>> implements EntityMetadata.Update<U> {

        private final JetEntityMetadata entityMetadata;
        private final IntObjectMap<EntityMetadataValue> updatedValues = new IntObjectHashMap<>();

        /**
         * Constructs the {@linkplain Update entity metadata update implementation}.
         *
         * @param entityMetadata the entity metadata that should be a base for the modified version
         * @since 1.0
         */
        protected Update(JetEntityMetadata entityMetadata) {
            this.entityMetadata = entityMetadata;
        }

        @Override
        public final U onFire(boolean value) {
            return this.updateSharedFlag(ON_FIRE_FLAG_INDEX, value);
        }

        @Override
        public final U sneaking(boolean value) {
            return this.updateSharedFlag(SNEAKING_FLAG_INDEX, value);
        }

        @Override
        public final U sprinting(boolean value) {
            return this.updateSharedFlag(SPRINTING_FLAG_INDEX, value);
        }

        @Override
        public final U swimming(boolean value) {
            return this.updateSharedFlag(SWIMMING_FLAG_INDEX, value);
        }

        @Override
        public final U invisible(boolean value) {
            return this.updateSharedFlag(INVISIBLE_FLAG_INDEX, value);
        }

        @Override
        public final U glowing(boolean value) {
            return this.updateSharedFlag(GLOWING_FLAG_INDEX, value);
        }

        @Override
        public final U gliding(boolean value) {
            return this.updateSharedFlag(GLIDING_FLAG_INDEX, value);
        }

        @Override
        public final U airSupply(int value) {
            return this.updateValue(AIR_SUPPLY_INDEX, new EntityMetadataValue.Int(value));
        }

        @Override
        public final U customNameVisible(boolean value) {
            return this.updateValue(CUSTOM_NAME_VISIBLE_INDEX, new EntityMetadataValue.Boolean(value));
        }

        @Override
        public final U customName(@Nullable Component value) {
            return this.updateValue(CUSTOM_NAME_INDEX, new EntityMetadataValue.OptionalComponentValue(value));
        }

        @Override
        public final U silent(boolean value) {
            return this.updateValue(SILENT_INDEX, new EntityMetadataValue.Boolean(value));
        }

        @Override
        public final U hasNoGravity(boolean value) {
            return this.updateValue(HAS_NO_GRAVITY_INDEX, new EntityMetadataValue.Boolean(value));
        }

        @Override
        public final U pose(Pose value) {
            return this.updateValue(POSE_INDEX, new EntityMetadataValue.PoseValue(value));
        }

        @Override
        public final U ticksFrozen(int value) {
            return this.updateValue(TICKS_FROZEN_INDEX, new EntityMetadataValue.Int(value));
        }

        @Override
        public final U performUpdate() {
            JetEntity entity = this.entityMetadata.entity;
            entity.server().ticker().ensureRunsInTickLoop();

            if (this.updatedValues.isEmpty()) return this.castUpdate();
            this.entityMetadata.values.putAll(this.updatedValues);

            ServerEntityMetadataPlayPacket packet = new ServerEntityMetadataPlayPacket(
                    entity.entityId(),
                    this.updatedValues
            ); // TODO: Cache

            entity.viewers().forEach(player -> player.sendPacket(packet));
            this.updatedValues.clear();
            return this.castUpdate();
        }

        /**
         * Updates {@linkplain EntityMetadataValue entity metadata value} at the specified index.
         * 
         * @param index the index to update the entity metadata value at
         * @param value the new entity metadata value that should be at the specified index
         * @return this update builder
         * @throws IllegalStateException if the current thread is not the thread that runs the game logic loop
         * @since 1.0
         */
        protected final U updateValue(int index, EntityMetadataValue value) {
            /* Certain methods updating fields depend on current values of these fields, meaning that these methods
               are not atomic, this is why we check whether the current thread is the main ticking thread.
               Additionally, we compare the current value with the specified value to avoid unnecessary updates,
               which is also not a thread-safe operation. */
            this.entityMetadata.entity.server().ticker().ensureRunsInTickLoop();
            if (this.currentValue(index, value.getClass()).equals(value)) return this.castUpdate();
            this.updatedValues.put(index, value);
            return this.castUpdate();
        }

        /**
         * Gets current {@linkplain EntityMetadataValue entity metadata value}
         * set at the specified index in this {@linkplain Update update builder}.
         *
         * <p>If this {@linkplain Update update builder} does not contain an updated value
         * at the specified index, the value from the {@linkplain JetEntityMetadata entity metadata}
         * (for which the update builder was created) is returned.</p>
         *
         * @param index the index that the metadata value is bound to
         * @param valueType the expected class of the metadata value
         * @return the entity metadata value
         * @param <T> the type of the expected entity metadata value
         * @since 1.0
         */
        protected final <T extends EntityMetadataValue> T currentValue(int index, Class<T> valueType) {
            T value = value(index, valueType, this.updatedValues);
            if (value == null) return this.entityMetadata.value(index, valueType);
            return value;
        }

        private U updateSharedFlag(int index, boolean value) {
            byte sharedFlags = this.currentValue(index, EntityMetadataValue.Byte.class).value();
            return this.updateValue(index, new EntityMetadataValue.Byte(ByteUtil.withBit(sharedFlags, index, value)));
        }

        private U castUpdate() {
            // noinspection unchecked ; it is expected that the type parameter refers to type of this class
            return (U) this;
        }
    }
}