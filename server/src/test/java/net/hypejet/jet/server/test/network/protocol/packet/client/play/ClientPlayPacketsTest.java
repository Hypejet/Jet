package net.hypejet.jet.server.test.network.protocol.packet.client.play;

import net.hypejet.jet.coordinate.BlockPosition;
import net.hypejet.jet.protocol.packet.client.play.ClientAcknowledgeMessagePlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientActionPlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientChangeDifficultyPlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientChatCommandPlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientChatSessionUpdatePlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientConfirmTeleportationPlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientKeepAlivePlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientOnGroundPlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientPluginMessagePlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientPositionPlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientQueryBlockEntityTagPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientRotationAndPositionPlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientRotationPlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientSignedChatCommandPlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientSignedChatMessagePlayPacket;
import net.hypejet.jet.server.test.network.protocol.packet.client.ClientPacketTestUtil;
import net.hypejet.jet.signing.SeenMessages;
import net.hypejet.jet.signing.SignedArgument;
import net.hypejet.jet.world.difficulty.Difficulty;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

import java.util.BitSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a test of reading and writing of {@linkplain net.hypejet.jet.protocol.packet.client.ClientPlayPacket
 * client play packets}.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.protocol.packet.client.ClientPlayPacket
 */
public final class ClientPlayPacketsTest {
    @Test
    public void testKeepAlive() {
        ClientPacketTestUtil.testPacket(new ClientKeepAlivePlayPacket(3521));
    }

    @Test
    public void testPluginMessage() {
        ClientPacketTestUtil.testPacket(new ClientPluginMessagePlayPacket(
                Key.key("a-play", "plugin-message"), new byte[] { 1, 2, 5, 7, 2, 5, 6, 32, 63, 62, 64, 42, 7, 3, 97 }
        ));
    }

    @Test
    public void testRotationAndPosition() {
        ClientPacketTestUtil.testPacket(new ClientRotationAndPositionPlayPacket(3, 5, 1, 4f, 6f, true));
    }

    @Test
    public void testPosition() {
        ClientPacketTestUtil.testPacket(new ClientPositionPlayPacket(2, 4, 1, false));
    }

    @Test
    public void testRotation() {
        ClientPacketTestUtil.testPacket(new ClientRotationPlayPacket(35f, 180f, true));
    }

    @Test
    public void testAction() {
        ClientPacketTestUtil.testPacket(new ClientActionPlayPacket(
                5, ClientActionPlayPacket.Action.START_FLYING_WITH_ELYTRA, 3
        ));
    }

    @Test
    public void testConfirmTeleportation() {
        ClientPacketTestUtil.testPacket(new ClientConfirmTeleportationPlayPacket(213));
    }

    @Test
    public void testQueryBlockEntityTag() {
        ClientPacketTestUtil.testPacket(new ClientQueryBlockEntityTagPacket(3, BlockPosition.blockPosition(2, 4, 6)));
    }

    @Test
    public void testChangeDifficulty() {
        ClientPacketTestUtil.testPacket(new ClientChangeDifficultyPlayPacket(Difficulty.NORMAL));
    }

    @Test
    public void testAcknowledgeMessage() {
        ClientPacketTestUtil.testPacket(new ClientAcknowledgeMessagePlayPacket(4));
    }

    @Test
    public void testChatCommand() {
        ClientPacketTestUtil.testPacket(new ClientChatCommandPlayPacket("summon minecraft:zombie ~ ~ ~"));
    }

    @Test
    public void testSignedChatCommand() {
        byte[] randomSignature1 = new byte[256];
        byte[] randomSignature2 = new byte[256];

        ThreadLocalRandom.current().nextBytes(randomSignature1);
        ThreadLocalRandom.current().nextBytes(randomSignature2);

        BitSet acknowledgedSeenMessages = new BitSet(20);
        acknowledgedSeenMessages.set(0, true);
        acknowledgedSeenMessages.set(1, false);
        acknowledgedSeenMessages.set(2, true);
        acknowledgedSeenMessages.set(3, true);

        ClientPacketTestUtil.testPacket(new ClientSignedChatCommandPlayPacket(
                "summon",
                System.currentTimeMillis(),
                ThreadLocalRandom.current().nextLong(),
                Set.of(new SignedArgument("type", randomSignature1), new SignedArgument("type", randomSignature2)),
                new SeenMessages(4, acknowledgedSeenMessages)
        ));
    }

    @Test
    public void testSignedChatMessage() {
        byte[] randomSignature = new byte[256];
        ThreadLocalRandom.current().nextBytes(randomSignature);

        BitSet acknowledgedSeenMessages = new BitSet(20);
        acknowledgedSeenMessages.set(0, false);
        acknowledgedSeenMessages.set(1, true);
        acknowledgedSeenMessages.set(2, true);
        acknowledgedSeenMessages.set(3, false);
        acknowledgedSeenMessages.set(4, true);

        ClientPacketTestUtil.testPacket(new ClientSignedChatMessagePlayPacket(
                "Hello world!", System.currentTimeMillis(), ThreadLocalRandom.current().nextLong(), randomSignature,
                new SeenMessages(5, acknowledgedSeenMessages)
        ));
    }

    @Test
    public void testChatSessionUpdate() {
        ClientPacketTestUtil.testPacket(new ClientChatSessionUpdatePlayPacket(UUID.randomUUID(),
                System.currentTimeMillis(), new byte[] { 2, 4, 1, 8, 3, 74, 72 }, new byte[] { 1, 4, 7, 2, 6, 3, 2 }));
    }

    @Test
    public void testOnGround() {
        ClientPacketTestUtil.testPacket(new ClientOnGroundPlayPacket(true));
    }
}