package net.hypejet.jet.data.generator;

import com.palantir.javapoet.JavaFile;
import net.hypejet.jet.data.generator.adpater.BannerPatternAdapter;
import net.hypejet.jet.data.generator.adpater.BiomeAdapter;
import net.hypejet.jet.data.generator.adpater.BlockAdapter;
import net.hypejet.jet.data.generator.adpater.BlockEntityTypeAdapter;
import net.hypejet.jet.data.generator.adpater.CatVariantAdapter;
import net.hypejet.jet.data.generator.adpater.ChatTypeAdapter;
import net.hypejet.jet.data.generator.adpater.ChickenVariantAdapter;
import net.hypejet.jet.data.generator.adpater.CowVariantAdapter;
import net.hypejet.jet.data.generator.adpater.DamageTypeAdapter;
import net.hypejet.jet.data.generator.adpater.DimensionTypeAdapter;
import net.hypejet.jet.data.generator.adpater.EnchantmentAdapter;
import net.hypejet.jet.data.generator.adpater.EntityTypeAdapter;
import net.hypejet.jet.data.generator.adpater.FrogVariantAdapter;
import net.hypejet.jet.data.generator.adpater.GameEventAdapter;
import net.hypejet.jet.data.generator.adpater.InstrumentAdapter;
import net.hypejet.jet.data.generator.adpater.ItemAdapter;
import net.hypejet.jet.data.generator.adpater.JukeboxSongAdapter;
import net.hypejet.jet.data.generator.adpater.PaintingVariantAdapter;
import net.hypejet.jet.data.generator.adpater.PigVariantAdapter;
import net.hypejet.jet.data.generator.adpater.TrimMaterialAdapter;
import net.hypejet.jet.data.generator.adpater.TrimPatternAdapter;
import net.hypejet.jet.data.generator.adpater.WolfSoundVariantAdapter;
import net.hypejet.jet.data.generator.adpater.WolfVariantAdapter;
import net.hypejet.jet.data.generator.extractor.BlockStateRegistryExtractor;
import net.hypejet.jet.data.generator.extractor.ConverterRegistryExtractor;
import net.hypejet.jet.data.generator.generator.CodeGenerator;
import net.hypejet.jet.data.generator.generator.Generator;
import net.hypejet.jet.data.generator.generator.ResourceGenerator;
import net.hypejet.jet.data.generator.generator.generators.KeyDefinitionGenerator;
import net.hypejet.jet.data.generator.generator.generators.RegistryExtractorResourceGenerator;
import net.hypejet.jet.data.generator.generator.generators.VersionInfoGenerator;
import net.hypejet.jet.data.json.model.biome.JsonBiome;
import net.hypejet.jet.data.json.model.block.JsonBlock;
import net.hypejet.jet.data.json.model.block.JsonBlockEntityType;
import net.hypejet.jet.data.json.model.enchantment.JsonEnchantment;
import net.hypejet.jet.data.json.model.entity.JsonEntityType;
import net.hypejet.jet.data.json.model.event.JsonGameEvent;
import net.hypejet.jet.data.json.model.instrument.JsonInstrument;
import net.hypejet.jet.data.json.model.item.JsonItem;
import net.hypejet.jet.data.json.model.pattern.banner.JsonBannerPattern;
import net.hypejet.jet.data.json.model.song.JsonJukeboxSong;
import net.hypejet.jet.data.json.model.type.chat.JsonChatType;
import net.hypejet.jet.data.json.model.trim.material.JsonTrimMaterial;
import net.hypejet.jet.data.json.model.trim.pattern.JsonTrimPattern;
import net.hypejet.jet.data.json.model.type.damage.JsonDamageType;
import net.hypejet.jet.data.json.model.type.dimension.JsonDimensionType;
import net.hypejet.jet.data.json.model.variant.cat.JsonCatVariant;
import net.hypejet.jet.data.json.model.variant.chicken.JsonChickenVariant;
import net.hypejet.jet.data.json.model.variant.cow.JsonCowVariant;
import net.hypejet.jet.data.json.model.variant.frog.JsonFrogVariant;
import net.hypejet.jet.data.json.model.variant.painting.JsonPaintingVariant;
import net.hypejet.jet.data.json.model.variant.pig.JsonPigVariant;
import net.hypejet.jet.data.json.model.variant.wolf.JsonWolfSoundVariant;
import net.hypejet.jet.data.json.model.variant.wolf.JsonWolfVariant;
import net.hypejet.jet.data.json.util.JsonUnit;
import net.minecraft.SharedConstants;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.ChatType;
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
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.animal.CatVariants;
import net.minecraft.world.entity.animal.ChickenVariant;
import net.minecraft.world.entity.animal.ChickenVariants;
import net.minecraft.world.entity.animal.CowVariant;
import net.minecraft.world.entity.animal.CowVariants;
import net.minecraft.world.entity.animal.PigVariant;
import net.minecraft.world.entity.animal.PigVariants;
import net.minecraft.world.entity.animal.frog.FrogVariant;
import net.minecraft.world.entity.animal.frog.FrogVariants;
import net.minecraft.world.entity.animal.wolf.WolfSoundVariant;
import net.minecraft.world.entity.animal.wolf.WolfSoundVariants;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import net.minecraft.world.entity.animal.wolf.WolfVariants;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.decoration.PaintingVariants;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.Instruments;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.JukeboxSongs;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import net.minecraft.world.item.equipment.trim.TrimPatterns;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatterns;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
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
                // ------------------------ Data driven registries ------------------------
                .add(
                        Registries.BIOME, BiomeAdapter::convert, Biomes.class,
                        Biome.class, JsonBiome.class, Path.of("biomes.json"), "BiomeKeys"
                )
                .add(
                        Registries.CHAT_TYPE, ChatTypeAdapter::convert, ChatType.class,
                        ChatType.class, JsonChatType.class, Path.of("chat-types.json"), "ChatTypeKeys"
                )
                .add(
                        Registries.TRIM_PATTERN, TrimPatternAdapter::convert, TrimPatterns.class,
                        TrimPattern.class, JsonTrimPattern.class, Path.of("trim-patterns.json"), "TrimPatternKeys"
                )
                .add(
                        Registries.TRIM_MATERIAL, TrimMaterialAdapter::convert, TrimMaterials.class,
                        TrimMaterial.class, JsonTrimMaterial.class, Path.of("trim-materials.json"), "TrimMaterialKeys"
                )
                .add(
                        Registries.WOLF_VARIANT, WolfVariantAdapter::convert, WolfVariants.class,
                        WolfVariant.class, JsonWolfVariant.class, Path.of("wolf-variants.json"), "WolfVariantKeys"
                )
                .add(
                        Registries.PIG_VARIANT, PigVariantAdapter::convert, PigVariants.class,
                        PigVariant.class, JsonPigVariant.class, Path.of("pig-variants.json"), "PigVariantKeys"
                )
                .add(
                        Registries.FROG_VARIANT, FrogVariantAdapter::convert, FrogVariants.class,
                        FrogVariant.class, JsonFrogVariant.class, Path.of("frog-variants.json"), "FrogVariantKeys"
                )
                .add(
                        Registries.CAT_VARIANT, CatVariantAdapter::convert, CatVariants.class,
                        CatVariant.class, JsonCatVariant.class, Path.of("cat-variants.json"), "CatVariantKeys"
                )
                .add(
                        Registries.COW_VARIANT, CowVariantAdapter::convert, CowVariants.class,
                        CowVariant.class, JsonCowVariant.class, Path.of("cow-variants.json"), "CowVariantKeys"
                )
                .add(
                        Registries.DAMAGE_TYPE, DamageTypeAdapter::convert, DamageTypes.class,
                        DamageType.class, JsonDamageType.class, Path.of("damage-types.json"), "DamageTypeKeys"
                )
                .add(
                        Registries.JUKEBOX_SONG, JukeboxSongAdapter::convert, JukeboxSongs.class,
                        JukeboxSong.class, JsonJukeboxSong.class, Path.of("jukebox-songs.json"), "JukeboxSongKeys"
                )
                .add(
                        Registries.INSTRUMENT, InstrumentAdapter::convert, Instruments.class,
                        Instrument.class, JsonInstrument.class, Path.of("instruments.json"), "InstrumentKeys"
                )
                .add(
                        Registries.WOLF_SOUND_VARIANT, WolfSoundVariantAdapter::convert, WolfSoundVariants.class,
                        WolfSoundVariant.class, JsonWolfSoundVariant.class, Path.of("wolf-sound-variants.json"),
                        "WolfSoundVariantKeys"
                )
                .add(
                        Registries.CHICKEN_VARIANT, ChickenVariantAdapter::convert, ChickenVariants.class,
                        ChickenVariant.class, JsonChickenVariant.class, Path.of("chicken-variants.json"),
                        "ChickenVariantKeys"
                )
                .add(
                        Registries.PAINTING_VARIANT, PaintingVariantAdapter::convert, PaintingVariants.class,
                        PaintingVariant.class, JsonPaintingVariant.class, Path.of("painting-variants.json"),
                        "PaintingVariantKeys"
                )
                .add(
                        Registries.DIMENSION_TYPE, DimensionTypeAdapter::convert, BuiltinDimensionTypes.class,
                        DimensionType.class, JsonDimensionType.class, Path.of("dimension-types.json"),
                        "DimensionTypeKeys"
                )
                .add(
                        Registries.BANNER_PATTERN, BannerPatternAdapter::convert, BannerPatterns.class,
                        BannerPattern.class, JsonBannerPattern.class, Path.of("banner-patterns.json"),
                        "BannerPatternKeys"
                )
                .add(
                        Registries.ENCHANTMENT, value -> EnchantmentAdapter.convert(value, registryAccess),
                        Enchantments.class, Enchantment.class, JsonEnchantment.class, Path.of("enchantments.json"),
                        "EnchantmentKeys"
                )
                // TODO: Dialogs
                // ------------------------ Built-in registries ------------------------
                .add(
                        Registries.ITEM, ItemAdapter::convert, Items.class,
                        Item.class, JsonItem.class, Path.of("items.json"), "ItemKeys"
                )
                .add(
                        Registries.BLOCK, BlockAdapter::convert, Blocks.class,
                        Block.class, JsonBlock.class, Path.of("blocks.json"), "BlockKeys"
                )
                .add(
                        Registries.ENTITY_TYPE, EntityTypeAdapter::convert, EntityType.class,
                        EntityType.class, JsonEntityType.class, Path.of("entity-types.json"), "EntityTypeKeys"
                )
                .add(
                        Registries.GAME_EVENT, GameEventAdapter::convert, GameEvent.class,
                        GameEvent.class, JsonGameEvent.class, Path.of("game-events.json"), "GameEventKeys"
                )
                .add(
                        Registries.FLUID, ignored -> JsonUnit.INSTANCE, Fluids.class,
                        Fluid.class, JsonUnit.class, Path.of("fluids.json"), "FluidKeys"
                )
                .add(
                        Registries.POINT_OF_INTEREST_TYPE, ignored -> JsonUnit.INSTANCE, PoiTypes.class,
                        PoiType.class, JsonUnit.class, Path.of("point-of-interest-types.json"),
                        "PointOfInterestTypeKeys"
                )
                .add(
                        Registries.BLOCK_ENTITY_TYPE, value -> BlockEntityTypeAdapter.convert(value, registryAccess),
                        BlockEntityType.class, BlockEntityType.class, JsonBlockEntityType.class,
                        Path.of("block-entity-types.json"), "BlockEntityTypeKeys"
                )
                .add(new RegistryExtractorResourceGenerator<>(
                        BlockStateRegistryExtractor.INSTANCE,
                        registryAccess,
                        Path.of("block-states.json")
                ))
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

                    Files.writeString(
                            path,
                            resourceGenerator.generate(),
                            StandardOpenOption.TRUNCATE_EXISTING,
                            StandardOpenOption.CREATE
                    );
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
         * @param keyDefinitionClass a class containing constants associated with values of the specified registry
         * @param unconvertedValueClass a class of the registry values
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
                                                        @NonNull Class<?> keyDefinitionClass,
                                                        @NonNull Class<? super MV> unconvertedValueClass,
                                                        @NonNull Class<CV> convertedValueClass,
                                                        @NonNull Path resourceFilePath, @NonNull String className) {
            return this
                    .add(new KeyDefinitionGenerator<>(
                            this.registryAccess.lookupOrThrow(registryKey),
                            className, unconvertedValueClass, keyDefinitionClass
                    ))
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