package net.hypejet.jet.server.network.protocol.codecs.chunk;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.BinaryTagCodec;
import net.hypejet.jet.world.chunk.BlockEntity;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain BlockEntity block
 * entity}.
 *
 * @since 1.0
 * @author Codestech
 * @see BlockEntity
 * @see NetworkCodec
 */
public final class BlockEntityNetworkCodec implements NetworkCodec<BlockEntity> {

    private static final BlockEntityNetworkCodec INSTANCE = new BlockEntityNetworkCodec();

    private BlockEntityNetworkCodec() {}

    @Override
    public @NonNull BlockEntity read(@NonNull ByteBuf buf) {
        short packedXZ = buf.readUnsignedByte();
        return new BlockEntity((byte) (packedXZ >> 4), (byte) (packedXZ & 15), buf.readShort(),
                VarIntNetworkCodec.instance().read(buf), BinaryTagCodec.instance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull BlockEntity object) {
        buf.writeByte(((object.x() & 15) << 4) | (object.z() & 15));
        buf.writeShort(object.y());
        VarIntNetworkCodec.instance().write(buf, object.type());
        BinaryTagCodec.instance().write(buf, object.data());
    }

    /**
     * Gets an instance of the {@linkplain BlockEntityNetworkCodec block entity network codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull BlockEntityNetworkCodec instance() {
        return INSTANCE;
    }
}