package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.codec.game.miscellaneous.BinaryTagNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.chunk.heightmap.HeightMapCollectionNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.chunk.light.LightSerializationDataNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.chunk.section.ChunkSectionNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.coordinate.relative.ChunkRelativeBlockPositionNetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerChunkAndLightDataPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerChunkAndLightDataPlayPacket.BlockEntity;
import net.hypejet.jet.server.world.chunk.section.JetChunkSection;
import net.hypejet.jet.world.coordinate.chunk.ChunkPosition;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerChunkAndLightDataPlayPacket a server chunk and light data play packet}.
 *
 * @since 1.0
 * @see ServerChunkAndLightDataPlayPacket
 * @see NetworkWriter
 */
public final class ServerChunkAndLightDataPlayPacketWriter
        implements NetworkWriter<ServerChunkAndLightDataPlayPacket> {
    /**
     * An instance of the {@linkplain ServerChunkAndLightDataPlayPacketWriter server chunk and light data play packet
     * writer}.
     *
     * @since 1.0
     */
    public static final ServerChunkAndLightDataPlayPacketWriter
            INSTANCE = new ServerChunkAndLightDataPlayPacketWriter();

    private static final CollectionNetworkWriter<JetChunkSection>
            SECTIONS_WRITER = new CollectionNetworkWriter<>(false, ChunkSectionNetworkWriter.INSTANCE);
    private static final CollectionNetworkWriter<Map.Entry<ChunkRelativeBlockPosition, BlockEntity>>
            BLOCK_ENTITY_ENTRIES_WRITER = new CollectionNetworkWriter<>(new BlockEntityEntryNetworkWriter());

    private ServerChunkAndLightDataPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerChunkAndLightDataPlayPacket object) {
        // Chunk positions in this packet are encoded differently
        ChunkPosition position = object.chunkPosition();
        buf.writeInt(position.chunkX());
        buf.writeInt(position.chunkZ());

        HeightMapCollectionNetworkWriter.INSTANCE.write(buf, object.heightMaps());

        ByteBuf sectionBuf = Unpooled.buffer();
        try {
            SECTIONS_WRITER.write(sectionBuf, object.chunkSectionList().sections());
            VarIntNetworkCodec.INSTANCE.write(buf, sectionBuf.readableBytes());
            buf.writeBytes(sectionBuf);
        } finally {
            sectionBuf.release();
        }

        BLOCK_ENTITY_ENTRIES_WRITER.write(buf, object.blockEntities().entrySet());
        LightSerializationDataNetworkWriter.INSTANCE.write(buf, object.lightSerializationData());
    }

    /**
     * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain Map.Entry map entries}
     * of {@linkplain Map a map} mapping {@linkplain ChunkRelativeBlockPosition chunk-relative block positions}
     * to {@linkplain BlockEntity block entities} of blocks at these positions.
     *
     * @since 1.0
     * @see BlockEntity
     * @see ChunkRelativeBlockPosition
     * @see Map.Entry
     * @see Map
     * @see NetworkWriter
     */
    private static final class BlockEntityEntryNetworkWriter
            implements NetworkWriter<Map.Entry<ChunkRelativeBlockPosition, BlockEntity>> {
        @Override
        public void write(@NonNull ByteBuf buf, Map.@NonNull Entry<ChunkRelativeBlockPosition, BlockEntity> object) {
            ChunkRelativeBlockPosition position = object.getKey();
            ChunkRelativeBlockPositionNetworkWriter.INSTANCE.write(buf, position);

            BlockEntity blockEntity = object.getValue();
            VarIntNetworkCodec.INSTANCE.write(buf, blockEntity.typeIdentifier());
            BinaryTagNetworkWriter.INSTANCE.write(buf, blockEntity.data());
        }
    }
}