package net.hypejet.jet.server.entity.metadata.mob.ageable;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.metadata.mob.ageable.DolphinEntityMetadata;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.entity.metadata.JetEntityMetadata;
import net.hypejet.jet.server.util.collection.IntObjectMapBuilder;

/**
 * An implementation of the {@linkplain DolphinEntityMetadata dolphin entity metadata}.
 *
 * @since 1.0
 * @see DolphinEntityMetadata
 */
@NullMarked
public class JetDolphinEntityMetadata extends JetAgeableMobEntityMetadata implements DolphinEntityMetadata {

    private static final int DOLPHIN_FISH_INDEX = 17;
    private static final int DOLPHIN_MOUISTURE_LEVEL_INDEX = 18;

    /**
     * Constructs the {@linkplain DolphinEntityMetadata dolphin entity metadata}.
     *
     * @param entity the entity that the dolphin entity metadata is being constructed for
     * @since 1.0
     */
    public JetDolphinEntityMetadata(JetEntity entity) {
        super(entity);
    }

    @Override
    public final boolean fish() {
        return this.value(DOLPHIN_FISH_INDEX, EntityMetadataValue.Boolean.class).value(); 
    }

    @Override
    public int moistureLevel() {
        return this.value(DOLPHIN_MOUISTURE_LEVEL_INDEX, EntityMetadataValue.Int.class).value(); 
    }

    @Override
    public Update<?> createUpdateBuilder() {
        return new Update<>(this);
    }

    @Override
    protected void defineDefaults(IntObjectMapBuilder<EntityMetadataValue> valuesBuilder) {
        super.defineDefaults(valuesBuilder);
        valuesBuilder.put(DOLPHIN_FISH_INDEX, new EntityMetadataValue.Boolean(false));
        valuesBuilder.put(DOLPHIN_MOUISTURE_LEVEL_INDEX, new EntityMetadataValue.Int(2400));
    }

    /**
     * An implementation of the {@linkplain DolphinEntityMetadata.Update dolphin entity metadata update}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see DolphinEntityMetadata.Update
     */
    public static class Update<U extends Update<U>> 
            extends JetAgeableMobEntityMetadata.Update<U>
            implements DolphinEntityMetadata.Update<U> {
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
        public U fish(boolean value) {
            return this.updateValue(DOLPHIN_FISH_INDEX, new EntityMetadataValue.Boolean(value)); 
        }

        @Override
        public U moistureLevel(int value) {
            return this.updateValue(DOLPHIN_MOUISTURE_LEVEL_INDEX, new EntityMetadataValue.Int(value));
        }
    } 
}
