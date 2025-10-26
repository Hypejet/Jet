package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.coordinate.chunk.section.ChunkSectionPositionNetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarLongNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerUpdateChunkSectionBlockStatesPlayPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.util.math.MathUtil;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerUpdateChunkSectionBlockStatesPlayPacket a server update chunk-section block states play packet}.
 *
 * @since 1.0
 * @see ServerUpdateChunkSectionBlockStatesPlayPacket
 * @see NetworkWriter
 */
public final class ServerUpdateChunkSectionBlockStatesPlayPacketWriter
        implements NetworkWriter<ServerUpdateChunkSectionBlockStatesPlayPacket> {
    /**
     * An instance of the {@linkplain ServerUpdateChunkSectionBlockStatesPlayPacketWriter server update chunk-section
     * block states play packet writer}.
     *
     * @since 1.0
     * @see ServerUpdateChunkSectionBlockStatesPlayPacketWriter
     */
    public static final ServerUpdateChunkSectionBlockStatesPlayPacketWriter
            INSTANCE = new ServerUpdateChunkSectionBlockStatesPlayPacketWriter();

    private static final CollectionNetworkWriter<Object2IntMap.Entry<ChunkPaletteRelativePosition>>
            UPDATES_WRITER = new CollectionNetworkWriter<>(new UpdateNetworkWriter());

    private ServerUpdateChunkSectionBlockStatesPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager,
                      @NonNull ServerUpdateChunkSectionBlockStatesPlayPacket object) {
        ChunkSectionPositionNetworkWriter.INSTANCE.write(buf, registryManager, object.position());
        UPDATES_WRITER.write(buf, registryManager, object.updates().object2IntEntrySet());
    }

    /**
     * Represents {@linkplain NetworkWriter a network writer}, which writes
     * {@linkplain Object2IntMap.Entry an object-to-int map entry}, which represents an update
     * of {@linkplain ServerUpdateChunkSectionBlockStatesPlayPacket a server update chunk section block states
     * play packet}.
     *
     * @since 1.0
     * @see Object2IntMap.Entry
     * @see ServerUpdateChunkSectionBlockStatesPlayPacket
     * @see NetworkWriter
     */
    private static final class UpdateNetworkWriter
            implements NetworkWriter<Object2IntMap.Entry<ChunkPaletteRelativePosition>> {

        private static final ChunkPaletteType CHUNK_PALETTE_TYPE = ChunkPaletteType.BLOCK_STATE;
        private static final int POSITION_VALUE_BITS = MathUtil.bitCount(CHUNK_PALETTE_TYPE.axisLength() - 1);

        @Override
        public void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager,
                          Object2IntMap.@NonNull Entry<ChunkPaletteRelativePosition> object) {
            ChunkPaletteRelativePosition position = object.getKey();
            if (position.paletteType() != CHUNK_PALETTE_TYPE) {
                throw new IllegalArgumentException(
                        "Palette type of the position must be a block-state chunk palette type"
                );
            }

            long packedValue = position.y();
            packedValue |= (long) position.z() << POSITION_VALUE_BITS;
            packedValue |= (long) position.x() << POSITION_VALUE_BITS * 2;
            packedValue |= (long) object.getIntValue() << POSITION_VALUE_BITS * 3;

            VarLongNetworkCodec.INSTANCE.write(buf, registryManager, packedValue);
        }
    }
}