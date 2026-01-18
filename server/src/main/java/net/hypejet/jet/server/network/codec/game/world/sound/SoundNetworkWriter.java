package net.hypejet.jet.server.network.codec.game.world.sound;

import org.checkerframework.checker.nullness.qual.NonNull;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.kyori.adventure.sound.Sound;

/**
 * A {@linkplain NetworkWriter network writer} writing {@linkplain Sound sound}.
 *
 * @since 1.0
 * @see NetworkWriter
 * @see Sound
 */
public final class SoundNetworkWriter implements NetworkWriter<Sound> {

    /**
     * An instance of the {@linkplain SoundNetworkWriter sound network writer}.
     *
     * @since 1.0
     */
    public static final SoundNetworkWriter INSTANCE = new SoundNetworkWriter();

    private SoundNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager, @NonNull Sound object) {
        VarIntNetworkCodec.INSTANCE.write(buf, registryManager, object.source().ordinal());
        buf.writeFloat(object.volume());
        buf.writeFloat(object.pitch());
        buf.writeFloat(object.seed().orElse(0L));
    }
}
