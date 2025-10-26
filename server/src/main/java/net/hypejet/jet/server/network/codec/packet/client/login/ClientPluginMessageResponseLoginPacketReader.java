package net.hypejet.jet.server.network.codec.packet.client.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.client.login.ClientPluginMessageResponseLoginPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientPluginMessageResponseLoginPacket a plugin message response packet}.
 *
 * @since 1.0
 * @see ClientPluginMessageResponseLoginPacket
 * @see NetworkReader
 */
public final class ClientPluginMessageResponseLoginPacketReader
        implements NetworkReader<ClientPluginMessageResponseLoginPacket> {
    /**
     * An instance of the {@linkplain ClientPluginMessageResponseLoginPacketReader client plugin message response login
     * packet reader}.
     *
     * @since 1.0
     */
    public static final ClientPluginMessageResponseLoginPacketReader
            INSTANCE = new ClientPluginMessageResponseLoginPacketReader();

    private ClientPluginMessageResponseLoginPacketReader() {}

    @Override
    public @NonNull ClientPluginMessageResponseLoginPacket read(@NonNull ByteBuf buf,
                                                                @NonNull JetRegistryManager registryManager) {
        return new ClientPluginMessageResponseLoginPacket(
                VarIntNetworkCodec.INSTANCE.read(buf, registryManager),
                buf.readBoolean(),
                NetworkUtil.readRemainingBytes(buf)
        );
    }
}