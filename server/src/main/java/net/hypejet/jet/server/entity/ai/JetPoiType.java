package net.hypejet.jet.server.entity.ai;

import net.hypejet.jet.entity.ai.PoiType;

/**
 * An implementation of a {@linkplain PoiType point of interest type}.
 *
 * @since 1.0
 * @see PoiType
 */
public record JetPoiType() implements PoiType {
    /**
     * An instance of the {@linkplain JetPoiType point of interest type implementation}.
     *
     * @since 1.0
     */
    public static final JetPoiType INSTANCE = new JetPoiType();
}