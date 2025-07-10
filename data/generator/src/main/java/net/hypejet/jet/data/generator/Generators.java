package net.hypejet.jet.data.generator;

import com.palantir.javapoet.JavaFile;
import net.hypejet.jet.data.generator.adpater.BiomeAdapter;
import net.hypejet.jet.data.generator.adpater.ChatTypeAdapter;
import net.hypejet.jet.data.generator.adpater.TrimMaterialAdapter;
import net.hypejet.jet.data.generator.adpater.TrimPatternAdapter;
import net.hypejet.jet.data.generator.extractor.ConverterRegistryExtractor;
import net.hypejet.jet.data.generator.generator.CodeGenerator;
import net.hypejet.jet.data.generator.generator.Generator;
import net.hypejet.jet.data.generator.generator.ResourceGenerator;
import net.hypejet.jet.data.generator.generator.generators.KeyDefinitionGenerator;
import net.hypejet.jet.data.generator.generator.generators.RegistryExtractorResourceGenerator;
import net.hypejet.jet.data.generator.generator.generators.VersionInfoGenerator;
import net.hypejet.jet.data.json.model.biome.JsonBiome;
import net.hypejet.jet.data.json.model.chat.type.JsonChatType;
import net.hypejet.jet.data.json.model.trim.material.JsonTrimMaterial;
import net.hypejet.jet.data.json.model.trim.pattern.JsonTrimPattern;
import net.minecraft.SharedConstants;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.Bootstrap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.tags.TagLoader;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.validation.DirectoryValidator;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * Represents something that executes {@linkplain Generator generators}.
 *
 * @since 1.0
 * @see Generator
 */
final class Generators {

    private static final String API_PACKAGE_NAME = "net.hypejet.jet.data.api";
    private static final String SERVER_PACKAGE_NAME = "net.hypejet.jet.data.server";

    private Generators() {}

    /**
     * Runs the generators.
     *
     * @param serverPath an output directory of generated Java server source files
     * @param apiPath an output directory of generated Java API source files
     * @param resourcesPath an output directory of generated resource files
     * @throws IOException when an I/O error occurs while writing files
     * @since 1.0
     */
    static void run(@NotNull Path serverPath, @NotNull Path apiPath, @NotNull Path resourcesPath) throws IOException {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        Bootstrap.validate();

        PackRepository packRepository = createPackRepository();
        RegistryAccess registryAccess = createRegistryAccess(packRepository);

        Set<Generator> generators = new GeneratorsBuilder(registryAccess)
                .add(new VersionInfoGenerator())
                .add(
                        Registries.BIOME, BiomeAdapter::convert, JsonBiome.class,
                        Path.of("biomes.json"), "BiomeKeys"
                )
                .add(
                        Registries.CHAT_TYPE, ChatTypeAdapter::convert, JsonChatType.class,
                        Path.of("chat-types.json"), "ChatTypeKeys"
                )
                .add(
                        Registries.TRIM_PATTERN, TrimPatternAdapter::convert, JsonTrimPattern.class,
                        Path.of("trim-patterns.json"), "TrimPatternKeys"
                )
                .add(
                        Registries.TRIM_MATERIAL, TrimMaterialAdapter::convert, JsonTrimMaterial.class,
                        Path.of("trim-materials.json"), "TrimMaterialKeys"
                )
                .build();

        for (Generator generator : generators) {
            switch (generator) {
                case CodeGenerator codeGenerator -> {
                    Path parentPath;
                    String packageName;

                    switch (codeGenerator.destination()) {
                        case API -> {
                            packageName = API_PACKAGE_NAME;
                            parentPath = apiPath;
                        }
                        case SERVER -> {
                            packageName = SERVER_PACKAGE_NAME;
                            parentPath = serverPath;
                        }
                        default -> throw new IllegalStateException("Unknown destination");
                    }

                    JavaFile.builder(packageName, codeGenerator.generate())
                            .indent("    ") // 4 spaces
                            .skipJavaLangImports(true)
                            .build()
                            .writeTo(parentPath);
                }
                case ResourceGenerator resourceGenerator -> {
                    Path path = resourcesPath.resolve(resourceGenerator.path());
                    Files.createDirectories(path.getParent());
                    Files.writeString(path, resourceGenerator.generate(), StandardOpenOption.CREATE);
                }
            }
        }
    }

    /**
     * Creates a {@linkplain PackRepository pack repository} containing all Minecraft built-in packs.
     *
     * @return the pack repository
     * @since 1.0
     */
    private static @NotNull PackRepository createPackRepository() {
        RepositorySource repositorySource = new ServerPacksSource(new DirectoryValidator(path -> false));
        PackRepository packRepository = new PackRepository(repositorySource);

        MinecraftServer.configurePackRepository(
                packRepository,
                new WorldDataConfiguration(DataPackConfig.DEFAULT, FeatureFlags.REGISTRY.allFlags()),
                false, true
        );

        return packRepository;
    }

    /**
     * Creates a {@linkplain RegistryAccess registry access} to all Minecraft registries with data extracted
     * from packs of the specified {@linkplain PackRepository pack repository}.
     *
     * @param packRepository the pack repository
     * @return the registry access
     * @since 1.0
     */
    private static @NotNull RegistryAccess createRegistryAccess(@NotNull PackRepository packRepository) {
        LayeredRegistryAccess<RegistryLayer> access = RegistryLayer.createRegistryAccess();
        List<PackResources> packResources = packRepository.openAllSelected();

        try (CloseableResourceManager resources = new MultiPackResourceManager(PackType.SERVER_DATA, packResources)) {
            List<Registry.PendingTags<?>> pendingTags = TagLoader.loadTagsForExistingRegistries(
                    resources,
                    access.getLayer(RegistryLayer.STATIC)
            );

            // Apply pending tags that have not been already applied
            pendingTags.forEach(Registry.PendingTags::apply);

            RegistryAccess.Frozen loadedRegistryData = RegistryDataLoader.load(
                    resources,
                    TagLoader.buildUpdatedLookups(access.getAccessForLoading(RegistryLayer.WORLDGEN), pendingTags),
                    RegistryDataLoader.WORLDGEN_REGISTRIES
            );

            return access.replaceFrom(RegistryLayer.WORLDGEN, loadedRegistryData).compositeAccess();
        }
    }

    /**
     * Represents builder of a {@linkplain Generator generator} {@linkplain Set set}.
     *
     * @since 1.0
     * @see Generator
     * @see Set
     */
    private static final class GeneratorsBuilder {

        private final Set<Generator> generators = new HashSet<>();
        private final RegistryAccess registryAccess;

        /**
         * Constructs the {@linkplain GeneratorsBuilder generators builder}.
         *
         * @param registryAccess a registry access that should be passed as an argument to registry extractors
         * @since 1.0
         */
        private GeneratorsBuilder(@NonNull RegistryAccess registryAccess) {
            this.registryAccess = registryAccess;
        }

        /**
         * Creates a {@linkplain KeyDefinitionGenerator key definition generator}
         * and a {@linkplain RegistryExtractorResourceGenerator registry-extractor resource generator}
         * using the specified values and adds them to this builder.
         *
         * @param registryKey resource key of a registry that the generators should use
         * @param valueConverter a function converting values from the specified registry to Jet data equivalents
         * @param convertedValueClass a class of Jet data equivalents of the registry values
         * @param resourceFilePath a relative path that output of the registry-extractor resource generator
         *                         should be written to
         * @param className a name of the class that output of the key definition generator should be written to
         * @return this builder
         * @param <MV> a type of the registry values
         * @param <CV> a type of Jet data equivalent the registry values
         * @since 1.0
         */
        private <MV, CV> @NonNull GeneratorsBuilder add(@NonNull ResourceKey<Registry<MV>> registryKey,
                                                        @NonNull Function<MV, CV> valueConverter,
                                                        @NonNull Class<CV> convertedValueClass,
                                                        @NonNull Path resourceFilePath, @NonNull String className) {
            return this
                    .add(new KeyDefinitionGenerator<>(this.registryAccess.lookupOrThrow(registryKey), className))
                    .add(new RegistryExtractorResourceGenerator<>(
                            new ConverterRegistryExtractor<>(registryKey, valueConverter, convertedValueClass),
                            this.registryAccess, resourceFilePath
                    ));
        }

        /**
         * Adds the specified {@linkplain Generator generator} to this builder.
         *
         * @param generator the generator
         * @return this builder
         * @since 1.0
         */
        private @NonNull GeneratorsBuilder add(@NonNull Generator generator) {
            this.generators.add(generator);
            return this;
        }

        /**
         * Builds the generator set.
         *
         * @return the built generator set
         * @since 1.0
         */
        private @NonNull Set<Generator> build() {
            return this.generators;
        }
    }
}