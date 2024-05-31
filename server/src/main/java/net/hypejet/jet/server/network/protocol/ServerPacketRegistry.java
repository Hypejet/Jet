package net.hypejet.jet.server.network.protocol;

import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.hypejet.jet.server.network.buffer.NetworkBuffer;
import net.hypejet.jet.server.network.protocol.writer.PacketWriter;
import net.hypejet.jet.server.network.protocol.writer.login.ServerCookieRequestPacketWriter;
import net.hypejet.jet.server.network.protocol.writer.login.ServerDisconnectPacketWriter;
import net.hypejet.jet.server.network.protocol.writer.login.ServerEnableCompressionPacketWriter;
import net.hypejet.jet.server.network.protocol.writer.login.ServerEncryptionRequestPacketWriter;
import net.hypejet.jet.server.network.protocol.writer.login.ServerLoginSuccessPacketWriter;
import net.hypejet.jet.server.network.protocol.writer.login.ServerPluginMessageRequestPacketWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Set;

public final class ServerPacketRegistry {

    private static final Collection<PacketWriter<?>> packetWriters = Set.of(
            new ServerCookieRequestPacketWriter(),
            new ServerDisconnectPacketWriter(),
            new ServerEnableCompressionPacketWriter(),
            new ServerEncryptionRequestPacketWriter(),
            new ServerLoginSuccessPacketWriter(),
            new ServerPluginMessageRequestPacketWriter()
    );

    private ServerPacketRegistry() {}

    /**
     * Writes a {@linkplain ServerPacket server packet} to a {@linkplain NetworkBuffer network buffer}.
     *
     * @param buffer the network buffer
     * @param packet the server packet
     * @throws IllegalStateException if an eligible packet writer for the packet could not be found
     * @since 1.0
     */
    public static void write(@NonNull NetworkBuffer buffer, @NonNull ServerPacket packet) {
        for (PacketWriter<?> writer : packetWriters)
            if (write(writer, buffer, packet)) return;
        String packetName = packet.getClass().getSimpleName();
        throw new IllegalStateException("Could not find an eligible packet writer for " + packetName);
    }

    /**
     * Tries to write a {@linkplain P packet} with the specified {@linkplain PacketWriter packet writer}.
     *
     * @param packetWriter the packet writer
     * @param buffer a {@linkplain NetworkBuffer network buffer} to which the packet should be written
     * @param packet a packet to write
     * @return true if the packet writer was eligible for the packet, false otherwise
     * @param <P> a type of the packet
     * @since 1.0
     */
    private static <P extends ServerPacket> boolean write(@NonNull PacketWriter<P> packetWriter,
                                                          @NonNull NetworkBuffer buffer,
                                                          @NonNull ServerPacket packet) {
        Class<P> writerPacketClass = packetWriter.getPacketClass();
        boolean result = writerPacketClass.isAssignableFrom(packet.getClass());

        if (result) {
            buffer.writeVarInt(packetWriter.getPacketId());
            packetWriter.write(writerPacketClass.cast(packet), buffer);
        }

        return result;
    }
}