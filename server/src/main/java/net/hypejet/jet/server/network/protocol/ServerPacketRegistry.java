package net.hypejet.jet.server.network.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerCookieRequestPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerDisconnectPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerEnableCompressionPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerEncryptionRequestPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerLoginSuccessPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerPluginMessageRequestPacket;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.writer.PacketWriter;
import net.hypejet.jet.server.network.protocol.writer.login.ServerCookieRequestPacketWriter;
import net.hypejet.jet.server.network.protocol.writer.login.ServerDisconnectPacketWriter;
import net.hypejet.jet.server.network.protocol.writer.login.ServerEnableCompressionPacketWriter;
import net.hypejet.jet.server.network.protocol.writer.login.ServerEncryptionRequestPacketWriter;
import net.hypejet.jet.server.network.protocol.writer.login.ServerLoginSuccessPacketWriter;
import net.hypejet.jet.server.network.protocol.writer.login.ServerPluginMessageRequestPacketWriter;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a registry of {@linkplain PacketWriter packet writers}, which can read and write
 * {@linkplain ServerPacket server packets}..
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 * @see NetworkCodec
 */
public final class ServerPacketRegistry {

    private static final EnumMap<ProtocolState, Map<Class<? extends ServerPacket>, PacketWriter<?>>> packetWriters;

    static {
        packetWriters = new EnumMap<>(ProtocolState.class);

        Map<Class<? extends ServerPacket>, PacketWriter<?>> login = new HashMap<>();
        login.put(ServerDisconnectPacket.class, new ServerDisconnectPacketWriter());
        login.put(ServerEncryptionRequestPacket.class, new ServerEncryptionRequestPacketWriter());
        login.put(ServerLoginSuccessPacket.class, new ServerLoginSuccessPacketWriter());
        login.put(ServerEnableCompressionPacket.class, new ServerEnableCompressionPacketWriter());
        login.put(ServerPluginMessageRequestPacket.class, new ServerPluginMessageRequestPacketWriter());
        login.put(ServerCookieRequestPacket.class, new ServerCookieRequestPacketWriter());

        packetWriters.put(ProtocolState.LOGIN, login);
    }

    private ServerPacketRegistry() {}

    /**
     * Finds a {@linkplain NetworkCodec network codec}, which can write a specific packet and writes it to
     * a {@linkplain ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @param packet the server packet
     * @param state a protocol state during which the packet is written
     * @throws IllegalStateException if an eligible packet writer for the packet could not be found or the protocol
     *                               state is not supported by protocol state of an eligible packet writer
     * @since 1.0
     */
    public static void write(@NonNull ByteBuf buf, @NonNull ServerPacket packet, @NonNull ProtocolState state) {
        Map<Class<? extends ServerPacket>, PacketWriter<?>> writers = packetWriters.get(state);
        if (writers == null) throw noEligibleWriter(packet);

        PacketWriter<? extends ServerPacket> writer = writers.get(packet.getClass());
        if (writer == null) throw noEligibleWriter(packet);

        write(writer, packet, buf);
    }

    /**
     * Writes a {@linkplain ServerPacket server packet} to a {@linkplain ByteBuf byte buf} using a specified
     * {@linkplain PacketWriter packet writer}.
     *
     * @param writer the packet writer
     * @param packet the server packet
     * @param buf the byte buf
     * @param <P> the type of the packet
     * @since 1.0
     */
    private static <P extends ServerPacket> void write(@NonNull PacketWriter<P> writer, @NonNull ServerPacket packet,
                                                       @NonNull ByteBuf buf) {
        NetworkUtil.writeVarInt(buf, writer.getPacketId());
        writer.write(buf, writer.getPacketClass().cast(packet));
    }

    private static @NonNull RuntimeException noEligibleWriter(@NonNull ServerPacket packet) {
        String packetName = packet.getClass().getSimpleName();
        return new IllegalStateException("Could not find an eligible packet writer for " + packetName);
    }
}
