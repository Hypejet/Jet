package net.hypejet.jet.server.network.protocol;

import net.hypejet.jet.protocol.ProtocolState;
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

/**
 * Represents a registry of a {@linkplain PacketWriter packet writers}.
 *
 * @since 1.0
 * @author Codestech
 */
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
     * @param state a protocol state during which the packet is written
     * @throws IllegalStateException if an eligible packet writer for the packet could not be found or the protocol
     *                               state is not supported by protocol state of an eligible packet writer
     * @since 1.0
     */
    public static void write(@NonNull NetworkBuffer buffer, @NonNull ServerPacket packet, @NonNull ProtocolState state) {
        for (PacketWriter<?> writer : packetWriters)
            if (write(writer, buffer, packet, state)) return;
        String packetName = packet.getClass().getSimpleName();
        throw new IllegalStateException("Could not find an eligible packet writer for " + packetName);
    }

    /**
     * Tries to write a {@linkplain P packet} with the specified {@linkplain PacketWriter packet writer}.
     *
     * @param packetWriter the packet writer
     * @param buffer a {@linkplain NetworkBuffer network buffer} to which the packet should be written
     * @param packet a packet to write
     * @param state a protocol state during which the packet is written
     * @return true if the packet writer was eligible for the packet, false otherwise
     * @param <P> a type of the packet
     * @throws IllegalStateException if the packet writer is eligible, but the protocol state does not support
     *                               the protocol state of writer
     * @since 1.0
     */
    private static <P extends ServerPacket> boolean write(@NonNull PacketWriter<P> packetWriter,
                                                          @NonNull NetworkBuffer buffer, @NonNull ServerPacket packet,
                                                          @NonNull ProtocolState state) {
        Class<P> writerPacketClass = packetWriter.getPacketClass();
        Class<? extends ServerPacket> packetClass = packet.getClass();

        boolean result = writerPacketClass.isAssignableFrom(packetClass);

        if (result) {
            if (state != packetWriter.getState()) {
                throw new IllegalArgumentException("Current protocol state does not support a packet with name of \""
                        + packetClass.getSimpleName() + "\"");
            }

            buffer.writeVarInt(packetWriter.getPacketId());
            packetWriter.write(writerPacketClass.cast(packet), buffer);
        }

        return result;
    }
}