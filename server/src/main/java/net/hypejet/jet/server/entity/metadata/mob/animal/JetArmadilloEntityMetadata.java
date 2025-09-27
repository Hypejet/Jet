package net.hypejet.jet.server.entity.metadata.mob.animal;

import java.util.Map;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.armadillo.ArmadilloState;
import net.hypejet.jet.entity.metadata.mob.animal.ArmadilloEntityMetadata;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.entity.metadata.JetEntityMetadata;
import net.hypejet.jet.server.entity.metadata.mob.ageable.JetAgeableMobEntityMetadata;
import net.hypejet.jet.server.util.collection.IntObjectMapBuilder;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.kyori.adventure.util.Index;

/**
 * An implementation of the {@linkplain ArmadilloEntityMetadata armadillo entity metadata}.
 *
 * @since 1.0
 * @see ArmadilloEntityMetadata
 */
@NullMarked
public class JetArmadilloEntityMetadata extends JetAgeableMobEntityMetadata implements ArmadilloEntityMetadata {

    private static final int ARMADILLO_STATE_INDEX = 17;

    private static final @NonNull Index<ArmadilloState, Integer> ARMADILLO_STATE =
        IndexUtil.fromMap(Map.of(
            0, ArmadilloState.IDLE,
            1, ArmadilloState.ROLLING,
            2, ArmadilloState.SCARED,
            3, ArmadilloState.UNROLLING
    ));

    /**
     * Constructs the {@linkplain ArmadilloEntityMetadata armadillo entity metadata}.
     *
     * @param entity the entity that the armadillo entity metadata is being constructed for
     * @since 1.0
     */ 
    public JetArmadilloEntityMetadata(JetEntity entity) {
        super(entity);
    }

    @Override
    public final ArmadilloState state() {
        int statusId = this.value(ARMADILLO_STATE_INDEX, EntityMetadataValue.Int.class).value();
        return ARMADILLO_STATE.key(statusId);
    }

    @Override
    public Update<?> createUpdateBuilder() {
        return new Update<>(this);
    }

    @Override
    protected void defineDefaults(IntObjectMapBuilder<EntityMetadataValue> valuesBuilder) {
        super.defineDefaults(valuesBuilder);
        valuesBuilder.put(ARMADILLO_STATE_INDEX, new EntityMetadataValue.Int(ARMADILLO_STATE.value(ArmadilloState.IDLE)));
    }

    /**
     * An implementation of the {@linkplain ArmadilloEntityMetadata.Update armadillo entity metadata update}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see ArmadilloEntityMetadata.Update
     */
    public static class Update<U extends Update<U>> 
            extends JetAgeableMobEntityMetadata.Update<U>
            implements ArmadilloEntityMetadata.Update<U> {
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
        public U state(ArmadilloState value) {
            return this.updateValue(ARMADILLO_STATE_INDEX, new EntityMetadataValue.Int(ARMADILLO_STATE.value(value)));
        }
    } 
}
