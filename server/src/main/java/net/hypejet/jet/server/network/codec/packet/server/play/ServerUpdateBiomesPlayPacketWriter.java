package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.registry.RegistryEntry;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.chunk.palette.ChunkPaletteNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.coordinate.chunk.ChunkPositionNetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerUpdateBiomesPlayPacket;
import net.hypejet.jet.server.world.chunk.palette.AbstractChunkPalette;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerUpdateBiomesPlayPacket a server update biomes play packet}.
 *
 * @since 1.0
 * @see ServerUpdateBiomesPlayPacket
 * @see NetworkWriter
 */
public final class ServerUpdateBiomesPlayPacketWriter implements NetworkWriter<ServerUpdateBiomesPlayPacket> {

    /**
     * An instance of the {@linkplain ServerUpdateBiomesPlayPacketWriter server update biomes play packet writer}.
     *
     * @since 1.0
     */
    public static final ServerUpdateBiomesPlayPacketWriter INSTANCE = new ServerUpdateBiomesPlayPacketWriter();

    private static final CollectionNetworkWriter<ServerUpdateBiomesPlayPacket.BiomeData>
            BIOME_DATA_WRITER = new CollectionNetworkWriter<>(new BiomeDataNetworkWriter());

    private ServerUpdateBiomesPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerUpdateBiomesPlayPacket object) {
        BIOME_DATA_WRITER.write(buf, object.data());
    }

    /**
     * Represents {@linkplain NetworkWriter a network writer}, which
     * writes {@linkplain ServerUpdateBiomesPlayPacket.BiomeData a biome data}.
     *
     * @since 1.0
     * @see ServerUpdateBiomesPlayPacket.BiomeData
     * @see NetworkWriter
     */
    private static final class BiomeDataNetworkWriter
            implements NetworkWriter<ServerUpdateBiomesPlayPacket.BiomeData> {
        @Override
        public void write(@NonNull ByteBuf buf, ServerUpdateBiomesPlayPacket.@NonNull BiomeData object) {
            ChunkPositionNetworkWriter.INSTANCE.write(buf, object.position());

            ByteBuf paletteBuf = Unpooled.buffer();
            try {
                for (AbstractChunkPalette<RegistryEntry<Biome>> palette : object.palettes())
                    ChunkPaletteNetworkWriter.INSTANCE.write(paletteBuf, palette);
                VarIntNetworkCodec.INSTANCE.write(buf, paletteBuf.readableBytes());
                buf.writeBytes(paletteBuf);
            } finally {
                paletteBuf.release();
            }
        }
    }
}