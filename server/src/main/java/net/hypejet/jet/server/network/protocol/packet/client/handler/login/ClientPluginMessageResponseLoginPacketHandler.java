package net.hypejet.jet.server.network.protocol.packet.client.handler.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.login.ClientPluginMessageResponseLoginPacket;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.LoginTask;
import net.hypejet.jet.server.network.session.task.SessionTask;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientPluginMessageResponseLoginPacket a plugin message response packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPluginMessageResponseLoginPacket
 * @see ClientPacketHandler
 */
public final class ClientPluginMessageResponseLoginPacketHandler
        implements ClientPacketHandler<ClientPluginMessageResponseLoginPacket> {
    @Override
    public @NonNull ClientPluginMessageResponseLoginPacket read(@NonNull ByteBuf buf) {
        return new ClientPluginMessageResponseLoginPacket(
                VarIntNetworkCodec.INSTANCE.read(buf),
                buf.readBoolean(),
                NetworkUtil.readRemainingBytes(buf)
        );
    }

    @Override
    public void handle(@NonNull ClientPluginMessageResponseLoginPacket packet,
                       @NonNull SessionTask sessionTask) {
        if (!(sessionTask instanceof LoginTask loginTask))
            throw new IllegalArgumentException("The current session task must be a login task");
        // TODO: Call an event?
        loginTask.handlePacket(packet);
    }
}