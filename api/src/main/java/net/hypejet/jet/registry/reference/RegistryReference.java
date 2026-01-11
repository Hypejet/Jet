package net.hypejet.jet.registry.reference;

import net.hypejet.jet.chat.ChatType;
import net.hypejet.jet.entity.EntityType;
import net.hypejet.jet.entity.ai.PoiType;
import net.hypejet.jet.entity.damage.type.DamageType;
import net.hypejet.jet.entity.variant.cat.CatVariant;
import net.hypejet.jet.entity.variant.chicken.ChickenVariant;
import net.hypejet.jet.entity.variant.cow.CowVariant;
import net.hypejet.jet.entity.variant.frog.FrogVariant;
import net.hypejet.jet.entity.variant.painting.PaintingVariant;
import net.hypejet.jet.entity.variant.pig.PigVariant;
import net.hypejet.jet.entity.variant.wolf.WolfSoundVariant;
import net.hypejet.jet.entity.variant.wolf.WolfVariant;
import net.hypejet.jet.inventory.item.Item;
import net.hypejet.jet.inventory.item.enchatment.Enchantment;
import net.hypejet.jet.inventory.item.trim.TrimMaterial;
import net.hypejet.jet.inventory.item.trim.TrimPattern;
import net.hypejet.jet.registry.MinecraftRegistry;
import net.hypejet.jet.world.biome.Biome;
import net.hypejet.jet.world.block.BlockType;
import net.hypejet.jet.world.block.banner.BannerPattern;
import net.hypejet.jet.world.block.entity.BlockEntityType;
import net.hypejet.jet.world.block.jukebox.JukeboxSong;
import net.hypejet.jet.world.dimension.DimensionType;
import net.hypejet.jet.world.event.game.GameEvent;
import net.hypejet.jet.world.fluid.Fluid;
import net.hypejet.jet.world.particle.ParticleType;
import net.hypejet.jet.world.sound.Instrument;
import net.hypejet.jet.world.sound.SoundEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * A reference to certain {@linkplain MinecraftRegistry registry}.
 *
 * @param <V> the value type of registry that this object references to
 * @since 1.0
 * @see MinecraftRegistry
 */
public final class RegistryReference<V> {
    /**
     * A reference to a {@linkplain Biome biome} registry.
     *
     * @since 1.0
     * @see Biome
     */
    public static final RegistryReference<Biome> BIOME = create("biome");

    /**
     * A reference to a {@linkplain ChatType chat-type} registry.
     *
     * @since 1.0
     * @see ChatType
     */
    public static final RegistryReference<ChatType> CHAT_TYPE = create("chat_type");

    /**
     * A reference to a {@linkplain DamageType damage-type} registry.
     *
     * @since 1.0
     * @see DamageType
     */
    public static final RegistryReference<DamageType> DAMAGE_TYPE = create("damage_type");

    /**
     * A reference to a {@linkplain ChatType dimension-type} registry.
     *
     * @since 1.0
     * @see ChatType
     */
    public static final RegistryReference<DimensionType> DIMENSION_TYPE = create("dimension_type");

    /**
     * A reference to a {@linkplain TrimPattern trim-pattern} registry.
     *
     * @since 1.0
     * @see TrimPattern
     */
    public static final RegistryReference<TrimPattern> TRIM_PATTERN = create("trim_pattern");

    /**
     * A reference to a {@linkplain TrimMaterial trim-material} registry.
     *
     * @since 1.0
     * @see TrimMaterial
     */
    public static final RegistryReference<TrimMaterial> TRIM_MATERIAL = create("trim_material");

    /**
     * A reference to an {@linkplain Enchantment enchantment} registry.
     *
     * @since 1.0
     * @see Enchantment
     */
    public static final RegistryReference<Enchantment> ENCHANTMENT = create("enchantment");

    /**
     * A reference to a {@linkplain WolfVariant wolf-variant} registry.
     *
     * @since 1.0
     * @see WolfVariant
     */
    public static final RegistryReference<WolfVariant> WOLF_VARIANT = create("wolf_variant");

    /**
     * A reference to a {@linkplain PigVariant pig-variant} registry.
     *
     * @since 1.0
     * @see PigVariant
     */
    public static final RegistryReference<PigVariant> PIG_VARIANT = create("pig_variant");

    /**
     * A reference to a {@linkplain FrogVariant frog-variant} registry.
     *
     * @since 1.0
     * @see FrogVariant
     */
    public static final RegistryReference<FrogVariant> FROG_VARIANT = create("frog_variant");

    /**
     * A reference to a {@linkplain CatVariant cat-variant} registry.
     *
     * @since 1.0
     * @see CatVariant
     */
    public static final RegistryReference<CatVariant> CAT_VARIANT = create("cat_variant");

    /**
     * A reference to a {@linkplain CowVariant cow-variant} registry.
     *
     * @since 1.0
     * @see CowVariant
     */
    public static final RegistryReference<CowVariant> COW_VARIANT = create("cow_variant");

    /**
     * A reference to a {@linkplain ChickenVariant chicken-variant} registry.
     *
     * @since 1.0
     * @see ChickenVariant
     */
    public static final RegistryReference<ChickenVariant> CHICKEN_VARIANT = create("chicken_variant");

    /**
     * A reference to a {@linkplain PaintingVariant painting-variant} registry.
     *
     * @since 1.0
     * @see PaintingVariant
     */
    public static final RegistryReference<PaintingVariant> PAINTING_VARIANT = create("painting_variant");

    /**
     * A reference to a {@linkplain WolfSoundVariant wolf-sound-variant} registry.
     *
     * @since 1.0
     * @see WolfSoundVariant
     */
    public static final RegistryReference<WolfSoundVariant> WOLF_SOUND_VARIANT = create("wolf_sound_variant");

    /**
     * A reference to a {@linkplain JukeboxSong jukebox-song} registry.
     *
     * @since 1.0
     * @see JukeboxSong
     */
    public static final RegistryReference<JukeboxSong> JUKEBOX_SONG = create("jukebox_song");

    /**
     * A reference to an {@linkplain Instrument instrument} registry.
     *
     * @since 1.0
     * @see Instrument
     */
    public static final RegistryReference<Instrument> INSTRUMENT = create("instrument");

    /**
     * A reference to a {@linkplain BannerPattern banner-pattern} registry.
     *
     * @since 1.0
     * @see BannerPattern
     */
    public static final RegistryReference<BannerPattern> BANNER_PATTERN = create("banner_pattern");

    /**
     * A reference to an {@linkplain Item item} registry.
     *
     * @since 1.0
     * @see Item
     */
    public static final RegistryReference<Item> ITEM = create("item");

    /**
     * A reference to a {@linkplain BlockType block} registry.
     *
     * @since 1.0
     * @see BlockType
     */
    public static final RegistryReference<BlockType> BLOCK = create("block");

    /**
     * A reference to an {@linkplain EntityType entity-type} registry.
     *
     * @since 1.0
     * @see EntityType
     */
    public static final RegistryReference<EntityType> ENTITY_TYPE = create("entity_type");

    /**
     * A reference to a {@linkplain GameEvent game-event} registry.
     *
     * @since 1.0
     * @see GameEvent
     */
    public static final RegistryReference<GameEvent> GAME_EVENT = create("game_event");

    /**
     * A reference to a {@linkplain Fluid fluid} registry.
     *
     * @since 1.0
     * @see Fluid
     */
    public static final RegistryReference<Fluid> FLUID = create("fluid");

    /**
     * A reference to a {@linkplain ParticleType particle type} registry.
     *
     * @since 1.0
     * @see ParticleType
     */
    public static final RegistryReference<ParticleType> PARTICLE_TYPE = create("particle_type");

    /**
     * A reference to a {@linkplain SoundEvent sound-event} registry.
     *
     * @since 1.0
     * @see SoundEvent
     */
    public static final RegistryReference<SoundEvent> SOUND_EVENT = create("sound_event");

    /**
     * A reference to a {@linkplain PoiType point-of-interest-type} registry.
     *
     * @since 1.0
     * @see PoiType
     */
    public static final RegistryReference<PoiType> POI_TYPE = create("poi_type");

    /**
     * A reference to a {@linkplain BlockEntityType block-entity-type} registry.
     *
     * @since 1.0
     * @see BlockEntityType
     */
    public static final RegistryReference<BlockEntityType> BLOCK_ENTITY_TYPE = create("block_entity_type");

    private final String name;

    private RegistryReference(@NonNull String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    @Override
    public String toString() {
        return "RegistryReference{" +
                "name='" + this.name + '\'' +
                '}';
    }

    private static <V> @NonNull RegistryReference<V> create(@NonNull String name) {
        return new RegistryReference<>(name);
    }
}