package net.hypejet.jet.server.test.command.argument;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.command.argument.codecs.EmptyArgumentCodec;
import net.hypejet.jet.server.command.argument.codecs.NumberArgumentCodec;
import net.hypejet.jet.server.command.argument.codecs.StringArgumentCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of various {@linkplain net.hypejet.jet.server.command.argument.ArgumentCodec argument codecs}.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.server.command.argument.ArgumentCodec
 */
public final class ArgumentCodecsTest {
    @Test
    public void testEmpty() {
        EmptyArgumentCodec<BoolArgumentType> codec = new EmptyArgumentCodec<>(0,
                BoolArgumentType.class,
                BoolArgumentType.bool());
        NetworkCodecTestUtil.test(codec, BoolArgumentType.bool(), Assertions::assertNotSame);
    }

    @Test
    public void testNumber() {
        NumberArgumentCodec<Integer, IntegerArgumentType> codec = new NumberArgumentCodec<>(0,
                IntegerArgumentType.class, ByteBuf::readInt, ByteBuf::writeInt, IntegerArgumentType::integer,
                IntegerArgumentType::getMinimum, IntegerArgumentType::getMaximum, Integer.MIN_VALUE,
                Integer.MAX_VALUE);
        NetworkCodecTestUtil.test(codec, IntegerArgumentType.integer(-64, 1532), (first, second) -> {
            Assertions.assertNotSame(first, second);
            Assertions.assertEquals(first.getMinimum(), second.getMinimum());
            Assertions.assertEquals(first.getMaximum(), second.getMaximum());
        });
    }

    @Test
    public void testString() {
        StringArgumentCodec codec = new StringArgumentCodec();
        NetworkCodecTestUtil.test(codec, StringArgumentType.string(), (first, second) -> {
            Assertions.assertNotSame(first, second);
            Assertions.assertSame(first.getType(), second.getType());
        });
    }
}