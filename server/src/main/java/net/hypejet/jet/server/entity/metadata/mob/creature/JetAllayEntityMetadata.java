package net.hypejet.jet.server.entity.metadata.mob.creature;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.metadata.mob.creature.AllayEntityMetadata;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.entity.metadata.JetEntityMetadata;
import net.hypejet.jet.server.entity.metadata.mob.JetMobEntityMetadata;
import net.hypejet.jet.server.util.collection.IntObjectMapBuilder;

/**
 * An implementation of the {@linkplain AllayEntityMetadata allay entity metadata}.
 *
 * @since 1.0
 * @see AllayEntityMetadata
 */
@NullMarked
public class JetAllayEntityMetadata extends JetMobEntityMetadata implements AllayEntityMetadata {

    private static final int ALLAY_DANCING_INDEX = 16;
    private static final int ALLAY_DUPLICATE_INDEX = 17;

    /**
     * Constructs the {@linkplain JetAllayEntityMetadata allay entity metadata}.
     *
     * @param entity the entity that the allay entity metadata is being constructed for
     * @since 1.0
     */
    public JetAllayEntityMetadata(JetEntity entity) {
        super(entity);
    }

    @Override
    public final boolean dancing() {
        return this.value(ALLAY_DANCING_INDEX, EntityMetadataValue.Boolean.class).value();
    }

    @Override
    public final boolean duplicate() {
        return this.value(ALLAY_DUPLICATE_INDEX, EntityMetadataValue.Boolean.class).value();
    }

    @Override
    public Update<?> createUpdateBuilder() {
        return new Update<>(this);
    }

    @Override
    protected void defineDefaults(IntObjectMapBuilder<EntityMetadataValue> valuesBuilder) {
        super.defineDefaults(valuesBuilder);
        valuesBuilder.put(ALLAY_DANCING_INDEX, new EntityMetadataValue.Boolean(false));
        valuesBuilder.put(ALLAY_DUPLICATE_INDEX, new EntityMetadataValue.Boolean(true));
    }

    /**
     * An implementation of the {@linkplain AllayEntityMetadata.Update allay entity metadata update}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see AllayEntityMetadata.Update
     */
    public static class Update<U extends Update<U>>
            extends JetMobEntityMetadata.Update<U>
            implements AllayEntityMetadata.Update<U> {
        /**
         * Constructs the {@linkplain Update allay entity metadata update implementation}.
         *
         * @param entityMetadata the entity metadata that should be a base for the modified version
         * @since 1.0
         */
        protected Update(JetEntityMetadata entityMetadata) {
            super(entityMetadata);
        }

        @Override
        public U dancing(boolean value) {
           return this.updateValue(ALLAY_DANCING_INDEX, new EntityMetadataValue.Boolean(value));
        } 

        @Override
        public U duplicate(boolean value) {
            return this.updateValue(ALLAY_DUPLICATE_INDEX, new EntityMetadataValue.Boolean(value));
        }
    }
}
