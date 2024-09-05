package net.hypejet.jet.server.registry.codecs.registry.biome.effects;

import net.hypejet.jet.data.codecs.util.mapper.Mapper;
import net.hypejet.jet.data.model.registry.registries.biome.effects.BiomeEffectSettings;
import net.hypejet.jet.data.model.registry.registries.biome.effects.modifier.GrassColorModifier;
import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.ColorBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.registry.biome.effects.music.BiomeMusicBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.registry.biome.effects.particle.BiomeParticleSettingsBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.registry.biome.effects.sound.BiomeAdditionalSoundBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.registry.biome.effects.sound.BiomeMoodSoundBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.registry.biome.effects.sound.BiomeSoundEventBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.mapper.MapperBinaryTagCodec;
import net.hypejet.jet.server.util.BinaryTagUtil;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain BinaryTagCodec binary tag codec}, which reads and writes
 * a {@linkplain BiomeEffectSettings biome effect settings}.
 *
 * @since 1.0
 * @author Codestech
 * @see BiomeEffectSettings
 * @see BinaryTagCodec
 */
public final class BiomeEffectSettingsBinaryTagCodec implements BinaryTagCodec<BiomeEffectSettings> {

    private static final BiomeEffectSettingsBinaryTagCodec INSTANCE = new BiomeEffectSettingsBinaryTagCodec();

    private static final String FOG_COLOR = "fog_color";
    private static final String WATER_COLOR = "water_color";
    private static final String WATER_FOG_COLOR = "water_fog_color";
    private static final String SKY_COLOR = "sky_color";

    private static final String FOLIAGE_COLOR = "foliage_color";
    private static final String GRASS_COLOR = "grass_color";

    private static final String GRASS_COLOR_MODIFIER = "grass_color_modifier";
    private static final String PARTICLE = "particle";

    private static final String AMBIENT_SOUND = "ambient_sound";
    private static final String MOOD_SOUND = "mood_sound";
    private static final String ADDITIONS_SOUND = "additions_sound";

    private static final String MUSIC = "music";

    private static final BinaryTagCodec<GrassColorModifier> GRASS_COLOR_MODIFIER_CODEC = MapperBinaryTagCodec
            .stringCodec(Mapper.builder(GrassColorModifier.class, String.class)
                    .register(GrassColorModifier.NONE, "none")
                    .register(GrassColorModifier.DARK_FOREST, "dark_forest")
                    .register(GrassColorModifier.SWAMP, "swamp")
                    .build());

    private BiomeEffectSettingsBinaryTagCodec() {}

    @Override
    public @NonNull BiomeEffectSettings read(@NonNull BinaryTag tag) {
        if (!(tag instanceof CompoundBinaryTag compound))
            throw new IllegalArgumentException("The binary tag is not a compound binary tag");

        BinaryTag binaryFogColor = compound.get(FOG_COLOR);
        BinaryTag binaryWaterColor = compound.get(WATER_COLOR);
        BinaryTag binaryWaterFogColor = compound.get(WATER_FOG_COLOR);
        BinaryTag binarySkyColor = compound.get(SKY_COLOR);

        if (binaryFogColor == null)
            throw new IllegalArgumentException("The fog color was not specified");
        if (binaryWaterColor == null)
            throw new IllegalArgumentException("The water color was not specified");
        if (binaryWaterFogColor == null)
            throw new IllegalArgumentException("The water fog color was not specified");
        if (binarySkyColor == null)
            throw new IllegalArgumentException("The sky color was not specified");

        ColorBinaryTagCodec colorCodec = ColorBinaryTagCodec.instance();

        return BiomeEffectSettings.builder()
                .fogColor(colorCodec.read(binaryFogColor))
                .waterColor(colorCodec.read(binaryWaterColor))
                .waterFogColor(colorCodec.read(binaryWaterFogColor))
                .skyColor(colorCodec.read(binarySkyColor))
                .foliageColorOverride(BinaryTagUtil.read(FOLIAGE_COLOR, compound, colorCodec))
                .grassColorOverride(BinaryTagUtil.read(GRASS_COLOR, compound, colorCodec))
                .grassColorModifier(BinaryTagUtil.read(GRASS_COLOR_MODIFIER, compound, GRASS_COLOR_MODIFIER_CODEC))
                .particleSettings(BinaryTagUtil.read(PARTICLE, compound, BiomeParticleSettingsBinaryTagCodec.instance()))
                .ambientSound(BinaryTagUtil.read(AMBIENT_SOUND, compound, BiomeSoundEventBinaryTagCodec.instance()))
                .moodSound(BinaryTagUtil.read(MOOD_SOUND, compound, BiomeMoodSoundBinaryTagCodec.instance()))
                .additionalSound(BinaryTagUtil.read(ADDITIONS_SOUND, compound,
                        BiomeAdditionalSoundBinaryTagCodec.instance()))
                .biomeMusic(BinaryTagUtil.read(MUSIC, compound, BiomeMusicBinaryTagCodec.instance()))
                .build();
    }

    @Override
    public @NonNull BinaryTag write(@NonNull BiomeEffectSettings object) {
        ColorBinaryTagCodec colorCodec = ColorBinaryTagCodec.instance();

        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
                .put(FOG_COLOR, colorCodec.write(object.fogColor()))
                .put(WATER_COLOR, colorCodec.write(object.waterColor()))
                .put(WATER_FOG_COLOR, colorCodec.write(object.waterFogColor()))
                .put(SKY_COLOR, colorCodec.write(object.skyColor()));

        BinaryTagUtil.write(FOLIAGE_COLOR, object.foliageColorOverride(), builder, colorCodec);
        BinaryTagUtil.write(GRASS_COLOR, object.grassColorOverride(), builder, colorCodec);
        BinaryTagUtil.write(GRASS_COLOR_MODIFIER, object.grassColorModifier(), builder, GRASS_COLOR_MODIFIER_CODEC);

        BinaryTagUtil.write(PARTICLE, object.particleSettings(), builder, BiomeParticleSettingsBinaryTagCodec.instance());

        BinaryTagUtil.write(AMBIENT_SOUND, object.ambientSound(), builder, BiomeSoundEventBinaryTagCodec.instance());
        BinaryTagUtil.write(MOOD_SOUND, object.moodSound(), builder, BiomeMoodSoundBinaryTagCodec.instance());
        BinaryTagUtil.write(ADDITIONS_SOUND, object.additionalSound(), builder,
                BiomeAdditionalSoundBinaryTagCodec.instance());

        BinaryTagUtil.write(MUSIC, object.music(), builder, BiomeMusicBinaryTagCodec.instance());

        return builder.build();
    }

    /**
     * Gets an instance of the {@linkplain BiomeEffectSettingsBinaryTagCodec biome effect settings binary-tag codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull BiomeEffectSettingsBinaryTagCodec instance() {
        return INSTANCE;
    }
}