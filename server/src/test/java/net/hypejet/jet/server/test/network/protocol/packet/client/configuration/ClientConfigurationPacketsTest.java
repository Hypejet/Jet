package net.hypejet.jet.server.test.network.protocol.packet.client.configuration;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.pack.DataPack;
import net.hypejet.jet.pack.ResourcePackResult;
import net.hypejet.jet.protocol.packet.client.configuration.ClientAcknowledgeFinishConfigurationPacket;
import net.hypejet.jet.protocol.packet.client.configuration.ClientCookieResponseConfigurationPacket;
import net.hypejet.jet.protocol.packet.client.configuration.ClientInformationConfigurationPacket;
import net.hypejet.jet.protocol.packet.client.configuration.ClientKeepAliveConfigurationPacket;
import net.hypejet.jet.protocol.packet.client.configuration.ClientKnownPacksConfigurationPacket;
import net.hypejet.jet.protocol.packet.client.configuration.ClientPluginMessageConfigurationPacket;
import net.hypejet.jet.protocol.packet.client.configuration.ClientPongConfigurationPacket;
import net.hypejet.jet.protocol.packet.client.configuration.ClientResourcePackResponseConfigurationPacket;
import net.hypejet.jet.server.test.network.protocol.packet.client.ClientPacketTestUtil;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a test of reading and writing of
 * {@linkplain net.hypejet.jet.protocol.packet.client.ClientConfigurationPacket client configuration packets}.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.protocol.packet.client.ClientConfigurationPacket
 */
public final class ClientConfigurationPacketsTest {
    @Test
    public void testPluginMessage() {
        ClientPacketTestUtil.testPacket(new ClientPluginMessageConfigurationPacket(
                Key.key("a-dummy", "plugin/message"), new byte[] { 1, 2, 5, 3, 6, 8, 3, 6, 6, 2, 6, 7, 3, 2, 3, 4, 6 }
        ));
    }

    @Test
    public void testClientInformation() {
        ClientPacketTestUtil.testPacket(new ClientInformationConfigurationPacket(new Player.Settings(
                Locale.US, (byte) 6, Player.ChatMode.COMMANDS_ONLY, true,
                List.of(Player.SkinPart.HAT, Player.SkinPart.CAPE), Entity.Hand.LEFT, true, false
        )));
    }

    @Test
    public void testCookieResponse() {
        ClientPacketTestUtil.testPacket(new ClientCookieResponseConfigurationPacket(
                Key.key("a.dummy", "custom-cookie"), new byte[] { 1 , 5, 67, 2, 6, 2, 54, 75, 124, 23, 35, 32, 63, 52 }
        ));
    }

    @Test
    public void testPong() {
        ClientPacketTestUtil.testPacket(new ClientPongConfigurationPacket(2432));
    }

    @Test
    public void testResourcePackResponse() {
        ClientPacketTestUtil.testPacket(new ClientResourcePackResponseConfigurationPacket(
                UUID.randomUUID(), ResourcePackResult.DISCARDED
        ));
    }

    @Test
    public void testKnownPacks() {
        ClientPacketTestUtil.testPacket(new ClientKnownPacksConfigurationPacket(Set.of(
                new DataPack(Key.key("core"), "1.21"), new DataPack(Key.key("custom", "data-pack"), "2.3.4")
        )));
    }

    @Test
    public void testKeepAlive() {
        ClientPacketTestUtil.testPacket(new ClientKeepAliveConfigurationPacket(232));
    }

    @Test
    public void testAcknowledgeFinishConfiguration() {
        ClientPacketTestUtil.testPacket(new ClientAcknowledgeFinishConfigurationPacket());
    }
}