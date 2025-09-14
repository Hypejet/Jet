package net.hypejet.jet.server.entity.metadata;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.metadata.LivingEntityMetadata;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.util.collection.IntObjectMapBuilder;
import net.hypejet.jet.server.util.number.ByteUtil;
import net.hypejet.jet.world.coordinate.BlockPosition;
import net.hypejet.jet.world.particle.ParticleEffect;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * An implementation of the {@linkplain LivingEntityMetadata living entity metadata}.
 *
 * @since 1.0
 * @see LivingEntityMetadata
 */
@NullMarked
public class JetLivingEntityMetadata extends JetEntityMetadata implements LivingEntityMetadata {

    private static final int LIVING_ENTITY_FLAGS_INDEX = 8;
    private static final int HEALTH_INDEX = 9;
    private static final int PARTICLE_EFFECTS_INDEX = 10;
    private static final int REDUCE_PARTICLE_EFFECTS_INDEX = 11;
    private static final int ARROW_COUNT_INDEX = 12;
    private static final int STINGER_COUNT_INDEX = 13;
    private static final int SLEEPING_POSITION_INDEX = 14;

    private static final int USING_ITEM_FLAG_INDEX = 0;
    private static final int USED_ITEM_HAND_FLAG_INDEX = 1;
    private static final int IN_AUTO_SPING_ATTACK_FLAG_INDEX = 2;

    /**
     * Constructs the {@linkplain JetLivingEntityMetadata living entity metadata}.
     *
     * @param entity the entity that the living entity metadata is being constructed for
     * @since 1.0
     */
    public JetLivingEntityMetadata(JetEntity entity) {
        super(entity);
    }

    @Override
    public final boolean usingItem() {
        return this.livingEntityFlag(USING_ITEM_FLAG_INDEX);
    }

    @Override
    public final boolean inAutoSpinAttack() {
        return this.livingEntityFlag(IN_AUTO_SPING_ATTACK_FLAG_INDEX);
    }

    @Override
    public final Entity.InteractionHand usedItemHand() {
        return this.livingEntityFlag(USED_ITEM_HAND_FLAG_INDEX)
                ? Entity.InteractionHand.OFFHAND
                : Entity.InteractionHand.MAIN_HAND;
    }

    @Override
    public final float health() {
        return this.value(HEALTH_INDEX, EntityMetadataValue.Float.class).value();
    }

    @Override
    public final List<ParticleEffect> particleEffects() {
        return List.of(); // TODO
    }

    @Override
    public final boolean reduceParticleEffects() {
        return this.value(REDUCE_PARTICLE_EFFECTS_INDEX, EntityMetadataValue.Boolean.class).value();
    }

    @Override
    public final int arrowCount() {
        return this.value(ARROW_COUNT_INDEX, EntityMetadataValue.Int.class).value();
    }

    @Override
    public final int stingerCount() {
        return this.value(STINGER_COUNT_INDEX, EntityMetadataValue.Int.class).value();
    }

    @Override
    public final @Nullable BlockPosition sleepingPosition() {
        return this.value(SLEEPING_POSITION_INDEX, EntityMetadataValue.OptionalBlockPositionValue.class).value();
    }

    @Override
    public Update<?> createUpdateBuilder() {
        return new Update<>(this);
    }

    @Override
    protected void defineDefaults(IntObjectMapBuilder<EntityMetadataValue> valuesBuilder) {
        valuesBuilder.put(LIVING_ENTITY_FLAGS_INDEX, new EntityMetadataValue.Byte((byte) 0))
                .put(PARTICLE_EFFECTS_INDEX, new EntityMetadataValue.ParticleList(List.of()))
                .put(REDUCE_PARTICLE_EFFECTS_INDEX, new EntityMetadataValue.Boolean(false))
                .put(ARROW_COUNT_INDEX, new EntityMetadataValue.Int(0))
                .put(STINGER_COUNT_INDEX, new EntityMetadataValue.Int(0))
                .put(HEALTH_INDEX, new EntityMetadataValue.Float(1f))
                .put(SLEEPING_POSITION_INDEX, new EntityMetadataValue.OptionalBlockPositionValue(null));
    }

    private boolean livingEntityFlag(int index) {
        EntityMetadataValue.Byte value = this.value(LIVING_ENTITY_FLAGS_INDEX, EntityMetadataValue.Byte.class);
        return ByteUtil.bitSet(value.value(), index);
    }

    /**
     * An implementation of the {@linkplain LivingEntityMetadata.Update living entity metadata update}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see LivingEntityMetadata.Update
     */
    public static class Update<U extends Update<U>>
            extends JetEntityMetadata.Update<U>
            implements LivingEntityMetadata.Update<U> {
        /**
         * Constructs the {@linkplain Update living entity metadata update implementation}.
         *
         * @param entityMetadata the entity metadata that should be a base for the modified version
         * @since 1.0
         */
        protected Update(JetEntityMetadata entityMetadata) {
            super(entityMetadata);
        }

        @Override
        public final U usingItem(boolean value) {
            return this.updateLivingEntityFlag(USING_ITEM_FLAG_INDEX, value);
        }

        @Override
        public final U inAutoSpinAttack(boolean value) {
            return this.updateLivingEntityFlag(IN_AUTO_SPING_ATTACK_FLAG_INDEX, value);
        }

        @Override
        public final U usedItemHand(Entity.InteractionHand value) {
            return this.updateLivingEntityFlag(USED_ITEM_HAND_FLAG_INDEX, value == Entity.InteractionHand.OFFHAND);
        }

        @Override
        public final U health(float value) {
            return this.updateValue(HEALTH_INDEX, new EntityMetadataValue.Float(value));
        }

        @Override
        public final U particleEffects(List<ParticleEffect> value) {
            // TODO: Implement this method
            throw new UnsupportedOperationException("Not implemented yet");
        }

        @Override
        public final U reducedParticleEffects(boolean value) {
            return this.updateValue(REDUCE_PARTICLE_EFFECTS_INDEX, new EntityMetadataValue.Boolean(true));
        }

        @Override
        public final U arrowCount(int value) {
            return this.updateValue(ARROW_COUNT_INDEX, new EntityMetadataValue.Int(value));
        }

        @Override
        public final U stingerCount(int value) {
            return this.updateValue(STINGER_COUNT_INDEX, new EntityMetadataValue.Int(value));
        }

        @Override
        public final U sleepingPosition(@Nullable BlockPosition value) {
            return this.updateValue(
                    SLEEPING_POSITION_INDEX,
                    new EntityMetadataValue.OptionalBlockPositionValue(value)
            );
        }

        private U updateLivingEntityFlag(int index, boolean value) {
            byte flags = this.currentValue(index, EntityMetadataValue.Byte.class).value();
            return this.updateValue(index, new EntityMetadataValue.Byte(ByteUtil.withBit(flags, index, value)));
        }
    }
}