package net.hypejet.jet.server.registry.codecs;

import net.kyori.adventure.nbt.BinaryTag;
import org.jspecify.annotations.NullMarked;

/**
 * Something handling {@linkplain BinaryTag binary-tag} serialization of certain object type.
 *
 * @param <V> the type of objects whose serialization is handled by this binary-tag codec
 * @since 1.0
 * @see BinaryTag
 */
@NullMarked
public interface BinaryTagCodec<V> {
    /**
     * Reads an object from the specified {@linkplain BinaryTag binary tag}.
     *
     * @param binaryTag the binary tag that the objects should be read from
     * @return the read object
     * @since 1.0
     */
    V decode(BinaryTag binaryTag);

    /**
     * Writes the specified object to a {@linkplain BinaryTag binary tag}.
     *
     * @param value the object that should be written to a binary tag
     * @return the specified object written as a binary tag
     * @since 1.0
     */
    BinaryTag encode(V value);
}