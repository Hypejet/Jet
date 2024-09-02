package net.hypejet.jet.server.registry.codecs.biome.effects.particle;

import net.hypejet.jet.registry.registries.biome.effects.particle.BiomeParticleSettings;
import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.identifier.PackedIdentifierBinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain BinaryTagCodec binary tag codec}, which reads and writes
 * a {@linkplain BiomeParticleSettings biome particle settings}.
 *
 * @since 1.0
 * @author Codestech
 * @see BiomeParticleSettings
 * @see BinaryTagCodec
 */
public final class BiomeParticleSettingsBinaryTagCodec implements BinaryTagCodec<BiomeParticleSettings> {

    private static final BiomeParticleSettingsBinaryTagCodec INSTANCE = new BiomeParticleSettingsBinaryTagCodec();

    private static final String OPTIONS = "options";
    private static final String PROBABILITY = "probability";

    private static final String OPTIONS_TYPE = "type";
    private static final String OPTIONS_VALUE = "value";

    private BiomeParticleSettingsBinaryTagCodec() {}

    @Override
    public @NonNull BiomeParticleSettings read(@NonNull BinaryTag tag) {
        if (!(tag instanceof CompoundBinaryTag compound))
            throw new IllegalArgumentException("The binary tag is not a compound binary tag");

        CompoundBinaryTag binaryOptions = compound.getCompound(OPTIONS);
        float probability = compound.getFloat(PROBABILITY);

        BinaryTag typeTag = binaryOptions.get(OPTIONS_TYPE);
        if (typeTag == null) {
            throw new IllegalArgumentException(String.format(
                    "Could not find a a binary tag with name of \"%s\" in the options binary tag",
                    OPTIONS_TYPE
            ));
        }

        BinaryTag data = binaryOptions.get(OPTIONS_VALUE);
        return new BiomeParticleSettings(PackedIdentifierBinaryTagCodec.instance().read(typeTag), data, probability);
    }

    @Override
    public @NonNull BinaryTag write(@NonNull BiomeParticleSettings object) {
        CompoundBinaryTag.Builder binaryOptions = CompoundBinaryTag.builder()
                .put(OPTIONS_TYPE, PackedIdentifierBinaryTagCodec.instance().write(object.key()));

        BinaryTag data = object.data();
        if (data != null)
            binaryOptions.put(OPTIONS_VALUE, data);

        return CompoundBinaryTag.builder()
                .put(OPTIONS, binaryOptions.build())
                .putFloat(PROBABILITY, object.probability())
                .build();
    }

    /**
     * Gets an instance of the {@linkplain BiomeParticleSettingsBinaryTagCodec biome particle settings binary-tag
     * codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull BiomeParticleSettingsBinaryTagCodec instance() {
        return INSTANCE;
    }
}