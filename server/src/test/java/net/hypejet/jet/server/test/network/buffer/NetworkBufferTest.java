package net.hypejet.jet.server.test.network.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hypejet.jet.player.profile.properties.GameProfileProperties;
import net.hypejet.jet.server.network.buffer.NetworkBuffer;
import net.hypejet.jet.server.network.protocol.codecs.GameProfilePropertiesCodec;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * A test for {@link NetworkBuffer network buffer}.
 *
 * @since 1.0
 * @author Codestech
 */
public final class NetworkBufferTest {
    @Test
    public void testBoolean() {
        testValues(true, false, NetworkBuffer::readBoolean, NetworkBuffer::writeBoolean);
    }

    @Test
    public void testByte() {
        testValues((byte) 0x35, (byte) 0x70, NetworkBuffer::readByte, NetworkBuffer::writeByte);
    }

    @Test
    public void testUnsignedByte() {
        testValues((short) 210, (short) 56, NetworkBuffer::readUnsignedByte, NetworkBuffer::writeUnsignedByte);
    }

    @Test
    public void testShort() {
        testValues((short) 354, (short) 5, NetworkBuffer::readShort, NetworkBuffer::writeShort);
    }

    @Test
    public void testUnsignedShort() {
        testValues(30345, 123, NetworkBuffer::readUnsignedShort, NetworkBuffer::writeUnsignedShort);
    }

    @Test
    public void testInt() {
        testValues(265, 461267, NetworkBuffer::readInt, NetworkBuffer::writeInt);
    }

    @Test
    public void testLong() {
        testValues(1245L, 21474836457L, NetworkBuffer::readLong, NetworkBuffer::writeLong);
    }

    @Test
    public void testFloat() {
        testValues(132473345343463452.56457455f, 325f, NetworkBuffer::readFloat, NetworkBuffer::writeFloat);
    }

    @Test
    public void testDouble() {
        testValues(3.402823E+45D, 1325D, NetworkBuffer::readDouble, NetworkBuffer::writeDouble);
    }

    @Test
    public void testString() {
        testValues("Hello-World!", "Hello-Jet-Users!", NetworkBuffer::readString, NetworkBuffer::writeString);
    }

    @Test
    public void testTextComponent() {
        testValues(
                Component.text("A simple component"),
                Component.text("A colorized component",
                        NamedTextColor.RED,
                        TextDecoration.BOLD,
                        TextDecoration.OBFUSCATED),
                NetworkBuffer::readTextComponent,
                NetworkBuffer::writeTextComponent
        );
    }

    @Test
    public void testJsonTextComponent() {
        testValues(
                Component.text("A simple json component"),
                Component.text("A colorized json component",
                        NamedTextColor.RED,
                        TextDecoration.BOLD,
                        TextDecoration.OBFUSCATED),
                NetworkBuffer::readJsonTextComponent,
                NetworkBuffer::writeJsonTextComponent
        );
    }

    @Test
    public void testVarInt() {
        testValues(45345, 1643554332, NetworkBuffer::readVarInt, NetworkBuffer::writeVarInt);
    }

    @Test
    public void testVarLong() {
        testValues(0x7fffffffL + 35, 125L, NetworkBuffer::readVarLong, NetworkBuffer::writeVarLong);
    }

    @Test
    public void testUniqueId() {
        testValues(UUID.randomUUID(), UUID.fromString("5353fe70-71b2-41d3-9b0a-85741118185f"),
                NetworkBuffer::readUniqueId, NetworkBuffer::writeUniqueId);
    }

    @Test
    public void testByteArray() {
        NetworkBuffer buffer = NetworkBuffer.create();

        byte[] first = new byte[0];
        byte[] second = new byte[6];

        ThreadLocalRandom.current().nextBytes(second);

        buffer.writeByteArray(first);
        buffer.writeByteArray(second);

        Assertions.assertArrayEquals(first, buffer.readByteArray());
        Assertions.assertArrayEquals(second, buffer.readByteArray());
    }

    @Test
    public void testByteArrayWithoutLength() {
        NetworkBuffer buffer = NetworkBuffer.create();

        byte[] value = new byte[5];
        ThreadLocalRandom.current().nextBytes(value);

        buffer.writeByteArray(value, false);
        Assertions.assertArrayEquals(value, buffer.readByteArray(false));
    }

    @Test
    public void testEmptyByteArrayWithoutLength() {
        NetworkBuffer buffer = NetworkBuffer.create();

        byte[] value = new byte[0];

        buffer.writeByteArray(value, false);
        Assertions.assertArrayEquals(value, buffer.readByteArray(false));
    }

    @Test
    public void testOptionalString() {
        testValues("first-string-test", "second-test-string", NetworkBuffer::readOptionalString,
                NetworkBuffer::writeOptionalString);
    }

    @Test
    public void testCollections() {
        Collection<GameProfileProperties> list = List.of(
                GameProfileProperties.builder()
                        .username("aaaa-first")
                        .signature("not-random")
                        .build(),
                GameProfileProperties.builder()
                        .username("second-bbbbbbb")
                        .signature("still-not-random")
                        .build(),
                GameProfileProperties.builder()
                        .username("ccc")
                        .signature("signature-for-ccc-is-STILL-not-random")
                        .build()
                );

        NetworkBuffer buffer = NetworkBuffer.create();
        buffer.writeCollection(GameProfilePropertiesCodec.instance(), list);

        Assertions.assertEquals(list, buffer.readCollection(GameProfilePropertiesCodec.instance()));
    }

    @Test
    public void writeBufferToAnotherTest() {
        NetworkBuffer first = NetworkBuffer.create();

        ThreadLocalRandom random = ThreadLocalRandom.current();

        int integer = random.nextInt();
        long aLong = random.nextLong();
        int aShort = random.nextInt(0, 65535);

        System.out.println(aShort);

        first.writeInt(integer);
        first.writeVarLong(aLong);
        first.writeUnsignedShort(aShort);

        NetworkBuffer second = NetworkBuffer.create();
        second.write(first);

        Assertions.assertEquals(integer, second.readInt());
        Assertions.assertEquals(aLong, second.readVarLong());
        Assertions.assertEquals(aShort, second.readUnsignedShort());
    }

    @Test
    public void testReadable() {
        ByteBuf buf = Unpooled.buffer();

        NetworkBuffer buffer = new NetworkBuffer(buf);
        buffer.writeInt(5);

        Assertions.assertTrue(buffer.isReadable(4));
        Assertions.assertFalse(buffer.isReadable(5));

        buffer.readInt();

        Assertions.assertFalse(buffer.isReadable(1));
        Assertions.assertTrue(buffer.isReadable(0));
    }

    /**
     * Tests reading and writing in {@link NetworkBuffer network buffer} with provided data.
     *
     * @param first a first value to test
     * @param second a second value to test
     * @param readFunction a function reading values from the network buffer
     * @param writeFunction a function writing values from the network buffer
     * @param <T> the type of values to test
     * @since 1.0
     */
    private static <T> void testValues(@NonNull T first, @NonNull T second,
                                        @NonNull Function<NetworkBuffer, T> readFunction,
                                        @NonNull BiConsumer<NetworkBuffer, T> writeFunction) {
        NetworkBuffer buffer = NetworkBuffer.create();

        writeFunction.accept(buffer, first);
        writeFunction.accept(buffer, second);

        Assertions.assertEquals(first, readFunction.apply(buffer));
        Assertions.assertEquals(second, readFunction.apply(buffer));
    }
}