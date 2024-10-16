package net.hypejet.jet.server.network.protocol.packet.client.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.configuration.ClientKnownPacksConfigurationPacket;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.protocol.codecs.pack.DataPackNetworkCodec;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents a {@linkplain ClientPacketCodec packet codec}, which reads and writes
 * a {@linkplain ClientKnownPacksConfigurationPacket known dataPacks configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientKnownPacksConfigurationPacket
 * @see ClientPacketCodec
 */
public final class ClientKnownPacksConfigurationPacketCodec
        extends ClientPacketCodec<ClientKnownPacksConfigurationPacket> {
    /**
     * Constructs the {@linkplain ClientKnownPacksConfigurationPacketCodec known dataPacks configuration packet codec}.
     *
     * @since 1.0
     */
    public ClientKnownPacksConfigurationPacketCodec() {
        super(ClientPacketIdentifiers.CONFIGURATION_KNOWN_PACKS, ClientKnownPacksConfigurationPacket.class);
    }

    @Override
    public @NonNull ClientKnownPacksConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ClientKnownPacksConfigurationPacket(DataPackNetworkCodec.collectionCodec().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientKnownPacksConfigurationPacket object) {
        DataPackNetworkCodec.collectionCodec().write(buf, object.dataPacks());
    }

    @Override
    public void handle(@NonNull ClientKnownPacksConfigurationPacket packet,
                       @NonNull SocketPlayerConnection connection) {
        JetPlayer player = Objects.requireNonNull(connection.player(), "The player must not be null");
        player.knownDataPacks(packet.dataPacks());
    }
}
