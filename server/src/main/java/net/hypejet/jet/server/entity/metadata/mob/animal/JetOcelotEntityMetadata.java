package net.hypejet.jet.server.entity.metadata.mob.animal;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.metadata.mob.animal.OcelotEntityMetadata;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.entity.metadata.mob.ageable.JetAgeableMobEntityMetadata;
import net.hypejet.jet.server.util.collection.IntObjectMapBuilder;

/**
 * An implementation of the {@linkplain OcelotEntityMetadata ocelot entity metadata}.
 *
 * @since 1.0
 * @see OcelotEntityMetadata
 */
@NullMarked
public class JetOcelotEntityMetadata extends JetAgeableMobEntityMetadata implements OcelotEntityMetadata {

    private static final int OCELOT_TRUSTING_INDEX = 17;

    /**
     * Constructs the {@linkplain OcelotEntityMetadata ocelot entity metadata}.
     *
     * @param entity the entity that the ocelot entity metadata is being constructed for
     * @since 1.0
     */
    public JetOcelotEntityMetadata(JetEntity entity) {
        super(entity);
    }

    @Override
    public boolean trusting() {
        return this.value(OCELOT_TRUSTING_INDEX, EntityMetadataValue.Boolean.class).value();
    }

    @Override
    protected void defineDefaults(IntObjectMapBuilder<EntityMetadataValue> valuesBuilder) {
        super.defineDefaults(valuesBuilder);
        valuesBuilder.put(OCELOT_TRUSTING_INDEX, new EntityMetadataValue.Boolean(false));
    }

    @Override
    public Update<?> createUpdateBuilder() {
        return new Update<>(this);
    }

    /**
     * An implementation of the {@linkplain OcelotEntityMetadata.Update ocelot entity metadata update}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see OcelotEntityMetadata.Update
     */
    public static class Update<U extends Update<U>>
            extends JetAgeableMobEntityMetadata.Update<U>
            implements OcelotEntityMetadata.Update<U> {

        /**
         * Constructs the {@linkplain Update ocelot entity metadata update implementation}.
         *
         * @param entityMetadata the entity metadata that should be a base for the modified version
         * @since 1.0
         */
        protected Update(JetOcelotEntityMetadata entityMetadata) {
            super(entityMetadata);
        }

        @Override
        public U trusting(boolean value) {
            return this.updateValue(OCELOT_TRUSTING_INDEX, new EntityMetadataValue.Boolean(value));
        }
    }
}
