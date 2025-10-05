package net.hypejet.jet.world.block.noteblock;

import org.jspecify.annotations.NullMarked;

/**
 * An instrument of a note block.
 *
 * <p>This is not an enum, since it depends on Minecraft.
 * Adding an enum entry could break enum switch cases for example./p>
 *
 * @since 1.0
 */
@NullMarked
public final class NoteBlockInstrument {
    /**
     * The harp instrument.
     *
     * @since 1.0
     */
    public static final NoteBlockInstrument HARP = new NoteBlockInstrument("harp");

    /**
     * The bass drum instrument.
     *
     * @since 1.0
     */
    public static final NoteBlockInstrument BASS_DRUM = new NoteBlockInstrument("basedrum");

    /**
     * The snare instrument.
     *
     * @since 1.0
     */
    public static final NoteBlockInstrument SNARE = new NoteBlockInstrument("snare");

    /**
     * The hat instrument.
     *
     * @since 1.0
     */
    public static final NoteBlockInstrument HAT = new NoteBlockInstrument("hat");

    /**
     * The bass instrument.
     *
     * @since 1.0
     */
    public static final NoteBlockInstrument BASS = new NoteBlockInstrument("bass");

    /**
     * The flute instrument.
     *
     * @since 1.0
     */
    public static final NoteBlockInstrument FLUTE = new NoteBlockInstrument("flute");

    /**
     * The bell instrument.
     *
     * @since 1.0
     */
    public static final NoteBlockInstrument BELL = new NoteBlockInstrument("bell");

    /**
     * The guitar instrument.
     *
     * @since 1.0
     */
    public static final NoteBlockInstrument GUITAR = new NoteBlockInstrument("guitar");

    /**
     * The chime instrument.
     *
     * @since 1.0
     */
    public static final NoteBlockInstrument CHIME = new NoteBlockInstrument("chime");

    /**
     * The xylophone instrument.
     *
     * @since 1.0
     */
    public static final NoteBlockInstrument XYLOPHONE = new NoteBlockInstrument("xylophone");

    /**
     * The iron xylophone instrument.
     *
     * @since 1.0
     */
    public static final NoteBlockInstrument IRON_XYLOPHONE = new NoteBlockInstrument("iron_xylophone");

    /**
     * The cow bell instrument.
     *
     * @since 1.0
     */
    public static final NoteBlockInstrument COW_BELL = new NoteBlockInstrument("cow_bell");

    /**
     * The didgeridoo instrument.
     *
     * @since 1.0
     */
    public static final NoteBlockInstrument DIDGERIDOO = new NoteBlockInstrument("didgeridoo");

    /**
     * The bit instrument.
     *
     * @since 1.0
     */
    public static final NoteBlockInstrument BIT = new NoteBlockInstrument("bit");

    /**
     * The banjo instrument.
     *
     * @since 1.0
     */
    public static final NoteBlockInstrument BANJO = new NoteBlockInstrument("banjo");

    /**
     * The pling instrument.
     *
     * @since 1.0
     */
    public static final NoteBlockInstrument PLING = new NoteBlockInstrument("pling");

    /**
     * The instrument imitating zombie mob sounds.
     *
     * @since 1.0
     */
    public static final NoteBlockInstrument ZOMBIE = new NoteBlockInstrument("zombie");

    /**
     * The instrument imitating skeleton mob sounds.
     *
     * @since 1.0
     */
    public static final NoteBlockInstrument SKELETON = new NoteBlockInstrument("skeleton");

    /**
     * The instrument imitating creeper mob sounds.
     *
     * @since 1.0
     */
    public static final NoteBlockInstrument CREEPER = new NoteBlockInstrument("creeper");

    /**
     * The instrument imitating ender dragon mob sounds.
     *
     * @since 1.0
     */
    public static final NoteBlockInstrument DRAGON = new NoteBlockInstrument("dragon");

    /**
     * The instrument imitating wither skeleton mob sounds.
     *
     * @since 1.0
     */
    public static final NoteBlockInstrument WITHER_SKELETON = new NoteBlockInstrument("wither_skeleton");

    /**
     * The instrument imitating piglin mob sounds.
     *
     * @since 1.0
     */
    public static final NoteBlockInstrument PIGLIN = new NoteBlockInstrument("piglin");

    /**
     * The instrument playing sound specified in properties of the custom head block placed under the note block.
     *
     * @since 1.0
     */
    public static final NoteBlockInstrument CUSTOM_HEAD = new NoteBlockInstrument("custom_head");

    private final String name;

    private NoteBlockInstrument(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "NoteBlockInstrument{" +
                "name='" + this.name + '\'' +
                '}';
    }
}