package net.hypejet.jet.server.network.codec.util.color;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.util.color.ARGBColor;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer} of {@linkplain ARGBColor ARGB colors}.
 *
 * @since 1.0
 * @see ARGBColor
 * @see NetworkWriter
 */
public final class ARGBColorNetworkWriter implements NetworkWriter<ARGBColor> {
    /**
     * An instance of the {@linkplain ARGBColorNetworkWriter ARGB color network writer}.
     *
     * @since 1.0
     */
    public static final ARGBColorNetworkWriter INSTANCE = new ARGBColorNetworkWriter();

    private ARGBColorNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf,
                      @NonNull JetRegistryManager registryManager,
                      @NonNull ARGBColor object) {
        buf.writeInt(object.value());
    }
}