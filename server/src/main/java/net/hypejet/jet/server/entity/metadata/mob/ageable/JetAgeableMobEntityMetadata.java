package net.hypejet.jet.server.entity.metadata.mob.ageable;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.metadata.mob.MobEntityMetadata;
import net.hypejet.jet.entity.metadata.mob.ageable.AgeableMobEntityMetadata;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.entity.metadata.JetEntityMetadata;
import net.hypejet.jet.server.entity.metadata.mob.JetMobEntityMetadata;
import net.hypejet.jet.server.util.collection.IntObjectMapBuilder;

/**
 * An implementation of the {@linkplain AgeableMobEntityMetadata ageable mob entity metadata}.
 *
 * @since 1.0
 * @see MobEntityMetadata
 */
@NullMarked
public class JetAgeableMobEntityMetadata extends JetMobEntityMetadata implements AgeableMobEntityMetadata {

    private static final int AGEABLE_BABY_INDEX = 16;

    /**
     * Constructs the {@linkplain JetAgeableMobEntityMetadata ageable mob entity metadata}.
     *
     * @param entity the entity that the ageable mob entity metadata is being constructed for
     * @since 1.0
     */
    public JetAgeableMobEntityMetadata(JetEntity entity) {
        super(entity);
    }

    @Override
    public final boolean baby() {
        return this.value(AGEABLE_BABY_INDEX, EntityMetadataValue.Boolean.class).value(); 
    }

    @Override
    public Update<?> createUpdateBuilder() {
        return new Update<>(this);
    }

    @Override
    protected void defineDefaults(IntObjectMapBuilder<EntityMetadataValue> valuesBuilder) {
        super.defineDefaults(valuesBuilder);
        valuesBuilder.put(AGEABLE_BABY_INDEX, new EntityMetadataValue.Boolean(false));
    }

    /**
     * An implementation of the {@linkplain AgeableMobEntityMetadata.Update ageable mob entity metadata update}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see AgeableMobEntityMetadata.Update
     */
    public static class Update<U extends Update<U>> 
            extends JetMobEntityMetadata.Update<U>
            implements AgeableMobEntityMetadata.Update<U> {
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
        public U baby(boolean value) {
            return this.updateValue(AGEABLE_BABY_INDEX, new EntityMetadataValue.Boolean(value)); 
        }
    }
}
