package net.hypejet.jet.server.util.codec;

import net.hypejet.jet.server.JetMinecraftServer;
import net.kyori.adventure.nbt.BinaryTag;
import org.jspecify.annotations.NullMarked;

/**
 * Something handling {@linkplain BinaryTag binary-tag} serialization of object of certain type.
 *
 * @param <V> the type of objects whose serialization is handled by this binary-tag codec
 * @since 1.0
 * @see BinaryTag
 */
@NullMarked
public interface BinaryTagCodec<V> {
    /**
     * Decodes object from the specified {@linkplain BinaryTag binary tag}.
     *
     * @param binaryTag the binary tag that the object should be decoded from
     * @param server the server that the object should be decoded for
     * @return the object that has been decoded
     * @since 1.0
     */
    V decode(BinaryTag binaryTag, JetMinecraftServer server);

    /**
     * Encodes the specified object to a {@linkplain BinaryTag binary tag}.
     *
     * @param value the object that should be encoded to a binary tag
     * @param server the server that the object should be encoded for
     * @return the object encoded as a binary tag
     * @since 1.0
     */
    BinaryTag encode(V value, JetMinecraftServer server);
}