package net.hypejet.jet.server.registry.codecs.registry;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.util.codec.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.KeyBinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain Holder holders}.
 *
 * @param <V> the value type of holders whose serialization is handled by this binary tag codec
 * @since 1.0
 * @see Holder
 * @see BinaryTagCodec
 */
@NullMarked
public final class HolderBinaryTagCodec<V> implements BinaryTagCodec<Holder<V>> {

    private final @Nullable BinaryTagCodec<V> valueCodec;
    private final boolean allowInline;

    /**
     * Constructs the {@linkplain HolderBinaryTagCodec holder binary tag codec} allowing direct holder deserialization.
     *
     * @param valueCodec the binary tag codec handling serialization of holder values, {@code null}
     *                   if serialization of direct holders should not be allowed
     * @since 1.0
     */
    public HolderBinaryTagCodec(@Nullable BinaryTagCodec<V> valueCodec) {
        this(valueCodec, true);
    }

    /**
     * Constructs the {@linkplain HolderBinaryTagCodec holder binary tag codec}.
     *
     * @param valueCodec the binary tag codec handling serialization of holder values, {@code null}
     *                   if serialization of direct holders should not be allowed
     * @param allowInline whether deserialization of direct holders should be allowed
     * @since 1.0
     */
    public HolderBinaryTagCodec(@Nullable BinaryTagCodec<V> valueCodec, boolean allowInline) {
        this.valueCodec = valueCodec;
        this.allowInline = allowInline;
    }

    @Override
    public Holder<V> decode(BinaryTag binaryTag, JetMinecraftServer server) {
        try {
            return new Holder.Reference<>(KeyBinaryTagCodec.INSTANCE.decode(binaryTag, server));
        } catch (Exception exception) {
            if (!this.allowInline)
                throw new IllegalArgumentException("Inline definitions are not allowed here");
            return new Holder.Direct<>(this.valueCodecOrThrow().decode(binaryTag, server));
        }
    }

    @Override
    public BinaryTag encode(Holder<V> value, JetMinecraftServer server) {
        return switch (value) {
            case Holder.Direct<V> direct -> this.valueCodecOrThrow().encode(direct.value(), server);
            case Holder.Reference<V> reference -> KeyBinaryTagCodec.INSTANCE.encode(reference.key(), server);
            default -> throw new IllegalArgumentException("Unknown holder class: " + value.getClass().getName());
        };
    }

    private BinaryTagCodec<V> valueCodecOrThrow() {
        if (this.valueCodec == null)
            throw new IllegalArgumentException("Direct holder serialization is not supported by this codec");
        return this.valueCodec;
    }
}