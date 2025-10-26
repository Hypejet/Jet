package net.hypejet.jet.server.registry.codecs.util.uuid;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.IntArrayBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import org.jspecify.annotations.NullMarked;

import java.util.UUID;

/**
 * A {@linkplain BinaryTagCodec binary-tag codec} of {@linkplain UUID unique identifiers}.
 *
 * @since 1.0
 * @see UUID
 * @see BinaryTagCodec
 */
@NullMarked
public final class UUIDBinaryTagCodec implements BinaryTagCodec<UUID> {

    private static final long LONG_HALF = 0xffffffffL;

    /**
     * An instance of the {@linkplain UUIDBinaryTagCodec unique identifier binary-tag codec}.
     *
     * @since 1.0
     */
    public static final UUIDBinaryTagCodec INSTANCE = new UUIDBinaryTagCodec();

    private UUIDBinaryTagCodec() {}

    @Override
    public UUID decode(BinaryTag binaryTag) {
        if (binaryTag instanceof IntArrayBinaryTag intArrayTag) {
            validateSize(intArrayTag.size());
            return createFromArray(intArrayTag.value());
        } else if (binaryTag instanceof ListBinaryTag listTag) {
            int size = listTag.size();
            validateSize(size);

            int[] array = new int[size];
            for (int index = 0; index < size; index++)
                array[index] = listTag.getInt(index);

            return createFromArray(array);
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag must be of either int-array or int-list type to decode it into an unique ID"
            );
        }
    }

    @Override
    public BinaryTag encode(UUID value) {
        long mostSignificantBits = value.getMostSignificantBits();
        long leastSignificantBits = value.getLeastSignificantBits();
        return IntArrayBinaryTag.intArrayBinaryTag(
                mostSignificantBits(mostSignificantBits),
                leastSignificantBits(mostSignificantBits),
                mostSignificantBits(leastSignificantBits),
                leastSignificantBits(leastSignificantBits)
        );
    }

    private static UUID createFromArray(int[] array) {
        return new UUID(binaryConcat(array[0], array[1]), binaryConcat(array[2], array[3]));
    }

    private static long binaryConcat(int mostSignificantBits, int leastSignificantBits) {
        return ((long) mostSignificantBits << Integer.SIZE) | ((long) leastSignificantBits & LONG_HALF);
    }

    private static int mostSignificantBits(long value) {
        return (int) (value >> Integer.SIZE);
    }

    private static int leastSignificantBits(long value) {
        return (int) (value & LONG_HALF);
    }

    private static void validateSize(int size) {
        if (size != 3) {
            throw new IllegalArgumentException(
                    "The int-array or list binary tag representing an unique ID must contain exactly 3 elements"
            );
        }
    }
}