package net.hypejet.jet.server.network.protocol.codecs.game.registry.tag;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerUpdateTagsConfigurationPacket.Tag;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.array.varint.VarIntArrayNetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.game.key.PackedKeyNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain Tag a tag}.
 *
 * @since 1.0
 * @author Codestech
 * @see Tag
 * @see NetworkWriter
 */
public final class TagNetworkWriter implements NetworkWriter<Tag> {

    /**
     * An instance of {@linkplain TagNetworkWriter a tag network writer}.
     *
     * @since 1.0
     */
    public static final TagNetworkWriter INSTANCE = new TagNetworkWriter();

    /**
     * An instance of {@linkplain CollectionNetworkWriter a collection network writer}, which writes elements
     * with type of {@linkplain Tag tag}.
     *
     * @since 1.0
     */
    public static final CollectionNetworkWriter<Tag> COLLECTION_WRITER = new CollectionNetworkWriter<>(INSTANCE);

    private TagNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Tag object) {
        PackedKeyNetworkCodec.INSTANCE.write(buf, object.identifier());
        VarIntArrayNetworkWriter.INSTANCE.write(buf, object.entries());
    }
}