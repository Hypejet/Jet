package net.hypejet.jet.server.registry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.hypejet.concurrency.object.notnull.NotNullObjectAcquisition;
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
import net.hypejet.jet.registry.feature.KnownPack;
import net.hypejet.jet.registry.reference.RegistryReference;
import net.hypejet.jet.server.entity.JetEntityType;
import net.hypejet.jet.server.entity.ai.JetPoiType;
import net.hypejet.jet.server.inventory.item.JetItem;
import net.hypejet.jet.server.network.NetworkManager;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerUpdateTagsPacket;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.task.ConfigurationSessionTask;
import net.hypejet.jet.server.network.session.task.PlaySessionTask;
import net.hypejet.jet.server.registry.blockstate.JetBlockStateRegistry;
import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
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
import net.hypejet.jet.server.world.block.JetBlockType;
import net.hypejet.jet.server.world.block.entity.JetBlockEntityType;
import net.hypejet.jet.server.world.fluid.JetFluid;
import net.hypejet.jet.server.world.particle.JetParticleType;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

/**
 * An implementation of the {@linkplain RegistryManager registry manager}.
 *
 * @since 1.0
 * @see RegistryManager
 */
public final class JetRegistryManager implements RegistryManager {

    private final NetworkManager networkManager;
    private final Map<RegistryReference<?>, JetMinecraftRegistry<?>> registries;
    private final JetBlockStateRegistry blockStateRegistry;

    private final ReadWriteLock tagsLock = new ReentrantReadWriteLock();

    /**
     * Constructs the {@linkplain JetRegistryManager registry manager}.
     *
     * @param eventNode an event node that registry events should be called in
     * @param networkManager a network manager of the server that the registry manager is being constructed for
     * @since 1.0
     */
    public JetRegistryManager(@NonNull EventNode<Object> eventNode, @NonNull NetworkManager networkManager) {
        Objects.requireNonNull(eventNode, "event node");
        this.networkManager = Objects.requireNonNull(networkManager, "network manager");

        this.registries = new RegistryMapBuilder(eventNode, this.tagsLock)
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
                        RegistryReference.PARTICLE_TYPE, Key.key("particle_type"),
                        JsonDataResourceFiles.PARTICLES,
                        JsonUnit.class, unit -> JetParticleType.INSTANCE
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
        Objects.requireNonNull(reference, "registry reference");
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

    @Override
    public void updateTags(@NonNull Map<RegistryReference<?>, Multimap<Key, Key>> tags) {
        Objects.requireNonNull(tags, "tags");
        try {
            this.tagsLock.writeLock().lock();
            tags.forEach((reference, tagToKeysMultimap) -> this.registry(reference).updateTags(tagToKeysMultimap));

            ServerUpdateTagsPacket updatePacket = this.createTagsPacket(); // TODO: Cache
            this.networkManager.connections().forEach(connection -> {
                try (NotNullObjectAcquisition<Session> acquisition = connection.acquireSessionRead()) {
                    switch (acquisition.get().sessionTask()) {
                        case ConfigurationSessionTask sessionTask -> sessionTask.sendTags(updatePacket, false);
                        // TODO: Replace "ignoredSessionTask" with underscore when JDK 25 releases
                        case PlaySessionTask ignoredSessionTask -> connection.sendPacket(updatePacket);
                        default -> {}
                    }
                }
            });
        } finally {
            this.tagsLock.writeLock().unlock();
        }
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
     * Initializes tags of {@linkplain JetMinecraftRegistry registries}
     * from this {@linkplain JetRegistryManager registry manager}
     * for the specified {@linkplain ConfigurationSessionTask configuration session task}.
     *
     * @param sessionTask the configuration session task that the tags should be initialized for
     * @throws IllegalStateException if the tags have already been initialized
     *                               for the specified configuration session task
     * @since 1.0
     */
    public void initializeTags(@NonNull ConfigurationSessionTask sessionTask) {
        try {
            this.tagsLock.readLock().lock();
            sessionTask.sendTags(this.createTagsPacket(), true);
        } finally {
            this.tagsLock.readLock().unlock();
        }
    }

    private @NonNull ServerUpdateTagsPacket createTagsPacket() {
        Set<ServerUpdateTagsPacket.TagRegistry> tagRegistries = new HashSet<>();
        this.registries().forEach(registry -> tagRegistries.add(registry.createTagRegistry()));
        return new ServerUpdateTagsPacket(tagRegistries);
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
        private final ReadWriteLock tagsLock;

        private final Map<RegistryReference<?>, JetMinecraftRegistry<?>> registries = new HashMap<>();

        /**
         * Constructs the {@linkplain RegistryMapBuilder registry-map builder}.
         *
         * @param eventNode an event node that registry events should be called in
         * @param tagsLock a read-write lock that should be acquired while working with tags
         *                 of registries added to the registry-map builder that is being constructed
         * @since 1.0
         */
        private RegistryMapBuilder(@NonNull EventNode<Object> eventNode, @NonNull ReadWriteLock tagsLock) {
            this.eventNode = Objects.requireNonNull(eventNode, "event node");
            this.tagsLock = Objects.requireNonNull(tagsLock, "tags lock");
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
            Multimap<Key, Key> tags = HashMultimap.create();

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

                dataEntry.tags().forEach(tag -> tags.put(tag, key));
            }

            JetMinecraftRegistry<CV> registry;

            if (valueCodec == null) {
                registry = new JetMinecraftRegistry<>(registryKey, registrations, tags, this.tagsLock, null);
            } else {
                NetworkableRegistryBuilder<CV> registryBuilder = new NetworkableRegistryBuilder<>(registrations);
                this.eventNode.call(new RegistryInitializeEvent<>(reference, registryBuilder));
                registry = registryBuilder.build(registryKey, tags, this.tagsLock, valueCodec);
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

            private final List<JetMinecraftRegistry.RegistrationInfo<V>> registrations;

            private boolean registryCreated;

            /**
             * Constructs the {@linkplain NetworkableRegistryBuilder networkable registry builder}.
             *
             * @param initialRegistrations an info list of initial registrations that the registry should have,
             *                             the order is preserved
             * @since 1.0
             */
            private NetworkableRegistryBuilder(
                    @NonNull List<JetMinecraftRegistry.RegistrationInfo<V>> initialRegistrations
            ) {
                this.registrations = new ArrayList<>(initialRegistrations);
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
             * @param tagToKeysMultimap a multimap associating keys of tags with keys of registry
             *                          values that should be associated with these tags
             * @param tagsLock a read-write lock that should be acquired while working with tags
             *                 of the registry that is being created
             * @param valueCodec a binary tag codec that values of the registry should be written with
             * @return the created registry
             * @since 1.0
             */
            private @NonNull JetMinecraftRegistry<V> build(@NonNull Key registryKey,
                                                           @NonNull Multimap<Key, Key> tagToKeysMultimap,
                                                           @NonNull ReadWriteLock tagsLock,
                                                           @NonNull BinaryTagCodec<V> valueCodec) {
                this.registryCreated = true;
                return new JetMinecraftRegistry<>(
                        registryKey, List.copyOf(this.registrations),
                        tagToKeysMultimap, tagsLock, valueCodec
                );
            }
        }
    }
}