package net.hypejet.jet.server.network.protocol.packet;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.server.codec.ServerPacketCodec;
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
 * Represents a registry of {@linkplain ServerPacketCodec packet codecs}, which can read and write
 * {@linkplain ServerPacket server packets}..
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 * @see NetworkCodec
 */
public final class ServerPacketRegistry {

    private static final Map<Class<? extends ServerPacket>, ServerPacketCodec<?>> packetCodecs = new HashMap<>();

    static {
        // Login packets
        register(new ServerDisconnectPacketCodec(), new ServerEncryptionRequestPacketCodec(),
                new ServerLoginSuccessPacketCodec(), new ServerEnableCompressionPacketCodec(),
                new ServerPluginMessageRequestPacketCodec(), new ServerCookieRequestPacketCodec());
    }

    private ServerPacketRegistry() {}

    /**
     * Finds a {@linkplain ServerPacketCodec server packet codec}, which can write a specific packet and writes it to
     * a {@linkplain ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @param packet the server packet
     * @throws IllegalStateException if an eligible packet codec for the packet could not be found or the protocol
     *                               state is not supported by protocol state of an eligible packet codec
     * @since 1.0
     */
    public static void write(@NonNull ByteBuf buf, @NonNull ServerPacket packet) {
        ServerPacketCodec<? extends ServerPacket> codec = packetCodecs.get(packet.getClass());

        if (codec == null) {
            String packetName = packet.getClass().getSimpleName();
            throw new IllegalStateException("Could not find an eligible server packet codec for " + packetName);
        }

        write(codec, packet, buf);
    }

    /**
     * Writes a {@linkplain ServerPacket server packet} to a {@linkplain ByteBuf byte buf} using a specified
     * {@linkplain ServerPacketCodec server packet codec}.
     *
     * @param codec the packet codec
     * @param packet the server packet
     * @param buf the byte buf
     * @param <P> the type of the packet
     * @since 1.0
     */
    private static <P extends ServerPacket> void write(@NonNull ServerPacketCodec<P> codec, @NonNull ServerPacket packet,
                                                       @NonNull ByteBuf buf) {
        NetworkUtil.writeVarInt(buf, codec.getPacketId());
        codec.write(buf, codec.getPacketClass().cast(packet));
    }

    /**
     * Registers {@linkplain ServerPacketCodec server packet codecs} to the registry.
     *
     * @param codecs the packet codecs
     * @since 1.0
     */
    private static void register(@NonNull ServerPacketCodec<?> @NonNull ... codecs) {
        for (ServerPacketCodec<?> codec : codecs)
            packetCodecs.put(codec.getPacketClass(), codec);
    }
}