package net.hypejet.jet.server.network.protocol.packet.client.handler.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.pack.ResourcePackResult;
import net.hypejet.jet.protocol.packet.client.configuration.ClientResourcePackResponseConfigurationPacket;
import net.hypejet.jet.server.network.protocol.codecs.enums.EnumVarIntCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.UUIDNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.ConfigurationTask;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacketHandler client packet handler}, which reads and handles
 * {@linkplain ClientResourcePackResponseConfigurationPacket a resource pack response configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientResourcePackResponseConfigurationPacket
 * @see ClientPacketHandler
 */
public final class ClientResourcePackResponseConfigurationPacketHandler
        implements ClientPacketHandler<ClientResourcePackResponseConfigurationPacket> {

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

    @Override
    public @NonNull ClientResourcePackResponseConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ClientResourcePackResponseConfigurationPacket(
                UUIDNetworkCodec.instance().read(buf),
                RESULT_CODEC.read(buf)
        );
    }

    @Override
    public void handle(@NonNull ClientResourcePackResponseConfigurationPacket packet,
                       @NonNull SessionTask sessionTask) {
        if (!(sessionTask instanceof ConfigurationTask configurationTask))
            throw new IllegalArgumentException("The session task must be a configuration task");
        configurationTask.player().handleResourcePackResponse(packet.uniqueId(), packet.result());
    }
}