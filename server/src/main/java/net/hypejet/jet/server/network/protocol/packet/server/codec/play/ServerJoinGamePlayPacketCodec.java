package net.hypejet.jet.server.network.protocol.packet.server.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.coordinate.BlockPosition;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.protocol.packet.server.play.ServerJoinGamePlayPacket;
import net.hypejet.jet.server.network.protocol.codecs.identifier.IdentifierNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.position.BlockPositionNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import net.hypejet.jet.server.util.gamemode.GameModeUtil;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes a {@linkplain ServerJoinGamePlayPacket
 * join game play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerJoinGamePlayPacket
 * @see PacketCodec
 */
public final class ServerJoinGamePlayPacketCodec extends PacketCodec<ServerJoinGamePlayPacket> {

    private static final int MAX_VIEW_DISTANCE = 32;
    private static final int MIN_VIEW_DISTANCE = 2;

    /**
     * Constructs the {@linkplain ServerJoinGamePlayPacket join game play packet}.
     *
     * @since 1.0
     */
    public ServerJoinGamePlayPacketCodec() {
        super(ServerPacketIdentifiers.PLAY_JOIN_GAME, ServerJoinGamePlayPacket.class);
    }

    @Override
    public @NonNull ServerJoinGamePlayPacket read(@NonNull ByteBuf buf) {
        int entityId = buf.readInt();
        boolean hardcore = buf.readBoolean();

        Collection<Key> dimensions = NetworkUtil.readCollection(buf, IdentifierNetworkCodec.instance());

        int maxPlayers = NetworkUtil.readVarInt(buf);
        int viewDistance = NetworkUtil.readVarInt(buf);

        if (viewDistance < MIN_VIEW_DISTANCE || viewDistance > MAX_VIEW_DISTANCE)
            throw new IllegalArgumentException("Invalid view distance: " + viewDistance);

        int simulationDistance = NetworkUtil.readVarInt(buf);

        boolean reducedDebugInfo = buf.readBoolean();
        boolean enableRespawnScreen = buf.readBoolean();
        boolean limitedCrafting = buf.readBoolean();

        int dimensionType = NetworkUtil.readVarInt(buf);
        Key dimensionName = IdentifierNetworkCodec.instance().read(buf);

        long hashedSeed = buf.readLong();

        Player.GameMode gameMode = Objects.requireNonNull(GameModeUtil.gameMode(buf.readByte()),
                "The game mode must not be null");
        Player.GameMode previousGameMode = GameModeUtil.gameMode(buf.readByte());

        boolean debug = buf.readBoolean();
        boolean flat = buf.readBoolean();

        ServerJoinGamePlayPacket.DeathLocation deathLocation;

        if (buf.readBoolean()) {
            Key deathDimensionName = IdentifierNetworkCodec.instance().read(buf);
            BlockPosition blockPosition = BlockPositionNetworkCodec.instance().read(buf);
            deathLocation = new ServerJoinGamePlayPacket.DeathLocation(deathDimensionName, blockPosition);
        } else {
            deathLocation = null;
        }

        int portalCooldown = NetworkUtil.readVarInt(buf);
        boolean enforcesSecureChat = buf.readBoolean();

        return new ServerJoinGamePlayPacket(entityId, hardcore, dimensions, maxPlayers, viewDistance,
                simulationDistance, reducedDebugInfo, enableRespawnScreen, limitedCrafting, dimensionType,
                dimensionName, hashedSeed, gameMode, previousGameMode, debug, flat, deathLocation,
                portalCooldown, enforcesSecureChat);
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerJoinGamePlayPacket object) {
        buf.writeInt(object.entityId());
        buf.writeBoolean(object.hardcore());

        NetworkUtil.writeCollection(buf, IdentifierNetworkCodec.instance(), object.dimensions());
        NetworkUtil.writeVarInt(buf, object.maxPlayers());

        int viewDistance = object.viewDistance();

        if (viewDistance < MIN_VIEW_DISTANCE || viewDistance > MAX_VIEW_DISTANCE)
            throw new IllegalArgumentException("Invalid view distance: " + viewDistance);

        NetworkUtil.writeVarInt(buf, viewDistance);
        NetworkUtil.writeVarInt(buf, object.simulationDistance());

        buf.writeBoolean(object.reducedDebugInfo());
        buf.writeBoolean(object.enableRespawnScreen());
        buf.writeBoolean(object.limitedCrafting());

        NetworkUtil.writeVarInt(buf, object.dimensionType());
        IdentifierNetworkCodec.instance().write(buf, object.dimensionName());

        buf.writeLong(object.hashedSeed());

        buf.writeByte(GameModeUtil.gameModeIdentifier(object.gameMode()));
        buf.writeByte(GameModeUtil.gameModeIdentifier(object.previousGameMode()));

        buf.writeBoolean(object.debug());
        buf.writeBoolean(object.flat());

        ServerJoinGamePlayPacket.DeathLocation deathLocation = object.deathLocation();
        buf.writeBoolean(deathLocation != null);

        if (deathLocation != null) {
            IdentifierNetworkCodec.instance().write(buf, deathLocation.deathDimensionName());
            BlockPositionNetworkCodec.instance().write(buf, deathLocation.deathPosition());
        }

        NetworkUtil.writeVarInt(buf, object.portalCooldown());
        buf.writeBoolean(object.enforcesSecureChat());
    }
}