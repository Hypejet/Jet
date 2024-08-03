package net.hypejet.jet.server.test.network.protocol.packet.server.play;
import net.hypejet.jet.coordinate.BlockPosition;
import net.hypejet.jet.coordinate.Position;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.protocol.packet.server.play.ServerActionBarPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerCenterChunkPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerChunkAndLightDataPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerDisconnectPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerGameEventPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerJoinGamePlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerKeepAlivePlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerPlayerListHeaderAndFooterPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerPluginMessagePlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerSynchronizePositionPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerSynchronizePositionPlayPacket.RelativeFlag;
import net.hypejet.jet.protocol.packet.server.play.ServerSystemMessagePlayPacket;
import net.hypejet.jet.server.network.protocol.codecs.other.PositionNetworkCodec;
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
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
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
        ServerPacketTestUtil.testPacket(new ServerKeepAlivePlayPacket(3254342325L));
    }

    @Test
    public void testDisconnect() {
        ServerPacketTestUtil.testPacket(new ServerDisconnectPlayPacket(
                Component.text("You got disconnected, again!", NamedTextColor.RED, TextDecoration.STRIKETHROUGH)
        ));
    }

    @Test
    public void testJoinGame() {
        ServerPacketTestUtil.testPacket(new ServerJoinGamePlayPacket(
                2134, false, Set.of(Key.key("overworld"), Key.key("nether"), Key.key("the_end")), 523325,
                16, 6, false, true, true, 6, Key.key("overworld"), 5252, Player.GameMode.ADVENTURE,
                Player.GameMode.SURVIVAL, false, true,
                new ServerJoinGamePlayPacket.DeathLocation(Key.key("nether"), new BlockPosition(6, 7, 6)),
                5, false
        ));
    }

    @Test
    public void testPluginMessage() {
        ServerPacketTestUtil.testPacket(new ServerPluginMessagePlayPacket(
                Key.key("a-play", "plugin-message"), new byte[] { 124, 24, 62, 72, 78, 32, 31, 63, 42, 64, 1, 32, 62 }
        ));
    }

    @Test
    public void testGameEvent() {
        ServerPacketTestUtil.testPacket(new ServerGameEventPlayPacket(new GameEvent.WinGame(true)));
    }

    @Test
    public void testSystemMessage() {
        ServerPacketTestUtil.testPacket(new ServerSystemMessagePlayPacket(
                Component.text("A system message!", NamedTextColor.YELLOW, TextDecoration.UNDERLINED), false
        ));
    }

    @Test
    public void testActionBar() {
        ServerPacketTestUtil.testPacket(new ServerActionBarPlayPacket(
                Component.text("An action bar message", NamedTextColor.DARK_PURPLE, TextDecoration.UNDERLINED))
        );
    }

    @Test
    public void testPlayerListHeaderAndFooter() {
        ServerPacketTestUtil.testPacket(new ServerPlayerListHeaderAndFooterPlayPacket(
                Component.text("A header", NamedTextColor.GREEN), Component.text("And a footer!")
        ));
    }

    @Test
    public void testCenterChunk() {
        ServerPacketTestUtil.testPacket(new ServerCenterChunkPlayPacket(5, 6));
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

        ServerPacketTestUtil.testPacket(new ServerChunkAndLightDataPlayPacket(0, 0, CompoundBinaryTag.empty(),
                sections, Set.of(new BlockEntity((byte) 0, (byte) 0, (short) 10, 8, CompoundBinaryTag.empty())),
                filledSet, emptySet, emptySet, filledSet, skyLightData, new byte[0][]));
    }

    @Test
    public void testSynchronizePosition() {
        ServerPacketTestUtil.testPacket(new ServerSynchronizePositionPlayPacket(
                new Position(4, 5, 1, 6f, 12f), Set.of(RelativeFlag.RELATIVE_Z, RelativeFlag.RELATIVE_YAW), 65
        ));
    }
}