package net.hypejet.jet.server.network.protocol.packet.client.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.pack.ResourcePackResult;
import net.hypejet.jet.protocol.packet.client.configuration.ClientResourcePackResponseConfigurationPacket;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.protocol.codecs.enums.EnumVarIntCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.UUIDNetworkCodec;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientResourcePackResponseConfigurationPacket resource pack response configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientResourcePackResponseConfigurationPacket
 * @see ClientPacketCodec
 */
public final class ClientResourcePackResponseConfigurationPacketCodec
        extends ClientPacketCodec<ClientResourcePackResponseConfigurationPacket> {

    private static final EnumVarIntCodec<ResourcePackResult> RESULT_CODEC = EnumVarIntCodec
            .builder(ResourcePackResult.class)
            .add(ResourcePackResult.SUCCESS, 0)
            .add(ResourcePackResult.DECLINED, 1)
            .add(ResourcePackResult.FAILED_TO_DOWNLOAD, 2)
            .add(ResourcePackResult.ACCEPTED, 3)
            .add(ResourcePackResult.DOWNLOADED, 4)
            .add(ResourcePackResult.INVALID_URL, 5)
            .add(ResourcePackResult.FAILED_TO_RELOAD, 6)
            .add(ResourcePackResult.DISCARDED, 7)
            .build();
    /**
     * Constructs a {@linkplain ClientResourcePackResponseConfigurationPacketCodec resource pack response configuration
     * packet codec}.
     *
     * @since 1.0
     */
    public ClientResourcePackResponseConfigurationPacketCodec() {
        super(ClientPacketIdentifiers.CONFIGURATION_RESOURCE_PACK_RESPONSE,
                ClientResourcePackResponseConfigurationPacket.class);
    }

    @Override
    public @NonNull ClientResourcePackResponseConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ClientResourcePackResponseConfigurationPacket(UUIDNetworkCodec.instance().read(buf),
                RESULT_CODEC.read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientResourcePackResponseConfigurationPacket object) {
        UUIDNetworkCodec.instance().write(buf, object.uniqueId());
        RESULT_CODEC.write(buf, object.result());
    }

    @Override
    public void handle(@NonNull ClientResourcePackResponseConfigurationPacket packet,
                       @NonNull SocketPlayerConnection connection) {
        JetPlayer player = Objects.requireNonNull(connection.player(), "Player cannot be null");
        player.handleResourcePackResponse(packet.uniqueId(), packet.result());
    }
}