package net.hypejet.jet.server.network.codec.packet.server.play;

import org.checkerframework.checker.nullness.qual.NonNull;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerEntityAnimationPlayPacket;

public final class ServerEntityAnimationPlayPacketWriter implements NetworkWriter<ServerEntityAnimationPlayPacket> {
    
    public static final ServerEntityAnimationPlayPacketWriter INSTANCE = new ServerEntityAnimationPlayPacketWriter();

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerEntityAnimationPlayPacket object) {
        VarIntNetworkCodec.INSTANCE.write(buf, object.entityId());
        VarIntNetworkCodec.INSTANCE.write(buf, object.animation());
    }
}
