package net.hypejet.jet.server.entity.metadata.mob.animal;

import java.util.Map;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.metadata.mob.animal.MooshRoomEntityMetadata;
import net.hypejet.jet.entity.mooshroom.MooshRoomVariant;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.entity.metadata.mob.ageable.JetAgeableMobEntityMetadata;
import net.hypejet.jet.server.util.collection.IntObjectMapBuilder;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.kyori.adventure.util.Index;

/**
 * An implementation of the {@linkplain MooshRoomEntityMetadata mooshroom entity metadata}.
 *
 * @since 1.0
 * @see MooshRoomEntityMetadata
 */
@NullMarked
public class JetMooshRoomEntityMetadata extends JetAgeableMobEntityMetadata implements MooshRoomEntityMetadata {

    private static final int MOOSHROOM_VARIANT_INDEX = 17;

    private static final @NonNull Index<MooshRoomVariant, Integer> MOOSHROOM_VARIANT =
        IndexUtil.fromMap(Map.of(
            0, MooshRoomVariant.RED,
            1, MooshRoomVariant.BROWN
    ));

    /**
     * Constructs the {@linkplain MooshRoomEntityMetadata mooshroom entity metadata}.
     *
     * @param entity the entity that the mooshroom entity metadata is being constructed for
     * @since 1.0
     */
    public JetMooshRoomEntityMetadata(JetEntity entity) {
        super(entity);
    }

    @Override
    public final MooshRoomVariant variant() {
        int variantId = this.value(MOOSHROOM_VARIANT_INDEX, EntityMetadataValue.Int.class).value();
        return MOOSHROOM_VARIANT.key(variantId);
    }

    @Override
    protected void defineDefaults(IntObjectMapBuilder<EntityMetadataValue> valuesBuilder) {
        super.defineDefaults(valuesBuilder);
        valuesBuilder.put(MOOSHROOM_VARIANT_INDEX, new EntityMetadataValue.Int(MOOSHROOM_VARIANT.value(MooshRoomVariant.RED)));
    }

    @Override
    public Update<?> createUpdateBuilder() {
        return new Update<>(this);
    }

    /**
     * An implementation of the {@linkplain MooshRoomEntityMetadata.Update mooshroom entity metadata update}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see MooshRoomEntityMetadata.Update
     */
    public static class Update<U extends Update<U>>
            extends JetAgeableMobEntityMetadata.Update<U>
            implements MooshRoomEntityMetadata.Update<U> {

        /**
         * Constructs the {@linkplain Update mooshroom entity metadata update implementation}.
         *
         * @param entityMetadata the entity metadata that should be a base for the modified version
         * @since 1.0
         */
        protected Update(JetMooshRoomEntityMetadata entityMetadata) {
            super(entityMetadata);
        }

        @Override
        public U variant(MooshRoomVariant value) {
            return this.updateValue(MOOSHROOM_VARIANT_INDEX, new EntityMetadataValue.Int(MOOSHROOM_VARIANT.value(value)));
        }
    }
}
