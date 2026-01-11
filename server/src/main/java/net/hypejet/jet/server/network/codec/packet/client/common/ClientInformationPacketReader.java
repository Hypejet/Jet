package net.hypejet.jet.server.network.codec.packet.client.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.game.settings.PlayerSettingsReader;
import net.hypejet.jet.server.network.packet.packets.client.common.ClientInformationPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientInformationPacket a client information packet}.
 *
 * @since 1.0
 * @see ClientInformationPacket
 * @see NetworkReader
 */
public final class ClientInformationPacketReader implements NetworkReader<ClientInformationPacket> {
    /**
     * An instance of the {@linkplain ClientInformationPacketReader client information packet reader}.
     *
     * @since 1.0
     */
    public static final ClientInformationPacketReader INSTANCE = new ClientInformationPacketReader();

    private ClientInformationPacketReader() {}

    @Override
    public @NonNull ClientInformationPacket read(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager) {
        return new ClientInformationPacket(PlayerSettingsReader.INSTANCE.read(buf, registryManager));
    }
}