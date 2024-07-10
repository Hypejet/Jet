package net.hypejet.jet.server.network.protocol.packet.server.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.play.ServerPlayerListHeaderAndFooterPlayPacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ServerPlayerListHeaderAndFooterPlayPacket player list header and footer play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPlayerListHeaderAndFooterPlayPacket
 * @see PacketCodec
 */
public final class ServerPlayerListHeaderAndFooterPlayPacketCodec
        extends PacketCodec<ServerPlayerListHeaderAndFooterPlayPacket> {
    /**
     * Constructs the {@linkplain ServerPlayerListHeaderAndFooterPlayPacket player list header and footer play packet
     * codec}.
     *
     * @since 1.0
     */
    public ServerPlayerListHeaderAndFooterPlayPacketCodec() {
        super(ServerPacketIdentifiers.PLAY_PLAYER_LIST_HEADER_AND_FOOTER,
                ServerPlayerListHeaderAndFooterPlayPacket.class);
    }

    @Override
    public @NonNull ServerPlayerListHeaderAndFooterPlayPacket read(@NonNull ByteBuf buf) {
        return new ServerPlayerListHeaderAndFooterPlayPacket(NetworkUtil.readComponent(buf),
                NetworkUtil.readComponent(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerPlayerListHeaderAndFooterPlayPacket object) {
        NetworkUtil.writeComponent(buf, object.headerText());
        NetworkUtil.writeComponent(buf, object.footerText());
    }
}