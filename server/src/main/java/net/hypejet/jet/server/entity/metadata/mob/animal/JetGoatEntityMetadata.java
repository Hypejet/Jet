package net.hypejet.jet.server.entity.metadata.mob.animal;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.metadata.mob.animal.GoatEntityMetadata;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.entity.metadata.mob.ageable.JetAgeableMobEntityMetadata;
import net.hypejet.jet.server.util.collection.IntObjectMapBuilder;

/**
 * An implementation of the {@linkplain GoatEntityMetadata goat entity metadata}.
 *
 * @since 1.0
 * @see GoatEntityMetadata
 */
@NullMarked
public class JetGoatEntityMetadata extends JetAgeableMobEntityMetadata implements GoatEntityMetadata {

    private static final int GOAT_SCREAMING_INDEX = 17;
    private static final int GOAT_LEFT_HORN_INDEX = 18;
    private static final int GOAT_RIGHT_HORN_INDEX = 19;

    /**
     * Constructs the {@linkplain GoatEntityMetadata goat entity metadata}.
     *
     * @param entity the entity that the goat entity metadata is being constructed for
     * @since 1.0
     */
    public JetGoatEntityMetadata(JetEntity entity) {
        super(entity);
    }

    @Override
    public final boolean screaming() {
        return this.value(GOAT_SCREAMING_INDEX, EntityMetadataValue.Boolean.class).value();
    }

    @Override
    public final boolean hasLeftHorn() {
        return this.value(GOAT_LEFT_HORN_INDEX, EntityMetadataValue.Boolean.class).value();
    }

    @Override
    public final boolean hasRightHorn() {
        return this.value(GOAT_RIGHT_HORN_INDEX, EntityMetadataValue.Boolean.class).value();
    }

    @Override
    public Update<?> createUpdateBuilder() {
        return new Update<>(this);
    }

    @Override
    protected void defineDefaults(IntObjectMapBuilder<EntityMetadataValue> valuesBuilder) {
        super.defineDefaults(valuesBuilder);
        valuesBuilder.put(GOAT_SCREAMING_INDEX, new EntityMetadataValue.Boolean(false));
        valuesBuilder.put(GOAT_LEFT_HORN_INDEX, new EntityMetadataValue.Boolean(true));
        valuesBuilder.put(GOAT_RIGHT_HORN_INDEX, new EntityMetadataValue.Boolean(true));
    }

    /**
     * An implementation of the {@linkplain GoatEntityMetadata.Update goat entity metadata update}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see GoatEntityMetadata.Update
     */
    public static class Update<U extends Update<U>>
            extends JetAgeableMobEntityMetadata.Update<U>
            implements GoatEntityMetadata.Update<U> {

        /**
         * Constructs the {@linkplain Update goat entity metadata update implementation}.
         *
         * @param entityMetadata the entity metadata that should be a base for the modified version
         * @since 1.0
         */
        protected Update(JetGoatEntityMetadata entityMetadata) {
            super(entityMetadata);
        }

        @Override
        public U screaming(boolean value) {
            return this.updateValue(GOAT_SCREAMING_INDEX, new EntityMetadataValue.Boolean(value));
        }

        @Override
        public U leftHorn(boolean value) {
            return this.updateValue(GOAT_LEFT_HORN_INDEX, new EntityMetadataValue.Boolean(value));
        }

        @Override
        public U rightHorn(boolean value) {
            return this.updateValue(GOAT_RIGHT_HORN_INDEX, new EntityMetadataValue.Boolean(value));
        }
    }
}
