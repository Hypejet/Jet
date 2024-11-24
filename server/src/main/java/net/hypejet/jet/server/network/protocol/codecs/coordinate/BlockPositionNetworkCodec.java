package net.hypejet.jet.server.network.protocol.codecs.coordinate;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.data.model.api.coordinate.BlockPosition;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkCodec a network codec}, which reads and writes {@linkplain BlockPosition a block
 * position}.
 *
 * @since 1.0
 * @author Codestech
 * @see BlockPosition
 * @see NetworkCodec
 */
public final class BlockPositionNetworkCodec implements NetworkCodec<BlockPosition> {

    /**
     * An instance of {@linkplain BlockPositionNetworkCodec a block position network codec}.
     *
     * @since 1.0
     */
    public static final BlockPositionNetworkCodec INSTANCE = new BlockPositionNetworkCodec();

    private BlockPositionNetworkCodec() {}

    @Override
    public @NonNull BlockPosition read(@NonNull ByteBuf buf) {
        // TODO
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull BlockPosition object) {
        // TODO
        throw new IllegalStateException("Not implemented yet");
    }
}