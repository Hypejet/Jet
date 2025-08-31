package net.hypejet.jet.data.generator.level;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.RecipeAccess;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.FuelValues;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.level.entity.TransientEntitySectionManager;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.ticks.BlackholeTickAccess;
import net.minecraft.world.ticks.LevelTickAccess;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * A {@linkplain Level level} having no functionality and providing fake values.
 *
 * <p><strong>This should be used carefully.</strong></p>
 *
 * @since 1.0
 * @see Level
 */
@NullMarked
public final class MockupLevel extends Level {

    private final FeatureFlagSet enabledFeatures;

    private final TickRateManager tickRateManager = new TickRateManager();
    private final Scoreboard scoreboard = new Scoreboard();

    private final PotionBrewing potionBrewing;
    private final FuelValues fuelValues;
    private final ChunkSource chunkSource;

    private final TransientEntitySectionManager<Entity>
            entityStorage = new TransientEntitySectionManager<>(Entity.class, new EmptyLevelCallback<>());

    /**
     * Constructs the {@linkplain MockupLevel mockup level}.
     *
     * @param registryAccess access to all Minecraft registries
     * @param enabledFeatures feature flags that are enabled for environment
     *                        that the mockup level is being constructed for
     * @since 1.0
     */
    public MockupLevel(RegistryAccess registryAccess, FeatureFlagSet enabledFeatures) {
        super(
                new MockupLevelData(),
                Registries.levelStemToLevel(LevelStem.OVERWORLD),
                registryAccess,
                registryAccess.lookupOrThrow(Registries.DIMENSION_TYPE).getOrThrow(BuiltinDimensionTypes.OVERWORLD),
                false,
                false,
                0L,
                1000000
        );

        this.enabledFeatures = enabledFeatures;
        this.potionBrewing = PotionBrewing.bootstrap(enabledFeatures);
        this.fuelValues = FuelValues.vanillaBurnTimes(registryAccess, enabledFeatures);
        this.chunkSource = new MockupChunkSource(this, registryAccess);
    }

    @Override
    public void sendBlockUpdated(BlockPos var1, BlockState var2, BlockState var3, int var4) {}

    @Override
    public void playSeededSound(@Nullable Entity var1, double var2, double var4, double var6,
                                Holder<SoundEvent> var8, SoundSource var9, float var10, float var11, long var12) {}

    @Override
    public void playSeededSound(@Nullable Entity var1, Entity var2, Holder<SoundEvent> var3,
                                SoundSource var4, float var5, float var6, long var7) {}

    @Override
    public void explode(@Nullable Entity var1, @Nullable DamageSource var2, @Nullable ExplosionDamageCalculator var3,
                        double var4, double var6, double var8, float var10, boolean var11, ExplosionInteraction var12,
                        ParticleOptions var13, ParticleOptions var14, Holder<SoundEvent> var15) {}

    @Override
    public String gatherChunkSourceStats() {
        return "";
    }

    @Override
    public @Nullable Entity getEntity(int var1) {
        return null;
    }

    @Override
    public Collection<EnderDragonPart> dragonParts() {
        return List.of();
    }

    @Override
    public TickRateManager tickRateManager() {
        return this.tickRateManager;
    }

    @Override
    public @Nullable MapItemSavedData getMapData(MapId var1) {
        return null;
    }

    @Override
    public void destroyBlockProgress(int var1, BlockPos var2, int var3) {}

    @Override
    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    @Override
    public RecipeAccess recipeAccess() {
        return EmptyRecipeAccess.INSTANCE;
    }

    @Override
    protected LevelEntityGetter<Entity> getEntities() {
        return this.entityStorage.getEntityGetter();
    }

    @Override
    public PotionBrewing potionBrewing() {
        return this.potionBrewing;
    }

    @Override
    public FuelValues fuelValues() {
        return this.fuelValues;
    }

    @Override
    public ChunkSource getChunkSource() {
        return this.chunkSource;
    }

    @Override
    public void levelEvent(@Nullable Entity var1, int var2, BlockPos var3, int var4) {}

    @Override
    public void gameEvent(Holder<GameEvent> var1, Vec3 var2, GameEvent.Context var3) {}

    @Override
    public float getShade(Direction var1, boolean var2) {
        return 0;
    }

    @Override
    public List<? extends Player> players() {
        return List.of();
    }

    @Override
    public Holder<Biome> getUncachedNoiseBiome(int var1, int var2, int var3) {
        return this.registryAccess().lookupOrThrow(Registries.BIOME).getOrThrow(Biomes.PLAINS);
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public FeatureFlagSet enabledFeatures() {
        return this.enabledFeatures;
    }

    @Override
    public LevelTickAccess<Block> getBlockTicks() {
        return BlackholeTickAccess.emptyLevelList();
    }

    @Override
    public LevelTickAccess<Fluid> getFluidTicks() {
        return BlackholeTickAccess.emptyLevelList();
    }
}