package net.hypejet.jet.server.network.protocol.packet.server.writer.play;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hypejet.jet.protocol.packet.server.play.ServerChunkAndLightDataPlayPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.array.bytes.ByteArrayNetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.array.object.ObjectArrayNetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.bitset.BitSetNetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.chunk.BlockEntityNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.chunk.ChunkSectionNetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.BinaryTagCodec;
import net.hypejet.jet.world.chunk.BlockEntity;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerChunkAndLightDataPlayPacket a chunk and light data play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerChunkAndLightDataPlayPacket
 */
public final class ServerChunkAndLightDataPlayPacketWriter implements NetworkWriter<ServerChunkAndLightDataPlayPacket> {

    private static final int MAX_SECTION_DATA_LENGTH = 2_097_152; // 2 MiB

    private static final CollectionNetworkWriter<BlockEntity> BLOCK_ENTITIES_WRITER =
            new CollectionNetworkWriter<>(BlockEntityNetworkCodec.instance());
    private static final ObjectArrayNetworkWriter<byte[]> ARRAY_OF_BYTE_ARRAY_WRITER =
            new ObjectArrayNetworkWriter<>(ByteArrayNetworkWriter.INSTANCE);

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerChunkAndLightDataPlayPacket object) {
        buf.writeInt(object.chunkX());
        buf.writeInt(object.chunkZ());
        BinaryTagCodec.instance().write(buf, object.heightmaps());

        ByteBuf sectionsBuf = Unpooled.buffer();

        try {
            object.sections().forEach(section -> ChunkSectionNetworkWriter.INSTANCE.write(sectionsBuf, section));
            VarIntNetworkCodec.instance().write(buf, sectionsBuf.readableBytes());
            buf.writeBytes(sectionsBuf);
        } finally {
            sectionsBuf.release();
        }

        BLOCK_ENTITIES_WRITER.write(buf, object.blockEntities());

        BitSetNetworkWriter.INSTANCE.write(buf, object.skyLightMask());
        BitSetNetworkWriter.INSTANCE.write(buf, object.blockLightMask());
        BitSetNetworkWriter.INSTANCE.write(buf, object.emptySkyLightMask());
        BitSetNetworkWriter.INSTANCE.write(buf, object.emptyBlockLightMask());

        ARRAY_OF_BYTE_ARRAY_WRITER.write(buf, object.skyLight());
        ARRAY_OF_BYTE_ARRAY_WRITER.write(buf, object.blockLight());
    }
}