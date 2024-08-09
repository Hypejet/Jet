package net.hypejet.jet.server.test.network.protocol.packet.server.play;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.hypejet.jet.coordinate.BlockPosition;
import net.hypejet.jet.coordinate.Position;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.protocol.packet.server.play.ServerActionBarPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerCenterChunkPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerChunkAndLightDataPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerCommandSuggestionsResponsePlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerCommandSuggestionsResponsePlayPacket.Suggestion;
import net.hypejet.jet.protocol.packet.server.play.ServerDeclareCommandsPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerDeclareCommandsPlayPacket.ArgumentNode;
import net.hypejet.jet.protocol.packet.server.play.ServerDeclareCommandsPlayPacket.LiteralNode;
import net.hypejet.jet.protocol.packet.server.play.ServerDeclareCommandsPlayPacket.Node;
import net.hypejet.jet.protocol.packet.server.play.ServerDeclareCommandsPlayPacket.RootNode;
import net.hypejet.jet.protocol.packet.server.play.ServerDeclareCommandsPlayPacket.SuggestionsType;
import net.hypejet.jet.protocol.packet.server.play.ServerDisconnectPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerGameEventPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerJoinGamePlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerKeepAlivePlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerPlayerListHeaderAndFooterPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerPluginMessagePlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerSynchronizePositionPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerSynchronizePositionPlayPacket.RelativeFlag;
import net.hypejet.jet.protocol.packet.server.play.ServerSystemMessagePlayPacket;
import net.hypejet.jet.server.test.network.protocol.packet.server.ServerPacketTestUtil;
import net.hypejet.jet.world.chunk.BlockEntity;
import net.hypejet.jet.world.chunk.ChunkSection;
import net.hypejet.jet.world.chunk.palette.DirectPalette;
import net.hypejet.jet.world.chunk.palette.IndirectPalette;
import net.hypejet.jet.world.chunk.palette.Palette;
import net.hypejet.jet.world.chunk.palette.SingleValuedPalette;
import net.hypejet.jet.world.event.GameEvent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Represents a test of reading and writing {@linkplain net.hypejet.jet.protocol.packet.server.ServerPlayPacket server
 * play packets}.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.protocol.packet.server.ServerPlayPacket
 */
public final class ServerPlayPacketsTest {
    @Test
    public void testKeepAlive() {
        ServerPacketTestUtil.testPacket(ServerKeepAlivePlayPacket.class, new ServerKeepAlivePlayPacket(3254342325L));
    }

    @Test
    public void testDisconnect() {
        ServerPacketTestUtil.testPacket(ServerDisconnectPlayPacket.class, new ServerDisconnectPlayPacket(
                Component.text("You got disconnected, again!", NamedTextColor.RED, TextDecoration.STRIKETHROUGH)
        ));
    }

    @Test
    public void testJoinGame() {
        ServerPacketTestUtil.testPacket(ServerJoinGamePlayPacket.class, new ServerJoinGamePlayPacket(
                2134, false, Set.of(Key.key("overworld"), Key.key("nether"), Key.key("the_end")), 523325,
                16, 6, false, true, true, 6, Key.key("overworld"), 5252, Player.GameMode.ADVENTURE,
                Player.GameMode.SURVIVAL, false, true,
                new ServerJoinGamePlayPacket.DeathLocation(Key.key("nether"), new BlockPosition(6, 7, 6)),
                5, false
        ));
    }

    @Test
    public void testPluginMessage() {
        ServerPacketTestUtil.testPacket(ServerPluginMessagePlayPacket.class, new ServerPluginMessagePlayPacket(
                Key.key("a-play", "plugin-message"), new byte[] { 124, 24, 62, 72, 78, 32, 31, 63, 42, 64, 1, 32, 62 }
        ));
    }

    @Test
    public void testGameEvent() {
        ServerPacketTestUtil.testPacket(
                ServerGameEventPlayPacket.class,
                new ServerGameEventPlayPacket(new GameEvent.WinGame(true))
        );
    }

    @Test
    public void testSystemMessage() {
        ServerPacketTestUtil.testPacket(ServerSystemMessagePlayPacket.class, new ServerSystemMessagePlayPacket(
                Component.text("A system message!", NamedTextColor.YELLOW, TextDecoration.UNDERLINED), false
        ));
    }

    @Test
    public void testActionBar() {
        ServerPacketTestUtil.testPacket(ServerActionBarPlayPacket.class, new ServerActionBarPlayPacket(
                Component.text("An action bar message", NamedTextColor.DARK_PURPLE, TextDecoration.UNDERLINED))
        );
    }

    @Test
    public void testPlayerListHeaderAndFooter() {
        ServerPacketTestUtil.testPacket(ServerPlayerListHeaderAndFooterPlayPacket.class,
                new ServerPlayerListHeaderAndFooterPlayPacket(Component.text("A header", NamedTextColor.GREEN),
                        Component.text("And a footer!"))
        );
    }

    @Test
    public void testCenterChunk() {
        ServerPacketTestUtil.testPacket(ServerCenterChunkPlayPacket.class, new ServerCenterChunkPlayPacket(5, 6));
    }

    @Test
    public void testChunkAndLightData() {
        List<ChunkSection> sections = new ArrayList<>();

        long[] biomeEntries = new long[64];
        Arrays.fill(biomeEntries, 0);
        IndirectPalette biomePalette = new IndirectPalette((short) 1, new int[] { 1 }, biomeEntries);

        for (int i = 0; i < 24; i++) {
            short blockCount;
            Palette blocks;

            if (i > 4) {
                blockCount = 0;
                blocks = new SingleValuedPalette(0);
            } else {
                blockCount = 16 * 16 * 16;
                long[] entries = new long[4096];
                Arrays.fill(entries, 1);
                blocks = new DirectPalette(entries);
            }

            sections.add(new ChunkSection(blockCount, blocks, biomePalette));
        }

        int sectionsSize = sections.size() + 2;

        BitSet filledSet = new BitSet(sectionsSize);
        BitSet emptySet = new BitSet(sectionsSize);

        byte[] lightValues = new byte[2048];
        Arrays.fill(lightValues, (byte) 0xFF);

        byte[][] skyLightData = new byte[sectionsSize][];

        for (int i = 0; i < sectionsSize; i++) {
            filledSet.set(i, true);
            emptySet.set(i, false);
            skyLightData[i] = lightValues;
        }

        ServerPacketTestUtil.testPacket(ServerChunkAndLightDataPlayPacket.class, new ServerChunkAndLightDataPlayPacket(
                0, 0, CompoundBinaryTag.empty(), sections, Set.of(new BlockEntity((byte) 0, (byte) 0, (short) 10, 8,
                CompoundBinaryTag.empty())), filledSet, emptySet, emptySet, filledSet, skyLightData, new byte[0][]
        ));
    }

    @Test
    public void testSynchronizePosition() {
        ServerPacketTestUtil.testPacket(ServerSynchronizePositionPlayPacket.class,
                new ServerSynchronizePositionPlayPacket(new Position(4, 5, 1, 6f, 12f),
                        Set.of(RelativeFlag.RELATIVE_Z, RelativeFlag.RELATIVE_YAW), 65)
        );
    }

    @Test
    public void testSuggestionsResponse() {
        ServerPacketTestUtil.testPacket(ServerCommandSuggestionsResponsePlayPacket.class,
                new ServerCommandSuggestionsResponsePlayPacket(0, 5, 0, Set.of(
                        new Suggestion("suggestion-1", Component.text("A suggestion tooltip", NamedTextColor.RED)),
                        new Suggestion("second-suggestion", null)
                ))
        );
    }

    @Test
    public void testDeclareCommands() {
        RootNode rootNode = new RootNode(Set.of(new LiteralNode(
                Set.of(new ArgumentNode(node -> {
                    LiteralNode redirectingNode = new LiteralNode(Set.of(), node, false, "redirect");
                    LiteralNode executableNode = new LiteralNode(Set.of(), null, true, "execute");
                    return List.of(redirectingNode, executableNode);
                }, null, false, "an-argument", StringArgumentType.word(), SuggestionsType.ASK_SERVER)),
                null, false, "test-command")
        ), null, false);

        // TODO: See if it is possible to implement equals and hashCode, if so, do it
        ServerPacketTestUtil.testPacket(
                ServerDeclareCommandsPlayPacket.class,
                new ServerDeclareCommandsPlayPacket(rootNode),
                (first, second) -> {
                    RootNode secondRootNode = second.rootNode();

                    Assertions.assertFalse(secondRootNode.executable());
                    Assertions.assertNull(secondRootNode.redirect());

                    Collection<Node> rootChildren = secondRootNode.children();
                    Assertions.assertEquals(rootChildren.size(), 1);

                    LiteralNode literalNode = Assertions.assertInstanceOf(LiteralNode.class,
                            rootChildren.iterator().next());

                    Assertions.assertFalse(literalNode.executable());
                    Assertions.assertNull(literalNode.redirect());
                    Assertions.assertEquals(literalNode.name(), "test-command");

                    Collection<Node> literalChildren = literalNode.children();
                    Assertions.assertEquals(literalChildren.size(), 1);

                    ArgumentNode argumentNode = Assertions.assertInstanceOf(ArgumentNode.class,
                            literalChildren.iterator().next());

                    Assertions.assertFalse(argumentNode.executable());
                    Assertions.assertNull(argumentNode.redirect());
                    Assertions.assertEquals(argumentNode.name(), "an-argument");

                    StringArgumentType argumentType = Assertions.assertInstanceOf(StringArgumentType.class,
                            argumentNode.argumentType());

                    Assertions.assertSame(argumentType.getType(), StringArgumentType.StringType.SINGLE_WORD);
                    Assertions.assertSame(argumentNode.suggestionsType(), SuggestionsType.ASK_SERVER);

                    if (!(argumentNode.children() instanceof List<Node> argumentChildren))
                        throw new IllegalArgumentException("The argument children is not a list");

                    Assertions.assertEquals(argumentChildren.size(), 2);

                    LiteralNode redirectingNode = Assertions.assertInstanceOf(LiteralNode.class,
                            argumentChildren.getFirst());
                    LiteralNode executableNode = Assertions.assertInstanceOf(LiteralNode.class,
                            argumentChildren.getLast());

                    Assertions.assertFalse(redirectingNode.executable());
                    Assertions.assertSame(redirectingNode.redirect(), argumentNode);
                    Assertions.assertEquals(redirectingNode.name(), "redirect");
                    Assertions.assertTrue(redirectingNode.children().isEmpty());

                    Assertions.assertTrue(executableNode.executable());
                    Assertions.assertNull(executableNode.redirect());
                    Assertions.assertEquals(executableNode.name(), "execute");
                    Assertions.assertTrue(executableNode.children().isEmpty());
                }
        );
    }
}