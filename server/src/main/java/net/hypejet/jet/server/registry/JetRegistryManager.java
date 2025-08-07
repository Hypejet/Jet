package net.hypejet.jet.server.registry;

import net.hypejet.jet.data.json.entry.JsonRegistryEntry;
import net.hypejet.jet.data.json.model.block.JsonBlock;
import net.hypejet.jet.data.json.model.block.JsonBlockEntityType;
import net.hypejet.jet.data.json.model.entity.JsonEntityType;
import net.hypejet.jet.data.json.model.event.JsonGameEvent;
import net.hypejet.jet.data.json.model.feature.JsonKnownPack;
import net.hypejet.jet.data.json.model.item.JsonItem;
import net.hypejet.jet.data.json.model.sound.JsonSoundEvent;
import net.hypejet.jet.data.json.resource.JsonDataResourceFiles;
import net.hypejet.jet.data.json.util.JsonUnit;
import net.hypejet.jet.event.events.registry.RegistryInitializeEvent;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.registry.MinecraftRegistry;
import net.hypejet.jet.registry.RegistryManager;
import net.hypejet.jet.registry.reference.RegistryReference;
import net.hypejet.jet.server.entity.JetEntityType;
import net.hypejet.jet.server.entity.ai.JetPoiType;
import net.hypejet.jet.server.inventory.item.JetItem;
import net.hypejet.jet.server.registry.blockstate.JetBlockStateRegistry;
import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.registry.feature.KnownPack;
import net.hypejet.jet.server.registry.codecs.chat.ChatTypeBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.entity.damage.type.DamageTypeBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.entity.variant.cat.CatVariantBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.entity.variant.chicken.ChickenVariantBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.entity.variant.cow.CowVariantBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.entity.variant.frog.FrogVariantBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.entity.variant.painting.PaintingVariantBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.entity.variant.pig.PigVariantBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.entity.variant.wolf.WolfSoundVariantBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.entity.variant.wolf.WolfVariantBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.inventory.item.enchantment.EnchantmentBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.inventory.item.trim.TrimMaterialBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.inventory.item.trim.TrimPatternBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.biome.BiomeBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.block.banner.BannerPatternBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.block.jukebox.JukeboxSongBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.dimension.DimensionTypeBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.sound.InstrumentBinaryTagCodec;
import net.hypejet.jet.server.util.data.JetDataUtil;
import net.hypejet.jet.server.world.block.entity.JetBlockEntityType;
import net.hypejet.jet.server.world.block.JetBlockType;
import net.hypejet.jet.server.world.fluid.JetFluid;
import net.hypejet.jet.world.event.game.GameEvent;
import net.hypejet.jet.world.sound.SoundEvent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.TagStringIO;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/**
 * Represents an implementation of {@linkplain RegistryManager a registry manager}.
 *
 * @since 1.0
 * @see RegistryManager
 */
public final class JetRegistryManager implements RegistryManager {

    private final Map<RegistryReference<?>, JetMinecraftRegistry<?>> registries;
    private final JetBlockStateRegistry blockStateRegistry;

    /**
     * Constructs the {@linkplain JetRegistryManager registry manager}.
     *
     * @param eventNode an event node where registry-related events should be called
     * @since 1.0
     */
    public JetRegistryManager(@NonNull EventNode<Object> eventNode) {
        this.registries = new RegistryMapBuilder(eventNode)
                .dataDriven(
                        RegistryReference.BIOME, JsonDataResourceFiles.BIOMES,
                        Key.key("worldgen/biome"), BiomeBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.CHAT_TYPE, JsonDataResourceFiles.CHAT_TYPES,
                        Key.key("chat_type"), ChatTypeBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.TRIM_PATTERN, JsonDataResourceFiles.TRIM_PATTERNS,
                        Key.key("trim_pattern"), TrimPatternBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.TRIM_MATERIAL, JsonDataResourceFiles.TRIM_MATERIALS,
                        Key.key("trim_material"), TrimMaterialBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.WOLF_VARIANT, JsonDataResourceFiles.WOLF_VARIANTS,
                        Key.key("wolf_variant"), WolfVariantBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.PIG_VARIANT, JsonDataResourceFiles.PIG_VARIANTS,
                        Key.key("pig_variant"), PigVariantBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.FROG_VARIANT, JsonDataResourceFiles.FROG_VARIANTS,
                        Key.key("frog_variant"), FrogVariantBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.CAT_VARIANT, JsonDataResourceFiles.CAT_VARIANTS,
                        Key.key("cat_variant"), CatVariantBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.COW_VARIANT, JsonDataResourceFiles.COW_VARIANTS,
                        Key.key("cow_variant"), CowVariantBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.DAMAGE_TYPE, JsonDataResourceFiles.DAMAGE_TYPES,
                        Key.key("damage_type"), DamageTypeBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.JUKEBOX_SONG, JsonDataResourceFiles.JUKEBOX_SONGS,
                        Key.key("jukebox_song"), JukeboxSongBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.INSTRUMENT, JsonDataResourceFiles.INSTRUMENTS,
                        Key.key("instrument"), InstrumentBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.WOLF_SOUND_VARIANT, JsonDataResourceFiles.WOLF_SOUND_VARIANTS,
                        Key.key("wolf_sound_variant"), WolfSoundVariantBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.CHICKEN_VARIANT, JsonDataResourceFiles.CHICKEN_VARIANTS,
                        Key.key("chicken_variant"), ChickenVariantBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.PAINTING_VARIANT, JsonDataResourceFiles.PAINTING_VARIANTS,
                        Key.key("painting_variant"), PaintingVariantBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.DIMENSION_TYPE, JsonDataResourceFiles.DIMENSION_TYPES,
                        Key.key("dimension_type"), DimensionTypeBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.BANNER_PATTERN, JsonDataResourceFiles.BANNER_PATTERNS,
                        Key.key("banner_pattern"), BannerPatternBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.ENCHANTMENT, JsonDataResourceFiles.ENCHANTMENTS,
                        Key.key("enchantment"), EnchantmentBinaryTagCodec.INSTANCE
                )
                .builtIn(
                        RegistryReference.ITEM, JsonDataResourceFiles.ITEMS,
                        JsonItem.class, JetItem::convert
                )
                .builtIn(
                        RegistryReference.BLOCK, JsonDataResourceFiles.BLOCKS,
                        JsonBlock.class, JetBlockType::convert
                )
                .builtIn(
                        RegistryReference.ENTITY_TYPE, JsonDataResourceFiles.ENTITY_TYPES,
                        JsonEntityType.class, JetEntityType::convert
                )
                .builtIn(
                        RegistryReference.GAME_EVENT, JsonDataResourceFiles.GAME_EVENTS,
                        JsonGameEvent.class, gameEvent -> new GameEvent(gameEvent.notificationRadius())
                )
                .builtIn(
                        RegistryReference.FLUID, JsonDataResourceFiles.FLUIDS,
                        JsonUnit.class, unit -> JetFluid.INSTANCE
                )
                .builtIn(
                        RegistryReference.SOUND_EVENT, JsonDataResourceFiles.SOUND_EVENTS,
                        JsonSoundEvent.class, soundEvent -> new SoundEvent(soundEvent.sound(), soundEvent.range())
                )
                .builtIn(
                        RegistryReference.POI_TYPE, JsonDataResourceFiles.POI_TYPES,
                        JsonUnit.class, unit -> JetPoiType.INSTANCE
                )
                .builtIn(
                        RegistryReference.BLOCK_ENTITY_TYPE, JsonDataResourceFiles.BLOCK_ENTITY_TYPES,
                        JsonBlockEntityType.class, JetBlockEntityType::convert
                )
                .build();

        this.blockStateRegistry = new JetBlockStateRegistry();
    }

    @Override
    public @NonNull <V> JetMinecraftRegistry<V> registry(@NonNull RegistryReference<V> reference) {
        if (!this.registries.containsKey(reference)) {
            throw new IllegalArgumentException(
                    "The specified registry reference is not recognised by the registry managed"
            );
        }
        // noinspection unchecked ; the registry map is created with generics
        return (JetMinecraftRegistry<V>) this.registries.get(reference);
    }

    @Override
    public @NonNull JetBlockStateRegistry blockStateRegistry() {
        return this.blockStateRegistry;
    }

    /**
     * Gets a {@linkplain Collection collection} of all registered {@linkplain JetMinecraftRegistry registries}.
     *
     * @return the collection
     * @since 1.0
     */
    public @NonNull Collection<JetMinecraftRegistry<?>> registries() {
        return this.registries.values();
    }

    /**
     * A builder of a {@linkplain Map map} associating {@linkplain RegistryReference registry references}
     * with corresponding {@linkplain JetMinecraftRegistry registries}. Also initializes these registries.
     *
     * @since 1.0
     * @see RegistryReference
     * @see JetMinecraftRegistry
     * @see Map
     */
    private static final class RegistryMapBuilder {

        private final EventNode<Object> eventNode;
        private final Map<RegistryReference<?>, JetMinecraftRegistry<?>> registries = new HashMap<>();

        /**
         * Constructs the {@linkplain RegistryMapBuilder registry-map builder}.
         *
         * @param eventNode an event node where registry-related events should be called
         * @since 1.0
         */
        private RegistryMapBuilder(@NonNull EventNode<Object> eventNode) {
            this.eventNode = Objects.requireNonNull(eventNode, "event node");
        }

        /**
         * Creates a "networkable" {@linkplain MinecraftRegistry registry} with the specified data and adds
         * a {@linkplain Map.Entry map entry} associating the specified
         * {@linkplain RegistryReference registry reference} with the created registry.
         *
         * <p>The resource file values are read as {@linkplain BinaryTagHolder binary tag holders}
         * and converted to final value types using the specified {@linkplain BinaryTagCodec binary tag codec}</p>
         *
         * @param reference the registry reference
         * @param resourceFileClasspath a classpath of a resource file with built-in entries
         *                              to be added to the registry
         * @param registryKey a key that the registry should have
         * @param valueCodec a binary tag codec to read and write the registry values with
         * @return this registry-map builder
         * @param <V> the type of values that the final registry should have
         * @since 1.0
         */
        private <V> @NonNull RegistryMapBuilder dataDriven(@NonNull RegistryReference<V> reference,
                                                           @NonNull String resourceFileClasspath,
                                                           @NonNull Key registryKey,
                                                           @NonNull BinaryTagCodec<V> valueCodec) {
            return this.put(reference, resourceFileClasspath, BinaryTagHolder.class, holder -> {
                try {
                    return valueCodec.decode(TagStringIO.tagStringIO().asTag(holder.string()));
                } catch (Exception exception) {
                    throw new RuntimeException("Failed to read a registry value", exception);
                }
            }, new JetMinecraftRegistry.NetworkableData<>(valueCodec, registryKey));
        }

        /**
         * Creates a built-in (not "networkable") {@linkplain MinecraftRegistry registry} with the specified data
         * and adds a {@linkplain Map.Entry map entry} associating the specified
         * {@linkplain RegistryReference registry reference} with the created registry.
         *
         * @param reference the registry reference
         * @param resourceFileClasspath a classpath of a resource file with built-in values to be added to the registry
         * @param valueType a class of the registry final value type
         * @param valueConverter a function converting values from the resource file to values that should be put
         *                       to the registry
         * @return this registry-map builder
         * @param <DV> the type of values available in the resource file
         * @param <CV> the type of values that the final registry should have
         * @since 1.0
         */
        private <DV, CV> @NonNull RegistryMapBuilder builtIn(@NonNull RegistryReference<CV> reference,
                                                             @NonNull String resourceFileClasspath,
                                                             @NonNull Class<DV> valueType,
                                                             @NonNull Function<DV, CV> valueConverter) {
            return this.put(reference, resourceFileClasspath, valueType, valueConverter, null);
        }

        /**
         * Creates a {@linkplain MinecraftRegistry registry} with the specified data and adds
         * a {@linkplain Map.Entry map entry} associating the specified
         * {@linkplain RegistryReference registry reference} with the created registry.
         *
         * @param reference the registry reference
         * @param resourceFileClasspath a classpath of a resource file with built-in values to be added to the registry
         * @param valueType a class of the registry final value type
         * @param valueConverter a function converting values from the resource file to values that should be put
         *                       to the registry
         * @param networkableData an additional data that the registry should have for network writing
         *                        purposes, {@code null} if values of the registry should not be able
         *                        to be written to network
         * @return this registry-map builder
         * @param <DV> the type of values available in the resource file
         * @param <CV> the type of values that the final registry should have
         * @since 1.0
         */
        private <DV, CV> @NonNull RegistryMapBuilder put(
                @NonNull RegistryReference<CV> reference, @NonNull String resourceFileClasspath,
                @NonNull Class<DV> valueType, @NonNull Function<DV, CV> valueConverter,
                JetMinecraftRegistry.@Nullable NetworkableData<CV> networkableData
        ) {
            if (this.registries.containsKey(reference)) {
                throw new IllegalArgumentException(String.format(
                        "Registry with reference %s has already been registered",
                        reference
                ));
            }

            List<JetMinecraftRegistry.RegistrationInfo<CV>> registrations = new ArrayList<>();
            Map<Key, Set<Key>> tags = new HashMap<>();

            List<JsonRegistryEntry<DV>> dataEntries = JetDataUtil.deserializeEntries(resourceFileClasspath, valueType);
            for (JsonRegistryEntry<DV> dataEntry : dataEntries) {
                Key key = dataEntry.key();
                JsonKnownPack knownPack = dataEntry.knownPack();

                KnownPack convertedKnownPack;
                if (knownPack == null) {
                    convertedKnownPack = null;
                } else {
                    convertedKnownPack = new KnownPack(
                            knownPack.namespace(),
                            knownPack.path(),
                            knownPack.version()
                    );
                }

                registrations.add(new JetMinecraftRegistry.RegistrationInfo<>(
                        key,
                        valueConverter.apply(dataEntry.value()),
                        convertedKnownPack
                ));

                tags.put(key, dataEntry.tags());
            }

            JetMinecraftRegistry<CV> registry;

            if (networkableData == null) {
                registry = new JetMinecraftRegistry<>(registrations, tags, null);
            } else {
                NetworkableRegistryBuilder<CV> registryBuilder = new NetworkableRegistryBuilder<>(registrations, tags);
                this.eventNode.call(new RegistryInitializeEvent<>(reference, registryBuilder));
                registry = registryBuilder.build(networkableData);
            }

            this.registries.put(reference, registry);
            return this;
        }

        /**
         * Builds the registry map.
         *
         * @return the created registry map
         * @since 1.0
         */
        private @NonNull Map<RegistryReference<?>, JetMinecraftRegistry<?>> build() {
            return Map.copyOf(this.registries);
        }

        /**
         * A builder of a {@linkplain JetMinecraftRegistry registry} that can be serialized to network.
         *
         * @param <V> the value type of the registry
         * @since 1.0
         * @see JetMinecraftRegistry
         */
        private static final class NetworkableRegistryBuilder<V> implements RegistryInitializeEvent.RegistryAccess<V> {

            private final List<JetMinecraftRegistry.RegistrationInfo<V>> registrations = new ArrayList<>();
            private final Map<Key, Set<Key>> tags = new HashMap<>();

            private boolean registryCreated;

            /**
             * Constructs the {@linkplain NetworkableRegistryBuilder networkable registry builder}.
             *
             * @param initialRegistrations an info list of initial registrations that the registry should have,
             *                             the order is preserved
             * @param initialTags a map associating keys of initial values that the registry should have
             *                    with initial tags that these values should be associated with
             * @since 1.0
             */
            private NetworkableRegistryBuilder(
                    @NonNull List<JetMinecraftRegistry.RegistrationInfo<V>> initialRegistrations,
                    @NonNull Map<Key, Set<Key>> initialTags
            ) {
                this.registrations.addAll(initialRegistrations);
                this.tags.putAll(initialTags);
            }

            @Override
            public void register(@NonNull Key key, @NonNull V value, @Nullable KnownPack knownPack) {
                Objects.requireNonNull(key, "key");
                Objects.requireNonNull(value, "value");
                if (this.registryCreated)
                    throw new IllegalArgumentException("The registry has already been created");
                this.registrations.add(new JetMinecraftRegistry.RegistrationInfo<>(key, value, knownPack));
            }

            /**
             * Builds the "networkable" {@linkplain JetMinecraftRegistry registry}.
             *
             * @param data a networkable data that the registry should have
             * @return the registry created
             * @since 1.0
             */
            private @NonNull JetMinecraftRegistry<V> build(JetMinecraftRegistry.@NonNull NetworkableData<V> data) {
                this.registryCreated = true;
                return new JetMinecraftRegistry<>(List.copyOf(this.registrations), this.tags, data);
            }
        }
    }
}