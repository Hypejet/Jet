package net.hypejet.jet.data.generator.extractor;

import net.hypejet.jet.data.generator.adpater.KeyAdapter;
import net.hypejet.jet.data.json.entry.JsonRegistryEntry;
import net.kyori.adventure.key.Key;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Represents a {@linkplain RegistryExtractor registry extractor} which extracts values
 * a single {@linkplain Registry Minecraft registry} and converts them to Jet data equivalents using
 * the specified extractor.
 *
 * @param <MV> a type of the unconverted value
 * @param <CV> a type of the converted value
 * @since 1.0
 * @see Registry
 * @see RegistryExtractor
 */
public final class ConverterRegistryExtractor<MV, CV> implements RegistryExtractor<CV> {

    private final ResourceKey<Registry<MV>> registryKey;
    private final Function<MV, CV> converter;
    private final Class<CV> convertedValueClass;

    /**
     * Constructs the {@linkplain ConverterRegistryExtractor converter registry extractor}.
     *
     * @param registryKey a registry key of the registry to extract the values from
     * @param converter a function that converts the values
     * @param convertedValueClass a class of the converted value
     * @since 1.0
     */
    public ConverterRegistryExtractor(@NonNull ResourceKey<Registry<MV>> registryKey,
                                      @NonNull Function<MV, CV> converter, @NonNull Class<CV> convertedValueClass) {
        this.registryKey = Objects.requireNonNull(registryKey, "registry key");
        this.converter = Objects.requireNonNull(converter, "converter");
        this.convertedValueClass = Objects.requireNonNull(convertedValueClass, "converted value class");
    }

    @Override
    public @NonNull List<JsonRegistryEntry<CV>> extract(@NonNull RegistryAccess registryAccess) {
        Registry<MV> registry = registryAccess.lookupOrThrow(this.registryKey);
        List<JsonRegistryEntry<CV>> entries = new ArrayList<>();

        for (MV value : registry) {
            ResourceKey<MV> key = registry.getResourceKey(value).orElseThrow();

            Set<Key> tags = registry.wrapAsHolder(value).tags()
                    .map(tag -> KeyAdapter.convert(tag.location()))
                    .collect(Collectors.toUnmodifiableSet());

            JsonRegistryEntry.FeaturePack featurePack = registry.registrationInfo(key)
                    .flatMap(RegistrationInfo::knownPackInfo)
                    .map(pack -> new JsonRegistryEntry.FeaturePack(pack.namespace(), pack.id(), pack.version()))
                    .orElse(null);

            CV convertedValue = this.converter.apply(value);
            entries.add(new JsonRegistryEntry<>(KeyAdapter.convert(key.location()), convertedValue, tags, featurePack));
        }

        return List.copyOf(entries);
    }

    @Override
    public @NonNull Class<CV> valueClass() {
        return this.convertedValueClass;
    }
}