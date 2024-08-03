package net.hypejet.jet.server.network.protocol.codecs.registry.tag;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerUpdateTagsConfigurationPacket.TagRegistry;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.identifier.PackedIdentifierNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes
 * a {@linkplain TagRegistry tag registry}.
 *
 * @since 1.0
 * @author Codestech
 * @see TagRegistry
 * @see net.hypejet.jet.protocol.packet.server.configuration.ServerUpdateTagsConfigurationPacket.Tag
 */
public final class TagRegistryNetworkCodec implements NetworkCodec<TagRegistry> {

    private static final TagRegistryNetworkCodec INSTANCE = new TagRegistryNetworkCodec();

    private TagRegistryNetworkCodec() {}

    @Override
    public @NonNull TagRegistry read(@NonNull ByteBuf buf) {
        return new TagRegistry(PackedIdentifierNetworkCodec.instance().read(buf),
                TagNetworkCodec.collectionCodec().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull TagRegistry object) {
        PackedIdentifierNetworkCodec.instance().write(buf, object.identifier());
        TagNetworkCodec.collectionCodec().write(buf, object.tags());
    }

    /**
     * Gets an instance of the {@linkplain TagRegistryNetworkCodec tag registry network codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull TagRegistryNetworkCodec instance() {
        return INSTANCE;
    }
}