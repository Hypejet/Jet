package net.hypejet.jet.server.network.protocol.codecs.component;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which reads and writes {@linkplain Component a component}
 * from/to a JSON string.
 *
 * @since 1.0
 * @author Codestech
 * @see Component
 * @see NetworkWriter
 */
public final class JsonComponentNetworkWriter implements NetworkWriter<Component> {

    /**
     * An instance of {@linkplain JsonComponentNetworkWriter a json component writer}.
     *
     * @since 1.0
     */
    public static final JsonComponentNetworkWriter INSTANCE =
            new JsonComponentNetworkWriter(JSONComponentSerializer.json());

    private final JSONComponentSerializer serializer;

    private JsonComponentNetworkWriter(@NonNull JSONComponentSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Component object) {
        StringNetworkCodec.INSTANCE.write(buf, this.serializer.serialize(object));
    }

    /**
     * Creates a {@linkplain JsonComponentNetworkWriter json component network writer}.
     *
     * @param serializer a serializer to serialize the components with
     * @return the component network writer, {@link #INSTANCE} if the serializer specified is the same as in
     *         the instance
     * @since 1.0
     */
    public static @NonNull JsonComponentNetworkWriter create(@NonNull JSONComponentSerializer serializer) {
        if (serializer == INSTANCE.serializer)
            return INSTANCE;
        return new JsonComponentNetworkWriter(serializer);
    }
}