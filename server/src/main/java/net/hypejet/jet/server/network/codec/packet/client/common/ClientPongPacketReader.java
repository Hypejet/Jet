package net.hypejet.jet.server.network.codec.packet.client.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.packet.packets.client.common.ClientPongPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientPongPacket a client pong packet}.
 *
 * @since 1.0
 * @see ClientPongPacket
 * @see NetworkReader
 */
public final class ClientPongPacketReader implements NetworkReader<ClientPongPacket> {
    /**
     * An instance of the {@linkplain ClientPongPacketReader client pong packet reader}.
     *
     * @since 1.0
     */
    public static final ClientPongPacketReader INSTANCE = new ClientPongPacketReader();

    private ClientPongPacketReader() {}

    @Override
    public @NonNull ClientPongPacket read(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager) {
        return new ClientPongPacket(buf.readInt());
    }
}