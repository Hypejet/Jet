package net.hypejet.jet.server.entity.metadata.mob.animal;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.metadata.mob.animal.BeeEntityMetadata;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.entity.metadata.mob.ageable.JetAgeableMobEntityMetadata;
import net.hypejet.jet.server.util.collection.IntObjectMapBuilder;

/**
 * An implementation of the {@linkplain BeeEntityMetadata bee entity metadata}.
 *
 * @since 1.0
 * @see BeeEntityMetadata
 */
@NullMarked
public class JetBeeEntityMetadata extends JetAgeableMobEntityMetadata implements BeeEntityMetadata {

    private static final int BEE_FLAGS_INDEX = 17;
    private static final int BEE_ANGER_TIME_INDEX = 18;

    private static final byte FLAGS_NONE = 0;
    private static final byte FLAGS_ANGRY = 2;
    private static final byte FLAGS_STUNG = 4;
    private static final byte FLAGS_NECTAR = 8;

    /**
     * Constructs the {@linkplain BeeEntityMetadata bee entity metadata}.
     *
     * @param entity the entity that the bee entity metadata is being constructed for
     * @since 1.0
     */
    public JetBeeEntityMetadata(JetEntity entity) {
        super(entity);
    }

    @Override
    public final boolean angry() {
        return this.value(BEE_FLAGS_INDEX, EntityMetadataValue.Byte.class).value() == FLAGS_ANGRY;
    }

    @Override
    public final boolean hasStung() {
        return this.value(BEE_FLAGS_INDEX, EntityMetadataValue.Byte.class).value() == FLAGS_STUNG;
    }

    @Override
    public final boolean hasNectar() {
        return this.value(BEE_FLAGS_INDEX, EntityMetadataValue.Byte.class).value() == FLAGS_NECTAR;
    }

    @Override
    public final int angerTimeTicks() {
        return this.value(BEE_ANGER_TIME_INDEX, EntityMetadataValue.Int.class).value();
    }

    @Override
    public Update<?> createUpdateBuilder() {
        return new Update<>(this);
    }

    @Override
    protected void defineDefaults(IntObjectMapBuilder<EntityMetadataValue> valuesBuilder) {
        super.defineDefaults(valuesBuilder);
        valuesBuilder.put(BEE_FLAGS_INDEX, new EntityMetadataValue.Byte(FLAGS_NONE));
        valuesBuilder.put(BEE_ANGER_TIME_INDEX, new EntityMetadataValue.Int(0));
    }

    /**
     * An implementation of the {@linkplain BeeEntityMetadata.Update bee entity metadata update}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see BeeEntityMetadata.Update
     */
    public static class Update<U extends Update<U>>
            extends JetAgeableMobEntityMetadata.Update<U>
            implements BeeEntityMetadata.Update<U> {
        /**
         * Constructs the {@linkplain Update bee entity metadata update implementation}.
         *
         * @param entityMetadata the entity metadata that should be a base for the modified version
         * @since 1.0
         */
        protected Update(JetBeeEntityMetadata entityMetadata) {
            super(entityMetadata);
        }
    
        @Override
        public U angry(boolean value) {
            return this.updateValue(BEE_FLAGS_INDEX, new EntityMetadataValue.Byte(value ? FLAGS_ANGRY : FLAGS_NONE));
        }

        @Override
        public U stung(boolean value) {
            return this.updateValue(BEE_FLAGS_INDEX, new EntityMetadataValue.Byte(value ? FLAGS_STUNG : FLAGS_NONE));
        }

        @Override
        public U nectar(boolean value) {
            return this.updateValue(BEE_FLAGS_INDEX, new EntityMetadataValue.Byte(value ? FLAGS_NECTAR : FLAGS_NONE));
        }

        public U angerTimeTicks(int ticks) {
            return this.updateValue(BEE_ANGER_TIME_INDEX, new EntityMetadataValue.Int(ticks));
        }
    }
}
