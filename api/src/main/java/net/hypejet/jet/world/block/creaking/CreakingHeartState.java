package net.hypejet.jet.world.block.creaking;

import org.jspecify.annotations.NullMarked;

/**
 * The state of a creaking heart block.
 *
 * <p>This is not an enum, since it depends on Minecraft.
 * Adding an enum entry could break enum switch cases for example./p>
 *
 * @since 1.0
 */
@NullMarked
public final class CreakingHeartState {
    /**
     * A {@linkplain CreakingHeartState creaking heart state} when the creaking heart is not in a tree.
     *
     * @since 1.0
     */
    public static final CreakingHeartState UPROOTED = new CreakingHeartState("uprooted");

    /**
     * A {@linkplain CreakingHeartState creaking heart state} when the creaking heart
     * is dormant, meaning that it currently does not maintain a creaking entity.
     *
     * @since 1.0
     */
    public static final CreakingHeartState DORMANT = new CreakingHeartState("dormant");

    /**
     * A {@linkplain CreakingHeartState creaking heart state} when the creaking
     * heart is awake, meaning that it currently maintains a creaking entity.
     *
     * @since 1.0
     */
    public static final CreakingHeartState AWAKE = new CreakingHeartState("awake");

    private final String name;

    private CreakingHeartState(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CreakingHeartState{" +
                "name='" + this.name + '\'' +
                '}';
    }
}