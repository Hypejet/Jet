package net.hypejet.jet.server.network.codec.packet.server.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.registry.feature.KnownPackNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.configuration.ServerKnownPacksConfigurationPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerKnownPacksConfigurationPacket a known packs configuration packet}.
 *
 * @since 1.0
 * @see ServerKnownPacksConfigurationPacket
 * @see NetworkWriter
 */
public final class ServerKnownPacksConfigurationPacketWriter
        implements NetworkWriter<ServerKnownPacksConfigurationPacket> {
    /**
     * An instance of the {@linkplain ServerKnownPacksConfigurationPacketWriter server known packs configuration packet
     * writer}.
     *
     * @since 1.0
     */
    public static final ServerKnownPacksConfigurationPacketWriter
            INSTANCE = new ServerKnownPacksConfigurationPacketWriter();

    private ServerKnownPacksConfigurationPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager,
                      @NonNull ServerKnownPacksConfigurationPacket object) {
        KnownPackNetworkCodec.COLLECTION_CODEC.write(buf, registryManager, object.knownPacks());
    }
}