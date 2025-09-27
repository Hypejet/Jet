package net.hypejet.jet.server.entity.metadata.mob.ageable;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.metadata.mob.ageable.GlowSquidEntityMetadata;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.entity.metadata.JetEntityMetadata;
import net.hypejet.jet.server.util.collection.IntObjectMapBuilder;

/**
 * An implementation of the {@linkplain GlowSquidEntityMetadata glow squid entity metadata}.
 *
 * @since 1.0
 * @see GlowSquidEntityMetadata
 */
@NullMarked
public class JetGlowSquidEntityMetadata extends JetAgeableMobEntityMetadata implements GlowSquidEntityMetadata {

     private static final int GLOW_SQUID_DARK_TICKS_REMAINING_INDEX = 17;
    
    /**
     * Constructs the {@linkplain GlowSquidEntityMetadata glow squid entity metadata}.
     *
     * @param entity the entity that the dolphin entity metadata is being constructed for
     * @since 1.0
     */
    public JetGlowSquidEntityMetadata(JetEntity entity) {
        super(entity);
    }

    @Override
    public final int darkTicksRemaining() {
        return this.value(GLOW_SQUID_DARK_TICKS_REMAINING_INDEX, EntityMetadataValue.Int.class).value(); 
    }

    @Override
    public Update<?> createUpdateBuilder() {
        return new Update<>(this);
    }

    @Override
    protected void defineDefaults(IntObjectMapBuilder<EntityMetadataValue> valuesBuilder) {
        super.defineDefaults(valuesBuilder);
        valuesBuilder.put(GLOW_SQUID_DARK_TICKS_REMAINING_INDEX, new EntityMetadataValue.Int(0));
    }

    /**
     * An implementation of the {@linkplain GlowSquidEntityMetadata.Update glow squid entity metadata update}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see GlowSquidEntityMetadata.Update
     */
    public static class Update<U extends Update<U>> 
            extends JetAgeableMobEntityMetadata.Update<U>
            implements GlowSquidEntityMetadata.Update<U> {
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
        public U darkTicksRemaining(int value) {
            return this.updateValue(GLOW_SQUID_DARK_TICKS_REMAINING_INDEX, new EntityMetadataValue.Int(value));
        }
    } 
}
