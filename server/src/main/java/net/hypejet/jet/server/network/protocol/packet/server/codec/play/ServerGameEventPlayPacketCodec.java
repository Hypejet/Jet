package net.hypejet.jet.server.network.protocol.packet.server.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.protocol.packet.server.play.ServerGameEventPlayPacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.server.util.gamemode.GameModeUtil;
import net.hypejet.jet.world.GameEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes a {@linkplain ServerGameEventPlayPacket
 * game event play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerGameEventPlayPacket
 * @see PacketCodec
 */
public final class ServerGameEventPlayPacketCodec extends PacketCodec<ServerGameEventPlayPacket> {

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

    /**
     * Constructs the {@linkplain ServerGameEventPlayPacketCodec game event play packet codec}.
     *
     * @since 1.0
     */
    public ServerGameEventPlayPacketCodec() {
        super(ServerPacketIdentifiers.PLAY_GAME_EVENT, ServerGameEventPlayPacket.class);
    }

    @Override
    public @NonNull ServerGameEventPlayPacket read(@NonNull ByteBuf buf) {
        short identifier = buf.readUnsignedByte();
        float value = buf.readFloat();

        GameEvent gameEvent = switch (identifier) {
            case ARROW_HIT_PLAYER -> GameEvent.arrowHitPlayer();
            case BEGIN_RAINING -> GameEvent.beginRaining();
            case START_WAITING_FOR_WORLD_CHUNKS -> GameEvent.waitForWorldChunks();
            case END_RAINING -> GameEvent.endRaining();
            case NO_RESPAWN_BLOCK_AVAILABLE -> GameEvent.noRespawnBlockAvailable();
            case PLAY_ELDER_GUARDIAN_MOB_APPEARANCE -> GameEvent.elderGuardianMobAppearance();
            case PLAY_PUFFERFISH_STING_SOUND -> GameEvent.pufferfishStringSound();

            case CHANGE_GAME_MODE -> new GameEvent.ChangeGameMode(
                    Objects.requireNonNull(GameModeUtil.gameMode((byte) identifier), "The game mode must not be null")
            );

            case DEMO_EVENT -> {
                byte castDemoEvent = (byte) value;

                GameEvent.DemoEvent.Event event = switch (castDemoEvent) {
                    case DEMO_SHOW_WELCOME_SCREEN -> GameEvent.DemoEvent.Event.WELCOME_TO_DEMO_SCREEN;
                    case DEMO_TELL_MOVEMENT_CONTROLS -> GameEvent.DemoEvent.Event.TELL_MOVEMENT_CONTROLS;
                    case DEMO_TELL_JUMP_CONTROL -> GameEvent.DemoEvent.Event.TELL_JUMP_CONTROL;
                    case DEMO_TELL_INVENTORY_CONTROL -> GameEvent.DemoEvent.Event.TELL_INVENTORY_CONTROL;
                    case DEMO_OVER -> GameEvent.DemoEvent.Event.DEMO_OVER;
                    default -> throw new IllegalStateException("Unknown demo event: " + castDemoEvent);
                };

                yield new GameEvent.DemoEvent(event);
            }

            case RAIN_LEVEL_CHANGE -> {
                if (value > MAX_RAIN_LEVEL || value < MIN_RAIN_LEVEL)
                    throw new IllegalArgumentException("Invalid rain level");
                yield new GameEvent.RainLevelChange(value);
            }

            case THUNDER_LEVEL_CHANGE -> {
                if (value > MAX_RAIN_LEVEL || value < MIN_RAIN_LEVEL)
                    throw new IllegalArgumentException("Invalid thunder level");
                yield new GameEvent.ThunderLevelChange(value);
            }

            case LIMITED_CRAFTING -> new GameEvent.EnableLimitedCrafting(booleanFromFloat(value));
            case ENABLE_RESPAWN_SCREEN -> new GameEvent.EnableRespawnScreen(booleanFromFloat(value));
            case WIN_GAME -> new GameEvent.WinGame(booleanFromFloat(value));

            default -> throw new IllegalStateException("Unknown game event with identifier of: " + identifier);
        };

        return new ServerGameEventPlayPacket(gameEvent);
    }

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
                value = rollCredits ? 0 : 1;
            }
        }

        buf.writeByte(identifier);
        buf.writeFloat(value);
    }

    private static boolean booleanFromFloat(float value) {
        if (value == 1) return true;
        if (value == 0) return false;
        throw new IllegalArgumentException(value + " cannot be represented as a boolean");
    }
}