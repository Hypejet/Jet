package net.hypejet.jet.server.entity.metadata.mob;

import java.util.Map;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.enderdragon.EnderDragonPhase;
import net.hypejet.jet.entity.metadata.mob.EnderDragonEntityMetadata;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.entity.metadata.JetEntityMetadata;
import net.hypejet.jet.server.util.collection.IntObjectMapBuilder;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.kyori.adventure.util.Index;

/**
 * An implementation of the {@linkplain EnderDragonEntityMetadata ender dragon entity metadata}.
 *
 * @since 1.0
 * @see EnderDragonEntityMetadata
 */
@NullMarked
public final class JetEnderDragonEntityMetadata extends JetMobEntityMetadata implements EnderDragonEntityMetadata {

    private static final int ENDER_DRAGON_PHASE_INDEX = 16;
  
    public static final @NonNull Index<EnderDragonPhase, Integer> ENDER_DRAGON_PHASE =
        IndexUtil.fromMap(Map.ofEntries(
                Map.entry(0, EnderDragonPhase.CIRCLING),
                Map.entry(1, EnderDragonPhase.STRAFING),
                Map.entry(2, EnderDragonPhase.FLYING_TO_PORTAL_TO_LAND),
                Map.entry(3, EnderDragonPhase.LANDING_ON_PORTAL),
                Map.entry(4, EnderDragonPhase.TAKING_OFF_FROM_PORTAL),
                Map.entry(5, EnderDragonPhase.BREATH_ATTACK),
                Map.entry(6, EnderDragonPhase.SEARCHING_FOR_PLAYER),
                Map.entry(7, EnderDragonPhase.ROARING),
                Map.entry(8, EnderDragonPhase.CHARGING_PLAYER),
                Map.entry(9, EnderDragonPhase.FLYING_TO_PORTAL_TO_DIE),
                Map.entry(10, EnderDragonPhase.HOVERING)
    ));

    /**
     * Constructs the {@linkplain JetEnderDragonEntityMetadata ender dragon entity metadata}.
     *
     * @param entity the entity that the ender dragon entity metadata is being constructed for
     * @since 1.0
     */
    public JetEnderDragonEntityMetadata(JetEntity entity) {
        super(entity);
    }

    @Override
    public final EnderDragonPhase phase() {
        int phaseId = this.value(ENDER_DRAGON_PHASE_INDEX, EntityMetadataValue.Int.class).value();
        return ENDER_DRAGON_PHASE.key(phaseId);
    }

    @Override
    public Update<?> createUpdateBuilder() {
        return new Update<>(this);
    }

    @Override
    protected void defineDefaults(IntObjectMapBuilder<EntityMetadataValue> valuesBuilder) {
        super.defineDefaults(valuesBuilder);
        valuesBuilder.put(ENDER_DRAGON_PHASE_INDEX, new EntityMetadataValue.Int(ENDER_DRAGON_PHASE.value(EnderDragonPhase.HOVERING)));
    }

    /**
     * An implementation of the {@linkplain EnderDragonEntityMetadata.Update ender dragon entity metadata update}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see EnderDragonEntityMetadata.Update
     */
    public static class Update<U extends Update<U>>
            extends JetMobEntityMetadata.Update<U>
            implements EnderDragonEntityMetadata.Update<U> {

        /**
         * Constructs the {@linkplain Update ender dragon entity metadata update implementation}.
         *
         * @param entityMetadata the entity metadata that should be a base for the modified version
         * @since 1.0
         */
        protected Update(JetEntityMetadata entityMetadata) {
            super(entityMetadata);
        }

        @Override
        public U phase(EnderDragonPhase value) {
            return this.updateValue(ENDER_DRAGON_PHASE_INDEX, new EntityMetadataValue.Int(ENDER_DRAGON_PHASE.value(value)));
        }
    }
}
