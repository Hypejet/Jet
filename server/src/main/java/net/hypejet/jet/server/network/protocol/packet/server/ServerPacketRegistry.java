package net.hypejet.jet.server.network.protocol.packet.server;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.login.ServerCookieRequestPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.login.ServerDisconnectPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.login.ServerEnableCompressionPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.login.ServerEncryptionRequestPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.login.ServerLoginSuccessPacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.login.ServerPluginMessageRequestPacketCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a registry of {@linkplain PacketCodec packet codecs}, which can read and write {@linkplain ServerPacket
 * server packets}..
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 * @see NetworkCodec
 */
public final class ServerPacketRegistry {

    private static final Map<Class<? extends ServerPacket>, PacketCodec<? extends ServerPacket>> packetCodecs;

    static {
        packetCodecs = new HashMap<>();

        // Login packets
        register(new ServerDisconnectPacketCodec(), new ServerEncryptionRequestPacketCodec(),
                new ServerLoginSuccessPacketCodec(), new ServerEnableCompressionPacketCodec(),
                new ServerPluginMessageRequestPacketCodec(), new ServerCookieRequestPacketCodec());
    }

    private ServerPacketRegistry() {}

    /**
     * Finds a {@linkplain PacketCodec packet codec}, which can write a specific packet and writes it to
     * a {@linkplain ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @param packet the server packet
     * @throws IllegalStateException if an eligible packet codec for the packet could not be found or the protocol
     *                               state is not supported by protocol state of an eligible packet codec
     * @since 1.0
     */
    public static void write(@NonNull ByteBuf buf, @NonNull ServerPacket packet) {
        PacketCodec<? extends ServerPacket> codec = packetCodecs.get(packet.getClass());

        if (codec == null) {
            String packetName = packet.getClass().getSimpleName();
            throw new IllegalStateException("Could not find an eligible server packet codec for " + packetName);
        }

        write(codec, packet, buf);
    }

    /**
     * Writes a {@linkplain ServerPacket server packet} to a {@linkplain ByteBuf byte buf} using a specified
     * {@linkplain PacketCodec packet codec}.
     *
     * @param codec the packet codec
     * @param packet the server packet
     * @param buf the byte buf
     * @param <P> the type of the packet
     * @since 1.0
     */
    private static <P extends ServerPacket> void write(@NonNull PacketCodec<P> codec, @NonNull ServerPacket packet,
                                                       @NonNull ByteBuf buf) {
        NetworkUtil.writeVarInt(buf, codec.getPacketId());
        codec.write(buf, codec.getPacketClass().cast(packet));
    }

    /**
     * Registers {@linkplain PacketCodec packet codecs} to the registry.
     *
     * @param codecs the packet codecs
     * @since 1.0
     */
    @SafeVarargs
    private static void register(@NonNull PacketCodec<? extends ServerPacket> @NonNull ... codecs) {
        for (PacketCodec<? extends ServerPacket> codec : codecs)
            packetCodecs.put(codec.getPacketClass(), codec);
    }
}