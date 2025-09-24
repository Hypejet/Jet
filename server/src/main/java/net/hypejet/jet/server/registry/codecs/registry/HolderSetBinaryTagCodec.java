package net.hypejet.jet.server.registry.codecs.registry;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.registry.holder.HolderSet;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.util.codec.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.KeyBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.primitive.ListBinaryTagCodec;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import org.jspecify.annotations.NullMarked;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * A {@linkplain BinaryTagCodec binary-tag codec} of {@linkplain HolderSet holder sets}.
 *
 * @param <V> the holder value type of holder sets whose serialization is handled by this binary tag codec
 * @since 1.0
 * @see HolderSet
 * @see BinaryTagCodec
 */
@NullMarked
public final class HolderSetBinaryTagCodec<V> implements BinaryTagCodec<HolderSet<V>> {

    private final BinaryTagCodec<List<Holder<V>>> holderListCodec;

    /**
     * Constructs the {@linkplain HolderSetBinaryTagCodec holder set binary-tag codec} with compact list support.
     *
     * @param holderCodec a binary-tag codec that should handle serialization of direct holder definitions
     * @since 1.0
     */
    public HolderSetBinaryTagCodec(BinaryTagCodec<Holder<V>> holderCodec) {
        this(holderCodec, true);
    }

    /**
     * Constructs the {@linkplain HolderSetBinaryTagCodec holder set binary-tag codec}.
     *
     * @param holderCodec a binary-tag codec that should handle serialization of direct holder definitions
     * @param compact whether the binary-tag codec serializing holder lists should support compact lists
     * @since 1.0
     */
    public HolderSetBinaryTagCodec(BinaryTagCodec<Holder<V>> holderCodec, boolean compact) {
        this.holderListCodec = new ListBinaryTagCodec<>(Objects.requireNonNull(holderCodec, "holder codec"), compact);
    }

    @Override
    public HolderSet<V> decode(BinaryTag binaryTag, JetMinecraftServer server) {
        try {
            return new HolderSet.Named<>(KeyBinaryTagCodec.HASHED_INSTANCE.decode(binaryTag, server));
        } catch (Exception exception) {
            List<Holder<V>> contents = this.holderListCodec.decode(binaryTag, server);
            return new HolderSet.Direct<>(validateList(contents));
        }
    }

    @Override
    public BinaryTag encode(HolderSet<V> value, JetMinecraftServer server) {
        return switch (value) {
            case HolderSet.Named<V>(Key tagKey) -> KeyBinaryTagCodec.HASHED_INSTANCE.encode(tagKey, server);
            case HolderSet.Direct<V>(List<Holder<V>> contents) ->
                    this.holderListCodec.encode(validateList(contents), server);
            default -> throw new IllegalStateException("Unknown holder set class: " + value.getClass().getName());
        };
    }

    private static <V> List<Holder<V>> validateList(List<Holder<V>> list) {
        Iterator<Holder<V>> iterator = list.iterator();
        if (iterator.hasNext()) {
            Class<?> firstValueClass = iterator.next().getClass();
            while (iterator.hasNext()) {
                Class<?> nextValueClass = iterator.next().getClass();
                if (firstValueClass != nextValueClass) {
                    throw new IllegalArgumentException(
                            "Contents of direct holder sets must be homogenous to be serialized"
                    );
                }
            }
        }
        return list;
    }
}