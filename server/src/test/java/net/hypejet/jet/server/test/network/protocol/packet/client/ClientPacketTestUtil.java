package net.hypejet.jet.server.test.network.protocol.packet.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hypejet.jet.protocol.packet.client.ClientPacket;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketRegistry;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Assertions;

/**
 * Represents a utility for testing of reading and writing of {@linkplain ClientPacket client packets}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPacket
 */
public final class ClientPacketTestUtil {

    private ClientPacketTestUtil() {}

    /**
     * Tests reading and writing of a {@linkplain ClientPacket client packet}.
     *
     * @param packet the client packet
     * @since 1.0
     */
    public static void testPacket(@NonNull ClientPacket packet) {
        ClientPacketCodec<?> codec = ClientPacketRegistry.codec(packet.getClass());

        Assertions.assertNotNull(codec);
        ByteBuf buf = Unpooled.buffer();

        try {
            writePacket(codec, buf, packet); // Write the packet with java generics

            ClientPacket readPacket = codec.read(buf);

            Assertions.assertEquals(packet, readPacket);
            Assertions.assertNotSame(packet, readPacket);
        } finally {
            buf.release();
        }
    }

    private static <P extends ClientPacket> void writePacket(@NonNull ClientPacketCodec<P> codec, @NonNull ByteBuf buf,
                                                             @NonNull ClientPacket packet) {
        codec.write(buf, codec.getPacketClass().cast(packet));
    }
}