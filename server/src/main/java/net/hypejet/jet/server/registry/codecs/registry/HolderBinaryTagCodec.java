package net.hypejet.jet.server.registry.codecs.registry;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.KeyBinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain Holder holders}.
 *
 * @param <V> the value type of holders whose serialization is handled by this binary tag codec
 * @since 1.0
 * @see Holder
 * @see BinaryTagCodec
 */
public final class HolderBinaryTagCodec<V> implements BinaryTagCodec<Holder<V>> {

    private final BinaryTagCodec<V> valueCodec;
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
    public @NotNull Holder<V> decode(@NotNull BinaryTag encoded) throws Exception {
        try {
            return new Holder.Reference<>(KeyBinaryTagCodec.INSTANCE.decode(encoded));
        } catch (Exception exception) {
            if (!this.allowInline)
                throw new IllegalArgumentException("Inline definitions are not allowed here");
            return new Holder.Direct<>(this.valueCodecOrThrow().decode(encoded));
        }
    }

    @Override
    public @NotNull BinaryTag encode(@NotNull Holder<V> decoded) throws Exception {
        return switch (decoded) {
            case Holder.Direct<V> direct -> this.valueCodecOrThrow().encode(direct.value());
            case Holder.Reference<V> reference -> KeyBinaryTagCodec.INSTANCE.encode(reference.key());
            default -> throw new IllegalArgumentException("Unknown holder: " + decoded);
        };
    }

    private @NonNull BinaryTagCodec<V> valueCodecOrThrow() {
        if (this.valueCodec == null)
            throw new IllegalArgumentException("Direct holder serialization is not supported by this codec");
        return this.valueCodec;
    }
}