package net.hypejet.jet.server.network.netty;

/**
 * Represents a holder of names of {@linkplain io.netty.channel.ChannelHandler channel handlers} managing
 * Minecraft packet flow.
 *
 * @since 1.0
 * @author Codestech
 */
public final class ChannelHandlers {
    /**
     * A handler name of a {@linkplain net.hypejet.jet.server.network.netty.encoder.PacketEncoder packet encoder}.
     *
     * @since 1.0
     */
    public static final String PACKET_ENCODER = "minecraft-packet-encoder";

    /**
     * A handler name of a {@linkplain net.hypejet.jet.server.network.netty.encoder.PacketCompressor packet
     * compressor}.
     *
     * @since 1.0
     */
    public static final String PACKET_COMPRESSOR = "minecraft-packet-compressor";

    /**
     * A handler name of a {@linkplain net.hypejet.jet.server.network.netty.encoder.PacketLengthEncoder packet length
     * encoder}.
     *
     * @since 1.0
     */
    public static final String PACKET_LENGTH_ENCODER = "minecraft-packet-length-encoder";

    /**
     * A handler name of a {@linkplain net.hypejet.jet.server.network.netty.decoder.PacketDecoder packet decoder}.
     *
     * @since 1.0
     */
    public static final String PACKET_DECODER = "minecraft-packet-decoder";

    /**
     * A handler name of a {@linkplain net.hypejet.jet.server.network.netty.decoder.PacketDecompressor packet
     * decompressor}.
     *
     * @since 1.0
     */
    public static final String PACKET_DECOMPRESSOR = "minecraft-packet-decompressor";

    /**
     * A handler name of a {@linkplain net.hypejet.jet.server.network.netty.decoder.PacketLengthDecoder packet length
     * decoder}.
     *
     * @since 1.0
     */
    public static final String PACKET_LENGTH_DECODER = "minecraft-packet-length-decoder";

    /**
     * A handler name of a {@linkplain PacketReader packet reader}.
     *
     * @since 1.0
     */
    public static final String PACKET_READER = "minecraft-packet-reader";

    private ChannelHandlers() {}
}