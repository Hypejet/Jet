package net.hypejet.jet.server.network.packet.server.writer.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.network.packet.server.configuration.ServerFeatureFlagsConfigurationPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.key.PackedKeyNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerFeatureFlagsConfigurationPacket a feature flags configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerFeatureFlagsConfigurationPacket
 * @see NetworkWriter
 */
public final class ServerFeatureFlagsConfigurationPacketWriter
        implements NetworkWriter<ServerFeatureFlagsConfigurationPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerFeatureFlagsConfigurationPacket object) {
        PackedKeyNetworkCodec.COLLECTION_CODEC.write(buf, object.featureFlags());
    }
}