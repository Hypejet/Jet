package net.hypejet.jet.world.coordinate.source;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Provider of a world position.
 *
 * <p>This interface is not sealed since it depends on Minecraft.
 * Adding new implementations could break switch cases for example.</p>
 *
 * @since 1.0
 */
@ApiStatus.NonExtendable
@NullMarked
public interface PositionSource {}