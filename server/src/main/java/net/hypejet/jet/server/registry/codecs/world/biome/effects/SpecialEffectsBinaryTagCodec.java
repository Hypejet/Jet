package net.hypejet.jet.server.registry.codecs.world.biome.effects;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.IndexBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.primitive.ListBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.primitive.StringBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.util.color.ColorBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.util.random.WeightedBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.sound.SoundEventBinaryTagCodec;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.hypejet.jet.util.color.Color;
import net.hypejet.jet.util.game.random.Weighted;
import net.hypejet.jet.world.biome.effects.AmbientAdditionsSettings;
import net.hypejet.jet.world.biome.effects.AmbientMoodSettings;
import net.hypejet.jet.world.biome.effects.AmbientParticleSettings;
import net.hypejet.jet.world.biome.effects.GrassColorModifier;
import net.hypejet.jet.world.biome.effects.Music;
import net.hypejet.jet.world.biome.effects.SpecialEffects;
import net.hypejet.jet.world.sound.SoundEvent;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain SpecialEffects special effects}.
 *
 * @since 1.0
 * @see SpecialEffects
 * @see BinaryTagCodec
 */
public final class SpecialEffectsBinaryTagCodec implements BinaryTagCodec<SpecialEffects> {

    private static final String FOG_COLOR_FIELD = "fog_color";
    private static final String WATER_COLOR_FIELD = "water_color";
    private static final String WATER_FOG_COLOR_FIELD = "water_fog_color";
    private static final String SKY_COLOR_FIELD = "sky_color";
    private static final String FOLIAGE_COLOR_FIELD = "foliage_color";
    private static final String DRY_FOLIAGE_COLOR_FIELD = "dry_foliage_color";
    private static final String GRASS_COLOR_FIELD = "grass_color";
    private static final String GRASS_COLOR_MODIFIER_FIELD = "grass_color_modifier";
    private static final String PARTICLE_SETTINGS_FIELD = "particle";
    private static final String AMBIENT_LOOP_SOUND_FIELD = "ambient_sound";
    private static final String AMBIENT_MOOD_SETTINGS_FIELD = "mood_sound";
    private static final String AMBIENT_ADDITIONS_SOUND_FIELD = "additions_sound";
    private static final String BACKGROUND_MUSIC_FIELD = "music";
    private static final String BACKGROUND_MUSIC_VOLUME_FIELD = "music_volume";

    private static final BinaryTagCodec<GrassColorModifier> GRASS_COLOR_MODIFIER_CODEC = new IndexBinaryTagCodec<>(
            IndexUtil.fromMap(Map.of(
                    "none", GrassColorModifier.NONE,
                    "dark_forest", GrassColorModifier.DARK_FOREST,
                    "swamp", GrassColorModifier.SWAMP
            )),
            StringBinaryTagCodec.INSTANCE
    );

    private static final BinaryTagCodec<List<Weighted<Music>>> MUSIC_WEIGHTED_LIST_CODEC = new ListBinaryTagCodec<>(
            new WeightedBinaryTagCodec<>(MusicBinaryTagCodec.INSTANCE)
    );

    /**
     * An instance of the {@linkplain SpecialEffectsBinaryTagCodec special effects binary tag codec}.
     *
     * @since 1.0
     */
    public static final SpecialEffectsBinaryTagCodec INSTANCE = new SpecialEffectsBinaryTagCodec();

    private SpecialEffectsBinaryTagCodec() {}

    @Override
    public @NotNull SpecialEffects decode(@NotNull BinaryTag encoded) throws Exception {
        if (encoded instanceof CompoundBinaryTag compound) {
            BinaryTag foliageColorTag = compound.get(FOLIAGE_COLOR_FIELD);
            BinaryTag dryFoliageColorTag = compound.get(DRY_FOLIAGE_COLOR_FIELD);
            BinaryTag grassColorTag = compound.get(GRASS_COLOR_FIELD);
            BinaryTag grassColorModifierTag = compound.get(GRASS_COLOR_MODIFIER_FIELD);
            BinaryTag ambientParticleSettingsTag = compound.get(PARTICLE_SETTINGS_FIELD);
            BinaryTag ambientLoopSoundTag = compound.get(AMBIENT_LOOP_SOUND_FIELD);
            BinaryTag ambientMoodSettingsTag = compound.get(AMBIENT_MOOD_SETTINGS_FIELD);
            BinaryTag ambientAdditionsSettingsTag = compound.get(AMBIENT_ADDITIONS_SOUND_FIELD);
            BinaryTag backgroundMusicTag = compound.get(BACKGROUND_MUSIC_FIELD);
            return new SpecialEffects(
                    ColorBinaryTagCodec.INSTANCE.decode(requiredTag(FOG_COLOR_FIELD, compound)),
                    ColorBinaryTagCodec.INSTANCE.decode(requiredTag(WATER_COLOR_FIELD, compound)),
                    ColorBinaryTagCodec.INSTANCE.decode(requiredTag(WATER_FOG_COLOR_FIELD, compound)),
                    ColorBinaryTagCodec.INSTANCE.decode(requiredTag(SKY_COLOR_FIELD, compound)),
                    foliageColorTag == null
                            ? null
                            : ColorBinaryTagCodec.INSTANCE.decode(foliageColorTag),
                    dryFoliageColorTag == null
                            ? null
                            : ColorBinaryTagCodec.INSTANCE.decode(dryFoliageColorTag),
                    grassColorTag == null
                            ? null
                            : ColorBinaryTagCodec.INSTANCE.decode(grassColorTag),
                    grassColorModifierTag == null
                            ? GrassColorModifier.NONE
                            : GRASS_COLOR_MODIFIER_CODEC.decode(requiredTag(GRASS_COLOR_MODIFIER_FIELD, compound)),
                    ambientParticleSettingsTag == null
                            ? null
                            : AmbientParticleSettingsBinaryTagCodec.INSTANCE.decode(ambientParticleSettingsTag),
                    ambientLoopSoundTag == null
                            ? null
                            : SoundEventBinaryTagCodec.HOLDER_CODEC.decode(ambientLoopSoundTag),
                    ambientMoodSettingsTag == null
                            ? null
                            : AmbientMoodSettingsBinaryTagCodec.INSTANCE.decode(ambientMoodSettingsTag),
                    ambientAdditionsSettingsTag == null
                            ? null
                            : AmbientAdditionsSettingsBinaryTagCodec.INSTANCE.decode(ambientAdditionsSettingsTag),
                    backgroundMusicTag == null
                            ? null
                            : MUSIC_WEIGHTED_LIST_CODEC.decode(backgroundMusicTag),
                    requiredTag(BACKGROUND_MUSIC_VOLUME_FIELD, compound, BinaryTagTypes.FLOAT).value()
            );
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag type must be of compound type to decode it to special effects"
            );
        }
    }

    @Override
    public @NotNull BinaryTag encode(@NotNull SpecialEffects decoded) throws Exception {
        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
                .put(FOG_COLOR_FIELD, ColorBinaryTagCodec.INSTANCE.encode(decoded.fogColor()))
                .put(WATER_COLOR_FIELD, ColorBinaryTagCodec.INSTANCE.encode(decoded.waterColor()))
                .put(WATER_FOG_COLOR_FIELD, ColorBinaryTagCodec.INSTANCE.encode(decoded.waterFogColor()))
                .put(SKY_COLOR_FIELD, ColorBinaryTagCodec.INSTANCE.encode(decoded.skyColor()))
                .putFloat(BACKGROUND_MUSIC_VOLUME_FIELD, decoded.backgroundMusicVolume());

        Color foliageColor = decoded.foliageColor();
        if (foliageColor != null) {
            builder.put(FOLIAGE_COLOR_FIELD, ColorBinaryTagCodec.INSTANCE.encode(foliageColor));
        }

        Color dryFoliageColor = decoded.dryFoliageColor();
        if (dryFoliageColor != null) {
            builder.put(DRY_FOLIAGE_COLOR_FIELD, ColorBinaryTagCodec.INSTANCE.encode(dryFoliageColor));
        }

        Color grassColor = decoded.grassColor();
        if (grassColor != null) {
            builder.put(GRASS_COLOR_FIELD, ColorBinaryTagCodec.INSTANCE.encode(decoded.grassColor()));
        }

        GrassColorModifier grassColorModifier = decoded.grassColorModifier();
        if (grassColorModifier != GrassColorModifier.NONE) {
            builder.put(GRASS_COLOR_MODIFIER_FIELD, GRASS_COLOR_MODIFIER_CODEC.encode(grassColorModifier));
        }

        AmbientParticleSettings particle = decoded.ambientParticleSettings();
        if (particle != null) {
            builder.put(PARTICLE_SETTINGS_FIELD, AmbientParticleSettingsBinaryTagCodec.INSTANCE.encode(particle));
        }

        Holder<SoundEvent> ambientLoopSoundEvent = decoded.ambientLoopSoundEvent();
        if (ambientLoopSoundEvent != null) {
            builder.put(AMBIENT_LOOP_SOUND_FIELD, SoundEventBinaryTagCodec.HOLDER_CODEC.encode(ambientLoopSoundEvent));
        }

        AmbientMoodSettings moodSettings = decoded.ambientMoodSettings();
        if (moodSettings != null) {
            builder.put(AMBIENT_MOOD_SETTINGS_FIELD, AmbientMoodSettingsBinaryTagCodec.INSTANCE.encode(moodSettings));
        }

        AmbientAdditionsSettings additionsSettings = decoded.ambientAdditionsSettings();
        if (additionsSettings != null) {
            builder.put(
                    AMBIENT_ADDITIONS_SOUND_FIELD,
                    AmbientAdditionsSettingsBinaryTagCodec.INSTANCE.encode(additionsSettings)
            );
        }

        List<Weighted<Music>> backgroundMusic = decoded.backgroundMusic();
        if (backgroundMusic != null) {
            builder.put(BACKGROUND_MUSIC_FIELD, MUSIC_WEIGHTED_LIST_CODEC.encode(backgroundMusic));
        }

        return builder.build();
    }
}
