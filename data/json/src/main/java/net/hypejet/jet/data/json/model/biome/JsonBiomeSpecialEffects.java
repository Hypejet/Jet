package net.hypejet.jet.data.json.model.biome;

import net.hypejet.jet.data.json.model.JsonHolder;
import net.hypejet.jet.data.json.model.JsonSoundEvent;
import net.hypejet.jet.data.json.model.JsonWeighted;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * Special effects of a {@linkplain JsonBiome biome}.
 *
 * @param fogColor the color of the fog effect when looking past the view distance
 * @param waterColor the tint color of the water blocks
 * @param waterFogColor the color of the fog effect when looking past the view distance underwater
 * @param skyColor the color of the sky
 * @param foliageColor the tint color of leaves, if {@code null}, it is calculated using other biome properties
 * @param dryFoliageColor the tint color of leaf litter, if {@code null}, it is calculated using other biome properties
 * @param grassColor the tint color of the grass, if {@code null}, it is calculated using other biome properties
 * @param grassColorModifier a modifier affecting the final grass color
 * @param particleSettings specification of the particle to be deployed on the biome, {@code null} if no particle
 *                         should be deployed
 * @param ambientSound a holder of sound event representing an ambient soundtrack that starts playing in loop when
 *                     entering the biome and fades out when exiting, {@code null} if no loop sound should be played
 * @param moodSound an additional sound that starts playing in moody situations, {@code null} if no sound should
 *                  be played in situations of this kind
 * @param additionsSound an additional sound that has a change playing randomly every tick, {@code null} if such
 *                       a sound should not be played
 * @param music a weighted list of possible music that should be played in the biome, {@code null} if no music
 *              should be played
 * @param musicVolume a volume of the music that should be played in the biome
 * @since 1.0
 */
public record JsonBiomeSpecialEffects(int fogColor, int waterColor, int waterFogColor, int skyColor,
                                      @Nullable Integer foliageColor, @Nullable Integer dryFoliageColor,
                                      @Nullable Integer grassColor, @NonNull JsonGrassColorModifier grassColorModifier,
                                      @Nullable JsonAmbientParticleSettings particleSettings,
                                      @Nullable JsonHolder<JsonSoundEvent> ambientSound,
                                      @Nullable JsonAmbientMoodSound moodSound,
                                      @Nullable JsonAdditionsSound additionsSound,
                                      @NonNull List<JsonWeighted<JsonMusic>> music, float musicVolume) {
    /**
     * Constructs the {@linkplain JsonBiomeSpecialEffects biome special effects}.
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
     * @param particleSettings specification of the particle to be deployed on the biome, {@code null} if no particle
     *                         should be deployed
     * @param ambientSound a holder of sound event representing an ambient soundtrack that starts playing in loop when
     *                     entering the biome and fades out when exiting, {@code null} if no loop sound
     *                     should be played
     * @param moodSound an additional sound that starts playing in moody situations, {@code null} if no sound should
     *                  be played in situations of this kind
     * @param additionsSound an additional sound that has a change playing randomly every tick, {@code null} if such
     *                       a sound should not be played
     * @param music a weighted list of possible music that should be played in the biome, {@code null} if no music
     *              should be played
     * @param musicVolume a volume of the music that should be played in the biome
     * @since 1.0
     */
    public JsonBiomeSpecialEffects {
        Objects.requireNonNull(grassColorModifier, "grass color modifier");
        music = List.copyOf(Objects.requireNonNull(music, "music"));
    }
}