package net.hypejet.jet.server.network.codec.game.world.chunk.entity;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.miscellaneous.BinaryTagNetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.world.chunk.entity.BlockEntity;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain BlockEntity a block entity}.
 *
 * @since 1.0
 * @see BlockEntity
 * @see NetworkWriter
 */
public final class BlockEntityNetworkWriter implements NetworkWriter<BlockEntity> {
    /**
     * An instance of the {@linkplain BlockEntityNetworkWriter block entity network writer}.
     *
     * @since 1.0
     */
    public static final BlockEntityNetworkWriter INSTANCE = new BlockEntityNetworkWriter();

    private BlockEntityNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull BlockEntity object) {
        buf.writeByte(((object.blockX() & 15) << 4) | (object.blockZ() & 15));
        buf.writeShort(object.blockY());
        VarIntNetworkCodec.INSTANCE.write(buf, object.type());
        BinaryTagNetworkWriter.INSTANCE.write(buf, object.data());
    }
}