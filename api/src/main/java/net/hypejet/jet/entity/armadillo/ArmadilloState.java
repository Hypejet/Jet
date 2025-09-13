package net.hypejet.jet.entity.armadillo;

import net.hypejet.jet.entity.Entity;
import org.jspecify.annotations.NonNull;

/**
 * A state of an armadillo {@linkplain Entity entity}.
 *
 * <p>This is not an enum since it depends on Minecraft.
 * Adding new entries could break switch cases for example.</p>
 *
 * @since 1.0
 * @see Entity
 */
public final class ArmadilloState {
    /**
     * An idle state.
     *
     * @since 1.0
     */
    public static final ArmadilloState IDLE = new ArmadilloState("idle");

    /**
     * A rolling state.
     *
     * @since 1.0
     */
    public static final ArmadilloState ROLLING = new ArmadilloState("rolling");

    /**
     * A scared state.
     *
     * @since 1.0
     */
    public static final ArmadilloState SCARED = new ArmadilloState("scared");

    /**
     * An unrolling state.
     *
     * @since 1.0
     */
    public static final ArmadilloState UNROLLING = new ArmadilloState("unrolling");

    private final String name;

    private ArmadilloState(@NonNull String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ArmadilloState{" +
                "name='" + this.name + '\'' +
                '}';
    }
}