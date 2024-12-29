package net.hypejet.jet.server.registry.writers.registry.biome.effects;

import net.hypejet.jet.data.codecs.util.mapper.Mapper;
import net.hypejet.jet.data.model.api.registries.biome.effects.BiomeEffectSettings;
import net.hypejet.jet.data.model.api.registries.biome.effects.modifier.GrassColorModifier;
import net.hypejet.jet.server.registry.writers.color.ColorBinaryTagWriter;
import net.hypejet.jet.server.registry.writers.mapper.MapperBinaryTagWriter;
import net.hypejet.jet.server.registry.writers.registry.biome.effects.music.BiomeMusicBinaryTagWriter;
import net.hypejet.jet.server.registry.writers.registry.biome.effects.particle.BiomeParticleSettingsBinaryTagWriter;
import net.hypejet.jet.server.registry.writers.registry.biome.effects.sound.BiomeAdditionalSoundBinaryTagWriter;
import net.hypejet.jet.server.registry.writers.registry.biome.effects.sound.BiomeMoodSoundBinaryTagWriter;
import net.hypejet.jet.server.registry.writers.registry.biome.effects.sound.BiomeSoundEventBinaryTagWriter;
import net.hypejet.jet.server.util.BinaryTagUtil;
import net.hypejet.jet.server.util.codec.Writer;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain Writer a writer}, which writes {@linkplain BiomeEffectSettings biome effect settings}
 * into {@linkplain CompoundBinaryTag a compound binary tag}.
 *
 * @since 1.0
 * @see BiomeEffectSettings
 * @see CompoundBinaryTag
 * @see Writer
 */
public final class BiomeEffectSettingsBinaryTagWriter implements Writer<BiomeEffectSettings, CompoundBinaryTag> {
    /**
     * An instance of the {@linkplain BiomeEffectSettingsBinaryTagWriter biome effect settings binary tag writer}.
     *
     * @since 1.0
     */
    public static final BiomeEffectSettingsBinaryTagWriter INSTANCE = new BiomeEffectSettingsBinaryTagWriter();

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

    private static final Writer<GrassColorModifier, StringBinaryTag> GRASS_COLOR_MODIFIER_WRITER =
            MapperBinaryTagWriter.stringCodec(Mapper.builder(GrassColorModifier.class, String.class)
                    .register(GrassColorModifier.NONE, "none")
                    .register(GrassColorModifier.DARK_FOREST, "dark_forest")
                    .register(GrassColorModifier.SWAMP, "swamp")
                    .build());

    private BiomeEffectSettingsBinaryTagWriter() {}

    @Override
    public @NonNull CompoundBinaryTag write(@NonNull BiomeEffectSettings object) {
        ColorBinaryTagWriter colorCodec = ColorBinaryTagWriter.INSTANCE;

        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
                .put(FOG_COLOR, colorCodec.write(object.fogColor()))
                .put(WATER_COLOR, colorCodec.write(object.waterColor()))
                .put(WATER_FOG_COLOR, colorCodec.write(object.waterFogColor()))
                .put(SKY_COLOR, colorCodec.write(object.skyColor()));

        BinaryTagUtil.writeOptional(FOLIAGE_COLOR, object.foliageColorOverride(), builder, colorCodec);
        BinaryTagUtil.writeOptional(GRASS_COLOR, object.grassColorOverride(), builder, colorCodec);
        BinaryTagUtil.writeOptional(GRASS_COLOR_MODIFIER, object.grassColorModifier(), builder,
                GRASS_COLOR_MODIFIER_WRITER);

        BinaryTagUtil.writeOptional(PARTICLE, object.particleSettings(), builder,
                BiomeParticleSettingsBinaryTagWriter.INSTANCE);

        BinaryTagUtil.writeOptional(AMBIENT_SOUND, object.ambientSound(), builder,
                BiomeSoundEventBinaryTagWriter.INSTANCE);
        BinaryTagUtil.writeOptional(MOOD_SOUND, object.moodSound(), builder,
                BiomeMoodSoundBinaryTagWriter.INSTANCE);
        BinaryTagUtil.writeOptional(ADDITIONS_SOUND, object.additionalSound(), builder,
                BiomeAdditionalSoundBinaryTagWriter.INSTANCE);

        BinaryTagUtil.writeOptional(MUSIC, object.music(), builder, BiomeMusicBinaryTagWriter.INSTANCE);

        return builder.build();
    }
}