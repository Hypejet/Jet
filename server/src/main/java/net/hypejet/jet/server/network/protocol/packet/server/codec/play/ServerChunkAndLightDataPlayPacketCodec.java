package net.hypejet.jet.server.network.protocol.packet.server.codec.play;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hypejet.jet.protocol.packet.server.play.ServerChunkAndLightDataPlayPacket;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.CollectionNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.arrays.ByteArrayNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.arrays.ObjectArrayNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.bitset.BitSetNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.chunk.BlockEntityNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.chunk.ChunkSectionNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.BinaryTagCodec;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.world.chunk.BlockEntity;
import net.hypejet.jet.world.chunk.ChunkSection;
import net.kyori.adventure.nbt.BinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ServerChunkAndLightDataPlayPacket chunk and light data play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerChunkAndLightDataPlayPacket
 */
public final class ServerChunkAndLightDataPlayPacketCodec extends PacketCodec<ServerChunkAndLightDataPlayPacket> {

    private static final int MAX_SECTION_DATA_LENGTH = 2_097_152; // 2 MiB

    private static final CollectionNetworkCodec<BlockEntity> BLOCK_ENTITIES_CODEC = CollectionNetworkCodec
            .create(BlockEntityNetworkCodec.instance());
    private static final ObjectArrayNetworkCodec<byte[]> ARRAY_OF_BYTE_ARRAY_CODEC = ObjectArrayNetworkCodec
            .create(ByteArrayNetworkCodec.instance(), byte[][]::new);

    /**
     * Constructs the {@linkplain ServerChunkAndLightDataPlayPacketCodec chunk and light data play packet codec}.
     *
     * @since 1.0
     */
    public ServerChunkAndLightDataPlayPacketCodec() {
        super(ServerPacketIdentifiers.PLAY_CHUNK_AND_LIGHT_DATA, ServerChunkAndLightDataPlayPacket.class);
    }

    @Override
    public @NonNull ServerChunkAndLightDataPlayPacket read(@NonNull ByteBuf buf) {
        int chunkX = buf.readInt();
        int chunkZ = buf.readInt();

        BinaryTag heightmaps = BinaryTagCodec.instance().read(buf);
        int sectionsLength = VarIntNetworkCodec.instance().read(buf);

        if (sectionsLength > MAX_SECTION_DATA_LENGTH)
            throw new IllegalArgumentException("The size of sections is longer than maximum allowed");

        int sectionCount = 24; // TODO: Get an actual count of sections from a registry
        List<ChunkSection> sections = new ArrayList<>(sectionCount);

        for (int i = 0; i < sectionCount; i++) {
            sections.add(ChunkSectionNetworkCodec.instance().read(buf));
        }

        Collection<BlockEntity> blockEntities = BLOCK_ENTITIES_CODEC.read(buf);

        BitSet skyLightMask = BitSetNetworkCodec.instance().read(buf);
        BitSet blockLightMask = BitSetNetworkCodec.instance().read(buf);
        BitSet emptySkyLightMask = BitSetNetworkCodec.instance().read(buf);
        BitSet emptyBlockLightMask = BitSetNetworkCodec.instance().read(buf);

        byte[][] skyLight = ARRAY_OF_BYTE_ARRAY_CODEC.read(buf);
        byte[][] blockLight = ARRAY_OF_BYTE_ARRAY_CODEC.read(buf);

        return new ServerChunkAndLightDataPlayPacket(chunkX, chunkZ, heightmaps, sections, blockEntities,
                skyLightMask, blockLightMask, emptySkyLightMask, emptyBlockLightMask, skyLight, blockLight);
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerChunkAndLightDataPlayPacket object) {
        buf.writeInt(object.chunkX());
        buf.writeInt(object.chunkZ());
        BinaryTagCodec.instance().write(buf, object.heightmaps());

        ByteBuf sectionsBuf = Unpooled.buffer();

        try {
            object.sections().forEach(section -> ChunkSectionNetworkCodec.instance().write(sectionsBuf, section));
            VarIntNetworkCodec.instance().write(buf, sectionsBuf.readableBytes());
            buf.writeBytes(sectionsBuf);
        } finally {
            sectionsBuf.release();
        }

        BLOCK_ENTITIES_CODEC.write(buf, object.blockEntities());

        BitSetNetworkCodec.instance().write(buf, object.skyLightMask());
        BitSetNetworkCodec.instance().write(buf, object.blockLightMask());
        BitSetNetworkCodec.instance().write(buf, object.emptySkyLightMask());
        BitSetNetworkCodec.instance().write(buf, object.emptyBlockLightMask());

        ARRAY_OF_BYTE_ARRAY_CODEC.write(buf, object.skyLight());
        ARRAY_OF_BYTE_ARRAY_CODEC.write(buf, object.blockLight());
    }
}