package net.hypejet.jet.server.network.codec.util.color;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.util.color.RGBColor;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer} of {@linkplain RGBColor RGB colors}.
 *
 * @since 1.0
 * @see RGBColor
 * @see NetworkWriter
 */
public final class RGBColorNetworkWriter implements NetworkWriter<RGBColor> {
    /**
     * An instance of the {@linkplain RGBColorNetworkWriter RGB color network writer}.
     *
     * @since 1.0
     */
    public static final RGBColorNetworkWriter INSTANCE = new RGBColorNetworkWriter();

    private RGBColorNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager, @NonNull RGBColor object) {
        buf.writeInt(object.value());
    }
}