package net.hypejet.jet.server.registry.codecs.adventure;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.serializer.nbt.NBTComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain Style styles}.
 *
 * @since 1.0
 * @see Style
 * @see BinaryTagCodec
 */
public final class StyleBinaryTagCodec implements BinaryTagCodec<Style> {
    /**
     * An instance of the {@linkplain StyleBinaryTagCodec style binary tag codec}
     * using the {@linkplain NBTComponentSerializer#nbt() default NBT component serializer}.
     *
     * @since 1.0
     * @see NBTComponentSerializer#nbt()
     */
    public static final StyleBinaryTagCodec INSTANCE = new StyleBinaryTagCodec(NBTComponentSerializer.nbt());

    private final NBTComponentSerializer serializer;

    private StyleBinaryTagCodec(@NonNull NBTComponentSerializer serializer) {
        this.serializer = Objects.requireNonNull(serializer, "serializer");
    }

    @Override
    public @NotNull Style decode(@NotNull BinaryTag encoded) {
        if (encoded instanceof CompoundBinaryTag compound) {
            return this.serializer.deserializeStyle(compound);
        } else {
            throw new IllegalArgumentException("The encoded tag must be of compound type to decode it to a style");
        }
    }

    @Override
    public @NotNull BinaryTag encode(@NotNull Style decoded) {
        return this.serializer.serializeStyle(decoded);
    }
}