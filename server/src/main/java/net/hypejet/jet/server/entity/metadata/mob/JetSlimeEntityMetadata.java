package net.hypejet.jet.server.entity.metadata.mob;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.metadata.mob.SlimeEntityMetadata;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.entity.metadata.JetEntityMetadata;
import net.hypejet.jet.server.util.collection.IntObjectMapBuilder;

/**
 * An implementation of the {@linkplain SlimeEntityMetadata slime entity metadata}.
 *
 * @since 1.0
 * @see SlimeEntityMetadata
 */
@NullMarked
public class JetSlimeEntityMetadata extends JetMobEntityMetadata implements SlimeEntityMetadata {

    private static final int SLIME_SIZE_INDEX = 16;

    /**
     * Constructs the {@linkplain JetSlimeEntityMetadata slime entity metadata}.
     *
     * @param entity the entity that the slime entity metadata is being constructed for
     * @since 1.0
     */
    public JetSlimeEntityMetadata(JetEntity entity) {
        super(entity);
    }

    @Override
    public final int size() {
        return this.value(SLIME_SIZE_INDEX, EntityMetadataValue.Int.class).value();
    }

    @Override
    public Update<?> createUpdateBuilder() {
        return new Update<>(this);
    }

    @Override
    protected void defineDefaults(IntObjectMapBuilder<EntityMetadataValue> valuesBuilder) {
        super.defineDefaults(valuesBuilder);
        valuesBuilder.put(SLIME_SIZE_INDEX, new EntityMetadataValue.Int(1));
    }

    /**
     * An implementation of the {@linkplain SlimeEntityMetadata.Update slime entity metadata update}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see SlimeEntityMetadata.Update
     */
    public static class Update<U extends Update<U>>
            extends JetMobEntityMetadata.Update<U>
            implements SlimeEntityMetadata.Update<U> {
        /**
         * Constructs the {@linkplain Update slime entity metadata update implementation}.
         *
         * @param entityMetadata the entity metadata that should be a base for the modified version
         * @since 1.0
         */
        protected Update(JetEntityMetadata entityMetadata) {
            super(entityMetadata);
        }

        @Override
        public U size(int value) {
            return this.updateValue(SLIME_SIZE_INDEX, new EntityMetadataValue.Int(value));
        }
    }
}
