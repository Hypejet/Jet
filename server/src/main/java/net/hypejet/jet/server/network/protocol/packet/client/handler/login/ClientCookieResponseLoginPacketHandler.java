package net.hypejet.jet.server.network.protocol.packet.client.handler.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.login.ClientCookieResponseLoginPacket;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.array.bytes.ByteArrayNetworkReader;
import net.hypejet.jet.server.network.protocol.codecs.game.key.PackedKeyNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.LoginTask;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientCookieResponseLoginPacket a cookie response login packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientCookieResponseLoginPacket
 * @see ClientPacketHandler
 */
public final class ClientCookieResponseLoginPacketHandler
        implements ClientPacketHandler<ClientCookieResponseLoginPacket> {
    @Override
    public @NonNull ClientCookieResponseLoginPacket read(@NonNull ByteBuf buf) {
        return new ClientCookieResponseLoginPacket(
                PackedKeyNetworkCodec.INSTANCE.read(buf),
                buf.readBoolean() ? ByteArrayNetworkReader.INSTANCE.read(buf) : null
        );
    }

    @Override
    public void handle(@NonNull ClientCookieResponseLoginPacket packet, @NonNull SessionTask sessionTask) {
        if (!(sessionTask instanceof LoginTask loginTask))
            throw new IllegalArgumentException("The current session task must be a login task");
        // TODO: Call an event? Replace all login handler task calls with a util?
        loginTask.handlePacket(packet);
    }
}