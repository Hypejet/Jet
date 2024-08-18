package net.hypejet.jet.biome.effects;

import net.hypejet.jet.biome.effects.modifier.GrassColorModifier;
import net.hypejet.jet.biome.effects.music.BiomeMusic;
import net.hypejet.jet.biome.effects.particle.BiomeParticleSettings;
import net.hypejet.jet.biome.effects.sound.BiomeAdditionalSound;
import net.hypejet.jet.biome.effects.sound.BiomeAmbientSound;
import net.hypejet.jet.biome.effects.sound.BiomeMoodSound;
import net.hypejet.jet.color.Color;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/**
 * Represents effects of a {@linkplain net.hypejet.jet.biome.Biome biome}.
 *
 * @param fogColor a color of the fog that the biome should have
 * @param waterColor a color of the water that the biome should have
 * @param waterFogColor a color of the underwater fog that the biome should have
 * @param skyColor a color of the sky that the biome should have
 * @param foliageColorOverride a color of leaves that the biome should have
 * @param grassColorOverride a color of grass that the biome should have, {@code null} if the value should be
 *                           based on temperature and downfall of the biome
 * @param grassColorModifier a modifier of the grass color that the biome should have, {@code null} if none
 * @param particleSettings a particle settings of the biome, {@code null} if none
 * @param ambientSound an ambient sound of the biome, {@code null} if none
 * @param moodSound a mood sound of the biome, {@code null} if none
 * @param additionalSound an additional sound of the biome, {@code null} if none
 * @param music a music of the biome, {@code null} if none
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.biome.Biome
 */
public record BiomeEffectSettings(@NonNull Color fogColor, @NonNull Color waterColor, @NonNull Color waterFogColor,
                                  @NonNull Color skyColor, @Nullable Color foliageColorOverride,
                                  @Nullable Color grassColorOverride, @Nullable GrassColorModifier grassColorModifier,
                                  @Nullable BiomeParticleSettings particleSettings,
                                  @Nullable BiomeAmbientSound ambientSound, @Nullable BiomeMoodSound moodSound,
                                  @Nullable BiomeAdditionalSound additionalSound, @Nullable BiomeMusic music) {
    /**
     * Constructs the {@linkplain BiomeEffectSettings biome effect settings}.
     *
     * @param fogColor a color of the fog that the biome should have
     * @param waterColor a color of the water that the biome should have
     * @param waterFogColor a color of the underwater fog that the biome should have
     * @param skyColor a color of the sky that the biome should have
     * @param foliageColorOverride a color of leaves that the biome should have
     * @param grassColorOverride a color of grass that the biome should have, {@code null} if the value should be
     *                           based on temperature and downfall of the biome
     * @param grassColorModifier a modifier of the grass color that the biome should have, {@code null} if none
     * @param particleSettings a particle settings of the biome, {@code null} if none
     * @param ambientSound an ambient sound of the biome, {@code null} if none
     * @param moodSound a mood sound of the biome, {@code null} if none
     * @param additionalSound an additional sound of the biome, {@code null} if none
     * @param music a music of the biome, {@code null} if none
     * @since 1.0
     */
    public BiomeEffectSettings {
        Objects.requireNonNull(fogColor, "The fog color must not be null");
        Objects.requireNonNull(waterColor, "The water color must not be null");
        Objects.requireNonNull(waterFogColor, "The water fog color must not be null");
        Objects.requireNonNull(skyColor, "The sky color must not be null");
    }

    /**
     * Creates a new {@linkplain Builder biome effect settings builder}.
     *
     * @return the new biome effect settings builder created
     * @since 1.0
     */
    public static @NonNull Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Color fogColor;
        private Color waterColor;
        private Color waterFogColor;
        private Color skyColor;

        private Color foliageColorOverride;
        private Color grassColorOverride;

        private GrassColorModifier grassColorModifier;
        private BiomeParticleSettings particleSettings;

        private BiomeAmbientSound ambientSound;
        private BiomeMoodSound moodSound;
        private BiomeAdditionalSound additionalSound;
        private BiomeMusic biomeMusic;

        private Builder() {}

        /**
         * Sets a fog {@linkplain Color color} that the {@linkplain BiomeEffectSettings biome effect settings} should
         * have.
         *
         * @param fogColor the fog color
         * @return this builder
         */
        public @NonNull Builder fogColor(@NonNull Color fogColor) {
            this.fogColor = Objects.requireNonNull(fogColor, "The fog color must not be null");
            return this;
        }

        /**
         * Sets a water {@linkplain Color color} that the {@linkplain BiomeEffectSettings biome effect settings} should
         * have.
         *
         * @param waterColor the water color
         * @return this builder
         */
        public @NonNull Builder waterColor(@NonNull Color waterColor) {
            this.waterColor = Objects.requireNonNull(waterColor, "The water color must not be null");
            return this;
        }

        /**
         * Sets a water fog {@linkplain Color color} that the {@linkplain BiomeEffectSettings biome effect settings}
         * should have.
         *
         * @param waterFogColor the water fog color
         * @return this builder
         */
        public @NonNull Builder waterFogColor(@NonNull Color waterFogColor) {
            this.waterFogColor = Objects.requireNonNull(waterFogColor, "The water fog color must not be null");
            return this;
        }

        /**
         * Sets a sky {@linkplain Color color} that the {@linkplain BiomeEffectSettings biome effect settings} should
         * have.
         *
         * @param skyColor the sky color
         * @return this builder
         */
        public @NonNull Builder skyColor(@NonNull Color skyColor) {
            this.skyColor = Objects.requireNonNull(skyColor, "The sky color must not be null");
            return this;
        }

        /**
         * Sets a foliage {@linkplain Color color} that the {@linkplain BiomeEffectSettings biome effect settings}
         * should have.
         *
         * @param foliageColorOverride the foliage color
         * @return this builder
         */
        public @NonNull Builder foliageColorOverride(@Nullable Color foliageColorOverride) {
            this.foliageColorOverride = foliageColorOverride;
            return this;
        }

        /**
         * Sets a grass {@linkplain Color color} that the {@linkplain BiomeEffectSettings biome effect settings}
         * should have.
         *
         * @param grassColorOverride the grass color
         * @return this builder
         */
        public @NonNull Builder grassColorOverride(@Nullable Color grassColorOverride) {
            this.grassColorOverride = grassColorOverride;
            return this;
        }

        /**
         * Sets a {@linkplain GrassColorModifier grass color modifier} that the {@linkplain BiomeEffectSettings biome
         * effect settings} should have.
         *
         * @param grassColorModifier the grass color modifier
         * @return this builder
         */
        public @NonNull Builder grassColorModifier(@Nullable GrassColorModifier grassColorModifier) {
            this.grassColorModifier = grassColorModifier;
            return this;
        }

        /**
         * Sets a {@linkplain BiomeParticleSettings particle settings} that the {@linkplain BiomeEffectSettings biome
         * effect settings} should have.
         *
         * @param particleSettings the particle settings
         * @return this builder
         */
        public @NonNull Builder particleSettings(@Nullable BiomeParticleSettings particleSettings) {
            this.particleSettings = particleSettings;
            return this;
        }

        /**
         * Sets a {@linkplain BiomeAmbientSound ambient sound} that the {@linkplain BiomeEffectSettings biome
         * effect settings} should have.
         *
         * @param ambientSound the ambient sound
         * @return this builder
         */
        public @NonNull Builder ambientSound(@Nullable BiomeAmbientSound ambientSound) {
            this.ambientSound = ambientSound;
            return this;
        }

        /**
         * Sets a {@linkplain BiomeMoodSound mood sound} that the {@linkplain BiomeEffectSettings biome
         * effect settings} should have.
         *
         * @param moodSound the mood sound
         * @return this builder
         */
        public @NonNull Builder moodSound(@Nullable BiomeMoodSound moodSound) {
            this.moodSound = moodSound;
            return this;
        }

        /**
         * Sets a {@linkplain BiomeAdditionalSound additional sound} that the {@linkplain BiomeEffectSettings biome
         * effect settings} should have.
         *
         * @param additionalSound the additional sound
         * @return this builder
         */
        public @NonNull Builder additionalSound(@Nullable BiomeAdditionalSound additionalSound) {
            this.additionalSound = additionalSound;
            return this;
        }

        /**
         * Sets a {@linkplain BiomeMusic biome music} that the {@linkplain BiomeEffectSettings biome
         * effect settings} should have.
         *
         * @param biomeMusic the biome music
         * @return this builder
         */
        public @NonNull Builder biomeMusic(@Nullable BiomeMusic biomeMusic) {
            this.biomeMusic = biomeMusic;
            return this;
        }

        /**
         * Builds a {@linkplain BiomeEffectSettings biome effect settings} with properties specified in this
         * {@linkplain Builder builder}.
         *
         * @return the biome effect settings
         * @since 1.0
         */
        public @NonNull BiomeEffectSettings build() {
            return new BiomeEffectSettings(
                    Objects.requireNonNull(this.fogColor, "The fog color was not specified"),
                    Objects.requireNonNull(this.waterColor, "The water color was not specified"),
                    Objects.requireNonNull(this.waterFogColor, "The water fog color was not specified"),
                    Objects.requireNonNull(this.skyColor, "The sky color was not specified"),
                    this.foliageColorOverride, this.grassColorOverride, this.grassColorModifier, this.particleSettings,
                    this.ambientSound, this.moodSound, this.additionalSound, this.biomeMusic
            );
        }
    }
}