package net.hypejet.jet.server.world.fluid;

import net.hypejet.jet.world.fluid.Fluid;

/**
 * An implementation of a {@linkplain Fluid fluid}.
 *
 * @since 1.0
 * @see Fluid
 */
public record JetFluid() implements Fluid {
    /**
     * An instance of the {@linkplain JetFluid fluid implementation}.
     *
     * @since 1.0
     */
    public static final JetFluid INSTANCE = new JetFluid();
}