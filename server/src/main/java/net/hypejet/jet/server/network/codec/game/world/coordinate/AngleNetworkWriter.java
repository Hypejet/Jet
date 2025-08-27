package net.hypejet.jet.server.network.codec.game.world.coordinate;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer} writing angles (in degrees) as a byte.
 *
 * @since 1.0
 * @see NetworkWriter
 */
public final class AngleNetworkWriter implements NetworkWriter<Float> {
    /**
     * An instance of the {@linkplain AngleNetworkWriter angle network writer}.
     *
     * @since 1.0
     */
    public static final AngleNetworkWriter INSTANCE = new AngleNetworkWriter();

    private AngleNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Float object) {
        buf.writeByte((byte) Math.floor(object * 256f / 360f));
    }
}