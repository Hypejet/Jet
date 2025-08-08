package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.key.KeyNetworkCodec;
import net.hypejet.jet.server.network.codec.game.player.spawn.PlayerSpawnInfoNetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerJoinGamePlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerJoinGamePlayPacket a join game play packet}.
 *
 * @since 1.0
 * @see ServerJoinGamePlayPacket
 * @see NetworkWriter
 */
public final class ServerJoinGamePlayPacketWriter implements NetworkWriter<ServerJoinGamePlayPacket> {
    /**
     * An instance of the {@linkplain ServerJoinGamePlayPacketWriter server join game play packet writer}.
     *
     * @since 1.0
     */
    public static final ServerJoinGamePlayPacketWriter INSTANCE = new ServerJoinGamePlayPacketWriter();

    private ServerJoinGamePlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerJoinGamePlayPacket object) {
        buf.writeInt(object.entityId());
        buf.writeBoolean(object.hardcore());

        KeyNetworkCodec.COLLECTION_CODEC.write(buf, object.worldKeys());

        VarIntNetworkCodec.INSTANCE.write(buf, object.maximumPlayers());
        VarIntNetworkCodec.INSTANCE.write(buf, object.maximumViewDistance());
        VarIntNetworkCodec.INSTANCE.write(buf, object.simulationDistance());

        buf.writeBoolean(object.reducedDebugInfo());
        buf.writeBoolean(object.enableRespawnScreen());
        buf.writeBoolean(object.showUnlockedRecipesOnly());

        PlayerSpawnInfoNetworkWriter.INSTANCE.write(buf, object.spawnInfo());
        buf.writeBoolean(object.enforcesSecureChat());
    }
}