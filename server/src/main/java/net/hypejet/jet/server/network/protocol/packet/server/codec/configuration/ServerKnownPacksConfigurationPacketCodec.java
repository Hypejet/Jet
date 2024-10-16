package net.hypejet.jet.server.network.protocol.packet.server.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerKnownPacksConfigurationPacket;
import net.hypejet.jet.server.network.protocol.codecs.pack.PackInfoNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ServerKnownPacksConfigurationPacket known packs configuration packet}.
 */
public final class ServerKnownPacksConfigurationPacketCodec extends PacketCodec<ServerKnownPacksConfigurationPacket> {
    /**
     * Constructs the {@linkplain ServerKnownPacksConfigurationPacket known packs configuration packet}.
     *
     * @since 1.0
     */
    public ServerKnownPacksConfigurationPacketCodec() {
        super(ServerPacketIdentifiers.CONFIGURATION_KNOWN_PACKS, ServerKnownPacksConfigurationPacket.class);
    }

    @Override
    public @NonNull ServerKnownPacksConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ServerKnownPacksConfigurationPacket(PackInfoNetworkCodec.collectionCodec().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerKnownPacksConfigurationPacket object) {
        PackInfoNetworkCodec.collectionCodec().write(buf, object.featurePacks());
    }
}