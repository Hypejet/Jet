package net.hypejet.jet.server.entity.metadata.mob;

import net.hypejet.jet.entity.metadata.mob.BatEntityMetadata;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.entity.metadata.JetEntityMetadata;
import net.hypejet.jet.server.util.collection.IntObjectMapBuilder;
import org.jspecify.annotations.NullMarked;

/**
 * An implementation of the {@linkplain BatEntityMetadata bat entity metadata}.
 *
 * @since 1.0
 * @see BatEntityMetadata
 */
@NullMarked
public class JetBatEntityMetadata extends JetMobEntityMetadata implements BatEntityMetadata {

    private static final int BAT_FLAGS_INDEX = 16;

    private static final byte FLAGS_NOT_RESTING = 0;
    private static final byte FLAGS_RESTING = 1;

    /**
     * Constructs the {@linkplain JetBatEntityMetadata bat entity metadata}.
     *
     * @param entity the entity that the bat entity metadata is being constructed for
     * @since 1.0
     */
    public JetBatEntityMetadata(JetEntity entity) {
        super(entity);
    }

    @Override
    public final boolean resting() {
        return this.value(BAT_FLAGS_INDEX, EntityMetadataValue.Byte.class).value() == FLAGS_RESTING;
    }

    @Override
    public Update<?> createUpdateBuilder() {
        return new Update<>(this);
    }

    @Override
    protected void defineDefaults(IntObjectMapBuilder<EntityMetadataValue> valuesBuilder) {
        super.defineDefaults(valuesBuilder);
        valuesBuilder.put(BAT_FLAGS_INDEX, new EntityMetadataValue.Byte(FLAGS_NOT_RESTING));
    }

    /**
     * An implementation of the {@linkplain BatEntityMetadata.Update bat entity metadata update}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see BatEntityMetadata.Update
     */
    public static class Update<U extends Update<U>>
            extends JetMobEntityMetadata.Update<U>
            implements BatEntityMetadata.Update<U> {
        /**
         * Constructs the {@linkplain Update bat entity metadata update implementation}.
         *
         * @param entityMetadata the entity metadata that should be a base for the modified version
         * @since 1.0
         */
        protected Update(JetEntityMetadata entityMetadata) {
            super(entityMetadata);
        }

        @Override
        public final U resting(boolean value) {
            return this.updateValue(
                    BAT_FLAGS_INDEX,
                    new EntityMetadataValue.Byte(value ? FLAGS_RESTING : FLAGS_NOT_RESTING)
            );
        }
    }
}