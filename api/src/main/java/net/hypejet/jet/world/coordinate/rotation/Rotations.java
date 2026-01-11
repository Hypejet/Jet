package net.hypejet.jet.world.coordinate.rotation;

import net.hypejet.jet.world.coordinate.Coordinate;

/**
 * Specification of rotations around each axis of a {@linkplain Coordinate coordinate}.
 *
 * @param x the rotation around the {@code X} axis, in degrees
 * @param y the rotation around the {@code Y} axis, in degrees
 * @param z the rotation around the {@code Z} axis, in degrees
 * @since 1.0
 * @see Coordinate
 */
public record Rotations(float x, float y, float z) {}