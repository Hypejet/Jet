package net.hypejet.jet.server.network.protocol.packet.server.writer.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.protocol.packet.server.play.ServerGameEventPlayPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.util.gamemode.GameModeUtil;
import net.hypejet.jet.world.event.GameEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerGameEventPlayPacket a game event play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerGameEventPlayPacket
 * @see NetworkWriter
 */
public final class ServerGameEventPlayPacketWriter implements NetworkWriter<ServerGameEventPlayPacket> {

    private static final byte NO_RESPAWN_BLOCK_AVAILABLE = 0;
    private static final byte BEGIN_RAINING = 1;
    private static final byte END_RAINING = 2;
    private static final byte CHANGE_GAME_MODE = 3;
    private static final byte WIN_GAME = 4;
    private static final byte DEMO_EVENT = 5;
    private static final byte ARROW_HIT_PLAYER = 6;
    private static final byte RAIN_LEVEL_CHANGE = 7;
    private static final byte THUNDER_LEVEL_CHANGE = 8;
    private static final byte PLAY_PUFFERFISH_STING_SOUND = 9;
    private static final byte PLAY_ELDER_GUARDIAN_MOB_APPEARANCE = 10;
    private static final byte ENABLE_RESPAWN_SCREEN = 11;
    private static final byte LIMITED_CRAFTING = 12;
    private static final byte START_WAITING_FOR_WORLD_CHUNKS = 13;

    private static final byte DEMO_SHOW_WELCOME_SCREEN = 0;
    private static final byte DEMO_TELL_MOVEMENT_CONTROLS = 101;
    private static final byte DEMO_TELL_JUMP_CONTROL = 102;
    private static final byte DEMO_TELL_INVENTORY_CONTROL = 103;
    private static final byte DEMO_OVER = 104;

    private static final int MIN_RAIN_LEVEL = 0;
    private static final int MAX_RAIN_LEVEL = 1;

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerGameEventPlayPacket object) {
        byte identifier;
        float value = 0;

        switch (object.gameEvent()) {
            case GameEvent.ArrowHitPlayer ignored -> identifier = ARROW_HIT_PLAYER;
            case GameEvent.BeginRaining ignored -> identifier = BEGIN_RAINING;
            case GameEvent.StartWaitingForWorldChunks ignored -> identifier = START_WAITING_FOR_WORLD_CHUNKS;
            case GameEvent.EndRaining ignored -> identifier = END_RAINING;
            case GameEvent.NoRespawnBlockAvailable ignored -> identifier = NO_RESPAWN_BLOCK_AVAILABLE;
            case GameEvent.PlayElderGuardianMobAppearance ignored -> identifier = PLAY_ELDER_GUARDIAN_MOB_APPEARANCE;
            case GameEvent.PlayPufferfishStingSound ignored -> identifier = PLAY_PUFFERFISH_STING_SOUND;

            case GameEvent.ChangeGameMode (Player.@NonNull GameMode gameMode) -> {
                identifier = CHANGE_GAME_MODE;
                value = GameModeUtil.gameModeIdentifier(gameMode);
            }

            case GameEvent.DemoEvent (GameEvent.DemoEvent.@NonNull Event event) -> {
                identifier = DEMO_EVENT;
                value = switch (event) {
                    case WELCOME_TO_DEMO_SCREEN -> DEMO_SHOW_WELCOME_SCREEN;
                    case TELL_MOVEMENT_CONTROLS -> DEMO_TELL_MOVEMENT_CONTROLS;
                    case TELL_JUMP_CONTROL -> DEMO_TELL_JUMP_CONTROL;
                    case TELL_INVENTORY_CONTROL -> DEMO_TELL_INVENTORY_CONTROL;
                    case DEMO_OVER -> DEMO_OVER;
                };
            }

            case GameEvent.EnableLimitedCrafting (boolean enable) -> {
                identifier = LIMITED_CRAFTING;
                value = enable ? 1 : 0;
            }

            case GameEvent.EnableRespawnScreen (boolean enable) -> {
                identifier = ENABLE_RESPAWN_SCREEN;
                value = enable ? 1 : 0;
            }

            case GameEvent.RainLevelChange (float level) -> {
                identifier = RAIN_LEVEL_CHANGE;
                if (level > MAX_RAIN_LEVEL || level < MIN_RAIN_LEVEL)
                    throw new IllegalArgumentException("The rain level is invalid");
                value = level;
            }

            case GameEvent.ThunderLevelChange (float level) -> {
                identifier = THUNDER_LEVEL_CHANGE;
                if (level > MAX_RAIN_LEVEL || level < MIN_RAIN_LEVEL)
                    throw new IllegalArgumentException("The thunder level is invalid");
                value = level;
            }

            case GameEvent.WinGame (boolean rollCredits) -> {
                identifier = WIN_GAME;
                value = rollCredits ? 1 : 0;
            }
        }

        buf.writeByte(identifier);
        buf.writeFloat(value);
    }
}