package net.hypejet.jet.server.network.codec.game.world.chunk.light;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.array.bytes.ByteArrayNetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.bitset.BitSetNetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.codec.mapped.MappedValueNetworkWriter;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.world.chunk.light.LightSerializationData;
import net.hypejet.jet.util.array.NibbleArray;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain LightSerializationData a light serialization data}.
 *
 * @since 1.0
 * @see LightSerializationData
 * @see NetworkWriter
 */
public final class LightSerializationDataNetworkWriter implements NetworkWriter<LightSerializationData> {
    /**
     * An instance of the {@linkplain LightSerializationDataNetworkWriter light serialization data network writer}.
     *
     * @since 1.0
     */
    public static final LightSerializationDataNetworkWriter INSTANCE = new LightSerializationDataNetworkWriter();

    private static final CollectionNetworkWriter<NibbleArray> LIGHT_DATA_WRITER =
            new CollectionNetworkWriter<>(
                    new MappedValueNetworkWriter<>(NibbleArray::packedArray, ByteArrayNetworkWriter.INSTANCE)
            );

    private LightSerializationDataNetworkWriter() {}


    @Override
    public void write(@NonNull ByteBuf buf,
                      @NonNull JetRegistryManager registryManager,
                      @NonNull LightSerializationData object) {
        BitSetNetworkWriter.INSTANCE.write(buf, registryManager, object.skyLightMask().bitSet());
        BitSetNetworkWriter.INSTANCE.write(buf, registryManager, object.blockLightMask().bitSet());
        BitSetNetworkWriter.INSTANCE.write(buf, registryManager, object.emptySkyLightMask().bitSet());
        BitSetNetworkWriter.INSTANCE.write(buf, registryManager, object.emptyBlockLightMask().bitSet());
        LIGHT_DATA_WRITER.write(buf, registryManager, object.skyLightData());
        LIGHT_DATA_WRITER.write(buf, registryManager, object.blockLightData());
    }
}