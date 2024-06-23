package net.hypejet.jet.server.network.protocol.codecs.tag;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerUpdateTagsConfigurationPacket.TagRegistry;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.identifier.IdentifierNetworkCodec;
import net.hypejet.jet.server.util.NetworkUtil;
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
        return new TagRegistry(IdentifierNetworkCodec.instance().read(buf),
                NetworkUtil.readCollection(buf, TagNetworkCodec.instance()));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull TagRegistry object) {
        IdentifierNetworkCodec.instance().write(buf, object.identifier());
        NetworkUtil.writeCollection(buf, TagNetworkCodec.instance(), object.tags());
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