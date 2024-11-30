package net.hypejet.jet.server.network.packet.server.writer.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.network.packet.server.configuration.ServerKnownPacksConfigurationPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.pack.PackInfoNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerKnownPacksConfigurationPacket a known packs configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerKnownPacksConfigurationPacket
 * @see NetworkWriter
 */
public final class ServerKnownPacksConfigurationPacketWriter
        implements NetworkWriter<ServerKnownPacksConfigurationPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerKnownPacksConfigurationPacket object) {
        PackInfoNetworkCodec.COLLECTION_CODEC.write(buf, object.featurePacks());
    }
}