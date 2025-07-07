package net.hypejet.jet.data.generator;

import com.palantir.javapoet.JavaFile;
import net.hypejet.jet.data.generator.generator.CodeGenerator;
import net.hypejet.jet.data.generator.generator.Generator;
import net.hypejet.jet.data.generator.generator.ResourceGenerator;
import net.hypejet.jet.data.generator.generator.generators.VersionInfoGenerator;
import net.minecraft.SharedConstants;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryDataLoader;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Set;

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

        Set<Generator> generators = Set.of(new VersionInfoGenerator());

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
}