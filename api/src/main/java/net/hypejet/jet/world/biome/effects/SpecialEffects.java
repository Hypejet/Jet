package net.hypejet.jet.world.biome.effects;

import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.registry.Holder;
import net.hypejet.jet.util.game.random.Weighted;
import net.hypejet.jet.world.sound.SoundEvent;
import net.kyori.adventure.util.RGBLike;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * Special effects of a {@linkplain Biome biome}.
 *
 * @param fogColor the color of the fog effect when looking past the view distance
 * @param waterColor the tint color of the water blocks
 * @param waterFogColor the color of the fog effect when looking past the view distance underwater
 * @param skyColor the color of the sky
 * @param foliageColor the tint color of leaves, if {@code null}, it is calculated using other biome properties
 * @param dryFoliageColor the tint color of leaf litter, if {@code null}, it is calculated using other biome properties
 * @param grassColor the tint color of the grass, if {@code null}, it is calculated using other biome properties
 * @param grassColorModifier a modifier affecting the final grass color
 * @param ambientParticleSettings specification of the particle to be deployed on the biome, {@code null} if
 *                                no particle should be deployed
 * @param ambientLoopSoundEvent a holder of sound event representing an ambient soundtrack that starts playing in loop
 *                              when entering the biome and fades out when exiting, {@code null} if no loop sound
 *                              should be played
 * @param ambientMoodSettings an additional sound that starts playing in moody situations, {@code null} if no sound
 *                            should be played in situations of this kind
 * @param ambientAdditionsSettings an additional sound that has a change playing randomly every tick, {@code null} if
 *                                 such a sound should not be played
 * @param backgroundMusic a weighted list of possible music that should be played in the biome, {@code null} if
 *                        no music should be played
 * @param backgroundMusicVolume a volume of the music that should be played in the biome
 * @since 1.0
 */
public record SpecialEffects(@NonNull RGBLike fogColor, @NonNull RGBLike waterColor,
                             @NonNull RGBLike waterFogColor, @NonNull RGBLike skyColor,
                             @Nullable RGBLike foliageColor, @Nullable RGBLike dryFoliageColor,
                             @Nullable RGBLike grassColor, @NonNull GrassColorModifier grassColorModifier,
                             @Nullable AmbientParticleSettings ambientParticleSettings,
                             @Nullable Holder<SoundEvent> ambientLoopSoundEvent,
                             @Nullable AmbientMoodSettings ambientMoodSettings,
                             @Nullable AmbientAdditionsSettings ambientAdditionsSettings,
                             @Nullable List<Weighted<Music>> backgroundMusic, float backgroundMusicVolume) {
    /**
     * Constructs the {@linkplain SpecialEffects special effects}.
     *
     * @param fogColor the color of the fog effect when looking past the view distance
     * @param waterColor the tint color of the water blocks
     * @param waterFogColor the color of the fog effect when looking past the view distance underwater
     * @param skyColor the color of the sky
     * @param foliageColor the tint color of leaves, if {@code null}, it is calculated using other biome properties
     * @param dryFoliageColor the tint color of leaf litter, if {@code null}, it is calculated using
     *                        other biome properties
     * @param grassColor the tint color of the grass, if {@code null}, it is calculated using other biome properties
     * @param grassColorModifier a modifier affecting the final grass color
     * @param ambientParticleSettings specification of the particle to be deployed on the biome, {@code null} if
     *                                no particle should be deployed
     * @param ambientLoopSoundEvent a holder of sound event representing an ambient soundtrack that starts playing
     *                              in loop when entering the biome and fades out when exiting, {@code null} if no loop
     *                              sound should be played
     * @param ambientMoodSettings an additional sound that starts playing in moody situations, {@code null} if no sound
     *                            should be played in situations of this kind
     * @param ambientAdditionsSettings an additional sound that has a change playing randomly every tick, {@code null}
     *                                 if such a sound should not be played
     * @param backgroundMusic a weighted list of possible music that should be played in the biome, {@code null} if
     *                        no music should be played
     * @param backgroundMusicVolume a volume of the music that should be played in the biome
     */
    public SpecialEffects {
        Objects.requireNonNull(fogColor, "fog color");
        Objects.requireNonNull(waterColor, "water color");
        Objects.requireNonNull(waterFogColor, "water fog color");
        Objects.requireNonNull(skyColor, "sky color");
        Objects.requireNonNull(grassColorModifier, "grass color modifier");

        if (backgroundMusic != null)
            backgroundMusic = List.copyOf(backgroundMusic);
    }
}