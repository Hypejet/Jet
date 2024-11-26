package net.hypejet.jet.server.network.protocol.packet.client.handler.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.data.codecs.util.mapper.Mapper;
import net.hypejet.jet.pack.ResourcePackResult;
import net.hypejet.jet.protocol.packet.client.configuration.ClientResourcePackResponseConfigurationPacket;
import net.hypejet.jet.server.network.protocol.codecs.mapper.MapperNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.UUIDNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.ConfigurationTask;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientResourcePackResponseConfigurationPacket a resource pack response configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientResourcePackResponseConfigurationPacket
 * @see ClientPacketHandler
 */
public final class ClientResourcePackResponseConfigurationPacketHandler
        implements ClientPacketHandler<ClientResourcePackResponseConfigurationPacket> {

    private static final MapperNetworkCodec<ResourcePackResult, Integer> RESULT_CODEC = new MapperNetworkCodec<>(
            Mapper.builder(ResourcePackResult.class, int.class)
                    .register(ResourcePackResult.SUCCESS, 0)
                    .register(ResourcePackResult.DECLINED, 1)
                    .register(ResourcePackResult.FAILED_TO_DOWNLOAD, 2)
                    .register(ResourcePackResult.ACCEPTED, 3)
                    .register(ResourcePackResult.DOWNLOADED, 4)
                    .register(ResourcePackResult.INVALID_URL, 5)
                    .register(ResourcePackResult.FAILED_TO_RELOAD, 6)
                    .register(ResourcePackResult.DISCARDED, 7)
                    .build(),
            VarIntNetworkCodec.INSTANCE
    );

    @Override
    public @NonNull ClientResourcePackResponseConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ClientResourcePackResponseConfigurationPacket(
                UUIDNetworkCodec.INSTANCE.read(buf),
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