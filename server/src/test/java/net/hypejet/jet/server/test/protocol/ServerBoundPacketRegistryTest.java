package net.hypejet.jet.server.test.protocol;

import io.netty.buffer.Unpooled;
import net.hypejet.jet.buffer.NetworkBuffer;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.serverbound.ServerBoundPacket;
import net.hypejet.jet.protocol.packet.serverbound.handshake.HandshakePacket;
import net.hypejet.jet.protocol.packet.serverbound.login.LoginRequestPacket;
import net.hypejet.jet.server.buffer.NetworkBufferImpl;
import net.hypejet.jet.server.protocol.ServerBoundPacketRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/**
 * Represents a test for {@link ServerBoundPacketRegistry server-bound packet registry}.
 *
 * @since 1.0
 * @author Codestech
 */
public final class ServerBoundPacketRegistryTest {

    private final ServerBoundPacketRegistry packetRegistry = new ServerBoundPacketRegistry();

    @Test
    public void testValidPacket() {
        Assertions.assertNotNull(this.packetRegistry.getReader(0, ProtocolState.HANDSHAKE));
    }

    @Test
    public void testHandshake() {
        NetworkBuffer buffer = createBuffer();

        int protocolVersion = 5;
        String serverAddress = "localhost";
        int serverPort = 25565;

        buffer.writeVarInt(protocolVersion);
        buffer.writeString(serverAddress);
        buffer.writeUnsignedShort(serverPort);
        buffer.writeVarInt(2);

        ServerBoundPacket packet = this.packetRegistry.read(0, ProtocolState.HANDSHAKE, buffer);
        Assertions.assertInstanceOf(HandshakePacket.class, packet);

        HandshakePacket handshakePacket = (HandshakePacket) packet;

        Assertions.assertEquals(protocolVersion, handshakePacket.protocolVersion());
        Assertions.assertEquals(serverAddress, handshakePacket.serverAddress());
        Assertions.assertEquals(serverPort, handshakePacket.serverPort());
        Assertions.assertEquals(ProtocolState.LOGIN, handshakePacket.nextState());
    }

    @Test
    public void testLoginRequest() {
        NetworkBuffer buffer = createBuffer();

        String username = "Codestech";
        UUID uuid = UUID.randomUUID();

        buffer.writeString(username);
        buffer.writeUniqueId(uuid);

        ServerBoundPacket packet = this.packetRegistry.read(0, ProtocolState.LOGIN, buffer);
        Assertions.assertInstanceOf(LoginRequestPacket.class, packet);

        LoginRequestPacket requestPacket = (LoginRequestPacket) packet;

        Assertions.assertEquals(username, requestPacket.username());
        Assertions.assertEquals(uuid, requestPacket.uniqueId());
    }

    @Test
    public void testInvalidPacket() {
        Assertions.assertNull(this.packetRegistry.getReader(1, ProtocolState.HANDSHAKE));
    }

    private static @NonNull NetworkBuffer createBuffer() {
        return new NetworkBufferImpl(Unpooled.buffer());
    }
}