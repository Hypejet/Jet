package net.hypejet.jet.server.network.codec.packet.server.play;

import org.checkerframework.checker.nullness.qual.NonNull;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.sound.SoundEventNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.sound.SoundNetworkWriter;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerEntitySoundEffectPlayPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;

/**
 * A {@linkplain NetworkWriter network writer}
 * of {@linkplain ServerEntitySoundEffectPlayPacket server entity sound effect play packets}.
 *
 * @since 1.0
 * @see ServerEntitySoundEffectPlayPacket
 * @see NetworkWriter
 */
public final class ServerEntitySoundEffectPlayPacketWriter implements NetworkWriter<ServerEntitySoundEffectPlayPacket> {

    /**
     * An instance of the {@linkplain ServerEntitySoundEffectPlayPacketWriter server entity sound effect play packet writer}.
     *
     * @since 1.0
     */
    public static final ServerEntitySoundEffectPlayPacketWriter INSTANCE = new ServerEntitySoundEffectPlayPacketWriter();

    private ServerEntitySoundEffectPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager,
            @NonNull ServerEntitySoundEffectPlayPacket object) {
        SoundEventNetworkWriter.INSTANCE.write(buf, registryManager, object.soundEvent());
        SoundNetworkWriter.INSTANCE.write(buf, registryManager, object.sound());
    }
}
