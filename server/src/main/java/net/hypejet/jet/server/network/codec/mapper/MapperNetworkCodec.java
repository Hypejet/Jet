package net.hypejet.jet.server.network.codec.mapper;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.data.codecs.util.mapper.Mapper;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkCodec a network codec}, which reads and writes values
 * using {@linkplain Mapper a mapper}.
 *
 * @param <R> a type of read value of the mapper
 * @param <W> a type of written value of the mapper
 * @since 1.0
 * @author Codestech
 * @see Mapper
 * @see NetworkCodec
 */
public final class MapperNetworkCodec<R, W> implements NetworkCodec<R> {

    private final Mapper<R, W> mapper;
    private final NetworkCodec<W> codec;

    /**
     * Constructs the {@linkplain MapperNetworkCodec mapper network codec}.
     *
     * @param mapper the mapper to use
     * @param codec a network reader reading/writing values from/to a byte buf to/from a written value of the mapper
     * @since 1.0
     */
    public MapperNetworkCodec(@NonNull Mapper<R, W> mapper, @NonNull NetworkCodec<W> codec) {
        this.mapper = NullabilityUtil.requireNonNull(mapper, "mapper");
        this.codec = NullabilityUtil.requireNonNull(codec, "codec");
    }

    @Override
    public @NonNull R read(@NonNull ByteBuf buf) {
        R read = this.mapper.read(this.codec.read(buf));
        if (read == null)
            throw new IllegalArgumentException("Could not find a mapping for the read object");
        return read;
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull R object) {
        W written = this.mapper.write(object);
        if (written == null)
            throw new IllegalArgumentException("Could not find a mapping for the object specified");
        this.codec.write(buf, written);
    }
}