package net.hypejet.jet.server.registry.codecs.primitive;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain Map maps}.
 *
 * @param <K> the key type of maps whose serialization is handled by this binary tag codec
 * @param <V> the value type of maps whose serialization is handled by this binary tag codec
 * @since 1.0
 * @see Map
 * @see BinaryTagCodec
 */
@NullMarked
public final class MapBinaryTagCodec<K, V> implements BinaryTagCodec<Map<K, V>> {

    private final Function<K, String> keyEncoder;
    private final Function<String, K> keyDecoder;

    private final BinaryTagCodec<V> valueCodec;

    /**
     * Constructs the {@linkplain MapBinaryTagCodec map binary-tag codec}.
     *
     * @param keyEncoder a function string-serializing keys of maps
     *                   that the constructed map binary-tag code should write
     * @param keyDecoder a function string-deserializing keys of maps
     *                   that the constructed map binary-tag code should write
     * @param valueCodec a binary tag codec that should handle value serialization
     *                   of maps that the constructed map binary-tag codec should write
     * @since 1.0
     */
    public MapBinaryTagCodec(Function<K, String> keyEncoder,
                             Function<String, K> keyDecoder,
                             BinaryTagCodec<V> valueCodec) {
        this.keyEncoder = Objects.requireNonNull(keyEncoder, "key encoder");
        this.keyDecoder = Objects.requireNonNull(keyDecoder, "key decoder");
        this.valueCodec = Objects.requireNonNull(valueCodec, "value codec");
    }

    @Override
    public Map<K, V> decode(BinaryTag binaryTag) {
        if (binaryTag instanceof CompoundBinaryTag compound) {
            Map<K, V> map = new HashMap<>();
            for (String encodedKey : compound.keySet()) {
                BinaryTag encodedValue = requiredTag(encodedKey, compound);
                K key = this.keyDecoder.apply(encodedKey);
                V value = this.valueCodec.decode(encodedValue);
                map.put(key, value);
            }
            return Map.copyOf(map);
        } else {
            throw new IllegalArgumentException("The encoded tag must be of compound type to decode it to a map");
        }
    }

    @Override
    public BinaryTag encode(Map<K, V> value) {
        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder();
        for (Map.Entry<K, V> entry : value.entrySet()) {
            String encodedKey = this.keyEncoder.apply(entry.getKey());
            BinaryTag encodedValue = this.valueCodec.encode(entry.getValue());
            builder.put(encodedKey, encodedValue);
        }
        return builder.build();
    }
}