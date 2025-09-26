package net.hypejet.jet.server.network.codec.packet.client.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.game.key.KeyNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.client.common.ClientPluginMessagePacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientPluginMessagePacket a client plugin message packet}.
 *
 * @since 1.0
 * @see ClientPluginMessagePacket
 * @see NetworkReader
 */
public final class ClientPluginMessagePacketReader implements NetworkReader<ClientPluginMessagePacket> {
    /**
     * An instance of the {@linkplain ClientPluginMessagePacketReader client plugin message packet reader}.
     *
     * @since 1.0
     */
    public static final ClientPluginMessagePacketReader INSTANCE = new ClientPluginMessagePacketReader();

    private ClientPluginMessagePacketReader() {}

    @Override
    public @NonNull ClientPluginMessagePacket read(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager) {
        return new ClientPluginMessagePacket(
                KeyNetworkCodec.INSTANCE.read(buf, registryManager),
                NetworkUtil.readRemainingBytes(buf)
        );
    }
}