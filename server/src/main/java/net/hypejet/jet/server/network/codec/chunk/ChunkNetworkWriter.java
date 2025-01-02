package net.hypejet.jet.server.network.codec.chunk;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.codec.chunk.entity.BlockEntityNetworkWriter;
import net.hypejet.jet.server.network.codec.chunk.light.LightDataNetworkWriter;
import net.hypejet.jet.server.network.codec.chunk.section.ChunkSectionNetworkWriter;
import net.hypejet.jet.server.network.codec.game.miscellaneous.BinaryTagNetworkWriter;
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
            SECTIONS_WRITER = new CollectionNetworkWriter<>(ChunkSectionNetworkWriter.INSTANCE);
    private static final CollectionNetworkWriter<BlockEntity>
            BLOCK_ENTITIES_WRITER = new CollectionNetworkWriter<>(BlockEntityNetworkWriter.INSTANCE);

    private ChunkNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Chunk object) {
        buf.writeInt(object.chunkX());
        buf.writeInt(object.chunkZ());

        BinaryTagNetworkWriter.INSTANCE.write(buf, object.heightmaps());
        SECTIONS_WRITER.write(buf, object.sections());
        BLOCK_ENTITIES_WRITER.write(buf, object.blockEntities());
        LightDataNetworkWriter.INSTANCE.write(buf, object.lightData());
    }
}