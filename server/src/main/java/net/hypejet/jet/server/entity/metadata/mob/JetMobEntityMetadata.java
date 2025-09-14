package net.hypejet.jet.server.entity.metadata.mob;

import net.hypejet.jet.entity.metadata.mob.MobEntityMetadata;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.entity.metadata.JetEntityMetadata;
import net.hypejet.jet.server.entity.metadata.JetLivingEntityMetadata;
import net.hypejet.jet.server.util.collection.IntObjectMapBuilder;
import net.hypejet.jet.server.util.number.ByteUtil;
import org.jspecify.annotations.NullMarked;

/**
 * An implementation of the {@linkplain MobEntityMetadata mob entity metadata}.
 *
 * @since 1.0
 * @see MobEntityMetadata
 */
@NullMarked
public class JetMobEntityMetadata extends JetLivingEntityMetadata implements MobEntityMetadata {

    private static final int MOB_FLAGS_INDEX = 15;

    private static final int NO_AI_FLAG_INDEX = 0;
    private static final int LEFT_HANDED_FLAG_INDEX = 1;
    private static final int AGGRESSIVE_FLAG_INDEX = 2;
    /**
     * Constructs the {@linkplain JetMobEntityMetadata mob entity metadata}.
     *
     * @param entity the entity that the mob entity metadata is being constructed for
     * @since 1.0
     */
    public JetMobEntityMetadata(JetEntity entity) {
        super(entity);
    }

    @Override
    public final boolean noAi() {
        return this.mobFlag(NO_AI_FLAG_INDEX);
    }

    @Override
    public final boolean leftHanded() {
        return this.mobFlag(LEFT_HANDED_FLAG_INDEX);
    }

    @Override
    public final boolean aggressive() {
        return this.mobFlag(AGGRESSIVE_FLAG_INDEX);
    }

    @Override
    public Update<?> createUpdateBuilder() {
        return new Update<>(this);
    }

    @Override
    protected void defineDefaults(IntObjectMapBuilder<EntityMetadataValue> valuesBuilder) {
        super.defineDefaults(valuesBuilder);
        valuesBuilder.put(MOB_FLAGS_INDEX, new EntityMetadataValue.Byte((byte) 0));
    }

    private boolean mobFlag(int index) {
        byte flags = this.value(index, EntityMetadataValue.Byte.class).value();
        return ByteUtil.bitSet(flags, index);
    }

    /**
     * An implementation of the {@linkplain MobEntityMetadata.Update mob entity metadata update}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see MobEntityMetadata.Update
     */
    public static class Update<U extends Update<U>>
            extends JetLivingEntityMetadata.Update<U>
            implements MobEntityMetadata.Update<U> {
        /**
         * Constructs the {@linkplain Update mob entity metadata update implementation}.
         *
         * @param entityMetadata the entity metadata that should be a base for the modified version
         * @since 1.0
         */
        protected Update(JetEntityMetadata entityMetadata) {
            super(entityMetadata);
        }

        @Override
        public final U noAi(boolean value) {
            return this.updateMobFlag(NO_AI_FLAG_INDEX, value);
        }

        @Override
        public final U leftHanded(boolean value) {
            return this.updateMobFlag(LEFT_HANDED_FLAG_INDEX, value);
        }

        @Override
        public final U aggressive(boolean value) {
            return this.updateMobFlag(AGGRESSIVE_FLAG_INDEX, value);
        }

        private U updateMobFlag(int index, boolean value) {
            byte flags = this.currentValue(MOB_FLAGS_INDEX, EntityMetadataValue.Byte.class).value();
            return this.updateValue(index, new EntityMetadataValue.Byte(ByteUtil.withBit(flags, index, value)));
        }
    }
}