package net.hypejet.jet.server.network.protocol.packet.server.writer.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.play.ServerJoinGamePlayPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.game.world.coordinate.BlockPositionNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.game.key.PackedKeyNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.util.gamemode.GameModeUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerJoinGamePlayPacket a join game play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerJoinGamePlayPacket
 * @see NetworkWriter
 */
public final class ServerJoinGamePlayPacketWriter implements NetworkWriter<ServerJoinGamePlayPacket> {

    private static final int MAX_VIEW_DISTANCE = 32;
    private static final int MIN_VIEW_DISTANCE = 2;

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerJoinGamePlayPacket object) {
        buf.writeInt(object.entityId());
        buf.writeBoolean(object.hardcore());

        PackedKeyNetworkCodec.COLLECTION_CODEC.write(buf, object.dimensions());
        VarIntNetworkCodec.INSTANCE.write(buf, object.maxPlayers());

        int viewDistance = object.viewDistance();

        if (viewDistance < MIN_VIEW_DISTANCE || viewDistance > MAX_VIEW_DISTANCE)
            throw new IllegalArgumentException("Invalid view distance: " + viewDistance);

        VarIntNetworkCodec.INSTANCE.write(buf, object.viewDistance());
        VarIntNetworkCodec.INSTANCE.write(buf, object.simulationDistance());

        buf.writeBoolean(object.reducedDebugInfo());
        buf.writeBoolean(object.enableRespawnScreen());
        buf.writeBoolean(object.limitedCrafting());

        VarIntNetworkCodec.INSTANCE.write(buf, object.dimensionType());
        PackedKeyNetworkCodec.INSTANCE.write(buf, object.dimensionName());

        buf.writeLong(object.hashedSeed());

        buf.writeByte(GameModeUtil.gameModeIdentifier(object.gameMode()));
        buf.writeByte(GameModeUtil.gameModeIdentifier(object.previousGameMode()));

        buf.writeBoolean(object.debug());
        buf.writeBoolean(object.flat());

        ServerJoinGamePlayPacket.DeathLocation deathLocation = object.deathLocation();
        buf.writeBoolean(deathLocation != null);

        if (deathLocation != null) {
            PackedKeyNetworkCodec.INSTANCE.write(buf, deathLocation.deathDimensionName());
            BlockPositionNetworkCodec.INSTANCE.write(buf, deathLocation.deathPosition());
        }

        VarIntNetworkCodec.INSTANCE.write(buf, object.portalCooldown());
        VarIntNetworkCodec.INSTANCE.write(buf, object.seaLevel());
        buf.writeBoolean(object.enforcesSecureChat());
    }
}