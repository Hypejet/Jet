package net.hypejet.jet.data.generator.level;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.EmptyLevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.lighting.LevelLightEngine;
import org.jspecify.annotations.NullMarked;

import java.util.function.BooleanSupplier;

/**
 * A {@linkplain ChunkSource chunk source} providing {@linkplain EmptyLevelChunk empty chunks}
 * and having no functionality.
 *
 * <p><strong>This should be used carefully.</strong></p>
 *
 * @since 1.0
 * @see EmptyLevelChunk
 * @see ChunkSource
 */
@NullMarked
final class MockChunkSource extends ChunkSource {

    private final Level level;
    private final RegistryAccess registryAccess;
    private final LevelLightEngine lightEngine;

    /**
     * Constructs the {@linkplain MockChunkSource mock chunk source}.
     *
     * @param level a level that the mockup chunk source is being constructed for
     * @param registryAccess access to all Minecraft registries
     * @since 1.0
     */
    MockChunkSource(Level level, RegistryAccess registryAccess) {
        this.level = level;
        this.registryAccess = registryAccess;
        this.lightEngine = new LevelLightEngine(this, true, level.dimensionType().hasSkyLight());
    }

    @Override
    public ChunkAccess getChunk(int var1, int var2, ChunkStatus var3, boolean var4) {
        return new EmptyLevelChunk(
                this.level,
                new ChunkPos(var1, var2),
                this.registryAccess.lookupOrThrow(Registries.BIOME).getOrThrow(Biomes.PLAINS)
        );
    }

    @Override
    public void tick(BooleanSupplier var1, boolean var2) {}

    @Override
    public String gatherStats() {
        return "";
    }

    @Override
    public int getLoadedChunksCount() {
        return 0;
    }

    @Override
    public LevelLightEngine getLightEngine() {
        return this.lightEngine;
    }

    @Override
    public BlockGetter getLevel() {
        return this.level;
    }
}