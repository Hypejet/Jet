package net.hypejet.jet.server.registry.codecs.mapper;

import net.hypejet.jet.data.codecs.util.mapper.Mapper;
import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Function;

/**
 * Represents a {@linkplain BinaryTagCodec binary-tag codec}, which reads and writes values using a {@linkplain Mapper
 * mapper}.
 *
 * @param <R> a type of read value
 * @param <W> a type of written value
 * @param <T> a type of binary tag representing written values
 * @since 1.0
 * @author Codestech
 */
public final class MapperBinaryTagCodec<R, W, T extends BinaryTag> implements BinaryTagCodec<R> {

    private final Mapper<R, W> mapper;

    private final Class<T> binaryTagType;

    private final Function<T, W> tagToWrittenFunction;
    private final Function<W, T> writtenToTagFunction;

    private MapperBinaryTagCodec(@NonNull Mapper<R, W> mapper, @NonNull Class<T> binaryTagType,
                                 @NonNull Function<T, W> tagToWrittenFunction,
                                 @NonNull Function<W, T> writtenToTagFunction) {
        this.mapper = mapper;
        this.binaryTagType = binaryTagType;
        this.tagToWrittenFunction = tagToWrittenFunction;
        this.writtenToTagFunction = writtenToTagFunction;
    }

    @Override
    public @NonNull R read(@NonNull BinaryTag tag) {
        if (!this.binaryTagType.isAssignableFrom(tag.getClass())) {
            throw new IllegalArgumentException(String.format("The binary tag specified is not a \"%s\"",
                    this.binaryTagType.getSimpleName()));
        }

        W written = this.tagToWrittenFunction.apply(this.binaryTagType.cast(tag));
        R read = this.mapper.read(written);

        if (read == null)
            throw new IllegalArgumentException(String.format("Could not find a mapping for \"%s\"", written));
        return read;
    }

    @Override
    public @NonNull BinaryTag write(@NonNull R object) {
        W written = this.mapper.write(object);
        if (written == null)
            throw new IllegalArgumentException(String.format("Could not find a mapping for \"%s\"", object));

        T binaryTag = this.writtenToTagFunction.apply(written);
        if (!this.binaryTagType.isAssignableFrom(binaryTag.getClass())) {
            throw new IllegalArgumentException(String.format("The binary tag specified is not a \"%s\"",
                    this.binaryTagType.getSimpleName()));
        }

        return binaryTag;
    }

    /**
     * Creates a {@linkplain MapperBinaryTagCodec mapper binary-tag codec}, which writes values to
     * a {@linkplain String string}.
     *
     * @param mapper the mapper, of which one value is a string
     * @return the codec
     * @param <R> a type of the read value of the mapper
     * @since 1.0
     */
    public static <R> @NonNull MapperBinaryTagCodec<R, ?, ?> stringCodec(@NonNull Mapper<R, String> mapper) {
        return codec(mapper, StringBinaryTag.class, StringBinaryTag::value, StringBinaryTag::stringBinaryTag);
    }

    /**
     * Creates a {@linkplain MapperBinaryTagCodec mapper binary-tag codec}.
     *
     * @param mapper the mapper
     * @param binaryTagType a class of type of the binary tag representing written values
     * @param tagToWrittenFunction a function converting a binary tag into a written value
     * @param writtenToTagFunction a function converting a written value into a binary tag
     * @return the codec
     * @param <R> a type of read value
     * @param <W> a type of written value
     * @param <T> a type of binary tag representing written values
     * @since 1.0
     */
    public static <R, W, T extends BinaryTag> @NonNull MapperBinaryTagCodec<R, W, T> codec(
            @NonNull Mapper<R, W> mapper, @NonNull Class<T> binaryTagType,
            @NonNull Function<T, W> tagToWrittenFunction, @NonNull Function<W, T> writtenToTagFunction
    ) {
        return new MapperBinaryTagCodec<>(mapper, binaryTagType, tagToWrittenFunction, writtenToTagFunction);
    }
}