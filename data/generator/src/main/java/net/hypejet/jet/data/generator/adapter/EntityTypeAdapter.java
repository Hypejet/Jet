package net.hypejet.jet.data.generator.adapter;

import com.mojang.authlib.GameProfile;
import net.hypejet.jet.data.json.model.entity.JsonEntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NullMarked;

import java.util.UUID;

/**
 * Represents something converting {@linkplain EntityType entity types} to a Jet data equivalent.
 *
 * @since 1.0
 * @see EntityType
 */
@NullMarked
public final class EntityTypeAdapter {

    private EntityTypeAdapter() {}

    /**
     * Converts the specified {@linkplain EntityType entity type} to a Jet data equivalent.
     *
     * @param type the entity type to convert
     * @param level the level that should be passed as an argument while constructing entity instances
     * @return the converted entity type
     * @since 1.0
     */
    public static JsonEntityType convert(EntityType<?> type, Level level) {
        /* Note: the entity instances should be used only for extraction of data
           that is constant and is only available inside the Entity class. */
        Entity entity;

        if (type == EntityType.PLAYER) {
            // Players cannot be constructed using a factory provided by the entity type as they require game profiles
            entity = new MockPlayer(level);
        } else {
            entity = type.create(level, EntitySpawnReason.LOAD);
            if (entity == null)
                throw new IllegalStateException("The entity created by factory provided by the entity type is null");
        }

        return new JsonEntityType(
                KeyAdapter.convertSet(FeatureFlags.REGISTRY.toNames(type.requiredFeatures())),
                entity.getMaxAirSupply(),
                entity instanceof LivingEntity,
                entity instanceof Mob
        );
    }

    /**
     * An implementation of {@linkplain Player player} having no functionality and providing fake values.
     *
     * <p><strong>This should be used carefully.</strong></p>
     *
     * @since 1.0
     * @see Player player
     */
    private static final class MockPlayer extends Player {
        /**
         * Constructs the {@linkplain MockPlayer mock player}.
         *
         * @param level the level that the player entity should be in
         * @since 1.0
         */
        private MockPlayer(Level level) {
            super(level, new GameProfile(UUID.randomUUID(), "MockPlayer"));
        }

        @Override
        public GameType gameMode() {
            return GameType.SURVIVAL;
        }
    }
}