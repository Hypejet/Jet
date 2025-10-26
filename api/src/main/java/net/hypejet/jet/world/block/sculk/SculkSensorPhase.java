package net.hypejet.jet.world.block.sculk;

import org.jspecify.annotations.NullMarked;

/**
 * The current phase of activation of a sculk sensor block.
 *
 * <p>This is not an enum, since it depends on Minecraft.
 * Adding an enum entry could break enum switch cases for example./p>
 *
 * @since 1.0
 */
@NullMarked
public final class SculkSensorPhase {
    /**
     * A {@linkplain SculkSensorPhase sculk sensor phase} used when the sculk sensor block is inactive.
     *
     * @since 1.0
     */
    public static final SculkSensorPhase INACTIVE = new SculkSensorPhase("inactive");

    /**
     * A {@linkplain SculkSensorPhase sculk sensor phase} used when the sculk sensor block is active.
     *
     * @since 1.0
     */
    public static final SculkSensorPhase ACTIVE = new SculkSensorPhase("active");

    /**
     * A {@linkplain SculkSensorPhase sculk sensor phase} used when the sculk
     * sensor block is in cooldown between activation and deactivation.
     *
     * @since 1.0
     */
    public static final SculkSensorPhase COOLDOWN = new SculkSensorPhase("cooldown");

    private final String name;

    private SculkSensorPhase(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SculkSensorPhase{" +
                "name='" + this.name + '\'' +
                '}';
    }
}