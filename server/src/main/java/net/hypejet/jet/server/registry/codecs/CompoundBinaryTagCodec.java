package net.hypejet.jet.server.registry.codecs;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

/**
 * Something handling {@linkplain CompoundBinaryTag compound-binary-tag} serialization of certain object type.
 *
 * @param <V> the type of objects whose compound-binary-tag serialization is handled by this codec
 * @since 1.0
 * @see CompoundBinaryTag
 */
@NullMarked
public interface CompoundBinaryTagCodec<V> {
    /**
     * Reads an object from the specified {@linkplain CompoundBinaryTag compound binary tag}.
     *
     * @param compound the compound binary tag that the objects should be read from
     * @return the read object
     * @since 1.0
     */
    V decode(CompoundBinaryTag compound);

    /**
     * Writes the specified object to the specified {@linkplain CompoundBinaryTag.Builder compound binary tag builder}.
     *
     * @param value the object that should be written to the compound binary tag builder
     * @param builder the compound binary tag builder that the object should be written to
     * @since 1.0
     */
    void encode(V value, CompoundBinaryTag.Builder builder);
}