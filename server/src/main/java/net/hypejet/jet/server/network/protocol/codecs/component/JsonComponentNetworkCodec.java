package net.hypejet.jet.server.network.protocol.codecs.component;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain Component component}
 * from/to a JSON string.
 *
 * @since 1.0
 * @author Codestech
 * @see Component
 * @see NetworkCodec
 */
public final class JsonComponentNetworkCodec implements NetworkCodec<Component> {

    private static final JsonComponentNetworkCodec INSTANCE = new JsonComponentNetworkCodec(
            JSONComponentSerializer.json()
    );

    private final JSONComponentSerializer serializer;

    private JsonComponentNetworkCodec(@NonNull JSONComponentSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public @NonNull Component read(@NonNull ByteBuf buf) {
        return this.serializer.deserialize(StringNetworkCodec.instance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Component object) {
        StringNetworkCodec.instance().write(buf, this.serializer.serialize(object));
    }

    /**
     * Creates a {@linkplain JsonComponentNetworkCodec json component network codec}.
     *
     * @param serializer a serializer to serialize the components with
     * @return the component network codec, {@link #INSTANCE} if the serializer specified
     *         is {@link JSONComponentSerializer#json()}
     * @since 1.0
     */
    public static @NonNull JsonComponentNetworkCodec create(@NonNull JSONComponentSerializer serializer) {
        if (serializer == JSONComponentSerializer.json())
            return INSTANCE;
        return new JsonComponentNetworkCodec(serializer);
    }

    /**
     * Gets an instance of the {@linkplain JSONComponentSerializer json component serializer}, which uses
     * {@link JSONComponentSerializer#json()}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull JsonComponentNetworkCodec instance() {
        return INSTANCE;
    }
}