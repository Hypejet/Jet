package net.hypejet.jet.server.entity.metadata.mob.creature;

import java.util.Map;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.metadata.mob.creature.PufferfishEntityMetadata;
import net.hypejet.jet.entity.pufferfish.PufferfishState;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.entity.metadata.JetEntityMetadata;
import net.hypejet.jet.server.entity.metadata.mob.JetMobEntityMetadata;
import net.hypejet.jet.server.util.collection.IntObjectMapBuilder;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.kyori.adventure.util.Index;

/**
 * An implementation of the {@linkplain PufferfishEntityMetadata pufferfish entity metadata}.
 *
 * @since 1.0
 * @see PufferfishEntityMetadata
 */
@NullMarked
public class JetPufferfishEntityMetadata extends JetMobEntityMetadata implements PufferfishEntityMetadata {

    private static final int PUFFERFISH_FROM_BUCKET_INDEX = 16;
    private static final int PUFFERFISH_STATE_INDEX = 17;

    private static final @NonNull Index<PufferfishState, Integer> PUFFERFISH_STATE =
        IndexUtil.fromMap(Map.of(
            0, PufferfishState.SMALL,
            1, PufferfishState.PARTIALLY_PUFFED,
            2, PufferfishState.FULL
    ));

    /**
     * Constructs the {@linkplain JetPufferfishEntityMetadata pufferfish entity metadata}.
     *
     * @param entity the entity that the pufferfish entity metadata is being constructed for
     * @since 1.0
     */
    public JetPufferfishEntityMetadata(JetEntity entity) {
        super(entity);
    }

    @Override
    public final boolean fromBucket() {
        return this.value(PUFFERFISH_FROM_BUCKET_INDEX, EntityMetadataValue.Boolean.class).value(); 
    }

    @Override
    public final PufferfishState state() {
        int stateId = this.value(PUFFERFISH_STATE_INDEX, EntityMetadataValue.Int.class).value();
        return PUFFERFISH_STATE.key(stateId);
    }

    @Override
    public Update<?> createUpdateBuilder() {
        return new Update<>(this);
    }

    @Override
    protected void defineDefaults(IntObjectMapBuilder<EntityMetadataValue> valuesBuilder) {
        super.defineDefaults(valuesBuilder);
        valuesBuilder.put(PUFFERFISH_FROM_BUCKET_INDEX, new EntityMetadataValue.Boolean(false));
        valuesBuilder.put(PUFFERFISH_STATE_INDEX, new EntityMetadataValue.Int(PUFFERFISH_STATE.value(PufferfishState.SMALL)));
    }

    /**
     * An implementation of the {@linkplain PufferfishEntityMetadata.Update pufferfish entity metadata update}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see PufferfishEntityMetadata.Update
     */
    public static class Update<U extends Update<U>>
            extends JetMobEntityMetadata.Update<U>
            implements PufferfishEntityMetadata.Update<U> {
        /**
         * Constructs the {@linkplain Update pufferfish entity metadata update implementation}.
         *
         * @param entityMetadata the entity metadata that should be a base for the modified version
         * @since 1.0
         */
        protected Update(JetEntityMetadata entityMetadata) {
            super(entityMetadata);
        }

        @Override
        public U fromBucket(boolean value) {
            return this.updateValue(PUFFERFISH_FROM_BUCKET_INDEX, new EntityMetadataValue.Boolean(value)); 
        }

        @Override
        public U state(PufferfishState value) {
            return this.updateValue(PUFFERFISH_STATE_INDEX, new EntityMetadataValue.Int(PUFFERFISH_STATE.value(value)));
        }
    }
}
