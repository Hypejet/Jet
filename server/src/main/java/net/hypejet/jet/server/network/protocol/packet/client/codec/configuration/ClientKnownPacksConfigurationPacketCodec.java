package net.hypejet.jet.server.network.protocol.packet.client.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.configuration.ClientKnownPacksConfigurationPacket;
import net.hypejet.jet.server.network.protocol.codecs.pack.DataPackNetworkCodec;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

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
        return new ClientKnownPacksConfigurationPacket(
                NetworkUtil.readCollection(buf, DataPackNetworkCodec.instance())
        );
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientKnownPacksConfigurationPacket object) {
        NetworkUtil.writeCollection(buf, DataPackNetworkCodec.instance(), object.dataPacks());
    }

    @Override
    public void handle(@NonNull ClientKnownPacksConfigurationPacket packet,
                       @NonNull SocketPlayerConnection connection) {
        // TODO
    }
}
