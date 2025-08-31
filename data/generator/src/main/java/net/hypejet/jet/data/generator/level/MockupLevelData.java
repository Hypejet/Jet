package net.hypejet.jet.data.generator.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.storage.WritableLevelData;
import org.jspecify.annotations.NullMarked;

/**
 * A {@linkplain WritableLevelData writable level data} providing constant data for unmodifiable fields
 * and having no additional functionality.
 *
 * <p><strong>This should be used carefully.</strong></p>
 *
 * @since 1.0
 * @see WritableLevelData
 */
@NullMarked
final class MockupLevelData implements WritableLevelData {

    private BlockPos spawnPos = BlockPos.ZERO;

    private boolean raining;
    private float spawnAngle;

    @Override
    public void setSpawn(BlockPos var1, float var2) {
        this.spawnPos = var1;
        this.spawnAngle = var2;
    }

    @Override
    public BlockPos getSpawnPos() {
        return this.spawnPos;
    }

    @Override
    public float getSpawnAngle() {
        return this.spawnAngle;
    }

    @Override
    public long getGameTime() {
        return 0;
    }

    @Override
    public long getDayTime() {
        return 0;
    }

    @Override
    public boolean isThundering() {
        return false;
    }

    @Override
    public boolean isRaining() {
        return this.raining;
    }

    @Override
    public void setRaining(boolean var1) {
        this.raining = var1;
    }

    @Override
    public boolean isHardcore() {
        return false;
    }

    @Override
    public Difficulty getDifficulty() {
        return Difficulty.NORMAL;
    }

    @Override
    public boolean isDifficultyLocked() {
        return false;
    }
}