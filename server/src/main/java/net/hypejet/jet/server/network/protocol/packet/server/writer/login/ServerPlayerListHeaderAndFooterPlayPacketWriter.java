package net.hypejet.jet.server.network.protocol.packet.server.writer.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.play.ServerPlayerListHeaderAndFooterPlayPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.game.component.ComponentNetworkWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerPlayerListHeaderAndFooterPlayPacket a player list header and footer play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPlayerListHeaderAndFooterPlayPacket
 * @see NetworkWriter
 */
public final class ServerPlayerListHeaderAndFooterPlayPacketWriter
        implements NetworkWriter<ServerPlayerListHeaderAndFooterPlayPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerPlayerListHeaderAndFooterPlayPacket object) {
        ComponentNetworkWriter.INSTANCE.write(buf, object.headerText());
        ComponentNetworkWriter.INSTANCE.write(buf, object.footerText());
    }
}