package net.hypejet.jet.server.network.packet.server.writer.play;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hypejet.jet.network.packet.server.play.ServerChunkAndLightDataPlayPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.array.bytes.ByteArrayNetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.array.object.ObjectArrayNetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.bitset.BitSetNetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.chunk.BlockEntityNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.chunk.ChunkSectionNetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.codec.game.miscellaneous.BinaryTagNetworkWriter;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import net.hypejet.jet.world.chunk.BlockEntity;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerChunkAndLightDataPlayPacket a chunk and light data play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerChunkAndLightDataPlayPacket
 */
public final class ServerChunkAndLightDataPlayPacketWriter implements NetworkWriter<ServerChunkAndLightDataPlayPacket> {

    private static final CollectionNetworkWriter<BlockEntity> BLOCK_ENTITIES_WRITER =
            new CollectionNetworkWriter<>(BlockEntityNetworkWriter.INSTANCE);

    private static final ObjectArrayNetworkWriter<byte[]> ARRAY_OF_BYTE_ARRAY_WRITER =
            new ObjectArrayNetworkWriter<>(ByteArrayNetworkWriter.INSTANCE);

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerChunkAndLightDataPlayPacket object) {
        buf.writeInt(object.chunkX());
        buf.writeInt(object.chunkZ());
        BinaryTagNetworkWriter.INSTANCE.write(buf, object.heightmaps());

        ByteBuf sectionsBuf = Unpooled.buffer();

        try {
            object.sections().forEach(section -> ChunkSectionNetworkWriter.INSTANCE.write(sectionsBuf, section));
            VarIntNetworkCodec.INSTANCE.write(buf, sectionsBuf.readableBytes());
            buf.writeBytes(sectionsBuf);
        } finally {
            sectionsBuf.release();
        }

        BLOCK_ENTITIES_WRITER.write(buf, object.blockEntities());

        BitSetNetworkWriter.INSTANCE.write(buf, object.skyLightMask().bitSet());
        BitSetNetworkWriter.INSTANCE.write(buf, object.blockLightMask().bitSet());
        BitSetNetworkWriter.INSTANCE.write(buf, object.emptySkyLightMask().bitSet());
        BitSetNetworkWriter.INSTANCE.write(buf, object.emptyBlockLightMask().bitSet());

        ARRAY_OF_BYTE_ARRAY_WRITER.write(buf, toArray(object.skyLight()));
        ARRAY_OF_BYTE_ARRAY_WRITER.write(buf, toArray(object.blockLight()));
    }

    private static byte @NonNull [] @NonNull [] toArray(@NonNull List<UnmodifiableByteArray> byteArrayList) {
        byte[][] array = new byte[byteArrayList.size()][];
        for (int index = 0; index < byteArrayList.size(); index++)
            array[index] = byteArrayList.get(index).array();
        return array;
    }
}