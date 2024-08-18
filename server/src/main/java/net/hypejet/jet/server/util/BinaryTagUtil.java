package net.hypejet.jet.server.util;

import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/**
 * Represents a utility for management of {@linkplain BinaryTag binary tags}.
 *
 * @since 1.0
 * @author Codestech
 */
public final class BinaryTagUtil {

    private BinaryTagUtil() {}

    /**
     * Reads an optional object from a {@linkplain CompoundBinaryTag compound binary tag} using
     * a {@linkplain BinaryTagCodec binary tag codec} provided.
     *
     * @param name a name of the object
     * @param compound the compound binary tag
     * @param codec the binary tag codec
     * @return the optional object, {@code null} if the value is not set
     * @param <O> a type of the object
     * @since 1.0
     */
    public static <O> @Nullable O read(@NonNull String name, @NonNull CompoundBinaryTag compound,
                                       @NonNull BinaryTagCodec<O> codec) {
        Objects.requireNonNull(name, "The name must not be null");
        Objects.requireNonNull(compound, "The compound must not be null");
        Objects.requireNonNull(codec, "The codec must not be null");

        BinaryTag binaryTag = compound.get(name);
        if (binaryTag == null)
            return null;
        return codec.read(binaryTag);
    }

    /**
     * Writes an optional object into a {@linkplain CompoundBinaryTag.Builder compound binary tag builder} using
     * a {@linkplain BinaryTagCodec binary tag codec} provided.
     *
     * @param name a name of the object
     * @param object the object
     * @param builder the compound binary tag builder
     * @param codec the binary tag codec
     * @param <O> a type of the object
     * @since 1.0
     */
    public static <O> void write(@NonNull String name, @Nullable O object, CompoundBinaryTag.@NonNull Builder builder,
                                 @NonNull BinaryTagCodec<O> codec) {
        Objects.requireNonNull(name, "The name must not be null");
        Objects.requireNonNull(builder, "The builder must not be null");
        Objects.requireNonNull(codec, "The codec must not be null");

        if (object == null) return;
        BinaryTag binaryTag = codec.write(object);
        builder.put(name, binaryTag);
    }
}