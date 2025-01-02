package net.hypejet.jet.server.network.codec.chunk.light;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.array.bytes.ByteArrayNetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.bitset.BitSetNetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.world.chunk.light.LightData;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain LightData a light data}.
 *
 * @since 1.0
 * @see LightData
 * @see NetworkWriter
 */
public final class LightDataNetworkWriter implements NetworkWriter<LightData> {
    /**
     * An instance of the {@linkplain LightDataNetworkWriter light data network writer}.
     *
     * @since 1.0
     */
    public static final LightDataNetworkWriter INSTANCE = new LightDataNetworkWriter();

    private static final CollectionNetworkWriter<byte[]>
            LIGHT_DATA_WRITER = new CollectionNetworkWriter<>(ByteArrayNetworkWriter.INSTANCE);

    private LightDataNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull LightData object) {
        BitSetNetworkWriter.INSTANCE.write(buf, object.skyLightMask().bitSet());
        BitSetNetworkWriter.INSTANCE.write(buf, object.blockLightMask().bitSet());
        BitSetNetworkWriter.INSTANCE.write(buf, object.emptySkyLightMask().bitSet());
        BitSetNetworkWriter.INSTANCE.write(buf, object.emptyBlockLightMask().bitSet());

        LIGHT_DATA_WRITER.write(buf, toByteArrayList(object.skyLightData()));
        LIGHT_DATA_WRITER.write(buf, toByteArrayList(object.blockLightData()));
    }

    private static @NonNull List<byte[]> toByteArrayList(@NonNull List<UnmodifiableByteArray> unmodifiableByteArrays) {
        List<byte[]> list = new ArrayList<>(unmodifiableByteArrays.size());
        for (UnmodifiableByteArray unmodifiableByteArray : unmodifiableByteArrays)
            list.add(unmodifiableByteArray.array());
        return List.copyOf(list);
    }
}