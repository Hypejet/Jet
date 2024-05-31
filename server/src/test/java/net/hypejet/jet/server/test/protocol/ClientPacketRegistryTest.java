package net.hypejet.jet.server.test.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.ClientPacket;
import net.hypejet.jet.protocol.packet.client.handshake.ClientHandshakePacket;
import net.hypejet.jet.protocol.packet.client.login.ClientCookieResponsePacket;
import net.hypejet.jet.protocol.packet.client.login.ClientEncryptionResponsePacket;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginRequestPacket;
import net.hypejet.jet.protocol.packet.client.login.ClientPluginMessageResponsePacket;
import net.hypejet.jet.server.network.buffer.NetworkBuffer;
import net.hypejet.jet.server.network.protocol.ClientPacketRegistry;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a test for {@link ClientPacketRegistry client packet registry}.
 *
 * @since 1.0
 * @author Codestech
 */
public final class ClientPacketRegistryTest {
    @Test
    public void testValidPacket() {
        Assertions.assertTrue(ClientPacketRegistry.hasPacketReader(0, ProtocolState.HANDSHAKE));
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

        ClientPacket packet = ClientPacketRegistry.read(0, ProtocolState.HANDSHAKE, buffer);
        Assertions.assertInstanceOf(ClientHandshakePacket.class, packet);

        ClientHandshakePacket clientHandshakePacket = (ClientHandshakePacket) packet;

        Assertions.assertEquals(protocolVersion, clientHandshakePacket.protocolVersion());
        Assertions.assertEquals(serverAddress, clientHandshakePacket.serverAddress());
        Assertions.assertEquals(serverPort, clientHandshakePacket.serverPort());
        Assertions.assertEquals(ProtocolState.LOGIN, clientHandshakePacket.nextState());
    }

    @Test
    public void testLoginRequest() {
        NetworkBuffer buffer = createBuffer();

        String username = "Codestech";
        UUID uuid = UUID.randomUUID();

        buffer.writeString(username);
        buffer.writeUniqueId(uuid);

        ClientPacket packet = ClientPacketRegistry.read(0, ProtocolState.LOGIN, buffer);
        Assertions.assertInstanceOf(ClientLoginRequestPacket.class, packet);

        ClientLoginRequestPacket requestPacket = (ClientLoginRequestPacket) packet;

        Assertions.assertEquals(username, requestPacket.username());
        Assertions.assertEquals(uuid, requestPacket.uniqueId());
    }

    @Test
    public void testEncryptionResponse() {
        NetworkBuffer buffer = createBuffer();

        byte[] sharedSecret = new byte[4];
        byte[] verifyToken = new byte[4];

        ThreadLocalRandom random = ThreadLocalRandom.current();

        random.nextBytes(sharedSecret);
        random.nextBytes(verifyToken);

        buffer.writeByteArray(sharedSecret);
        buffer.writeByteArray(verifyToken);

        ClientPacket packet = ClientPacketRegistry.read(1, ProtocolState.LOGIN, buffer);
        Assertions.assertInstanceOf(ClientEncryptionResponsePacket.class, packet);

        ClientEncryptionResponsePacket responsePacket = (ClientEncryptionResponsePacket) packet;

        Assertions.assertArrayEquals(sharedSecret, responsePacket.sharedSecret());
        Assertions.assertArrayEquals(verifyToken, responsePacket.verifyToken());
    }

    @Test
    public void testCookieResponse() {
        ByteBuf buf = Unpooled.buffer();
        NetworkBuffer buffer = new NetworkBuffer(buf);

        ThreadLocalRandom random = ThreadLocalRandom.current();

        Key identifier = Key.key("hypejet", "jet");
        byte[] data = random.nextBoolean() ? new byte[10] : null;

        if (data != null) {
            random.nextBytes(data);
        }

        buffer.writeIdentifier(identifier);
        buffer.writeBoolean(data != null);

        if (data != null) {
            buffer.writeByteArray(data);
        }

        ClientPacket packet = ClientPacketRegistry.read(4, ProtocolState.LOGIN, buffer);
        Assertions.assertInstanceOf(ClientCookieResponsePacket.class, packet);

        ClientCookieResponsePacket responsePacket = (ClientCookieResponsePacket) packet;

        Assertions.assertEquals(identifier, responsePacket.identifier());
        Assertions.assertArrayEquals(data, responsePacket.data());
    }

    @Test
    public void testPluginMessageResponse() {
        NetworkBuffer buffer = createBuffer();

        ThreadLocalRandom random = ThreadLocalRandom.current();

        int messageId = random.nextInt();
        boolean successful = random.nextBoolean();
        byte[] data = new byte[random.nextInt(35)];

        random.nextBytes(data);

        buffer.writeVarInt(messageId);
        buffer.writeBoolean(successful);
        buffer.writeByteArray(data, false);

        ClientPacket packet = ClientPacketRegistry.read(2, ProtocolState.LOGIN, buffer);
        Assertions.assertInstanceOf(ClientPluginMessageResponsePacket.class, packet);

        ClientPluginMessageResponsePacket responsePacket = (ClientPluginMessageResponsePacket) packet;

        Assertions.assertEquals(messageId, responsePacket.messageId());
        Assertions.assertEquals(successful, responsePacket.successful());
        Assertions.assertArrayEquals(data, responsePacket.data());
    }

    @Test
    public void testInvalidPacket() {
        Assertions.assertFalse(ClientPacketRegistry.hasPacketReader(1, ProtocolState.HANDSHAKE));
    }

    private static @NonNull NetworkBuffer createBuffer() {
        return new NetworkBuffer(Unpooled.buffer());
    }
}