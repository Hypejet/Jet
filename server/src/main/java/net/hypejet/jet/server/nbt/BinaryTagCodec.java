package net.hypejet.jet.server.nbt;

import net.kyori.adventure.nbt.BinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents something that reads and writes an {@linkplain O object} from/to {@linkplain BinaryTag binary tag}.
 *
 * @param <O> a type of the object to read and write
 * @since 1.0
 * @author Codetech
 * @see BinaryTag
 */
public interface BinaryTagCodec<O> {
    /**
     * Reads an {@linkplain O object} from a {@linkplain BinaryTag binary tag}.
     *
     * @param tag the binary tag
     * @return the object
     * @since 1.0
     */
    @NonNull O read(@NonNull BinaryTag tag);

    /**
     * Writes an {@linkplain O object} to a {@linkplain BinaryTag binary tag}.
     *
     * @param object the object
     * @return the binary tag
     * @since 1.0
     */
    @NonNull BinaryTag write(@NonNull O object);
}