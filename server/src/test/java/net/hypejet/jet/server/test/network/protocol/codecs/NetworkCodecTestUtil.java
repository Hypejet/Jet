package net.hypejet.jet.server.test.network.protocol.codecs;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Assertions;

import java.util.function.BiConsumer;

/**
 * Represents a utility for testing of reading and writing of {@linkplain NetworkCodec network codecs}.
 *
 * @since 1.0
 * @author Codestech
 * @see NetworkCodec
 */
public final class NetworkCodecTestUtil {

    private NetworkCodecTestUtil() {}

    /**
     * Tests reading and writing of a {@linkplain NetworkCodec network codec}.
     *
     * @param codec the codec
     * @param expected an expected value
     * @param <T> a type of the object that the codec reads and writes
     * @since 1.0
     */
    public static <T> void test(@NonNull NetworkCodec<T> codec, @NonNull T expected) {
        test(codec, expected, Assertions::assertEquals);
    }

    /**
     * Tests reading and writing of a {@linkplain NetworkCodec network codec}.
     *
     * <p>This will only success when an invalid value is provided.</p>
     *
     * @param codec the codec
     * @param value the invalid value
     * @param <T> a type of the object that the codec reads and writes
     * @since 1.0
     */
    public static <T> void testInvalid(@NonNull NetworkCodec<T> codec, @NonNull T value) {
        testInvalid(codec, value, Assertions::assertEquals);
    }

    /**
     * Tests reading and writing of a {@linkplain NetworkCodec network codec}.
     *
     * <p>This will only success when an invalid value is provided.</p>
     *
     * @param codec the codec
     * @param value the invalid value
     * @param equalsFunction a function, which tests whether the expected value and the read value are equal
     * @param <T> a type of the object that the codec reads and writes
     * @since 1.0
     */
    public static <T> void testInvalid(@NonNull NetworkCodec<T> codec, @NonNull T value,
                                       @NonNull BiConsumer<T, T> equalsFunction) {
        Throwable throwable = null;

        try {
            test(codec, value, equalsFunction);
        } catch (Throwable thrownThrowable) {
            throwable = thrownThrowable;
        }

        if (throwable == null) {
            throw new IllegalArgumentException("The network codec did not thrown an exception on an invalid value");
        }
    }

    /**
     * Tests reading and writing of a {@linkplain NetworkCodec network codec}.
     *
     * @param codec the codec
     * @param expected an expected value
     * @param equalsFunction a function, which tests whether the expected value and the read value are equal
     * @param <T> a type of the object that the codec reads and writes
     * @since 1.0
     */
    public static <T> void test(@NonNull NetworkCodec<T> codec, @NonNull T expected,
                                @NonNull BiConsumer<T, T> equalsFunction) {
        ByteBuf buf = Unpooled.buffer();

        try {
            codec.write(buf, expected);
            T readValue = codec.read(buf);
            equalsFunction.accept(expected, readValue);
        } finally {
            buf.release();
        }
    }
}