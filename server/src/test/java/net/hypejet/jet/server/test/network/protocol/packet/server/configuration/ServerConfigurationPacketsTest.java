package net.hypejet.jet.server.test.network.protocol.packet.server.configuration;

import net.hypejet.jet.link.ServerLink;
import net.hypejet.jet.link.label.BuiltinLabel;
import net.hypejet.jet.link.label.ComponentLabel;
import net.hypejet.jet.pack.DataPack;
import net.hypejet.jet.protocol.packet.server.configuration.ServerAddResourcePackConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerCookieRequestConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerCustomLinksConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerCustomReportDetailsConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerCustomReportDetailsConfigurationPacket.Details;
import net.hypejet.jet.protocol.packet.server.configuration.ServerDisconnectConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerFeatureFlagsConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerFinishConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerKeepAliveConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerKnownPacksConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerPingConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerPluginMessageConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerRegistryDataConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerRemoveResourcePackConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerResetChatConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerStoreCookieConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerTransferConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerUpdateTagsConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerUpdateTagsConfigurationPacket.Tag;
import net.hypejet.jet.protocol.packet.server.configuration.ServerUpdateTagsConfigurationPacket.TagRegistry;
import net.hypejet.jet.server.test.network.protocol.packet.server.ServerPacketTestUtil;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a test of reading and writing
 * of {@linkplain net.hypejet.jet.protocol.packet.server.ServerConfigurationPacket server configuration packets}.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.protocol.packet.server.ServerConfigurationPacket
 */
public final class ServerConfigurationPacketsTest {
    @Test
    public void testAddResourcePack() {
        ServerPacketTestUtil.testPacket(ServerAddResourcePackConfigurationPacket.class,
                new ServerAddResourcePackConfigurationPacket(UUID.randomUUID(), "https://resourcepack.hypejet.net",
                        "some-very-long-hash", true, Component.text("Please download my resource pack",
                        NamedTextColor.LIGHT_PURPLE, TextDecoration.ITALIC))
        );
    }

    @Test
    public void testCookieRequest() {
        ServerPacketTestUtil.testPacket(ServerCookieRequestConfigurationPacket.class,
                new ServerCookieRequestConfigurationPacket(Key.key("configuration", "cookie")));
    }

    @Test
    public void testCustomLinks() {
        ServerPacketTestUtil.testPacket(ServerCustomLinksConfigurationPacket.class,
                new ServerCustomLinksConfigurationPacket(List.of(
                        new ServerLink(BuiltinLabel.BUG_REPORT, "https://bugreport.hypejet.net"),
                        new ServerLink(
                                new ComponentLabel(Component.text("A link", NamedTextColor.GREEN, TextDecoration.BOLD)),
                                "https://a-link.hypejet.net"
                        )
                ))
        );
    }

    @Test
    public void testCustomReportDetails() {
        ServerPacketTestUtil.testPacket(ServerCustomReportDetailsConfigurationPacket.class,
                new ServerCustomReportDetailsConfigurationPacket(List.of(
                        new Details("crash-report-title", "i-do-not-know")
                ))
        );
    }

    @Test
    public void testDisconnect() {
        ServerPacketTestUtil.testPacket(ServerDisconnectConfigurationPacket.class,
                new ServerDisconnectConfigurationPacket(
                        Component.text("You got disconnected during a configuration protocol state", NamedTextColor.YELLOW)
                )
        );
    }

    @Test
    public void testFeatureFlags() {
        ServerPacketTestUtil.testPacket(ServerFeatureFlagsConfigurationPacket.class,
                new ServerFeatureFlagsConfigurationPacket(Set.of(Key.key("vanilla"), Key.key("trade-rebalance")))
        );
    }

    @Test
    public void testKeepAlive() {
        ServerPacketTestUtil.testPacket(ServerKeepAliveConfigurationPacket.class,
                new ServerKeepAliveConfigurationPacket(352363256));
    }

    @Test
    public void testKnownPacks() {
        ServerPacketTestUtil.testPacket(ServerKnownPacksConfigurationPacket.class,
                new ServerKnownPacksConfigurationPacket(Set.of(new DataPack(Key.key("core"), "1.21"),
                        new DataPack(Key.key("some", "datapack"), "ver-1.0")))
        );
    }

    @Test
    public void testPing() {
        ServerPacketTestUtil.testPacket(ServerPingConfigurationPacket.class, new ServerPingConfigurationPacket(326));
    }

    @Test
    public void testPluginMessage() {
        ServerPacketTestUtil.testPacket(ServerPluginMessageConfigurationPacket.class,
                new ServerPluginMessageConfigurationPacket(Key.key("a-plugin-message"),
                        new byte[] { 1, 62, 23, 73, 72, 93 })
        );
    }

    @Test
    public void testRegistryData() {
        ServerPacketTestUtil.testPacket(ServerRegistryDataConfigurationPacket.class,
                new ServerRegistryDataConfigurationPacket(Key.key("a-registry", "data"), Set.of(
                        new ServerRegistryDataConfigurationPacket.Entry(Key.key("entry-1"), CompoundBinaryTag.empty()),
                        new ServerRegistryDataConfigurationPacket.Entry(
                                Key.key("an", "entry"),
                                CompoundBinaryTag.builder()
                                        .putBoolean("a-boolean", true)
                                        .putString("some-string", "Hello World!!!")
                                        .putFloat("a-number-which-is-float", 12151f)
                                        .build()
                        )
                ))
        );
    }

    @Test
    public void testRemoveResourcePack() {
        ServerPacketTestUtil.testPacket(ServerRemoveResourcePackConfigurationPacket.class,
                new ServerRemoveResourcePackConfigurationPacket(UUID.randomUUID()));
    }

    @Test
    public void testStoreCookie() {
        ServerPacketTestUtil.testPacket(ServerStoreCookieConfigurationPacket.class,
                new ServerStoreCookieConfigurationPacket(Key.key("some-test", "cookie"),
                        new byte[] { 1, 62, 76, 72, 91, 93, 84, 24, 82, 71, 87, 12, 63, 33 })
        );
    }

    @Test
    public void testTransfer() {
        ServerPacketTestUtil.testPacket(ServerTransferConfigurationPacket.class,
                new ServerTransferConfigurationPacket("localhost", 25566));
    }

    @Test
    public void testUpdateTags() {
        ServerPacketTestUtil.testPacket(ServerUpdateTagsConfigurationPacket.class,
                new ServerUpdateTagsConfigurationPacket(Set.of(
                        new TagRegistry(Key.key("hypejet", "tag-registry"), Set.of(
                                new Tag(Key.key("tag"),
                                        new int[] { 1252, 12534, 6132644, 462523, 6426, 753, 643, 35, 5325, 754, 42 }),
                                new Tag(Key.key("hypejet", "some-tag"),
                                        new int[] { 1252, 12534, 6132644, 234521, 52, 63, 462523, 6426, 753, 643, 8 })
                        )),
                        new TagRegistry(Key.key("another-tag-registry"), Set.of(
                                new Tag(Key.key("another-tag", "in-another-reg"),
                                        new int[] { 4234253, 5325, 65254, 74362, 75362, 75, 643, 35, 5325, 754, 42 }),
                                new Tag(Key.key("mc", "some-tag"),
                                        new int[] { 1252, 12534, 6132644, 23443563, 462523, 6426, 753, 643, 8534, 6 })
                        ))
                ))
        );
    }

    @Test
    public void testFinishConfiguration() {
        ServerPacketTestUtil.testPacket(ServerFinishConfigurationPacket.class, new ServerFinishConfigurationPacket());
    }

    @Test
    public void testResetChat() {
        ServerPacketTestUtil.testPacket(ServerResetChatConfigurationPacket.class,
                new ServerResetChatConfigurationPacket());
    }
}