package net.hypejet.jet.world.coordinate;

import net.hypejet.jet.entity.Entity;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain Coordinate coordinate} with an information about an {@linkplain Entity entity} rotation.
 *
 * @param x an {@code X} axis value of the position
 * @param y an {@code Y} axis value of the position
 * @param z an {@code Z} axis value of the position
 * @param yaw a yaw angle of the view, in Minecraft-like degrees
 * @param pitch a pitch angle of the view, in Minecraft-like degrees
 * @since 1.0
 * @see Entity
 * @see Coordinate
 */
public record Position(double x, double y, double z, float yaw, float pitch) implements Coordinate<Position> {
    @Override
    public @NonNull Position withValues(double x, double y, double z) {
        return new Position(x, y, z, this.yaw, this.pitch);
    }

    /**
     * Creates a copy of this {@linkplain Position position} with the specified yaw angle.
     *
     * @param yaw the yaw angle that the position should have
     * @return the copied position
     * @since 1.0
     */
    public @NonNull Position withYaw(float yaw) {
        return this.withView(yaw, this.pitch);
    }

    /**
     * Creates a copy of this {@linkplain Position position} with the specified pitch angle.
     *
     * @param pitch the pitch angle that the position should have
     * @return the copied position
     * @since 1.0
     */
    public @NonNull Position withPitch(float pitch) {
        return this.withView(this.yaw, pitch);
    }

    /**
     * Creates a copy of this {@linkplain Position position} with the specified yaw and pitch angles.
     *
     * @param yaw the yaw angle that the position should have
     * @param pitch the pitch angle that the position should have
     * @return the copied position
     * @since 1.0
     */
    public @NonNull Position withView(float yaw, float pitch) {
        return new Position(this.x, this.y, this.z, yaw, pitch);
    }
}