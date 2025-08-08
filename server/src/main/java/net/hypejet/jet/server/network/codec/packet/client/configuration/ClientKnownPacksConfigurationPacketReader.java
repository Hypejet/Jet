package net.hypejet.jet.server.network.codec.packet.client.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.game.registry.feature.KnownPackNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.client.configuration.ClientKnownPacksConfigurationPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientKnownPacksConfigurationPacket a known packs configuration packet}.
 *
 * @since 1.0
 * @see ClientKnownPacksConfigurationPacket
 * @see NetworkReader
 */
public final class ClientKnownPacksConfigurationPacketReader
        implements NetworkReader<ClientKnownPacksConfigurationPacket> {

    /**
     * An instance of the {@linkplain ClientKnownPacksConfigurationPacketReader client known packs configuration packet
     * reader}.
     *
     * @since 1.0
     */
    public static final ClientKnownPacksConfigurationPacketReader
            INSTANCE = new ClientKnownPacksConfigurationPacketReader();

    private ClientKnownPacksConfigurationPacketReader() {}

    @Override
    public @NonNull ClientKnownPacksConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ClientKnownPacksConfigurationPacket(List.copyOf(KnownPackNetworkCodec.COLLECTION_CODEC.read(buf)));
    }
}
