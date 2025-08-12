package net.hypejet.jet.server.world.block.entity;

import net.hypejet.jet.data.json.model.block.JsonBlockEntityType;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.registry.holder.HolderSet;
import net.hypejet.jet.world.block.BlockType;
import net.hypejet.jet.world.block.entity.BlockEntityType;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An implementation of a {@linkplain BlockEntityType block entity type}.
 *
 * @param validBlocks a holder set referencing to blocks that support block entities of this type
 * @since 1.0
 * @see BlockEntityType
 */
public record JetBlockEntityType(@NonNull HolderSet<BlockType> validBlocks) implements BlockEntityType {
    /**
     * Constructs the {@linkplain JetBlockEntityType block entity type}.
     *
     * @param validBlocks a holder set referencing to blocks that should support block entities
     *                    of the constructed block entity type
     * @since 1.0
     */
    public JetBlockEntityType {
        Objects.requireNonNull(validBlocks, "valid blocks");
    }

    /**
     * Converts the specified {@linkplain JsonBlockEntityType Jet data block entity type} to a Jet equivalent.
     *
     * @param blockEntityType the block entity type to convert
     * @return the converted block entity type
     * @since 1.0
     */
    public static @NonNull JetBlockEntityType convert(@NonNull JsonBlockEntityType blockEntityType) {
        List<Holder<BlockType>> validBlocks = new ArrayList<>();
        for (Key key : blockEntityType.validBlocks())
            validBlocks.add(new Holder.Reference<>(key));
        return new JetBlockEntityType(new HolderSet.Direct<>(validBlocks));
    }
}