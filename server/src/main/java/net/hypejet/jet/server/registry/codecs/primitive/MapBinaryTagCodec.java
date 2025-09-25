package net.hypejet.jet.server.registry.codecs.primitive;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.util.Codec;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
public final class MapBinaryTagCodec<K, V> implements BinaryTagCodec<Map<K, V>> {

    private final Codec<K, String, Exception, Exception> keyCodec;
    private final BinaryTagCodec<V> valueCodec;

    /**
     * Constructs the {@linkplain MapBinaryTagCodec map binary tag codec}.
     *
     * @param keyCodec a codec that should handle serialization of map keys
     * @param valueCodec a binary tag codec that should handle serialization of map values
     * @since 1.0
     */
    public MapBinaryTagCodec(@NonNull Codec<K, String, Exception, Exception> keyCodec,
                             @NonNull BinaryTagCodec<V> valueCodec) {
        this.keyCodec = Objects.requireNonNull(keyCodec, "key codec");
        this.valueCodec = Objects.requireNonNull(valueCodec, "value codec");
    }

    @Override
    public @NotNull Map<K, V> decode(@NotNull BinaryTag encoded) throws Exception {
        if (encoded instanceof CompoundBinaryTag compound) {
            Map<K, V> map = new HashMap<>();
            for (String encodedKey : compound.keySet()) {
                BinaryTag encodedValue = requiredTag(encodedKey, compound);
                K key = this.keyCodec.decode(encodedKey);
                V value = this.valueCodec.decode(encodedValue);
                map.put(key, value);
            }
            return Map.copyOf(map);
        } else {
            throw new IllegalArgumentException("The encoded tag must be of compound type to decode it to a map");
        }
    }

    @Override
    public @NotNull BinaryTag encode(@NotNull Map<K, V> decoded) throws Exception {
        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder();
        for (Map.Entry<K, V> entry : decoded.entrySet()) {
            String encodedKey = this.keyCodec.encode(entry.getKey());
            BinaryTag encodedValue = this.valueCodec.encode(entry.getValue());
            builder.put(encodedKey, encodedValue);
        }
        return builder.build();
    }
}