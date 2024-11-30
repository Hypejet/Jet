package net.hypejet.jet.server.network.codec.game.registry.tag;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.network.packet.server.common.ServerUpdateTagsPacket.TagRegistry;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.codec.game.key.PackedKeyNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain TagRegistry a tag registry}.
 *
 * @since 1.0
 * @author Codestech
 * @see TagRegistry
 */
public final class TagRegistryNetworkWriter implements NetworkWriter<TagRegistry> {

    /**
     * An instance of {@linkplain TagRegistryNetworkWriter a tag registry network writer}.
     *
     * @since 1.0
     */
    public static final TagRegistryNetworkWriter INSTANCE = new TagRegistryNetworkWriter();

    /**
     * An instance of {@linkplain CollectionNetworkWriter a collection network writer}, which writes elements
     * with a type of {@linkplain TagRegistry tag registry}.
     *
     * @since 1.0
     */
    public static final CollectionNetworkWriter<TagRegistry> COLLECTION_WRITER
            = new CollectionNetworkWriter<>(INSTANCE);

    private TagRegistryNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull TagRegistry object) {
        PackedKeyNetworkCodec.INSTANCE.write(buf, object.identifier());
        TagNetworkWriter.COLLECTION_WRITER.write(buf, object.tags());
    }
}