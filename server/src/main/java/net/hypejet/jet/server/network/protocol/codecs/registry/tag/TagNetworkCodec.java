package net.hypejet.jet.server.network.protocol.codecs.registry.tag;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerUpdateTagsConfigurationPacket.Tag;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.CollectionNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.arrays.VarIntArrayNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.IdentifierNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain Tag tag}.
 *
 * @since 1.0
 * @author Codestech
 * @see Tag
 * @see NetworkCodec
 */
public final class TagNetworkCodec implements NetworkCodec<Tag> {

    private static final TagNetworkCodec INSTANCE = new TagNetworkCodec();
    private static final CollectionNetworkCodec<Tag> COLLECTION_CODEC = CollectionNetworkCodec.create(INSTANCE);

    private TagNetworkCodec() {}

    @Override
    public @NonNull Tag read(@NonNull ByteBuf buf) {
        return new Tag(IdentifierNetworkCodec.instance().read(buf), VarIntArrayNetworkCodec.instance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Tag object) {
        IdentifierNetworkCodec.instance().write(buf, object.identifier());
        VarIntArrayNetworkCodec.instance().write(buf, object.entries());
    }

    /**
     * Gets an instance of the {@linkplain TagNetworkCodec tag network codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull TagNetworkCodec instance() {
        return INSTANCE;
    }

    /**
     * Gets an instance of a {@linkplain CollectionNetworkCodec collection network codec}, which reads and writes
     * a {@linkplain java.util.Collection collection} of a {@linkplain Tag tag}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull CollectionNetworkCodec<Tag> collectionCodec() {
        return COLLECTION_CODEC;
    }
}