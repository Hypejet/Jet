package net.hypejet.jet.server.entity.metadata.mob.animal;

import java.util.Map;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.axolotl.AxolotlVariant;
import net.hypejet.jet.entity.metadata.mob.animal.AxolotlEntityMetadata;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.entity.metadata.JetEntityMetadata;
import net.hypejet.jet.server.entity.metadata.mob.ageable.JetAgeableMobEntityMetadata;
import net.hypejet.jet.server.util.collection.IntObjectMapBuilder;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.kyori.adventure.util.Index;

/**
 * An implementation of the {@linkplain AxolotlEntityMetadata axolotl entity metadata}.
 *
 * @since 1.0
 * @see AxolotlEntityMetadata
 */
@NullMarked
public class JetAxolotlEntityMetadata extends JetAgeableMobEntityMetadata implements AxolotlEntityMetadata {

    private static final int AXOLOTL_VARIANT_INDEX = 17;
    private static final int AXOLOTL_PLAYING_DEAD_INDEX = 18;
    private static final int AXOLOTL_FROM_BUCKET_INDEX = 19; 

    private static final @NonNull Index<AxolotlVariant, Integer> AXOLOTL_VARIANT =
        IndexUtil.fromMap(Map.of(
            0, AxolotlVariant.LUCY,
            1, AxolotlVariant.WILD,
            2, AxolotlVariant.GOLD,
            3, AxolotlVariant.CYAN,
            4, AxolotlVariant.BLUE
    ));

    /**
     * Constructs the {@linkplain AxolotlEntityMetadata axolotl entity metadata}.
     *
     * @param entity the entity that the axolotl entity metadata is being constructed for
     * @since 1.0
     */ 
    public JetAxolotlEntityMetadata(JetEntity entity) {
        super(entity);
    }

    @Override
    public final AxolotlVariant variant() {
        int variantId = this.value(AXOLOTL_VARIANT_INDEX, EntityMetadataValue.Int.class).value();
        return AXOLOTL_VARIANT.key(variantId);
    }

    @Override
    public final boolean playingDead() {
        return this.value(AXOLOTL_PLAYING_DEAD_INDEX, EntityMetadataValue.Boolean.class).value();  
    }

    @Override
    public final boolean fromBucket() {
        return this.value(AXOLOTL_FROM_BUCKET_INDEX, EntityMetadataValue.Boolean.class).value();
    }

    @Override
    public Update<?> createUpdateBuilder() {
        return new Update<>(this);
    }

    @Override
    protected void defineDefaults(IntObjectMapBuilder<EntityMetadataValue> valuesBuilder) {
        super.defineDefaults(valuesBuilder);
        valuesBuilder.put(AXOLOTL_VARIANT_INDEX, new EntityMetadataValue.Int(AXOLOTL_VARIANT.value(AxolotlVariant.LUCY)));
        valuesBuilder.put(AXOLOTL_PLAYING_DEAD_INDEX, new EntityMetadataValue.Boolean(false));
        valuesBuilder.put(AXOLOTL_FROM_BUCKET_INDEX, new EntityMetadataValue.Boolean(false));
    }

    /**
     * An implementation of the {@linkplain AxolotlEntityMetadata.Update axolotl entity metadata update}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see AxolotlEntityMetadata.Update
     */
    public static class Update<U extends Update<U>> 
            extends JetAgeableMobEntityMetadata.Update<U>
            implements AxolotlEntityMetadata.Update<U> {
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
        public U variant(AxolotlVariant value) {
            return this.updateValue(AXOLOTL_VARIANT_INDEX, new EntityMetadataValue.Int(AXOLOTL_VARIANT.value(value)));
        }

        @Override
        public U playingDead(boolean value) {
            return this.updateValue(AXOLOTL_PLAYING_DEAD_INDEX, new EntityMetadataValue.Boolean(value));
        }

        @Override
        public U fromBucket(boolean value) {
            return this.updateValue(AXOLOTL_FROM_BUCKET_INDEX, new EntityMetadataValue.Boolean(value));
        }
    }
}
