package net.hypejet.jet.server.test.network.protocol.packet.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Assertions;

import java.util.function.BiConsumer;

/**
 * Represents a utility for testing of reading and writing of {@linkplain ServerPacket server packets}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 */
public final class ServerPacketTestUtil {
    /**
     * Tests reading and writing of a {@linkplain ServerPacket server packet} specified.
     *
     * <p>The equality of the packets is checked via {@link Assertions#assertEquals(Object, Object)}.</p>
     *
     * @param packetClass a class of the packet
     * @param packet the server packet
     * @since 1.0
     * @param <P> a type of the packet
     */
    public static <P extends ServerPacket> void testPacket(@NonNull Class<P> packetClass, @NonNull P packet) {
        testPacket(packetClass, packet, Assertions::assertEquals);
    }

    /**
     * Tests reading and writing of a {@linkplain ServerPacket server packet} specified.
     *
     * @param packetClass a class of the packet
     * @param packet the server packet
     * @param equalsTest a function, which tests whether two packets are equal to each other
     * @since 1.0
     * @param <P> a type of the packet
     */
    public static <P extends ServerPacket> void testPacket(@NonNull Class<P> packetClass, @NonNull P packet,
                                                           @NonNull BiConsumer<P, P> equalsTest) {
        PacketCodec<? extends ServerPacket> codec = ServerPacketRegistry.codec(packet.getClass());
        Assertions.assertNotNull(codec);

        ByteBuf buf = Unpooled.buffer();

        try {
            write(codec, buf, packet);
            ServerPacket readPacket = codec.read(buf);

            Assertions.assertNotSame(packet, readPacket);
            equalsTest.accept(packet, packetClass.cast(packet));
        } finally {
            buf.release();
        }
    }

    private static <P extends ServerPacket> void write(@NonNull PacketCodec<P> codec, @NonNull ByteBuf buf,
                                                       @NonNull ServerPacket packet) {
        codec.write(buf, codec.getPacketClass().cast(packet));
    }
}