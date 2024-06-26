package net.hypejet.jet.server.network.protocol.packet.client.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.configuration.ClientPongConfigurationPacket;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientPongConfigurationPacket pong configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPongConfigurationPacket
 * @see ClientPacketCodec
 */
public final class ClientPongConfigurationPacketCodec
        extends ClientPacketCodec<ClientPongConfigurationPacket> {
    /**
     * Constructs a {@linkplain ClientPongConfigurationPacketCodec pong configuration packet codec}.
     *
     * @since 1.0
     */
    public ClientPongConfigurationPacketCodec() {
        super(ClientPacketIdentifiers.CONFIGURATION_PONG, ClientPongConfigurationPacket.class);
    }

    @Override
    public @NonNull ClientPongConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ClientPongConfigurationPacket(buf.readInt());
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientPongConfigurationPacket object) {
        buf.writeInt(object.pingIdentifier());
    }

    @Override
    public void handle(@NonNull ClientPongConfigurationPacket packet, @NonNull SocketPlayerConnection connection) {
        JetPlayer player = Objects.requireNonNull(connection.player(), "The player must not be null");
        player.handlePong(packet.pingIdentifier());
    }
}