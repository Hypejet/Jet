package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.player.spawn.PlayerSpawnInfoNetworkWriter;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerRespawnPlayPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}
 * of {@linkplain ServerRespawnPlayPacket server respawn play packet}.
 *
 * @since 1.0
 * @see ServerRespawnPlayPacket
 * @see NetworkWriter
 */
public final class ServerRespawnPlayPacketWriter implements NetworkWriter<ServerRespawnPlayPacket> {

    /**
     * An instance of the {@linkplain ServerRespawnPlayPacketWriter server respawn play packet writer}.
     *
     * @since 1.0
     */
    public static final ServerRespawnPlayPacketWriter INSTANCE = new ServerRespawnPlayPacketWriter();

    private static final byte KEEP_ATTRIBUTES_BIT_MASK = 0x01;
    private static final byte KEEP_METADATA_BIT_MASK = 0x02;

    private ServerRespawnPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf,
                      @NonNull JetRegistryManager registryManager,
                      @NonNull ServerRespawnPlayPacket object) {
        byte packedDataToKeep = 0;

        if (object.keepAttributes())
            packedDataToKeep |= KEEP_ATTRIBUTES_BIT_MASK;
        if (object.keepMetadata())
            packedDataToKeep |= KEEP_METADATA_BIT_MASK;

        PlayerSpawnInfoNetworkWriter.INSTANCE.write(buf, registryManager, object.spawnInfo());
        buf.writeByte(packedDataToKeep);
    }
}