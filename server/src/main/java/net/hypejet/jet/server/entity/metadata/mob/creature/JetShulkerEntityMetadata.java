package net.hypejet.jet.server.entity.metadata.mob.creature;

import java.util.Map;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.metadata.mob.creature.ShulkerEntityMetadata;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.entity.metadata.JetEntityMetadata;
import net.hypejet.jet.server.entity.metadata.mob.JetMobEntityMetadata;
import net.hypejet.jet.server.util.collection.IntObjectMapBuilder;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.hypejet.jet.world.direction.Direction;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.util.Index;

/**
 * An implementation of the {@linkplain ShulkerEntityMetadata shulker entity metadata}.
 *
 * @since 1.0
 * @see ShulkerEntityMetadata
 */
@NullMarked
public class JetShulkerEntityMetadata extends JetMobEntityMetadata implements ShulkerEntityMetadata {

    private static final int SHULKER_ATTACH_FACE_INDEX = 16;
    private static final int SHULKER_SHIELD_HEIGHT_INDEX = 17;
    private static final int SHULKER_COLOR_INDEX = 18;

    private static final @NonNull Index<Direction, Integer> SHULKER_ATTACH_FACE =
            IndexUtil.fromMap(Map.ofEntries(
                    Map.entry(0, Direction.DOWN),
                    Map.entry(1, Direction.UP),
                    Map.entry(2, Direction.NORTH),
                    Map.entry(3, Direction.SOUTH),
                    Map.entry(4, Direction.WEST),
                    Map.entry(5, Direction.EAST)
    ));
 
    private static final @NonNull Index<NamedTextColor, Integer> SHULKER_COLOR =
            IndexUtil.fromMap(Map.ofEntries(
                    Map.entry(0, NamedTextColor.BLACK),
                    Map.entry(1, NamedTextColor.DARK_BLUE),
                    Map.entry(2, NamedTextColor.DARK_GREEN),
                    Map.entry(3, NamedTextColor.DARK_AQUA),
                    Map.entry(4, NamedTextColor.DARK_RED),
                    Map.entry(5, NamedTextColor.DARK_PURPLE),
                    Map.entry(6, NamedTextColor.GOLD),
                    Map.entry(7, NamedTextColor.GRAY),
                    Map.entry(8, NamedTextColor.DARK_GRAY),
                    Map.entry(9, NamedTextColor.BLUE),
                    Map.entry(10, NamedTextColor.GREEN),
                    Map.entry(11, NamedTextColor.AQUA),
                    Map.entry(12, NamedTextColor.RED),
                    Map.entry(13, NamedTextColor.LIGHT_PURPLE),
                    Map.entry(14, NamedTextColor.YELLOW),
                    Map.entry(15, NamedTextColor.WHITE)
    ));

    /**
     * Constructs the {@linkplain JetShulkerEntityMetadata shulker entity metadata}.
     *
     * @param entity the entity that the shulker entity metadata is being constructed for
     * @since 1.0
     */
    public JetShulkerEntityMetadata(JetEntity entity) {
        super(entity);   
    }

    @Override
    public Direction attachFace() {
        int attachId = this.value(SHULKER_ATTACH_FACE_INDEX, EntityMetadataValue.Int.class).value();
        return SHULKER_ATTACH_FACE.key(attachId);
    }

    @Override
    public int shieldHeight() {
        return this.value(SHULKER_SHIELD_HEIGHT_INDEX, EntityMetadataValue.Int.class).value(); 
    }

    @Override
    public NamedTextColor color() {
        int colorId = this.value(SHULKER_COLOR_INDEX, EntityMetadataValue.Int.class).value();
        return SHULKER_COLOR.key(colorId);
    }

    @Override
    public Update<?> createUpdateBuilder() {
        return new Update<>(this);
    }

    @Override
    protected void defineDefaults(IntObjectMapBuilder<EntityMetadataValue> valuesBuilder) {
        super.defineDefaults(valuesBuilder);
        valuesBuilder.put(SHULKER_ATTACH_FACE_INDEX, new EntityMetadataValue.Int(SHULKER_ATTACH_FACE.value(Direction.DOWN)));
        valuesBuilder.put(SHULKER_SHIELD_HEIGHT_INDEX, new EntityMetadataValue.Int(0));
        valuesBuilder.put(SHULKER_COLOR_INDEX, new EntityMetadataValue.Int(SHULKER_COLOR.value(NamedTextColor.WHITE)));
    }

    /**
     * An implementation of the {@linkplain ShulkerEntityMetadata.Update shulker entity metadata update}.
     *
     * @param <U> the type of this entity metadata update
     * @since 1.0
     * @see ShulkerEntityMetadata.Update
     */
    public static class Update<U extends Update<U>>
            extends JetMobEntityMetadata.Update<U>
            implements ShulkerEntityMetadata.Update<U> {
        /**
         * Constructs the {@linkplain Update shulker entity metadata update implementation}.
         *
         * @param entityMetadata the entity metadata that should be a base for the modified version
         * @since 1.0
         */
        protected Update(JetEntityMetadata entityMetadata) {
            super(entityMetadata);
        }

        @Override
        public U attachFace(Direction value) {
             return this.updateValue(SHULKER_ATTACH_FACE_INDEX, new EntityMetadataValue.Int(SHULKER_ATTACH_FACE.value(value)));           
        }

        @Override
        public U shieldHeight(int value) {
            return this.updateValue(SHULKER_SHIELD_HEIGHT_INDEX, new EntityMetadataValue.Int(value));
        }

        @Override
        public U color(NamedTextColor value) {
            return this.updateValue(SHULKER_COLOR_INDEX, new EntityMetadataValue.Int(SHULKER_COLOR.value(value)));
        }
    }
}
