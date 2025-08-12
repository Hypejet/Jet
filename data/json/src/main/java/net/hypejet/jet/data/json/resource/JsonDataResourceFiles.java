package net.hypejet.jet.data.json.resource;

import net.hypejet.jet.data.json.model.block.JsonBlock;
import net.hypejet.jet.data.json.model.block.JsonBlockEntityType;
import net.hypejet.jet.data.json.model.block.JsonBlockState;
import net.hypejet.jet.data.json.model.entity.JsonEntityType;
import net.hypejet.jet.data.json.model.event.JsonGameEvent;
import net.hypejet.jet.data.json.model.item.JsonItem;
import net.hypejet.jet.data.json.model.sound.JsonSoundEvent;
import org.jspecify.annotations.NonNull;

/**
 * A holder of resource files that JSON representation of extracted and converted Minecraft registry entries
 * are written to.
 *
 * @since 1.0
 */
public final class JsonDataResourceFiles {
    /**
     * A resource-directory-relative classpath where biome registry entries are written to.
     *
     * @since 1.0
     */
    public static final String BIOMES = resourceClasspath("biomes.json");

    /**
     * A resource-directory-relative classpath where chat-type registry entries are written to.
     *
     * @since 1.0
     */
    public static final String CHAT_TYPES = resourceClasspath("chat_types.json");

    /**
     * A resource-directory-relative classpath where trim-pattern registry entries are written to.
     *
     * @since 1.0
     */
    public static final String TRIM_PATTERNS = resourceClasspath("trim_patterns.json");

    /**
     * A resource-directory-relative classpath where trim-material registry entries are written to.
     *
     * @since 1.0
     */
    public static final String TRIM_MATERIALS = resourceClasspath("trim_materials.json");

    /**
     * A resource-directory-relative classpath where wolf-variant registry entries are written to.
     *
     * @since 1.0
     */
    public static final String WOLF_VARIANTS = resourceClasspath("wolf_variants.json");

    /**
     * A resource-directory-relative classpath where pig-variant registry entries are written to.
     *
     * @since 1.0
     */
    public static final String PIG_VARIANTS = resourceClasspath("pig_variants.json");

    /**
     * A resource-directory-relative classpath where frog-variant registry entries are written to.
     *
     * @since 1.0
     */
    public static final String FROG_VARIANTS = resourceClasspath("frog_variants.json");

    /**
     * A resource-directory-relative classpath where cat-variant registry entries are written to.
     *
     * @since 1.0
     */
    public static final String CAT_VARIANTS = resourceClasspath("cat_variants.json");

    /**
     * A resource-directory-relative classpath where cow-variant registry entries are written to.
     *
     * @since 1.0
     */
    public static final String COW_VARIANTS = resourceClasspath("cow_variants.json");

    /**
     * A resource-directory-relative classpath where damage-type registry entries are written to.
     *
     * @since 1.0
     */
    public static final String DAMAGE_TYPES = resourceClasspath("damage_types.json");

    /**
     * A resource-directory-relative classpath where jukebox-song registry entries are written to.
     *
     * @since 1.0
     */
    public static final String JUKEBOX_SONGS = resourceClasspath("jukebox_songs.json");

    /**
     * A resource-directory-relative classpath where instrument registry entries are written to.
     *
     * @since 1.0
     */
    public static final String INSTRUMENTS = resourceClasspath("instruments.json");

    /**
     * A resource-directory-relative classpath where wolf-sound-variant registry entries are written to.
     *
     * @since 1.0
     */
    public static final String WOLF_SOUND_VARIANTS = resourceClasspath("wolf_sound_variants.json");

    /**
     * A resource-directory-relative classpath where chicken-variant registry entries are written to.
     *
     * @since 1.0
     */
    public static final String CHICKEN_VARIANTS = resourceClasspath("chicken_variants.json");

    /**
     * A resource-directory-relative classpath where painting-variant registry entries are written to.
     *
     * @since 1.0
     */
    public static final String PAINTING_VARIANTS = resourceClasspath("painting_variants.json");

    /**
     * A resource-directory-relative classpath where dimension-type registry entries are written to.
     *
     * @since 1.0
     */
    public static final String DIMENSION_TYPES = resourceClasspath("dimension_types.json");

    /**
     * A resource-directory-relative classpath where banner-pattern registry entries are written to.
     *
     * @since 1.0
     */
    public static final String BANNER_PATTERNS = resourceClasspath("banner_patterns.json");

    /**
     * A resource-directory-relative classpath where enchantment registry entries are written to.
     *
     * @since 1.0
     */
    public static final String ENCHANTMENTS = resourceClasspath("enchantments.json");

    /**
     * A resource-directory-relative classpath where {@linkplain JsonItem item} registry entries are written to.
     *
     * @since 1.0
     */
    public static final String ITEMS = resourceClasspath("items.json");

    /**
     * A resource-directory-relative classpath where {@linkplain JsonBlock block} registry entries are written to.
     *
     * @since 1.0
     */
    public static final String BLOCKS = resourceClasspath("blocks.json");

    /**
     * A resource-directory-relative classpath where {@linkplain JsonEntityType entity-type} registry entries
     * are written to.
     *
     * @since 1.0
     */
    public static final String ENTITY_TYPES = resourceClasspath("entity_types.json");

    /**
     * A resource-directory-relative classpath where {@linkplain JsonGameEvent game-event} registry entries
     * are written to.
     *
     * @since 1.0
     */
    public static final String GAME_EVENTS = resourceClasspath("game_events.json");

    /**
     * A resource-directory-relative classpath where fluid registry entries are written to.
     *
     * @since 1.0
     */
    public static final String FLUIDS = resourceClasspath("fluids.json");

    /**
     * A resource-directory-relative classpath where {@linkplain JsonSoundEvent sound-event} registry entries
     * are written to.
     *
     * @since 1.0
     */
    public static final String SOUND_EVENTS = resourceClasspath("sound_events.json");

    /**
     * A resource-directory-relative classpath where point-of-interest-type registry entries
     * are written to.
     *
     * @since 1.0
     */
    public static final String POI_TYPES = resourceClasspath("poi_types.json");

    /**
     * A resource-directory-relative classpath where {@linkplain JsonBlockEntityType block-entity-type}
     * registry entries are written to.
     *
     * @since 1.0
     */
    public static final String BLOCK_ENTITY_TYPES = resourceClasspath("block_entity_types.json");

    /**
     * A resource-directory-relative classpath where {@linkplain JsonBlockState block-state} registry entries
     * are written to.
     *
     * @since 1.0
     */
    public static final String BLOCK_STATES = resourceClasspath("block_states.json");

    private JsonDataResourceFiles() {}

    private static @NonNull String resourceClasspath(@NonNull String file) {
        return "minecraft/registries/" + file;
    }
}