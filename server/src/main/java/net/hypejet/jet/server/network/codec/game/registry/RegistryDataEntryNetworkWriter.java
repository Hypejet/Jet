package net.hypejet.jet.server.network.codec.game.registry;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.network.packet.server.configuration.ServerRegistryDataConfigurationPacket.Entry;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.codec.game.key.PackedKeyNetworkCodec;
import net.hypejet.jet.server.network.codec.game.miscellaneous.BinaryTagNetworkWriter;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain Entry a registry data entry}.
 *
 * @since 1.0
 * @author Codestech
 * @see Entry
 * @see NetworkWriter
 */
public final class RegistryDataEntryNetworkWriter implements NetworkWriter<Entry> {

    /**
     * An instance of {@linkplain RegistryDataEntryNetworkWriter a registry data entry network writer}.
     *
     * @since 1.0
     */
    public static final RegistryDataEntryNetworkWriter INSTANCE = new RegistryDataEntryNetworkWriter();

    /**
     * An instance of {@linkplain CollectionNetworkWriter a collection network writer}, which writes elements
     * with type of {@linkplain Entry entry}.
     *
     * @since 1.0
     */
    public static final CollectionNetworkWriter<Entry> COLLECTION_WRITER = new CollectionNetworkWriter<>(INSTANCE);

    private RegistryDataEntryNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Entry object) {
        PackedKeyNetworkCodec.INSTANCE.write(buf, object.key());
        NetworkUtil.writeOptional(object.data(), BinaryTagNetworkWriter.INSTANCE, buf);
    }
}