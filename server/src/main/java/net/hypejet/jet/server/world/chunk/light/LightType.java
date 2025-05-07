package net.hypejet.jet.server.world.chunk.light;

/**
 * Represents a type of Minecraft light.
 *
 * @since 1.0
 */
public enum LightType {
    /**
     * {@linkplain LightType a light type}, which is used when the light comes from the sky.
     *
     * @since 1.0
     */
    SKY,
    /**
     * {@linkplain LightType a light type}, which is used when the light comes from a block.
     *
     * @since 1.0
     */
    BLOCK
}