package net.hypejet.jet.server.network.protocol.packet.client.codec.status;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.event.events.ping.ServerListPingEvent;
import net.hypejet.jet.ping.ServerListPing;
import net.hypejet.jet.protocol.packet.client.status.ClientServerListRequestStatusPacket;
import net.hypejet.jet.protocol.packet.server.status.ServerListResponseStatusPacket;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.configuration.JetServerConfiguration;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientServerListRequestStatusPacket server list request status packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientServerListRequestStatusPacket
 * @see ClientPacketCodec
 */
public final class ClientServerListRequestStatusPacketCodec
        extends ClientPacketCodec<ClientServerListRequestStatusPacket> {
    /**
     * Constructs the {@linkplain ClientServerListRequestStatusPacketCodec server list request status packet codec}.
     *
     * @since 1.0
     */
    public ClientServerListRequestStatusPacketCodec() {
        super(ClientPacketIdentifiers.STATUS_SERVER_LIST_REQUEST, ClientServerListRequestStatusPacket.class);
    }

    @Override
    public @NonNull ClientServerListRequestStatusPacket read(@NonNull ByteBuf buf) {
        return new ClientServerListRequestStatusPacket();
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientServerListRequestStatusPacket object) {
        // NOOP
    }

    @Override
    public void handle(@NonNull ClientServerListRequestStatusPacket packet, @NonNull SocketPlayerConnection connection) {
        JetMinecraftServer server = connection.server();
        JetServerConfiguration configuration = server.configuration();

        ServerListPing ping = new ServerListPing(
                new ServerListPing.Version(server.minecraftVersion(), server.protocolVersion()),
                // TODO: An actual list of players online
                new ServerListPing.Players(configuration.maxPlayers(), 0, List.of()),
                configuration.serverListDescription(),
                server.serverIcon(),
                false, // TODO: An actual property
                false, // TODO: An actual property
                null
        );

        ServerListPingEvent pingEvent = new ServerListPingEvent(connection, ping);
        server.eventNode().call(pingEvent);
        ping = pingEvent.getPing();

        connection.sendPacket(new ServerListResponseStatusPacket(ping));
    }
}
