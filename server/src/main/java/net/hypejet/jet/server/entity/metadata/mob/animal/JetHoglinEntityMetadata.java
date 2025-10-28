package net.hypejet.jet.server.entity.metadata.mob.animal;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.metadata.mob.animal.HoglinEntityMetadata;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.entity.metadata.mob.ageable.JetAgeableMobEntityMetadata;
import net.hypejet.jet.server.util.collection.IntObjectMapBuilder;

/**
 * An implementation of the {@linkplain HoglinEntityMetadata hoglin entity metadata}.
 *
 * @since 1.0
 * @see HoglinEntityMetadata
 */
@NullMarked
public class JetHoglinEntityMetadata extends JetAgeableMobEntityMetadata implements HoglinEntityMetadata {

    private static final int HAPPY_HOGLIN_IMMUNE_INDEX = 17;

    /**
     * Constructs the {@linkplain HoglinEntityMetadata hoglin entity metadata}.
     *
     * @param entity the entity that the hoglin entity metadata is being constructed for
     * @since 1.0
     */
    public JetHoglinEntityMetadata(JetEntity entity) {
        super(entity);
    }

    @Override
    public final boolean immuneToZombification() {
        return this.value(HAPPY_HOGLIN_IMMUNE_INDEX, EntityMetadataValue.Boolean.class).value();
    }

    @Override
    public Update<?> createUpdateBuilder() {
        return new Update<>(this);
    }

    @Override
    protected void defineDefaults(IntObjectMapBuilder<EntityMetadataValue> valuesBuilder) {
        super.defineDefaults(valuesBuilder);
        valuesBuilder.put(HAPPY_HOGLIN_IMMUNE_INDEX, new EntityMetadataValue.Boolean(false));
    }

    /**
     * An implementation of the {@linkplain HoglinEntityMetadata.Update hoglin entity metadata update}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see HoglinEntityMetadata.Update
     */
    public static class Update<U extends Update<U>>
            extends JetAgeableMobEntityMetadata.Update<U>
            implements HoglinEntityMetadata.Update<U> {
        /**
         * Constructs the {@linkplain Update hoglin entity metadata update implementation}.
         *
         * @param entityMetadata the entity metadata that should be a base for the modified version
         * @since 1.0
         */
        protected Update(JetHoglinEntityMetadata entityMetadata) {
            super(entityMetadata);
        }

        @Override
        public U immuneToZombification(boolean value) {
            return this.updateValue(HAPPY_HOGLIN_IMMUNE_INDEX, new EntityMetadataValue.Boolean(value));
        }
    }
}
