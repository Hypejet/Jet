package net.hypejet.jet.server.network.protocol.packet.client.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.configuration.ClientKnownPacksConfigurationPacket;
import net.hypejet.jet.server.network.protocol.codecs.pack.PackInfoNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.network.session.task.ConfigurationTask;
import net.hypejet.jet.server.network.session.task.SessionTask;
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
        return new ClientKnownPacksConfigurationPacket(PackInfoNetworkCodec.collectionCodec().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientKnownPacksConfigurationPacket object) {
        PackInfoNetworkCodec.collectionCodec().write(buf, object.featurePacks());
    }

    @Override
    public void handle(@NonNull ClientKnownPacksConfigurationPacket packet, @NonNull SessionTask sessionTask) {
        if (!(sessionTask instanceof ConfigurationTask configurationTask))
            throw new IllegalArgumentException("The session task is must be a configuration task");
        configurationTask.handleKnownPacks(packet);
    }
}
