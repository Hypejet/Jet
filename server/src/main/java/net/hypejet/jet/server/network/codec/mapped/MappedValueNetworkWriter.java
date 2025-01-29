package net.hypejet.jet.server.network.codec.mapped;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Function;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which accepts a value, maps it using a mapping function
 * and writes it using a network writer, both specified during construction.
 *
 * @param mapper the mapping function
 * @param mappedValueWriter the writer of the mapped value
 * @param <O> a type of the original value
 * @param <M> a type of the mapped value
 * @since 1.0
 */
public record MappedValueNetworkWriter<O, M>(@NonNull Function<O, M> mapper,
                                             @NonNull NetworkWriter<M> mappedValueWriter) implements NetworkWriter<O> {
    /**
     * Constructs the {@linkplain MappedValueNetworkWriter mapped value writer}.
     *
     * @param mapper the mapping function
     * @param mappedValueWriter the writer of the mapped value
     * @since 1.0
     */
    public MappedValueNetworkWriter {
        NullabilityUtil.requireNonNull(mapper, "mapper");
        NullabilityUtil.requireNonNull(mappedValueWriter, "mapped value writer");
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull O object) {
        this.mappedValueWriter.write(buf, this.mapper.apply(object));
    }
}