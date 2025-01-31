package net.hypejet.jet.server.network.codec.game.chunk.heightmap;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.miscellaneous.BinaryTagNetworkWriter;
import net.hypejet.jet.server.world.chunk.heightmap.HeightMap;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain Collection a collection}
 * of {@linkplain HeightMap height maps}.
 *
 * @since 1.0
 * @see HeightMap
 * @see Collection
 * @see NetworkWriter
 */
public final class HeightMapCollectionNetworkWriter implements NetworkWriter<Collection<HeightMap>> {

    /**
     * An instance of the {@linkplain HeightMapCollectionNetworkWriter height map collection network writer}.
     *
     * @since 1.0
     */
    public static final HeightMapCollectionNetworkWriter INSTANCE = new HeightMapCollectionNetworkWriter();

    private HeightMapCollectionNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Collection<HeightMap> object) {
        CompoundBinaryTag.Builder binaryTagBuilder = CompoundBinaryTag.builder();
        for (HeightMap heightMap : object)
            binaryTagBuilder.putLongArray(heightMap.type().serializationName(), heightMap.data().data());
        BinaryTagNetworkWriter.INSTANCE.write(buf, binaryTagBuilder.build());
    }
}