package net.hypejet.jet.server.registry.writers.registry.biome.effects.particle;

import net.hypejet.jet.data.model.api.registries.biome.effects.particle.BiomeParticleSettings;
import net.hypejet.jet.server.registry.writers.key.PackedKeyBinaryTagWriter;
import net.hypejet.jet.server.util.codec.Writer;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain Writer a writer}, which writes {@linkplain BiomeParticleSettings biome particle settings}
 * into {@linkplain CompoundBinaryTag a compound binary tag}.
 *
 * @since 1.0
 * @see BiomeParticleSettings
 * @see CompoundBinaryTag
 * @see Writer
 */
public final class BiomeParticleSettingsBinaryTagWriter implements Writer<BiomeParticleSettings, CompoundBinaryTag> {
    /**
     * An instance of the {@linkplain BiomeParticleSettingsBinaryTagWriter biome particle settings binary tag writer}.
     *
     * @since 1.0
     */
    public static final BiomeParticleSettingsBinaryTagWriter INSTANCE = new BiomeParticleSettingsBinaryTagWriter();

    private static final String OPTIONS = "options";
    private static final String PROBABILITY = "probability";

    private static final String OPTIONS_TYPE = "type";
    private static final String OPTIONS_VALUE = "value";

    private BiomeParticleSettingsBinaryTagWriter() {}

    @Override
    public @NonNull CompoundBinaryTag write(@NonNull BiomeParticleSettings object) {
        CompoundBinaryTag.Builder binaryOptions = CompoundBinaryTag.builder()
                .put(OPTIONS_TYPE, PackedKeyBinaryTagWriter.INSTANCE.write(object.key()));

        BinaryTag data = object.data();
        if (data != null)
            binaryOptions.put(OPTIONS_VALUE, data);

        return CompoundBinaryTag.builder()
                .put(OPTIONS, binaryOptions.build())
                .putFloat(PROBABILITY, object.probability())
                .build();
    }
}