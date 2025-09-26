package net.hypejet.jet.server.network.codec.packet.client.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.codec.other.UUIDNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.client.login.ClientLoginRequestLoginPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@link NetworkReader a network reader}, which reads {@link ClientLoginRequestLoginPacket a login request
 * login packet}.
 *
 * @since 1.0
 * @see ClientLoginRequestLoginPacket
 * @see NetworkReader
 */
public final class ClientLoginRequestLoginPacketReader implements NetworkReader<ClientLoginRequestLoginPacket> {
    /**
     * An instance of the {@linkplain ClientLoginRequestLoginPacketReader client login request login packet reader}.
     *
     * @since 1.0
     */
    public static final ClientLoginRequestLoginPacketReader INSTANCE = new ClientLoginRequestLoginPacketReader();

    private ClientLoginRequestLoginPacketReader() {}

    @Override
    public @NonNull ClientLoginRequestLoginPacket read(@NonNull ByteBuf buf,
                                                       @NonNull JetRegistryManager registryManager) {
        return new ClientLoginRequestLoginPacket(
                StringNetworkCodec.MAX_16_INSTANCE.read(buf, registryManager),
                UUIDNetworkCodec.INSTANCE.read(buf, registryManager)
        );
    }
}