package net.hypejet.jet.server.network.codec.game.component;

import io.netty.buffer.ByteBuf;
import java.util.Objects;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.miscellaneous.BinaryTagNetworkWriter;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.serializer.nbt.NBTComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer} of {@linkplain Style a style}.
 *
 * @since 1.0
 * @see NetworkWriter
 */
public final class StyleNetworkWriter implements NetworkWriter<Style> {

    /**
     * A default instance of the {@linkplain StyleNetworkWriter style network writer}.
     *
     * @since 1.0
     */
    public static final StyleNetworkWriter INSTANCE = new StyleNetworkWriter(NBTComponentSerializer.nbt());

    private final NBTComponentSerializer serializer;

    /**
     * Constructs the {@linkplain StyleNetworkWriter style network writer}.
     *
     * @param serializer a component serializer that should be used for the style serialization
     * @since 1.0
     */
    private StyleNetworkWriter(@NonNull NBTComponentSerializer serializer) {
        this.serializer = Objects.requireNonNull(serializer, "serializer");
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Style object) {
        BinaryTagNetworkWriter.INSTANCE.write(buf, this.serializer.serializeStyle(object));
    }
}