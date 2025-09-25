package net.hypejet.jet.server.registry.codecs;

import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.util.Codec;

/**
 * A {@linkplain Codec codec} with encoded type of {@linkplain BinaryTag binary tag}.
 *
 * @param <V> a type of decoded values
 * @since 1.0
 */
public interface BinaryTagCodec<V> extends Codec<V, BinaryTag, Exception, Exception> {}