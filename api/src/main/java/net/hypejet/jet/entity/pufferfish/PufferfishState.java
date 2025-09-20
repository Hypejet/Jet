package net.hypejet.jet.entity.pufferfish;

import org.jspecify.annotations.NonNull;

import net.hypejet.jet.entity.Entity;

/**
 * A state of an pufferfish {@linkplain Entity entity}.
 *
 * <p>This is not an enum since it depends on Minecraft.
 * Adding new entries could break switch cases for example.</p>
 *
 * @since 1.0
 * @see Entity
 */
public final class PufferfishState {

    /**
     * A small state.
     *
     * @since 1.0
     */
    public static final PufferfishState SMALL = new PufferfishState("small");

    /**
     * A partially puffed state.
     *
     * @since 1.0
     */
    public static final PufferfishState PARTIALLY_PUFFED = new PufferfishState("partially_puffed");

    /**
     * A fully puffed state.
     *
     * @since 1.0
     */
    public static final PufferfishState FULL = new PufferfishState("full");

    private final String name;

    private PufferfishState(@NonNull String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PufferfishState{" +
                "name='" + this.name + '\'' +
                '}';
    }
}
