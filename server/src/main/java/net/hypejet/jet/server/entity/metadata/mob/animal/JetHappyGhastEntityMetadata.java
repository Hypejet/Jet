package net.hypejet.jet.server.entity.metadata.mob.animal;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.metadata.mob.animal.HappyGhastEntityMetadata;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.entity.metadata.mob.ageable.JetAgeableMobEntityMetadata;
import net.hypejet.jet.server.util.collection.IntObjectMapBuilder;

/**
 * An implementation of the {@linkplain HappyGhastEntityMetadata happy ghast entity metadata}.
 *
 * @since 1.0
 * @see HappyGhastEntityMetadata
 */
@NullMarked
public class JetHappyGhastEntityMetadata extends JetAgeableMobEntityMetadata implements HappyGhastEntityMetadata {

    private static final int HAPPY_GHAST_LEASH_HOLDER_INDEX = 17;
    private static final int HAPPY_GHAST_STAYS_STILL_INDEX = 18;

    /**
     * Constructs the {@linkplain HappyGhastEntityMetadata happy ghast entity metadata}.
     *
     * @param entity the entity that the happy ghast entity metadata is being constructed for
     * @since 1.0
     */
    public JetHappyGhastEntityMetadata(JetEntity entity) {
        super(entity);
    }

    @Override
    public final boolean leashHolder() {
        return this.value(HAPPY_GHAST_LEASH_HOLDER_INDEX, EntityMetadataValue.Boolean.class).value();
    }

    @Override
    public final boolean staysStill() {
        return this.value(HAPPY_GHAST_STAYS_STILL_INDEX, EntityMetadataValue.Boolean.class).value();
    }

    @Override
    public Update<?> createUpdateBuilder() {
        return new Update<>(this);
    }

    @Override
    protected void defineDefaults(IntObjectMapBuilder<EntityMetadataValue> valuesBuilder) {
        super.defineDefaults(valuesBuilder);
        valuesBuilder.put(HAPPY_GHAST_LEASH_HOLDER_INDEX, new EntityMetadataValue.Boolean(false));
        valuesBuilder.put(HAPPY_GHAST_STAYS_STILL_INDEX, new EntityMetadataValue.Boolean(false));
    }

    /**
     * An implementation of the {@linkplain HappyGhastEntityMetadata.Update happy ghast entity metadata update}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see HappyGhastEntityMetadata.Update
     */
    public static class Update<U extends Update<U>>
            extends JetAgeableMobEntityMetadata.Update<U>
            implements HappyGhastEntityMetadata.Update<U> {
        /**
         * Constructs the {@linkplain Update happy ghast entity metadata update implementation}.
         *
         * @param entityMetadata the entity metadata that should be a base for the modified version
         * @since 1.0
         */
        protected Update(JetHappyGhastEntityMetadata entityMetadata) {
            super(entityMetadata);
        }

        @Override
        public U leashHolder(boolean value) {
            return this.updateValue(HAPPY_GHAST_LEASH_HOLDER_INDEX, new EntityMetadataValue.Boolean(value));
        }

        @Override
        public U staysStill(boolean value) {
            return this.updateValue(HAPPY_GHAST_STAYS_STILL_INDEX, new EntityMetadataValue.Boolean(value));
        }
    }
}
