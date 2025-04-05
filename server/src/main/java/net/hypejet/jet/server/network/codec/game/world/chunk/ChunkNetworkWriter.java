package net.hypejet.jet.server.network.codec.game.world.chunk;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hypejet.jet.data.model.api.block.entity.BlockEntityType;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.codec.game.miscellaneous.BinaryTagNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.chunk.heightmap.HeightMapCollectionNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.chunk.light.LightSerializationDataNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.chunk.section.ChunkSectionNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.coordinate.relative.ChunkRelativeBlockPositionNetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.registry.JetMinecraftRegistry;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.server.world.chunk.section.JetChunkSection;
import net.hypejet.jet.world.block.entity.BlockEntity;
import net.hypejet.jet.world.coordinate.chunk.relative.ChunkRelativeBlockPosition;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain JetChunk a chunk}.
 *
 * @since 1.0
 * @see JetChunk
 * @see NetworkWriter
 */
public final class ChunkNetworkWriter implements NetworkWriter<JetChunk> {
    /**
     * An instance of the {@linkplain ChunkNetworkWriter chunk network writer}.
     *
     * @since 1.0
     */
    public static final ChunkNetworkWriter INSTANCE = new ChunkNetworkWriter();

    private static final CollectionNetworkWriter<JetChunkSection>
            SECTIONS_WRITER = new CollectionNetworkWriter<>(false, ChunkSectionNetworkWriter.INSTANCE);
    private static final CollectionNetworkWriter<BlockEntityEntry>
            BLOCK_ENTITY_ENTRIES_WRITER = new CollectionNetworkWriter<>(new BlockEntityEntryNetworkWriter());

    private ChunkNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull JetChunk object) {
        HeightMapCollectionNetworkWriter.INSTANCE.write(buf, object.heightMaps());

        ByteBuf sectionBuf = Unpooled.buffer();
        try {
            SECTIONS_WRITER.write(sectionBuf, object.sections());
            VarIntNetworkCodec.INSTANCE.write(buf, sectionBuf.readableBytes());
            buf.writeBytes(sectionBuf);
        } finally {
            sectionBuf.release();
        }

        JetRegistryManager registryManager = object.server().registryManager();
        JetMinecraftRegistry<BlockEntityType> blockEntityTypeRegistry = registryManager.blockEntityTypeRegistry();

        Set<BlockEntityEntry> entries = new HashSet<>();
        for (Map.Entry<ChunkRelativeBlockPosition, BlockEntity> entry : object.blockEntities().entrySet()) {
            BlockEntity blockEntity = entry.getValue();
            int blockEntityTypeIdentifier = blockEntityTypeRegistry.identifierOf(blockEntity.type());
            entries.add(new BlockEntityEntry(entry.getKey(), blockEntityTypeIdentifier, blockEntity.data()));
        }

        BLOCK_ENTITY_ENTRIES_WRITER.write(buf, entries);
        LightSerializationDataNetworkWriter.INSTANCE.write(buf, object.lightSerializationData());
    }

    /**
     * Represents a serialization entry of {@linkplain BlockEntity a block entity}.
     *
     * @param position a chunk-relative block position of a block that the block entity is associated with
     * @param typeIdentifier an identifier of a type of the block entity
     * @param data data of the block entity
     * @since 1.0
     */
    private record BlockEntityEntry(@NonNull ChunkRelativeBlockPosition position, int typeIdentifier,
                                    @NonNull CompoundBinaryTag data) {
        /**
         * Constructs the {@linkplain BlockEntityEntry block entity entry}.
         *
         * @param position a chunk-relative position of a block that the block entity is associated with
         * @param typeIdentifier an identifier of a type of the block entity
         * @param data data of the block entity
         * @since 1.0
         */
        private BlockEntityEntry {
            NullabilityUtil.requireNonNull(position, "position");
            NullabilityUtil.requireNonNull(data, "data");
        }
    }

    /**
     * Represents {@linkplain NetworkWriter a network writer}, which
     * writes {@linkplain BlockEntityEntry a block entity entry}.
     *
     * @since 1.0
     * @see NetworkWriter
     * @see BlockEntityEntry
     */
    private static final class BlockEntityEntryNetworkWriter implements NetworkWriter<BlockEntityEntry> {
        @Override
        public void write(@NonNull ByteBuf buf, @NonNull BlockEntityEntry object) {
            ChunkRelativeBlockPositionNetworkWriter.INSTANCE.write(buf, object.position());
            VarIntNetworkCodec.INSTANCE.write(buf, object.typeIdentifier());
            BinaryTagNetworkWriter.INSTANCE.write(buf, object.data());
        }
    }
}