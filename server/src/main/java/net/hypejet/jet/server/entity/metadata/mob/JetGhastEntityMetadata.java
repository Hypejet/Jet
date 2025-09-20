package net.hypejet.jet.server.entity.metadata.mob;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.metadata.mob.GhastEntityMetadata;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.entity.metadata.JetEntityMetadata;
import net.hypejet.jet.server.util.collection.IntObjectMapBuilder;

/**
 * An implementation of the {@linkplain GhastEntityMetadata ghast entity metadata}.
 *
 * @since 1.0
 * @see GhastEntityMetadata
 */
@NullMarked
public class JetGhastEntityMetadata extends JetMobEntityMetadata implements GhastEntityMetadata {

    private static final int GHAST_ATTACKING_INDEX = 16;

    /**
     * Constructs the {@linkplain JetGhastEntityMetadata ghast entity metadata}.
     *
     * @param entity the entity that the ghast entity metadata is being constructed for
     * @since 1.0
     */
    public JetGhastEntityMetadata(JetEntity entity) {
        super(entity);
    }

    @Override
    public final boolean attacking() {
        return this.value(GHAST_ATTACKING_INDEX, EntityMetadataValue.Boolean.class).value();
    }

    @Override
    public Update<?> createUpdateBuilder() {
        return new Update<>(this);
    }

    @Override
    protected void defineDefaults(IntObjectMapBuilder<EntityMetadataValue> valuesBuilder) {
        super.defineDefaults(valuesBuilder);
        valuesBuilder.put(GHAST_ATTACKING_INDEX, new EntityMetadataValue.Boolean(false));
    }

    /**
     * An implementation of the {@linkplain GhastEntityMetadata.Update ghast entity metadata update}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see GhastEntityMetadata.Update
     */
    public static class Update<U extends Update<U>>
            extends JetMobEntityMetadata.Update<U>
            implements GhastEntityMetadata.Update<U> {
        /**
         * Constructs the {@linkplain Update ghast entity metadata update implementation}.
         *
         * @param entityMetadata the entity metadata that should be a base for the modified version
         * @since 1.0
         */
        protected Update(JetEntityMetadata entityMetadata) {
            super(entityMetadata);
        }

        @Override
        public U attacking(boolean value) {
            return this.updateValue(GHAST_ATTACKING_INDEX, new EntityMetadataValue.Boolean(value));
        }
    }
}
