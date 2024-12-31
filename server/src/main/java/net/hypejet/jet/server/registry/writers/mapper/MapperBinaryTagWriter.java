package net.hypejet.jet.server.registry.writers.mapper;

import net.hypejet.jet.data.codecs.util.mapper.Mapper;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.util.codec.Writer;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Function;

/**
 * Represents {@linkplain Writer a writer}, which writes values into {@linkplain BinaryTag a binary tag}
 * using {@linkplain Mapper a mapper}.
 *
 * @param <R> a type of read mapper value
 * @param <W> a type of written mapper value
 * @param <T> a type of the binary tag representing written values
 * @since 1.0
 * @see Mapper
 * @see BinaryTag
 * @see Writer
 */
public final class MapperBinaryTagWriter<R, W, T extends BinaryTag> implements Writer<R, T> {

    private final Mapper<R, W> mapper;
    private final Class<T> binaryTagType;
    private final Function<W, T> writtenToTagFunction;

    /**
     * Constructs the {@linkplain MapperBinaryTagWriter mapper binary-tag writer}.
     *
     * @param mapper the mapper
     * @param binaryTagType a class of type of the binary tag representing written values
     * @param writtenToTagFunction a function converting a written value into a binary tag
     * @since 1.0
     */
    public MapperBinaryTagWriter(@NonNull Mapper<R, W> mapper, @NonNull Class<T> binaryTagType,
                                 @NonNull Function<W, T> writtenToTagFunction) {
        this.mapper = NullabilityUtil.requireNonNull(mapper, "mapper");
        this.binaryTagType = NullabilityUtil.requireNonNull(binaryTagType, "binary tag type");
        this.writtenToTagFunction = NullabilityUtil.requireNonNull(writtenToTagFunction, "written to tag function");
    }

    @Override
    public @NonNull T write(@NonNull R object) {
        W written = this.mapper.write(object);
        if (written == null)
            throw new IllegalArgumentException(String.format("Could not find a mapping for \"%s\"", object));

        T binaryTag = this.writtenToTagFunction.apply(written);
        if (!this.binaryTagType.isAssignableFrom(binaryTag.getClass())) {
            throw new IllegalArgumentException(String.format(
                    "The binary tag specified is not a \"%s\"",
                    this.binaryTagType.getSimpleName()
            ));
        }

        return binaryTag;
    }

    /**
     * Creates {@linkplain MapperBinaryTagWriter a mapper binary-tag writer}, which writes values into
     * {@linkplain String a string}.
     *
     * @param mapper the mapper, whose written value is a type of string
     * @return the codec
     * @param <R> a type of the read value of the mapper
     * @since 1.0
     */
    public static <R> @NonNull MapperBinaryTagWriter<R, String, StringBinaryTag> stringCodec(
            @NonNull Mapper<R, String> mapper
    ) {
        return new MapperBinaryTagWriter<>(mapper, StringBinaryTag.class, StringBinaryTag::stringBinaryTag);
    }
}