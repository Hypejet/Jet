package net.hypejet.jet.data.json.adapters.model.biome;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.JsonHolder;
import net.hypejet.jet.data.json.model.JsonSoundEvent;
import net.hypejet.jet.data.json.model.JsonWeighted;
import net.hypejet.jet.data.json.model.biome.JsonAdditionsSound;
import net.hypejet.jet.data.json.model.biome.JsonAmbientMoodSound;
import net.hypejet.jet.data.json.model.biome.JsonAmbientParticleSettings;
import net.hypejet.jet.data.json.model.biome.JsonBiomeSpecialEffects;
import net.hypejet.jet.data.json.model.biome.JsonGrassColorModifier;
import net.hypejet.jet.data.json.model.biome.JsonMusic;
import net.hypejet.jet.data.json.token.DataJsonTypes;
import net.hypejet.jet.data.json.util.JsonUtil;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonBiomeSpecialEffects biome special effects}.
 *
 * @since 1.0
 * @see JsonBiomeSpecialEffects
 * @see TypeAdapter
 */
final class BiomeSpecialEffectsTypeAdapter extends TypeAdapter<JsonBiomeSpecialEffects> {

    private static final String FOG_COLOR_FIELD = "fog-color";
    private static final String WATER_COLOR_FIELD = "water-color";
    private static final String WATER_FOG_COLOR_FIELD = "water-fog-color";
    private static final String SKY_COLOR_FIELD = "sky-color";
    private static final String FOLIAGE_COLOR_FIELD = "foliage-color";
    private static final String DRY_FOLIAGE_COLOR_FIELD = "dry-foliage-color";
    private static final String GRASS_COLOR_FIELD = "grass-color";
    private static final String GRASS_COLOR_MODIFIER_FIELD = "grass-color-modifier";
    private static final String PARTICLE_SETTINGS_FIELD = "particle-settings";
    private static final String AMBIENT_SOUND_FIELD = "ambient-sound";
    private static final String MOOD_SOUND_FIELD = "mood-sound";
    private static final String ADDITIONS_SOUND_FIELD = "additions-sound";
    private static final String MUSIC_FIELD = "music";
    private static final String MUSIC_VOLUME_FIELD = "music-volume";

    private static final Type MUSIC_WEIGHTED_TYPE = new TypeToken<JsonWeighted<JsonMusic>>() {}.getType();

    private final Gson gson;

    /**
     * Constructs the {@linkplain BiomeSpecialEffectsTypeAdapter biome special effects type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    BiomeSpecialEffectsTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonBiomeSpecialEffects value) throws IOException {
        out.beginObject();

        out.name(FOG_COLOR_FIELD);
        out.value(value.fogColor());

        out.name(WATER_COLOR_FIELD);
        out.value(value.waterColor());

        out.name(WATER_FOG_COLOR_FIELD);
        out.value(value.waterFogColor());

        out.name(SKY_COLOR_FIELD);
        out.value(value.skyColor());

        Integer foliageColor = value.foliageColor();
        if (foliageColor != null) {
            out.name(FOLIAGE_COLOR_FIELD);
            out.value(foliageColor);
        }

        Integer dryFoliageColor = value.dryFoliageColor();
        if (dryFoliageColor != null) {
            out.name(DRY_FOLIAGE_COLOR_FIELD);
            out.value(dryFoliageColor);
        }

        Integer grassColor = value.grassColor();
        if (grassColor != null) {
            out.name(GRASS_COLOR_FIELD);
            out.value(grassColor);
        }

        out.name(GRASS_COLOR_MODIFIER_FIELD);
        this.gson.toJson(value.grassColorModifier(), JsonGrassColorModifier.class, out);

        JsonAmbientParticleSettings particleSettings = value.particleSettings();
        if (particleSettings != null) {
            out.name(PARTICLE_SETTINGS_FIELD);
            this.gson.toJson(particleSettings, JsonAmbientParticleSettings.class, out);
        }

        JsonHolder<JsonSoundEvent> ambientSound = value.ambientSound();
        if (ambientSound != null) {
            out.name(AMBIENT_SOUND_FIELD);
            this.gson.toJson(ambientSound, DataJsonTypes.SOUND_EVENT_HOLDER, out);
        }

        JsonAmbientMoodSound moodSound = value.moodSound();
        if (moodSound != null) {
            out.name(MOOD_SOUND_FIELD);
            this.gson.toJson(moodSound, JsonAmbientMoodSound.class, out);
        }

        JsonAdditionsSound additionsSound = value.additionsSound();
        if (additionsSound != null) {
            out.name(ADDITIONS_SOUND_FIELD);
            this.gson.toJson(additionsSound, JsonAdditionsSound.class, out);
        }

        List<JsonWeighted<JsonMusic>> music = value.music();
        if (!music.isEmpty()) {
            out.name(MUSIC_FIELD);
            JsonUtil.writeCollection(out, this.gson, MUSIC_WEIGHTED_TYPE, value.music());
        }

        out.name(MUSIC_VOLUME_FIELD);
        out.value(value.musicVolume());

        out.endObject();
    }

    @Override
    public JsonBiomeSpecialEffects read(JsonReader in) throws IOException {
        in.beginObject();

        int fogColor = 0;
        int waterColor = 0;
        int waterFogColor = 0;
        int skyColor = 0;
        Integer foliageColor = null;
        Integer dryFoliageColor = null;
        Integer grassColor = null;
        JsonGrassColorModifier grassColorModifier = null;
        JsonAmbientParticleSettings particleSettings = null;
        JsonHolder<JsonSoundEvent> ambientSound = null;
        JsonAmbientMoodSound moodSound = null;
        JsonAdditionsSound additionsSound = null;
        List<JsonWeighted<JsonMusic>> music = new ArrayList<>();
        float musicVolume = 0;

        boolean fogColorInitialized = false;
        boolean waterColorInitialized = false;
        boolean waterFogColorInitialized = false;
        boolean skyColorInitialized = false;
        boolean musicVolumeInitialized = false;

        while (in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            switch (name) {
                case FOG_COLOR_FIELD -> {
                    fogColor = in.nextInt();
                    fogColorInitialized = true;
                }
                case WATER_COLOR_FIELD -> {
                    waterColor = in.nextInt();
                    waterColorInitialized = true;
                }
                case WATER_FOG_COLOR_FIELD -> {
                    waterFogColor = in.nextInt();
                    waterFogColorInitialized = true;
                }
                case SKY_COLOR_FIELD -> {
                    skyColor = in.nextInt();
                    skyColorInitialized = true;
                }
                case FOLIAGE_COLOR_FIELD -> foliageColor = in.nextInt();
                case DRY_FOLIAGE_COLOR_FIELD -> dryFoliageColor = in.nextInt();
                case GRASS_COLOR_FIELD -> grassColor = in.nextInt();
                case GRASS_COLOR_MODIFIER_FIELD ->
                        grassColorModifier = this.gson.fromJson(in, JsonGrassColorModifier.class);
                case PARTICLE_SETTINGS_FIELD ->
                        particleSettings = this.gson.fromJson(in, JsonAmbientParticleSettings.class);
                case AMBIENT_SOUND_FIELD -> ambientSound = this.gson.fromJson(in, DataJsonTypes.SOUND_EVENT_HOLDER);
                case MOOD_SOUND_FIELD -> moodSound = this.gson.fromJson(in, JsonAmbientMoodSound.class);
                case ADDITIONS_SOUND_FIELD -> additionsSound = this.gson.fromJson(in, JsonAdditionsSound.class);
                case MUSIC_FIELD -> JsonUtil.readCollection(in, this.gson, MUSIC_WEIGHTED_TYPE, music);
                case MUSIC_VOLUME_FIELD -> {
                    musicVolume = in.nextInt();
                    musicVolumeInitialized = true;
                }
            }
        }

        in.endObject();

        if (!fogColorInitialized) {
            throw new JsonParseException("The fog color field has not been specified");
        } else if (!waterColorInitialized) {
            throw new JsonParseException("The water color field has not been specified");
        } else if (!waterFogColorInitialized) {
            throw new JsonParseException("The water fog color field has not been specified");
        } else if (!skyColorInitialized) {
            throw new JsonParseException("The sky color field has not been specified");
        } else if (grassColorModifier == null) {
            throw new JsonParseException("The grass color modifier field has not been specified");
        } else if (!musicVolumeInitialized) {
            throw new JsonParseException("The music volume field has not been specified");
        }

        return new JsonBiomeSpecialEffects(
                fogColor, waterColor, waterFogColor, skyColor, foliageColor, dryFoliageColor, grassColor,
                grassColorModifier, particleSettings, ambientSound, moodSound, additionsSound, music, musicVolume
        );
    }
}