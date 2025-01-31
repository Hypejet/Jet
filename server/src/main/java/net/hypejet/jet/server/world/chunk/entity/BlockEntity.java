package net.hypejet.jet.server.world.chunk.entity;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.kyori.adventure.nbt.BinaryTag;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an extra data associated with
 * {@linkplain net.hypejet.jet.server.world.block.JetBlockState a block state}.
 *
 * @param blockX an {@code X} value - relative to beginning of the chunk - of coordinates where the block is placed at
 * @param blockY an {@code Y} value - relative to bottom of the world - of coordinates where the block is placed at
 * @param blockZ an {@code Z} value - relative to beginning of the chunk - of coordinates where the block is placed at
 * @param type a type of the block entity
 * @param data a data of the block entity stored in a binary tag
 * @since 1.0
 * @see net.hypejet.jet.server.world.block.JetBlockState
 */
public record BlockEntity(byte blockX, short blockY, byte blockZ, int type, @NotNull BinaryTag data) {
    /**
     * Constructs the {@linkplain BlockEntity block entity}.
     *
     * @param blockX an {@code X} value - relative to beginning of the chunk - of coordinates where the block is
     *               placed at
     * @param blockY an {@code Y} value - relative to bottom of the world - of coordinates where the block is placed at
     * @param blockZ an {@code Z} value - relative to beginning of the chunk - of coordinates where the block is
     *               placed at
     * @param type a type of the block entity
     * @param data a data of the block entity stored in a binary tag
     * @since 1.0
     */
    public BlockEntity {
        NullabilityUtil.requireNonNull(data, "data");
    }
}