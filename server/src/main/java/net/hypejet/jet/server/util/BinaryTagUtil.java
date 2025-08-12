package net.hypejet.jet.server.util;

import net.hypejet.jet.server.util.codec.Writer;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/**
 * Represents a utility for management of {@linkplain BinaryTag binary tags}.
 *
 * @since 1.0
 */
public final class BinaryTagUtil {

    private BinaryTagUtil() {}

    /**
     * Writes an optional object into {@linkplain CompoundBinaryTag.Builder a compound binary tag builder} using
     * {@linkplain Writer a writer} provided.
     *
     * @param name a name of the object
     * @param object the object
     * @param builder the compound binary tag builder
     * @param writer the writer
     * @param <O> a type of the object
     * @since 1.0
     */
    public static <O> void writeOptional(@NonNull String name, @Nullable O object,
                                         CompoundBinaryTag.@NonNull Builder builder,
                                         @NonNull Writer<O, ? extends BinaryTag> writer) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(builder, "builder");
        Objects.requireNonNull(writer, "writer");

        if (object == null) return;
        BinaryTag binaryTag = writer.write(object);
        builder.put(name, binaryTag);
    }
}