package net.hypejet.jet.server.network.codec.game.world.sound;

import org.checkerframework.checker.nullness.qual.NonNull;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.registry.reference.RegistryReference;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.world.sound.SoundEvent;

/**
 * A {@linkplain NetworkWriter network writer} writing {@linkplain SoundEvent sound events}.
 *
 * @since 1.0
 * @see NetworkWriter
 * @see SoundEvent
 */
public final class SoundEventNetworkWriter implements NetworkWriter<SoundEvent>{

    /**
     * An instance of the {@linkplain SoundEventNetworkWriter sound event network writer}.
     *
     * @since 1.0
     */
    public static final SoundEventNetworkWriter INSTANCE = new SoundEventNetworkWriter();

    private SoundEventNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager, @NonNull SoundEvent object) {
        int soundEventId = registryManager
                .registry(RegistryReference.SOUND_EVENT)
                .indexOf(object.sound());

        VarIntNetworkCodec.INSTANCE.write(buf, registryManager, soundEventId);
    }
}
