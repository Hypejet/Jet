package net.hypejet.jet.server.network.codec.game.registry.tag;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.array.varint.VarIntArrayNetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.codec.game.key.PackedKeyNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerUpdateTagsPacket.Tag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain Tag a tag}.
 *
 * @since 1.0
 * @see Tag
 * @see NetworkWriter
 */
public final class TagNetworkWriter implements NetworkWriter<Tag> {

    /**
     * An instance of the {@linkplain TagNetworkWriter tag network writer}.
     *
     * @since 1.0
     */
    public static final TagNetworkWriter INSTANCE = new TagNetworkWriter();

    /**
     * An instance of the {@linkplain CollectionNetworkWriter collection network writer}, which writes elements
     * with type of {@linkplain Tag tag}.
     *
     * @since 1.0
     */
    public static final CollectionNetworkWriter<Tag> COLLECTION_WRITER = new CollectionNetworkWriter<>(INSTANCE);

    private TagNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Tag object) {
        PackedKeyNetworkCodec.INSTANCE.write(buf, object.key());
        VarIntArrayNetworkWriter.INSTANCE.write(buf, object.entries().array());
    }
}