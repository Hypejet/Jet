package net.hypejet.jet.server.network.protocol.packet.client.handler.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.login.ClientEncryptionResponseLoginPacket;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.array.bytes.ByteArrayNetworkReader;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.LoginTask;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@link ClientPacketHandler a client packet handler}, which reads and handles
 * {@link ClientEncryptionResponseLoginPacket an encryption response login packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientEncryptionResponseLoginPacket
 * @see ClientPacketHandler
 */
public final class ClientEncryptionResponseLoginPacketHandler
        implements ClientPacketHandler<ClientEncryptionResponseLoginPacket> {
    @Override
    public @NonNull ClientEncryptionResponseLoginPacket read(@NonNull ByteBuf buf) {
        return new ClientEncryptionResponseLoginPacket(
                ByteArrayNetworkReader.INSTANCE.read(buf),
                ByteArrayNetworkReader.INSTANCE.read(buf)
        );
    }

    @Override
    public void handle(@NonNull ClientEncryptionResponseLoginPacket packet, @NonNull SessionTask sessionTask) {
        if (!(sessionTask instanceof LoginTask loginTask))
            throw new IllegalArgumentException("The current session task must be a login task");
        loginTask.handlePacket(packet);
    }
}