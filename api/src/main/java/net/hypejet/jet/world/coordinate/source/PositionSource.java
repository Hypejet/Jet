package net.hypejet.jet.world.coordinate.source;

import net.hypejet.jet.world.coordinate.Vector;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Provider of an absolute position represented by a {@linkplain Vector vector}.
 *
 * <p>This interface is not sealed since it depends on Minecraft.
 * Adding new implementations could break switch cases for example.</p>
 *
 * @since 1.0
 * @see Vector
 */
@ApiStatus.NonExtendable
@NullMarked
public interface PositionSource {
    /**
     * Gets the position.
     *
     * @return the position, as a vector
     * @since 1.0
     */
    Vector position();
}