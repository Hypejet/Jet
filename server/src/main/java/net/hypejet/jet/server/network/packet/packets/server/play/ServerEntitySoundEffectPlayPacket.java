package net.hypejet.jet.server.network.packet.packets.server.play;

import java.util.Objects;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.world.sound.SoundEvent;
import net.kyori.adventure.sound.Sound;

/**
 * A {@linkplain ServerPacket server packet} playing a {@linkplain SoundEvent sound event}
 * on the client using the given {@linkplain Sound sound settings}.
 *
 * @param soundEvent a sound event that should be played
 * @param sound a sound containing source, volume, pitch and seed information
 *
 * @since 1.0
 * @see SoundEvent
 * @see Sound
 * @see ServerPacket
 */
@NullMarked
public record ServerEntitySoundEffectPlayPacket(SoundEvent soundEvent, Sound sound) implements ServerPacket {

    /**
     * Constructs the {@linkplain ServerEntitySoundEffectPlayPacket server entity sound effect play packet}.
     *
     * @param soundEvent a sound event that should be played
     * @param sound a sound containing source, volume, pitch and seed information
     *
     * @since 1.0
     */
    public ServerEntitySoundEffectPlayPacket {
        Objects.requireNonNull(soundEvent, "sound event");
        Objects.requireNonNull(sound, "sound");
    }
}
