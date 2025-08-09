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
                        RegistryReference.BIOME, Key.key("worldgen/biome"),
                        JsonDataResourceFiles.BIOMES, BiomeBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.CHAT_TYPE, Key.key("chat_type"),
                        JsonDataResourceFiles.CHAT_TYPES, ChatTypeBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.TRIM_PATTERN, Key.key("trim_pattern"),
                        JsonDataResourceFiles.TRIM_PATTERNS, TrimPatternBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.TRIM_MATERIAL, Key.key("trim_material"),
                        JsonDataResourceFiles.TRIM_MATERIALS, TrimMaterialBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.WOLF_VARIANT, Key.key("wolf_variant"),
                        JsonDataResourceFiles.WOLF_VARIANTS, WolfVariantBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.PIG_VARIANT, Key.key("pig_variant"),
                        JsonDataResourceFiles.PIG_VARIANTS, PigVariantBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.FROG_VARIANT, Key.key("frog_variant"),
                        JsonDataResourceFiles.FROG_VARIANTS, FrogVariantBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.CAT_VARIANT, Key.key("cat_variant"),
                        JsonDataResourceFiles.CAT_VARIANTS, CatVariantBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.COW_VARIANT, Key.key("cow_variant"),
                        JsonDataResourceFiles.COW_VARIANTS, CowVariantBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.DAMAGE_TYPE, Key.key("damage_type"),
                        JsonDataResourceFiles.DAMAGE_TYPES, DamageTypeBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.JUKEBOX_SONG, Key.key("jukebox_song"),
                        JsonDataResourceFiles.JUKEBOX_SONGS, JukeboxSongBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.INSTRUMENT, Key.key("instrument"),
                        JsonDataResourceFiles.INSTRUMENTS, InstrumentBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.WOLF_SOUND_VARIANT, Key.key("wolf_sound_variant"),
                        JsonDataResourceFiles.WOLF_SOUND_VARIANTS, WolfSoundVariantBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.CHICKEN_VARIANT, Key.key("chicken_variant"),
                        JsonDataResourceFiles.CHICKEN_VARIANTS, ChickenVariantBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.PAINTING_VARIANT, Key.key("painting_variant"),
                        JsonDataResourceFiles.PAINTING_VARIANTS, PaintingVariantBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.DIMENSION_TYPE, Key.key("dimension_type"),
                        JsonDataResourceFiles.DIMENSION_TYPES, DimensionTypeBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.BANNER_PATTERN, Key.key("banner_pattern"),
                        JsonDataResourceFiles.BANNER_PATTERNS, BannerPatternBinaryTagCodec.INSTANCE
                )
                .dataDriven(
                        RegistryReference.ENCHANTMENT, Key.key("enchantment"),
                        JsonDataResourceFiles.ENCHANTMENTS, EnchantmentBinaryTagCodec.INSTANCE
                )
                .builtIn(
                        RegistryReference.ITEM, Key.key("item"),
                        JsonDataResourceFiles.ITEMS,
                        JsonItem.class, JetItem::convert
                )
                .builtIn(
                        RegistryReference.BLOCK, Key.key("block"),
                        JsonDataResourceFiles.BLOCKS,
                        JsonBlock.class, JetBlockType::convert
                )
                .builtIn(
                        RegistryReference.ENTITY_TYPE, Key.key("entity_type"),
                        JsonDataResourceFiles.ENTITY_TYPES,
                        JsonEntityType.class, JetEntityType::convert
                )
                .builtIn(
                        RegistryReference.GAME_EVENT, Key.key("game_event"),
                        JsonDataResourceFiles.GAME_EVENTS,
                        JsonGameEvent.class, gameEvent -> new GameEvent(gameEvent.notificationRadius())
                )
                .builtIn(
                        RegistryReference.FLUID, Key.key("fluid"),
                        JsonDataResourceFiles.FLUIDS,
                        JsonUnit.class, unit -> JetFluid.INSTANCE
                )
                .builtIn(
                        RegistryReference.SOUND_EVENT, Key.key("sound_event"),
                        JsonDataResourceFiles.SOUND_EVENTS,
                        JsonSoundEvent.class, soundEvent -> new SoundEvent(soundEvent.sound(), soundEvent.range())
                )
                .builtIn(
                        RegistryReference.POI_TYPE, Key.key("point_of_interest_type"),
                        JsonDataResourceFiles.POI_TYPES,
                        JsonUnit.class, unit -> JetPoiType.INSTANCE
                )
                .builtIn(
                        RegistryReference.BLOCK_ENTITY_TYPE, Key.key("block_entity_type"),
                        JsonDataResourceFiles.BLOCK_ENTITY_TYPES,
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
         * @param registryKey the key that the registry should have
         * @param resourceFileClasspath a classpath of a resource file with built-in entries
         *                              to be added to the registry
         * @param valueCodec a binary tag codec to read and write the registry values with
         * @return this registry-map builder
         * @param <V> the type of values that the final registry should have
         * @since 1.0
         */
        private <V> @NonNull RegistryMapBuilder dataDriven(@NonNull RegistryReference<V> reference,
                                                           @NonNull Key registryKey,
                                                           @NonNull String resourceFileClasspath,
                                                           @NonNull BinaryTagCodec<V> valueCodec) {
            return this.put(reference, registryKey, resourceFileClasspath, BinaryTagHolder.class, holder -> {
                try {
                    return valueCodec.decode(TagStringIO.tagStringIO().asTag(holder.string()));
                } catch (Exception exception) {
                    throw new RuntimeException("Failed to read a registry value", exception);
                }
            }, valueCodec);
        }

        /**
         * Creates a built-in (not "networkable") {@linkplain MinecraftRegistry registry} with the specified data
         * and adds a {@linkplain Map.Entry map entry} associating the specified
         * {@linkplain RegistryReference registry reference} with the created registry.
         *
         * @param reference the registry reference
         * @param registryKey the key that the registry should have
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
                                                             @NonNull Key registryKey,
                                                             @NonNull String resourceFileClasspath,
                                                             @NonNull Class<DV> valueType,
                                                             @NonNull Function<DV, CV> valueConverter) {
            return this.put(reference, registryKey, resourceFileClasspath, valueType, valueConverter, null);
        }

        /**
         * Creates a {@linkplain MinecraftRegistry registry} with the specified data and adds
         * a {@linkplain Map.Entry map entry} associating the specified
         * {@linkplain RegistryReference registry reference} with the created registry.
         *
         * @param reference the registry reference
         * @param registryKey the key that the registry should have
         * @param resourceFileClasspath a classpath of a resource file with built-in values to be added to the registry
         * @param valueType a class of the registry final value type
         * @param valueConverter a function converting values from the resource file to values that should be put
         *                       to the registry
         * @param valueCodec a binary tag codec that values of the registry should be written with, {@code null} if
         *                   values of the registry should not be able to be written to network
         * @return this registry-map builder
         * @param <DV> the type of values available in the resource file
         * @param <CV> the type of values that the final registry should have
         * @since 1.0
         */
        private <DV, CV> @NonNull RegistryMapBuilder put(
                @NonNull RegistryReference<CV> reference, @NonNull Key registryKey,
                @NonNull String resourceFileClasspath, @NonNull Class<DV> valueType,
                @NonNull Function<DV, CV> valueConverter, @Nullable BinaryTagCodec<CV> valueCodec
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

            if (valueCodec == null) {
                registry = new JetMinecraftRegistry<>(registryKey, registrations, tags, null);
            } else {
                NetworkableRegistryBuilder<CV> registryBuilder = new NetworkableRegistryBuilder<>(registrations, tags);
                this.eventNode.call(new RegistryInitializeEvent<>(reference, registryBuilder));
                registry = registryBuilder.build(registryKey, valueCodec);
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
             * @param registryKey the key that the registry should have
             * @param valueCodec a binary tag codec that values of the registry should be written with
             * @return the created registry
             * @since 1.0
             */
            private @NonNull JetMinecraftRegistry<V> build(@NonNull Key registryKey,
                                                           @NonNull BinaryTagCodec<V> valueCodec) {
                this.registryCreated = true;
                return new JetMinecraftRegistry<>(registryKey, List.copyOf(this.registrations), this.tags, valueCodec);
            }
        }
    }
}