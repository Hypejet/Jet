package net.hypejet.jet.server.network.codec;

import io.netty.buffer.ByteBuf;

/**
 * Represents a holder of {@linkplain NetworkCodec network codecs}, which read and writes types already supported
 * by {@linkplain ByteBuf a byte buf}.
 *
 * <p>This was made to support situations where you need to pass a network codec of a primitive type for example.
 * This approach however should be avoided as much as possible.</p>
 *
 * @since 1.0
 * @author Codestech
 * @see ByteBuf
 * @see NetworkCodec
 */
public final class PrimitiveNetworkCodecs {
    /**
     * {@linkplain NetworkCodec A network codec} reading and writing a byte primitive type.
     *
     * @since 1.0
     */
    public static final NetworkCodec<Byte> BYTE = new CombinedNetworkCodec<>(ByteBuf::readByte,
            (buf, object) -> buf.writeByte(object));

    private PrimitiveNetworkCodecs() {}
}