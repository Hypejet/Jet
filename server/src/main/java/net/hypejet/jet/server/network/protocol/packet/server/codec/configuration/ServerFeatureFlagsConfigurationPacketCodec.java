package net.hypejet.jet.server.network.protocol.packet.server.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerFeatureFlagsConfigurationPacket;
import net.hypejet.jet.server.network.protocol.codecs.other.IdentifierNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ServerFeatureFlagsConfigurationPacket feature flags configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerFeatureFlagsConfigurationPacket
 * @see PacketCodec
 */
public final class ServerFeatureFlagsConfigurationPacketCodec
        extends PacketCodec<ServerFeatureFlagsConfigurationPacket> {
    /**
     * Constructs the {@linkplain ServerFeatureFlagsConfigurationPacketCodec feature flags configuration packet codec}.
     *
     * @since 1.0
     */
    public ServerFeatureFlagsConfigurationPacketCodec() {
        super(ServerPacketIdentifiers.CONFIGURATION_FEATURE_FLAGS, ServerFeatureFlagsConfigurationPacket.class);
    }

    @Override
    public @NonNull ServerFeatureFlagsConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ServerFeatureFlagsConfigurationPacket(IdentifierNetworkCodec.collectionCodec().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerFeatureFlagsConfigurationPacket object) {
        IdentifierNetworkCodec.collectionCodec().write(buf, object.featureFlags());
    }
}