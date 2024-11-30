package net.hypejet.jet.server.network.packet.client.reader.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.network.packet.client.configuration.ClientKnownPacksConfigurationPacket;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.game.pack.PackInfoNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientKnownPacksConfigurationPacket a known packs configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientKnownPacksConfigurationPacket
 * @see NetworkReader
 */
public final class ClientKnownPacksConfigurationPacketReader
        implements NetworkReader<ClientKnownPacksConfigurationPacket> {
    @Override
    public @NonNull ClientKnownPacksConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ClientKnownPacksConfigurationPacket(PackInfoNetworkCodec.COLLECTION_CODEC.read(buf));
    }
}
