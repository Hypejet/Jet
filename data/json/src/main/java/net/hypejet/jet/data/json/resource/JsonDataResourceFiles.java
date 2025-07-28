package net.hypejet.jet.data.json.resource;

import net.hypejet.jet.data.json.model.block.JsonBlock;
import net.hypejet.jet.data.json.model.block.JsonBlockEntityType;
import net.hypejet.jet.data.json.model.block.JsonBlockState;
import net.hypejet.jet.data.json.model.entity.JsonEntityType;
import net.hypejet.jet.data.json.model.event.JsonGameEvent;
import net.hypejet.jet.data.json.model.item.JsonItem;
import net.hypejet.jet.data.json.model.sound.JsonSoundEvent;

import java.nio.file.Path;

/**
 * A holder of resource files that JSON representation of extracted and converted Minecraft registry entries
 * are written to.
 *
 * @since 1.0
 */
public final class JsonDataResourceFiles {
    /**
     * A root path containing registry resource files.
     *
     * @since 1.0
     */
    private static final Path REGISTRIES_ROOT_PATH = Path.of("minecraft", "registries");

    /**
     * A resource-directory-relative {@linkplain Path path} where biome registry entries are written to.
     *
     * @since 1.0
     */
    public static final Path BIOMES = REGISTRIES_ROOT_PATH.resolve(Path.of("biomes.json"));

    /**
     * A resource-directory-relative {@linkplain Path path} where chat-type registry entries are written to.
     *
     * @since 1.0
     */
    public static final Path CHAT_TYPES = REGISTRIES_ROOT_PATH.resolve(Path.of("chat_types.json"));

    /**
     * A resource-directory-relative {@linkplain Path path} where trim-pattern registry entries are written to.
     *
     * @since 1.0
     */
    public static final Path TRIM_PATTERNS = REGISTRIES_ROOT_PATH.resolve(Path.of("trim_patterns.json"));

    /**
     * A resource-directory-relative {@linkplain Path path} where trim-material registry entries are written to.
     *
     * @since 1.0
     */
    public static final Path TRIM_MATERIALS = REGISTRIES_ROOT_PATH.resolve(Path.of("trim_materials.json"));

    /**
     * A resource-directory-relative {@linkplain Path path} where wolf-variant registry entries are written to.
     *
     * @since 1.0
     */
    public static final Path WOLF_VARIANTS = REGISTRIES_ROOT_PATH.resolve(Path.of("wolf_variants.json"));

    /**
     * A resource-directory-relative {@linkplain Path path} where pig-variant registry entries are written to.
     *
     * @since 1.0
     */
    public static final Path PIG_VARIANTS = REGISTRIES_ROOT_PATH.resolve(Path.of("biomes.json"));

    /**
     * A resource-directory-relative {@linkplain Path path} where frog-variant registry entries are written to.
     *
     * @since 1.0
     */
    public static final Path FROG_VARIANTS = REGISTRIES_ROOT_PATH.resolve(Path.of("frog_variants.json"));

    /**
     * A resource-directory-relative {@linkplain Path path} where cat-variant registry entries are written to.
     *
     * @since 1.0
     */
    public static final Path CAT_VARIANTS = REGISTRIES_ROOT_PATH.resolve(Path.of("cat_variants.json"));

    /**
     * A resource-directory-relative {@linkplain Path path} where cow-variant registry entries are written to.
     *
     * @since 1.0
     */
    public static final Path COW_VARIANTS = REGISTRIES_ROOT_PATH.resolve(Path.of("cow_variants.json"));

    /**
     * A resource-directory-relative {@linkplain Path path} where damage-type registry entries are written to.
     *
     * @since 1.0
     */
    public static final Path DAMAGE_TYPES = REGISTRIES_ROOT_PATH.resolve(Path.of("damage_types.json"));

    /**
     * A resource-directory-relative {@linkplain Path path} where jukebox-song registry entries are written to.
     *
     * @since 1.0
     */
    public static final Path JUKEBOX_SONGS = REGISTRIES_ROOT_PATH.resolve(Path.of("jukebox_songs.json"));

    /**
     * A resource-directory-relative {@linkplain Path path} where instrument registry entries are written to.
     *
     * @since 1.0
     */
    public static final Path INSTRUMENTS = REGISTRIES_ROOT_PATH.resolve(Path.of("instruments.json"));

    /**
     * A resource-directory-relative {@linkplain Path path} where wolf-sound-variant registry entries are written to.
     *
     * @since 1.0
     */
    public static final Path WOLF_SOUND_VARIANTS = REGISTRIES_ROOT_PATH.resolve(Path.of("wolf_sound_variants.json"));

    /**
     * A resource-directory-relative {@linkplain Path path} where chicken-variant registry entries are written to.
     *
     * @since 1.0
     */
    public static final Path CHICKEN_VARIANTS = REGISTRIES_ROOT_PATH.resolve(Path.of("chicken_variants.json"));

    /**
     * A resource-directory-relative {@linkplain Path path} where painting-variant registry entries are written to.
     *
     * @since 1.0
     */
    public static final Path PAINTING_VARIANTS = REGISTRIES_ROOT_PATH.resolve(Path.of("painting_variants.json"));

    /**
     * A resource-directory-relative {@linkplain Path path} where dimension-type registry entries are written to.
     *
     * @since 1.0
     */
    public static final Path DIMENSION_TYPES = REGISTRIES_ROOT_PATH.resolve(Path.of("dimension_types.json"));

    /**
     * A resource-directory-relative {@linkplain Path path} where banner-pattern registry entries are written to.
     *
     * @since 1.0
     */
    public static final Path BANNER_PATTERNS = REGISTRIES_ROOT_PATH.resolve(Path.of("banner_patterns.json"));

    /**
     * A resource-directory-relative {@linkplain Path path} where enchantment registry entries are written to.
     *
     * @since 1.0
     */
    public static final Path ENCHANTMENTS = REGISTRIES_ROOT_PATH.resolve(Path.of("enchantments.json"));

    /**
     * A resource-directory-relative {@linkplain Path path} where {@linkplain JsonItem item} registry entries
     * are written to.
     *
     * @since 1.0
     */
    public static final Path ITEMS = REGISTRIES_ROOT_PATH.resolve(Path.of("items.json"));

    /**
     * A resource-directory-relative {@linkplain Path path} where {@linkplain JsonBlock block} registry entries
     * are written to.
     *
     * @since 1.0
     */
    public static final Path BLOCKS = REGISTRIES_ROOT_PATH.resolve(Path.of("blocks.json"));

    /**
     * A resource-directory-relative {@linkplain Path path} where {@linkplain JsonEntityType entity-type}
     * registry entries are written to.
     *
     * @since 1.0
     */
    public static final Path ENTITY_TYPES = REGISTRIES_ROOT_PATH.resolve(Path.of("entity_types.json"));

    /**
     * A resource-directory-relative {@linkplain Path path} where {@linkplain JsonGameEvent game-event}
     * registry entries are written to.
     *
     * @since 1.0
     */
    public static final Path GAME_EVENTS = REGISTRIES_ROOT_PATH.resolve(Path.of("game_events.json"));

    /**
     * A resource-directory-relative {@linkplain Path path} where fluid registry entries are written to.
     *
     * @since 1.0
     */
    public static final Path FLUIDS = REGISTRIES_ROOT_PATH.resolve(Path.of("fluids.json"));

    /**
     * A resource-directory-relative {@linkplain Path path} where {@linkplain JsonSoundEvent sound-event}
     * registry entries are written to.
     *
     * @since 1.0
     */
    public static final Path SOUND_EVENTS = REGISTRIES_ROOT_PATH.resolve(Path.of("sound_events.json"));

    /**
     * A resource-directory-relative {@linkplain Path path} where point-of-interest-type registry entries
     * are written to.
     *
     * @since 1.0
     */
    public static final Path POI_TYPES = REGISTRIES_ROOT_PATH.resolve(Path.of("poi_types.json"));

    /**
     * A resource-directory-relative {@linkplain Path path} where {@linkplain JsonBlockEntityType block-entity-type}
     * registry entries are written to.
     *
     * @since 1.0
     */
    public static final Path BLOCK_ENTITY_TYPES = REGISTRIES_ROOT_PATH.resolve(Path.of("block_entity_types.json"));

    /**
     * A resource-directory-relative {@linkplain Path path} where {@linkplain JsonBlockState block-state}
     * registry entries are written to.
     *
     * @since 1.0
     */
    public static final Path BLOCK_STATES = REGISTRIES_ROOT_PATH.resolve(Path.of("block_states.json"));

    private JsonDataResourceFiles() {}
}