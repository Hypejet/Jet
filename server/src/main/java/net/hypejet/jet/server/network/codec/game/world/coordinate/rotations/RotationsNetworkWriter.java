package net.hypejet.jet.server.network.codec.game.world.coordinate.rotations;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.world.coordinate.rotation.Rotations;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer} of {@linkplain Rotations rotations}.
 *
 * @since 1.0
 * @see Rotations
 * @see NetworkWriter
 */
public final class RotationsNetworkWriter implements NetworkWriter<Rotations> {
    /**
     * An instance of the {@linkplain RotationsNetworkWriter rotations network writer}.
     *
     * @since 1.0
     */
    public static final RotationsNetworkWriter INSTANCE = new RotationsNetworkWriter();

    private RotationsNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager, @NonNull Rotations object) {
        buf.writeFloat(object.x());
        buf.writeFloat(object.y());
        buf.writeFloat(object.z());
    }
}