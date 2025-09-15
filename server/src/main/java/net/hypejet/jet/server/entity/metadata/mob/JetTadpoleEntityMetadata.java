package net.hypejet.jet.server.entity.metadata.mob;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.metadata.mob.TadpoleEntityMetadata;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.entity.metadata.JetEntityMetadata;
import net.hypejet.jet.server.util.collection.IntObjectMapBuilder;

/**
 * An implementation of the {@linkplain TadpoleEntityMetadata tadpole entity metadata}.
 *
 * @since 1.0
 * @see TadpoleEntityMetadata
 */
@NullMarked
public class JetTadpoleEntityMetadata extends JetCreatureEntityMetadata implements TadpoleEntityMetadata {

    private static final int TADPOLE_FROM_BUCKET_INDEX = 16;

    /**
     * Constructs the {@linkplain JetTadpoleEntityMetadata tadpole entity metadata}.
     *
     * @param entity the entity that the tadpole entity metadata is being constructed for
     * @since 1.0
     */
    public JetTadpoleEntityMetadata(JetEntity entity) {
        super(entity);
    }

    @Override
    public final boolean fromBucket() {
        return this.value(TADPOLE_FROM_BUCKET_INDEX, EntityMetadataValue.Boolean.class).value();
    }

    @Override
    public Update<?> createUpdateBuilder() {
        return new Update<>(this);
    }

    @Override
    protected void defineDefaults(IntObjectMapBuilder<EntityMetadataValue> valuesBuilder) {
        super.defineDefaults(valuesBuilder);
        valuesBuilder.put(TADPOLE_FROM_BUCKET_INDEX, new EntityMetadataValue.Boolean(false));
    }

    /**
     * An implementation of the {@linkplain TadpoleEntityMetadata.Update tadpole entity metadata update}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see TadpoleEntityMetadata.Update
     */
    public static class Update<U extends Update<U>>
            extends JetMobEntityMetadata.Update<U>
            implements TadpoleEntityMetadata.Update<U> {
        /**
         * Constructs the {@linkplain Update tadpole entity metadata update implementation}.
         *
         * @param entityMetadata the entity metadata that should be a base for the modified version
         * @since 1.0
         */
        protected Update(JetEntityMetadata entityMetadata) {
            super(entityMetadata);
        }

        @Override
        public U fromBucket(boolean value) {
            return this.updateValue(TADPOLE_FROM_BUCKET_INDEX, new EntityMetadataValue.Boolean(value));
        }
    }
}
