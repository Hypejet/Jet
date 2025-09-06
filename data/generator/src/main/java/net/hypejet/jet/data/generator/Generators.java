package net.hypejet.jet.data.generator;

import com.mojang.serialization.Codec;
import com.palantir.javapoet.JavaFile;
import net.hypejet.jet.data.generator.adapter.BlockAdapter;
import net.hypejet.jet.data.generator.adapter.BlockEntityTypeAdapter;
import net.hypejet.jet.data.generator.adapter.EntityTypeAdapter;
import net.hypejet.jet.data.generator.adapter.GameEventAdapter;
import net.hypejet.jet.data.generator.adapter.ItemAdapter;
import net.hypejet.jet.data.generator.adapter.SoundEventAdapter;
import net.hypejet.jet.data.generator.extractor.BlockStateRegistryExtractor;
import net.hypejet.jet.data.generator.extractor.ConverterRegistryExtractor;
import net.hypejet.jet.data.generator.generator.CodeGenerator;
import net.hypejet.jet.data.generator.generator.Generator;
import net.hypejet.jet.data.generator.generator.ResourceGenerator;
import net.hypejet.jet.data.generator.generator.generators.KeyDefinitionGenerator;
import net.hypejet.jet.data.generator.generator.generators.PacketIdentifierGenerator;
import net.hypejet.jet.data.generator.generator.generators.RegistryExtractorResourceGenerator;
import net.hypejet.jet.data.generator.generator.generators.VersionInfoGenerator;
import net.hypejet.jet.data.generator.level.MockLevel;
import net.hypejet.jet.data.generator.util.FileUtils;
import net.hypejet.jet.data.json.model.block.JsonBlock;
import net.hypejet.jet.data.json.model.block.JsonBlockEntityType;
import net.hypejet.jet.data.json.model.entity.JsonEntityType;
import net.hypejet.jet.data.json.model.event.JsonGameEvent;
import net.hypejet.jet.data.json.model.item.JsonItem;
import net.hypejet.jet.data.json.model.sound.JsonSoundEvent;
import net.hypejet.jet.data.json.resource.JsonDataResourceFiles;
import net.hypejet.jet.data.json.util.JsonUnit;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.minecraft.SharedConstants;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.ProtocolInfo;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.protocol.configuration.ConfigurationPacketTypes;
import net.minecraft.network.protocol.configuration.ConfigurationProtocols;
import net.minecraft.network.protocol.game.GamePacketTypes;
import net.minecraft.network.protocol.game.GameProtocols;
import net.minecraft.network.protocol.handshake.HandshakePacketTypes;
import net.minecraft.network.protocol.handshake.HandshakeProtocols;
import net.minecraft.network.protocol.login.LoginPacketTypes;
import net.minecraft.network.protocol.login.LoginProtocols;
import net.minecraft.network.protocol.status.StatusPacketTypes;
import net.minecraft.network.protocol.status.StatusProtocols;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.RegistryOps;
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
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.level.Level;
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
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

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

    private Generators() {}

    /**
     * Runs the generators.
     *
     * @param serverPath an output directory of generated Java server source files, {@code null} if these
     *                   files should not be generated
     * @param apiPath an output directory of generated Java API source files, {@code null} if these
     *                files should not be generated
     * @param resourcesPath an output directory of generated resource files, {@code null} if these
     *                      files should not be generated
     * @throws IOException if an I/O error occurs during file management
     * @since 1.0
     */
    static void run(@Nullable Path serverPath, @Nullable Path apiPath,
                    @Nullable Path resourcesPath) throws IOException {
        if (serverPath != null)
            FileUtils.deleteRecursively(serverPath);
        if (apiPath != null)
            FileUtils.deleteRecursively(apiPath);
        if (resourcesPath != null)
            FileUtils.deleteRecursively(resourcesPath);

        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        Bootstrap.validate();

        PackRepository packRepository = createPackRepository();
        RegistryAccess registryAccess = createRegistryAccess(packRepository);

        // noinspection resource ; mock levels do not have anything to close
        Level level = new MockLevel(registryAccess, packRepository.getRequestedFeatureFlags());

        Set<Generator> generators = new GeneratorsBuilder(registryAccess)
                .add(VersionInfoGenerator.INSTANCE)
                // ------------------------ Data driven registries ------------------------
                .add(
                        Registries.BIOME, Biome.NETWORK_CODEC, Biomes.class,
                        Biome.class, JsonDataResourceFiles.BIOMES, "BiomeKeys"
                )
                .add(
                        Registries.CHAT_TYPE, ChatType.DIRECT_CODEC, ChatType.class,
                        ChatType.class, JsonDataResourceFiles.CHAT_TYPES, "ChatTypeKeys"
                )
                .add(
                        Registries.TRIM_PATTERN, TrimPattern.DIRECT_CODEC, TrimPatterns.class,
                        TrimPattern.class, JsonDataResourceFiles.TRIM_PATTERNS, "TrimPatternKeys"
                )
                .add(
                        Registries.TRIM_MATERIAL, TrimMaterial.DIRECT_CODEC, TrimMaterials.class,
                        TrimMaterial.class, JsonDataResourceFiles.TRIM_MATERIALS, "TrimMaterialKeys"
                )
                .add(
                        Registries.WOLF_VARIANT, WolfVariant.NETWORK_CODEC, WolfVariants.class,
                        WolfVariant.class, JsonDataResourceFiles.WOLF_VARIANTS, "WolfVariantKeys"
                )
                .add(
                        Registries.PIG_VARIANT, PigVariant.NETWORK_CODEC, PigVariants.class,
                        PigVariant.class, JsonDataResourceFiles.PIG_VARIANTS, "PigVariantKeys"
                )
                .add(
                        Registries.FROG_VARIANT, FrogVariant.NETWORK_CODEC, FrogVariants.class,
                        FrogVariant.class, JsonDataResourceFiles.FROG_VARIANTS, "FrogVariantKeys"
                )
                .add(
                        Registries.CAT_VARIANT, CatVariant.NETWORK_CODEC, CatVariants.class,
                        CatVariant.class, JsonDataResourceFiles.CAT_VARIANTS, "CatVariantKeys"
                )
                .add(
                        Registries.COW_VARIANT, CowVariant.NETWORK_CODEC, CowVariants.class,
                        CowVariant.class, JsonDataResourceFiles.COW_VARIANTS, "CowVariantKeys"
                )
                .add(
                        Registries.DAMAGE_TYPE, DamageType.DIRECT_CODEC, DamageTypes.class,
                        DamageType.class, JsonDataResourceFiles.DAMAGE_TYPES, "DamageTypeKeys"
                )
                .add(
                        Registries.JUKEBOX_SONG, JukeboxSong.DIRECT_CODEC, JukeboxSongs.class,
                        JukeboxSong.class, JsonDataResourceFiles.JUKEBOX_SONGS, "JukeboxSongKeys"
                )
                .add(
                        Registries.INSTRUMENT, Instrument.DIRECT_CODEC, Instruments.class,
                        Instrument.class, JsonDataResourceFiles.INSTRUMENTS, "InstrumentKeys"
                )
                .add(
                        Registries.WOLF_SOUND_VARIANT, WolfSoundVariant.NETWORK_CODEC, WolfSoundVariants.class,
                        WolfSoundVariant.class, JsonDataResourceFiles.WOLF_SOUND_VARIANTS, "WolfSoundVariantKeys"
                )
                .add(
                        Registries.CHICKEN_VARIANT, ChickenVariant.NETWORK_CODEC, ChickenVariants.class,
                        ChickenVariant.class, JsonDataResourceFiles.CHICKEN_VARIANTS, "ChickenVariantKeys"
                )
                .add(
                        Registries.PAINTING_VARIANT, PaintingVariant.DIRECT_CODEC, PaintingVariants.class,
                        PaintingVariant.class, JsonDataResourceFiles.PAINTING_VARIANTS, "PaintingVariantKeys"
                )
                .add(
                        Registries.DIMENSION_TYPE, DimensionType.DIRECT_CODEC, BuiltinDimensionTypes.class,
                        DimensionType.class, JsonDataResourceFiles.DIMENSION_TYPES, "DimensionTypeKeys"
                )
                .add(
                        Registries.BANNER_PATTERN, BannerPattern.DIRECT_CODEC, BannerPatterns.class,
                        BannerPattern.class, JsonDataResourceFiles.BANNER_PATTERNS, "BannerPatternKeys"
                )
                .add(
                        Registries.ENCHANTMENT, Enchantment.DIRECT_CODEC, Enchantments.class,
                        Enchantment.class, JsonDataResourceFiles.ENCHANTMENTS, "EnchantmentKeys"
                )
                // TODO: Dialogs
                // ------------------------ Built-in registries ------------------------
                .add(
                        Registries.ITEM, ItemAdapter::convert, Items.class,
                        Item.class, JsonItem.class, JsonDataResourceFiles.ITEMS, "ItemKeys"
                )
                .add(
                        Registries.BLOCK, BlockAdapter::convert, Blocks.class,
                        Block.class, JsonBlock.class, JsonDataResourceFiles.BLOCKS, "BlockKeys"
                )
                .add(
                        Registries.GAME_EVENT, GameEventAdapter::convert, GameEvent.class,
                        GameEvent.class, JsonGameEvent.class, JsonDataResourceFiles.GAME_EVENTS, "GameEventKeys"
                )
                .add(
                        Registries.FLUID, ignored -> JsonUnit.INSTANCE, Fluids.class,
                        Fluid.class, JsonUnit.class, JsonDataResourceFiles.FLUIDS, "FluidKeys"
                )
                .add(
                        Registries.SOUND_EVENT, SoundEventAdapter::convert, SoundEvents.class,
                        SoundEvent.class, JsonSoundEvent.class, JsonDataResourceFiles.SOUND_EVENTS, "SoundEventKeys"
                )
                .add(
                        Registries.POINT_OF_INTEREST_TYPE, ignored -> JsonUnit.INSTANCE, PoiTypes.class,
                        PoiType.class, JsonUnit.class, JsonDataResourceFiles.POI_TYPES, "PointOfInterestTypeKeys"
                )
                .add(
                        Registries.ENTITY_TYPE, value -> EntityTypeAdapter.convert(value, level),
                        EntityType.class, EntityType.class, JsonEntityType.class,
                        JsonDataResourceFiles.ENTITY_TYPES, "EntityTypeKeys"
                )
                .add(
                        Registries.BLOCK_ENTITY_TYPE, value -> BlockEntityTypeAdapter.convert(value, registryAccess),
                        BlockEntityType.class, BlockEntityType.class, JsonBlockEntityType.class,
                        JsonDataResourceFiles.BLOCK_ENTITY_TYPES, "BlockEntityTypeKeys"
                )
                .add(new RegistryExtractorResourceGenerator<>(
                        BlockStateRegistryExtractor.INSTANCE,
                        registryAccess,
                        JsonDataResourceFiles.BLOCK_STATES
                ))
                // ------------------------ Packets ------------------------
                .add(
                        "ClientHandshakePackets", HandshakeProtocols.SERVERBOUND_TEMPLATE,
                        "A definition of identifiers of server-bound handshake packets.",
                        HandshakePacketTypes.class
                )
                .add(
                        "ServerStatusPackets", StatusProtocols.CLIENTBOUND_TEMPLATE,
                        "A definition of identifiers of client-bound status packets.",
                        StatusPacketTypes.class
                ).add(
                        "ClientStatusPackets", StatusProtocols.SERVERBOUND_TEMPLATE,
                        "A definition of identifiers of server-bound status packets.",
                        StatusPacketTypes.class
                )
                .add(
                        "ServerLoginPackets", LoginProtocols.CLIENTBOUND_TEMPLATE,
                        "A definition of identifiers of client-bound login packets.",
                        LoginPacketTypes.class
                ).add(
                        "ClientLoginPackets", LoginProtocols.SERVERBOUND_TEMPLATE,
                        "A definition of identifiers of server-bound login packets.",
                        LoginPacketTypes.class
                )
                .add(
                        "ServerConfigurationPackets", ConfigurationProtocols.CLIENTBOUND_TEMPLATE,
                        "A definition of identifiers of client-bound configuration packets.",
                        ConfigurationPacketTypes.class
                ).add(
                        "ClientConfigurationPackets", ConfigurationProtocols.SERVERBOUND_TEMPLATE,
                        "A definition of identifiers of server-bound configuration packets.",
                        ConfigurationPacketTypes.class
                )
                .add(
                        "ServerPlayPackets", GameProtocols.CLIENTBOUND_TEMPLATE,
                        "A definition of identifiers of client-bound play packets.",
                        GamePacketTypes.class
                ).add(
                        "ClientPlayPackets", GameProtocols.SERVERBOUND_TEMPLATE,
                        "A definition of identifiers of server-bound play packets.",
                        GamePacketTypes.class
                )
                .build();

        for (Generator generator : generators) {
            switch (generator) {
                case CodeGenerator codeGenerator -> {
                    Path parentPath = switch (codeGenerator.destination()) {
                        case API -> apiPath;
                        case SERVER -> serverPath;
                    };

                    if (parentPath == null) continue;

                    JavaFile.builder(codeGenerator.packageName(), codeGenerator.generate())
                            .indent("    ") // 4 spaces
                            .skipJavaLangImports(true)
                            .build()
                            .writeTo(parentPath);
                }
                case ResourceGenerator resourceGenerator -> {
                    if (resourcesPath == null) continue;

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
    private static @NonNull PackRepository createPackRepository() {
        RepositorySource repositorySource = new ServerPacksSource(new DirectoryValidator(path -> false));
        PackRepository packRepository = new PackRepository(repositorySource);

        MinecraftServer.configurePackRepository(
                packRepository,
                new WorldDataConfiguration(DataPackConfig.DEFAULT, FeatureFlags.VANILLA_SET),
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
    private static @NonNull RegistryAccess createRegistryAccess(@NonNull PackRepository packRepository) {
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
        private final RegistryOps<Tag> nbtRegistryOps;

        /**
         * Constructs the {@linkplain GeneratorsBuilder generators builder}.
         *
         * @param registryAccess a registry access that should be passed as an argument to registry extractors
         * @since 1.0
         */
        private GeneratorsBuilder(@NonNull RegistryAccess registryAccess) {
            this.registryAccess = registryAccess;
            this.nbtRegistryOps = RegistryOps.create(NbtOps.INSTANCE, registryAccess);
        }


        /**
         * Creates a {@linkplain KeyDefinitionGenerator key definition generator}
         * and a {@linkplain RegistryExtractorResourceGenerator registry-extractor resource generator}
         * using the specified values and adds them to this builder.
         *
         * @param registryKey resource key of a registry that the generators should use
         * @param valueCodec a codec handling network serialization of values of the specified registry
         * @param keyDefinitionClass a class containing constants associated with values of the specified registry
         * @param valueClass a class of the registry values
         * @param resourceFilePath a relative classpath that output of the registry-extractor resource generator
         *                         should be written to
         * @param className a name of the class that output of the key definition generator should be written to
         * @return this builder
         * @param <V> a type of the registry values
         * @since 1.0
         */
        private <V> @NonNull GeneratorsBuilder add(@NonNull ResourceKey<Registry<V>> registryKey,
                                                   @NonNull Codec<V> valueCodec, @NonNull Class<?> keyDefinitionClass,
                                                   @NonNull Class<? super V> valueClass,
                                                   @NonNull String resourceFilePath, @NonNull String className) {
            return this.add(
                    registryKey,
                    value -> BinaryTagHolder.binaryTagHolder(
                            valueCodec.encodeStart(this.nbtRegistryOps, value)
                                    .getOrThrow()
                                    .toString()
                    ),
                    keyDefinitionClass,
                    valueClass,
                    BinaryTagHolder.class,
                    resourceFilePath,
                    className
            );
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
         * @param resourceFilePath a relative classpath that output of the registry-extractor resource generator
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
                                                        @NonNull String resourceFilePath, @NonNull String className) {
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
         * Creates a {@linkplain PacketIdentifierGenerator packet identifier generator}
         * with values specified and adds it to this builder.
         *
         * @param className a name that the generator output class should have
         * @param detailsProvider a details provider providing information about
         *                        packets whose identifiers should be extracted
         * @param javadocHeader a javadoc header that the generator output class should have
         * @param keyDefinitionClass a class containing constants associated with
         *                           packets whose identifiers should be extracted
         * @return this builder
         * @since 1.0
         */
        private @NonNull GeneratorsBuilder add(
                @NonNull String className, ProtocolInfo.@NonNull DetailsProvider detailsProvider,
                @NonNull String javadocHeader, @NonNull Class<?> keyDefinitionClass
        ) {
            return this.add(new PacketIdentifierGenerator(
                    className, detailsProvider, javadocHeader, keyDefinitionClass
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
