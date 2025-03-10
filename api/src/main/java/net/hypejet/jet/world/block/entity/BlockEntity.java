package net.hypejet.jet.world.block.entity;

import net.hypejet.jet.data.model.api.block.entity.BlockEntityType;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.registry.RegistryEntry;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents additional data of a Minecraft block.
 *
 * @param type a registry entry of a type, of which the block entity should be
 * @param data a data that the block entity should have, represented as a compound binary tag
 * @since 1.0
 */
public record BlockEntity(@NonNull RegistryEntry<BlockEntityType> type, @NonNull CompoundBinaryTag data) {
    /**
     * Constructs the {@linkplain BlockEntity block entity}.
     *
     * @param type a registry entry of a type, of which the block entity should be
     * @param data a data that the block entity should have, represented as a compound binary tag
     * @since 1.0
     */
    public BlockEntity {
        NullabilityUtil.requireNonNull(type, "type");
        NullabilityUtil.requireNonNull(data, "data");
    }
}