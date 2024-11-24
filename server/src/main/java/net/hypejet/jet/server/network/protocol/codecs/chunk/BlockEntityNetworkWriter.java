package net.hypejet.jet.server.network.protocol.codecs.chunk;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.BinaryTagNetworkWriter;
import net.hypejet.jet.world.chunk.BlockEntity;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain BlockEntity a block entity}.
 *
 * @since 1.0
 * @author Codestech
 * @see BlockEntity
 * @see NetworkWriter
 */
public final class BlockEntityNetworkWriter implements NetworkWriter<BlockEntity> {

    /**
     * An instance of {@linkplain BlockEntityNetworkWriter a block entity network writer}.
     *
     * @since 1.0
     */
    public static final BlockEntityNetworkWriter INSTANCE = new BlockEntityNetworkWriter();

    private BlockEntityNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull BlockEntity object) {
        buf.writeByte(((object.x() & 15) << 4) | (object.z() & 15));
        buf.writeShort(object.y());
        VarIntNetworkCodec.INSTANCE.write(buf, object.type());
        BinaryTagNetworkWriter.INSTANCE.write(buf, object.data());
    }
}