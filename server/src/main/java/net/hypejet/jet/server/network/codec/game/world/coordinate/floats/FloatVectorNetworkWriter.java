package net.hypejet.jet.server.network.codec.game.world.coordinate.floats;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.world.coordinate.floats.FloatVector;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer} of {@linkplain FloatVector float vectors}.
 *
 * @since 1.0
 * @see FloatVector
 * @see NetworkWriter
 */
public final class FloatVectorNetworkWriter implements NetworkWriter<FloatVector> {
    /**
     * An instance of the {@linkplain FloatVectorNetworkWriter float vector network writer}.
     *
     * @since 1.0
     */
    public static final FloatVectorNetworkWriter INSTANCE = new FloatVectorNetworkWriter();

    private FloatVectorNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf,
                      @NonNull JetRegistryManager registryManager,
                      @NonNull FloatVector object) {
        buf.writeFloat(object.x());
        buf.writeFloat(object.y());
        buf.writeFloat(object.z());
    }
}