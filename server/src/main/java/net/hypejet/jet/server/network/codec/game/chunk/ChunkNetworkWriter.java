package net.hypejet.jet.server.network.codec.game.chunk;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.codec.game.chunk.entity.BlockEntityNetworkWriter;
import net.hypejet.jet.server.network.codec.game.chunk.heightmap.HeightMapCollectionNetworkWriter;
import net.hypejet.jet.server.network.codec.game.chunk.light.LightSerializationDataNetworkWriter;
import net.hypejet.jet.server.network.codec.game.chunk.section.ChunkSectionNetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.model.light.LightSerializationData;
import net.hypejet.jet.server.world.chunk.Chunk;
import net.hypejet.jet.server.world.chunk.entity.BlockEntity;
import net.hypejet.jet.server.world.chunk.section.ChunkSection;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain Chunk a chunk}.
 *
 * @since 1.0
 * @see Chunk
 * @see NetworkWriter
 */
public final class ChunkNetworkWriter implements NetworkWriter<Chunk> {
    /**
     * An instance of the {@linkplain ChunkNetworkWriter chunk network writer}.
     *
     * @since 1.0
     */
    public static final ChunkNetworkWriter INSTANCE = new ChunkNetworkWriter();

    private static final CollectionNetworkWriter<ChunkSection>
            SECTIONS_WRITER = new CollectionNetworkWriter<>(false, ChunkSectionNetworkWriter.INSTANCE);
    private static final CollectionNetworkWriter<BlockEntity>
            BLOCK_ENTITIES_WRITER = new CollectionNetworkWriter<>(BlockEntityNetworkWriter.INSTANCE);

    private ChunkNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Chunk object) {
        HeightMapCollectionNetworkWriter.INSTANCE.write(buf, object.heightMaps());

        ByteBuf sectionBuf = Unpooled.buffer();
        try {
            SECTIONS_WRITER.write(sectionBuf, object.sections());
            VarIntNetworkCodec.INSTANCE.write(buf, sectionBuf.readableBytes());
            buf.writeBytes(sectionBuf);
        } finally {
            sectionBuf.release();
        }

        BLOCK_ENTITIES_WRITER.write(buf, object.blockEntities());
        LightSerializationDataNetworkWriter.INSTANCE.write(buf, LightSerializationData.create(object.lightSections()));
    }
}