package net.hypejet.jet.server.network.codec.packet.server.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.key.KeyNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.configuration.ServerFeatureFlagsConfigurationPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerFeatureFlagsConfigurationPacket a feature flags configuration packet}.
 *
 * @since 1.0
 * @see ServerFeatureFlagsConfigurationPacket
 * @see NetworkWriter
 */
public final class ServerFeatureFlagsConfigurationPacketWriter
        implements NetworkWriter<ServerFeatureFlagsConfigurationPacket> {
    /**
     * An instance of the {@linkplain ServerFeatureFlagsConfigurationPacketWriter server feature flags configuration
     * packet writer}.
     *
     * @since 1.0
     */
    public static final ServerFeatureFlagsConfigurationPacketWriter
            INSTANCE = new ServerFeatureFlagsConfigurationPacketWriter();

    private ServerFeatureFlagsConfigurationPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager,
                      @NonNull ServerFeatureFlagsConfigurationPacket object) {
        KeyNetworkCodec.COLLECTION_CODEC.write(buf, registryManager, object.featureFlags());
    }
}