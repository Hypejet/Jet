package net.hypejet.jet.server.entity.metadata.mob.creature;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.metadata.mob.creature.SnowGolemEntityMetadata;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.entity.metadata.JetEntityMetadata;
import net.hypejet.jet.server.entity.metadata.mob.JetMobEntityMetadata;
import net.hypejet.jet.server.util.collection.IntObjectMapBuilder;

/**
 * An implementation of the {@linkplain SnowGolemEntityMetadata snow golem entity metadata}.
 *
 * @since 1.0
 * @see SnowGolemEntityMetadata
 */
@NullMarked
public class JetSnowGolemEntityMetadata extends JetMobEntityMetadata implements SnowGolemEntityMetadata {
    
    private static final int SNOW_GOLEM_FLAGS_INDEX = 16;

    private static final byte FLAG_NO_PUMPKIN = 0;
    private static final byte FLAG_PUMPKIN = 0x10;

    /**
     * Constructs the {@linkplain JetSnowGolemEntityMetadata snow golem entity metadata}.
     *
     * @param entity the entity that the snow golem entity metadata is being constructed for
     * @since 1.0
     */ 
    public JetSnowGolemEntityMetadata(JetEntity entity) {
        super(entity); 
    }

    public final boolean pumpkinHat() {
        return this.value(SNOW_GOLEM_FLAGS_INDEX, EntityMetadataValue.Byte.class).value() == FLAG_PUMPKIN;
    }

    @Override
    public Update<?> createUpdateBuilder() {
        return new Update<>(this);
    }

    @Override
    protected void defineDefaults(IntObjectMapBuilder<EntityMetadataValue> valuesBuilder) {
        super.defineDefaults(valuesBuilder);
        valuesBuilder.put(SNOW_GOLEM_FLAGS_INDEX, new EntityMetadataValue.Byte(FLAG_PUMPKIN));
    }

    /**
     * An implementation of the {@linkplain SnowGolemEntityMetadata.Update snow golem entity metadata update}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see SnowGolemEntityMetadata.Update
     */
    public static class Update<U extends Update<U>>
            extends JetMobEntityMetadata.Update<U>
            implements SnowGolemEntityMetadata.Update<U> {
        /**
         * Constructs the {@linkplain Update snow golem entity metadata update implementation}.
         *
         * @param entityMetadata the entity metadata that should be a base for the modified version
         * @since 1.0
         */
        protected Update(JetEntityMetadata entityMetadata) {
            super(entityMetadata);
        }

        @Override
        public U pumpkinHat(boolean value) {
            return this.updateValue(
                    SNOW_GOLEM_FLAGS_INDEX,
                    new EntityMetadataValue.Byte(value ? FLAG_PUMPKIN : FLAG_NO_PUMPKIN)
            );
        }
    } 
}
