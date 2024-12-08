package net.hypejet.jet.server.network.codec.other;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.value.qual.IntRange;

import java.nio.charset.StandardCharsets;

/**
 * Represents {@linkplain NetworkCodec a network codec}, which reads and writes {@linkplain String a string}.
 *
 * @since 1.0
 * @author Codestech
 * @see String
 * @see NetworkCodec
 */
public final class StringNetworkCodec implements NetworkCodec<String> {

    private static final short MAX_STRING_SIZE = 32767;

    /**
     * An instance of the {@linkplain StringNetworkCodec string network codec}, which
     * supports string sizes up to {@linkplain #MAX_STRING_SIZE a maximum allowed string size}.
     *
     * @since 1.0
     */
    public static final StringNetworkCodec INSTANCE = new StringNetworkCodec(MAX_STRING_SIZE);

    /**
     * An instance of the {@linkplain StringNetworkCodec string network codec}, which supports string
     * sizes up to {@code 16}.
     *
     * @since 1.0
     */
    public static final StringNetworkCodec MAX_16_INSTANCE = new StringNetworkCodec(16);

    private final int maxStringSize;

    private StringNetworkCodec(int maxStringSize) {
        this.maxStringSize = maxStringSize;
    }

    @Override
    public @NonNull String read(@NonNull ByteBuf buf) {
        int length = VarIntNetworkCodec.INSTANCE.read(buf);

        if (length < 0 || length > this.maxStringSize)
            throw invalidLengthException(length);

        if (!buf.isReadable(length)) {
            throw new IllegalArgumentException(String.format(
                    "A buffer does not contain at least %s readable bytes",
                    length
            ));
        }

        String string = buf.toString(buf.readerIndex(), length, StandardCharsets.UTF_8);
        buf.skipBytes(length);

        return string;
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull String object) {
        int length = ByteBufUtil.utf8Bytes(object);

        if (length < 0 || length > this.maxStringSize)
            throw invalidLengthException(length);

        VarIntNetworkCodec.INSTANCE.write(buf, length);
        buf.writeCharSequence(object, StandardCharsets.UTF_8);
    }

    private static @NonNull IllegalArgumentException invalidLengthException(int length) {
        return new IllegalArgumentException(String.format("Invalid length of a string - %s.", length));
    }

    /**
     * Creates {@linkplain StringNetworkCodec a string network codec}.
     *
     * @param maxStringSize a maximum length of string allowed by the codec, the {@link #INSTANCE}
     *                      or {@link #MAX_16_INSTANCE} may be returned if the max string size specified is the same
     *                      as in them
     * @return the string network codec
     * @since 1.0
     */
    public static @NonNull StringNetworkCodec create(@IntRange(from = 0, to = MAX_STRING_SIZE) int maxStringSize) {
        if (maxStringSize > MAX_STRING_SIZE)
            throw new IllegalArgumentException(String.format("The maximum string size is %s", MAX_STRING_SIZE));
        if (maxStringSize == MAX_STRING_SIZE)
            return INSTANCE;
        if (maxStringSize == 16)
            return MAX_16_INSTANCE;
        return new StringNetworkCodec(maxStringSize);
    }
}