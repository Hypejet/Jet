package net.hypejet.jet.server.entity.metadata.mob.creature;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.metadata.mob.creature.IronGolemEntityMetadata;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.entity.metadata.JetEntityMetadata;
import net.hypejet.jet.server.entity.metadata.mob.JetMobEntityMetadata;
import net.hypejet.jet.server.util.collection.IntObjectMapBuilder;

/**
 * An implementation of the {@linkplain IronGolemEntityMetadata iron golem entity metadata}.
 *
 * @since 1.0
 * @see IronGolemEntityMetadata
 */
@NullMarked
public class JetIronGolemEntityMetadata extends JetMobEntityMetadata implements IronGolemEntityMetadata {

    private static final int IRON_GOLEM_FLAGS_INDEX = 16;

    private static final byte FLAGS_NOT_PLAYER_CREATED = 0;
    private static final byte FLAGS_PLAYER_CREATED = 1;

    /**
     * Constructs the {@linkplain JetIronGolemEntityMetadata iron golem entity metadata}.
     *
     * @param entity the entity that the iron golem entity metadata is being constructed for
     * @since 1.0
     */
    public JetIronGolemEntityMetadata(JetEntity entity) {
        super(entity);
    }

    @Override
    public boolean playerCreated() {
        return this.value(IRON_GOLEM_FLAGS_INDEX, EntityMetadataValue.Byte.class).value() == FLAGS_PLAYER_CREATED;
    }

    @Override
    public Update<?> createUpdateBuilder() {
        return new Update<>(this);
    }

    @Override
    protected void defineDefaults(IntObjectMapBuilder<EntityMetadataValue> valuesBuilder) {
        super.defineDefaults(valuesBuilder);
        valuesBuilder.put(IRON_GOLEM_FLAGS_INDEX, new EntityMetadataValue.Byte(FLAGS_NOT_PLAYER_CREATED));
    }

    /**
     * An implementation of the {@linkplain IronGolemEntityMetadata.Update iron golem entity metadata update}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see IronGolemEntityMetadata.Update
     */
    public static class Update<U extends Update<U>>
            extends JetMobEntityMetadata.Update<U>
            implements IronGolemEntityMetadata.Update<U> {
        /**
         * Constructs the {@linkplain Update iron golem entity metadata update implementation}.
         *
         * @param entityMetadata the entity metadata that should be a base for the modified version
         * @since 1.0
         */
        protected Update(JetEntityMetadata entityMetadata) {
            super(entityMetadata);
        }

        @Override
        public final U playerCreated(boolean value) {
            return this.updateValue(
                    IRON_GOLEM_FLAGS_INDEX,
                    new EntityMetadataValue.Byte(value ? FLAGS_PLAYER_CREATED : FLAGS_NOT_PLAYER_CREATED)
            );
        }
    }
}
