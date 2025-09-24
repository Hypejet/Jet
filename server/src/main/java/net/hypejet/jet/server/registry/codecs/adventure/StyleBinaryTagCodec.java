package net.hypejet.jet.server.registry.codecs.adventure;

import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.util.codec.BinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.serializer.nbt.NBTComponentSerializer;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain Style styles}.
 *
 * @since 1.0
 * @see Style
 * @see BinaryTagCodec
 */
@NullMarked
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

    private StyleBinaryTagCodec(NBTComponentSerializer serializer) {
        this.serializer = Objects.requireNonNull(serializer, "serializer");
    }

    @Override
    public Style decode(BinaryTag binaryTag, JetMinecraftServer server) {
        if (binaryTag instanceof CompoundBinaryTag compound) {
            return this.serializer.deserializeStyle(compound);
        } else {
            throw new IllegalArgumentException("The encoded tag must be of compound type to decode it to a style");
        }
    }

    @Override
    public BinaryTag encode(Style value, JetMinecraftServer server) {
        return this.serializer.serializeStyle(value);
    }
}