package net.hypejet.jet.server.network.protocol.codecs.other;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.value.qual.IntRange;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain String string}.
 *
 * @since 1.0
 * @author Codestech
 * @see String
 * @see NetworkCodec
 */
public final class StringNetworkCodec implements NetworkCodec<String> {

    private static final short MAX_STRING_SIZE = 32767;

    private static final StringNetworkCodec MAX_SIZE_INSTANCE = new StringNetworkCodec(MAX_STRING_SIZE);
    private static final StringNetworkCodec MAX_16_INSTANCE = new StringNetworkCodec(16);

    private final int maxStringSize;

    private StringNetworkCodec(int maxStringSize) {
        this.maxStringSize = maxStringSize;
    }

    @Override
    public @NonNull String read(@NonNull ByteBuf buf) {
        int length = VarIntNetworkCodec.instance().read(buf);

        if (length < 0 || length > this.maxStringSize)
            throw invalidLengthException(length);

        if (!buf.isReadable(length)) {
            throw new IllegalArgumentException("A buffer does not contain at least " + length + " readable bytes");
        }

        String string = buf.toString(buf.readerIndex(), length, StandardCharsets.UTF_8);
        buf.skipBytes(length);

        return string;
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull String object) {
        Objects.requireNonNull(object, "The object must not be null");

        int length = ByteBufUtil.utf8Bytes(object);

        if (length < 0 || length > this.maxStringSize)
            throw invalidLengthException(length);

        VarIntNetworkCodec.instance().write(buf, length);
        buf.writeCharSequence(object, StandardCharsets.UTF_8);
    }

    private static @NonNull IllegalArgumentException invalidLengthException(int length) {
        return new IllegalArgumentException("Invalid length of a string - " + length + ".");
    }

    /**
     * Creates a {@linkplain StringNetworkCodec string network codec}.
     *
     * @param maxStringSize a maximum length of string allowed by the codec, the maximum is {@link #MAX_STRING_SIZE},
     *                      when it is the maximum, the {@link #MAX_SIZE_INSTANCE} is returned,
     *                      when it is {@code 16}, the {@link #MAX_16_INSTANCE} is returned
     * @return the string network codec
     * @since 1.0
     */
    public static @NonNull StringNetworkCodec create(@IntRange(from = 0, to = MAX_STRING_SIZE) int maxStringSize) {
        if (maxStringSize > MAX_STRING_SIZE)
            throw new IllegalArgumentException("The maximum string size is: " + MAX_STRING_SIZE);
        if (maxStringSize == MAX_STRING_SIZE)
            return MAX_SIZE_INSTANCE;
        if (maxStringSize == 16)
            return MAX_16_INSTANCE;
        return new StringNetworkCodec(maxStringSize);
    }

    /**
     * Gets an instance of the {@linkplain StringNetworkCodec string network codec}, whose max length
     * is {@link #MAX_STRING_SIZE}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull StringNetworkCodec instance() {
        return MAX_SIZE_INSTANCE;
    }
}