package net.hypejet.jet.server.network.codec.packet.client.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.packet.packets.client.common.ClientInformationPacket;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.game.settings.PlayerSettingsReader;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads {@linkplain ClientInformationPacket a client
 * information packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientInformationPacket
 * @see NetworkReader
 */
public final class ClientInformationPacketReader implements NetworkReader<ClientInformationPacket> {
    @Override
    public @NonNull ClientInformationPacket read(@NonNull ByteBuf buf) {
        return new ClientInformationPacket(PlayerSettingsReader.INSTANCE.read(buf));
    }
}