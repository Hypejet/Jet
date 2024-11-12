package net.hypejet.jet.server.network.protocol.packet.client.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.configuration.ClientCookieResponseConfigurationPacket;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.arrays.ByteArrayNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.identifier.PackedIdentifierNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.network.session.task.ConfigurationTask;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientCookieResponseConfigurationPacket cookie response configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientCookieResponseConfigurationPacket
 * @see ClientPacketCodec
 */
public final class ClientCookieResponseConfigurationPacketCodec
        extends ClientPacketCodec<ClientCookieResponseConfigurationPacket> {
    /**
     * Constructs the {@linkplain ClientCookieResponseConfigurationPacketCodec cookie response configuration packet
     * codec}.
     *
     * @since 1.0
     */
    public ClientCookieResponseConfigurationPacketCodec() {
        super(ClientPacketIdentifiers.CONFIGURATION_COOKIE_RESPONSE, ClientCookieResponseConfigurationPacket.class);
    }

    @Override
    public @NonNull ClientCookieResponseConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ClientCookieResponseConfigurationPacket(
                PackedIdentifierNetworkCodec.instance().read(buf),
                buf.readBoolean() ? ByteArrayNetworkCodec.instance().read(buf) : null
        );
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientCookieResponseConfigurationPacket object) {
        PackedIdentifierNetworkCodec.instance().write(buf, object.identifier());

        byte[] data = object.data();
        buf.writeBoolean(data != null);

        if (data != null) {
            ByteArrayNetworkCodec.instance().write(buf, object.data());
        }
    }

    @Override
    public void handle(@NonNull ClientCookieResponseConfigurationPacket packet, @NonNull SessionTask sessionTask) {
        if (!(sessionTask instanceof ConfigurationTask configurationTask))
            throw new IllegalArgumentException("The session task must be a configuration task");
        configurationTask.player().handleCookieResponse(packet.identifier(), packet.data());
    }
}
