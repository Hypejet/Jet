package net.hypejet.jet.server.registry.codecs.world.coordinate;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.world.coordinate.BlockPosition;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.IntArrayBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import org.jspecify.annotations.NullMarked;

/**
 * A {@linkplain BinaryTagCodec binary-tag codec} of {@linkplain BlockPosition block positions}.
 *
 * @since 1.0
 * @see BlockPosition
 * @see BinaryTagCodec
 */
@NullMarked
public final class BlockPositionBinaryTagCodec implements BinaryTagCodec<BlockPosition> {
    /**
     * An instance of the {@linkplain BlockPositionBinaryTagCodec block position binary-tag codec}.
     *
     * @since 1.0
     */
    public static final BlockPositionBinaryTagCodec INSTANCE = new BlockPositionBinaryTagCodec();

    private BlockPositionBinaryTagCodec() {}

    @Override
    public BlockPosition decode(BinaryTag binaryTag) {
        if (binaryTag instanceof IntArrayBinaryTag intArrayTag) {
            validateSize(intArrayTag.size());
            return new BlockPosition(intArrayTag.get(0), intArrayTag.get(1), intArrayTag.get(2));
        } else if (binaryTag instanceof ListBinaryTag listTag) {
            validateSize(listTag.size());
            return new BlockPosition(listTag.getInt(0), listTag.getInt(1), listTag.getInt(2));
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag must be of either int-array or int-list type to decode it into block position"
            );
        }
    }

    @Override
    public BinaryTag encode(BlockPosition value) {
        return IntArrayBinaryTag.intArrayBinaryTag(value.blockX(), value.blockY(), value.blockZ());
    }

    private static void validateSize(int size) {
        if (size != 3) {
            throw new IllegalArgumentException(
                    "The int-array or list binary tag representing a block position must contain exactly 3 elements"
            );
        }
    }
}