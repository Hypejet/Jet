package net.hypejet.jet.world.coordinate;

import net.hypejet.jet.entity.Entity;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Range;

/**
 * A {@linkplain Coordinate coordinate} with an information about an {@linkplain Entity entity} head rotation.
 *
 * @param x an {@code X} axis value of the position
 * @param y an {@code Y} axis value of the position
 * @param z an {@code Z} axis value of the position
 * @param yaw a yaw angle of the head rotation, in Minecraft-like degrees
 * @param pitch a pitch angle of the head rotation, in Minecraft-like degrees
 * @since 1.0
 * @see Entity
 * @see Coordinate
 */
public record Position(double x, double y, double z,
                       @Range(from = MIN_YAW, to = MAX_YAW) float yaw,
                       @Range(from = MIN_PITCH, to = MAX_PITCH) float pitch) implements Coordinate<Position> {

    private static final int MAX_YAW = 180;
    private static final int MIN_YAW = -MAX_YAW;

    private static final int MAX_PITCH = 90;
    private static final int MIN_PITCH = -MAX_PITCH;

    private static final Position ZERO = new Position(0, 0, 0, 0f, 0f);

    /**
     * Constructs the {@linkplain Position position}.
     *
     * @param x the {@code X} axis value that the position should have
     * @param y the {@code Y} axis value that the position should have
     * @param z the {@code Z} axis value that the position should have
     * @param yaw the yaw angle of the head rotation that the position should have
     * @param pitch the pitch angle of the head rotation that the position should have
     * @since 1.0
     */
    // TODO: Block manual construction
    public Position {
        yaw = toMinecraftLikeDegrees(yaw);
        pitch = Math.clamp(pitch, MIN_PITCH, MAX_PITCH);
    }

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
    public @NonNull Position withYaw(@Range(from = MIN_YAW, to = MAX_YAW) float yaw) {
        return this.withView(yaw, this.pitch);
    }

    /**
     * Creates a copy of this {@linkplain Position position} with the specified pitch angle.
     *
     * @param pitch the pitch angle that the position should have
     * @return the copied position
     * @since 1.0
     */
    public @NonNull Position withPitch(@Range(from = MIN_PITCH, to = MAX_PITCH) float pitch) {
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
    public @NonNull Position withView(@Range(from = MIN_YAW, to = MAX_YAW) float yaw,
                                      @Range(from = MIN_PITCH, to = MAX_PITCH) float pitch) {
        return new Position(this.x, this.y, this.z, yaw, pitch);
    }

    /**
     * Creates a {@linkplain Position position} with the specified {@code X}, {@code Y} and {@code Z}
     * axis values and the specified head rotation values.
     *
     * @param x the {@code X} axis value that the position should have
     * @param y the {@code Y} axis value that the position should have
     * @param z the {@code Z} axis value that the position should have
     * @param yaw the yaw angle of the head rotation that the position should have
     * @param pitch the pitch angle of the head rotation that the position should have
     * @return the created position, or {@link #zero()} if all the specified values are equal to {@code 0}
     * @since 1.0
     */
    public static @NonNull Position create(double x, double y, double z,
                                           @Range(from = MIN_YAW, to = MAX_YAW) float yaw,
                                           @Range(from = MIN_PITCH, to = MAX_PITCH) float pitch) {
        if (ZERO.x == x && ZERO.y == y && ZERO.z == z && ZERO.yaw == yaw && ZERO.pitch == pitch) return ZERO;
        return new Position(x, y, z, yaw, pitch);
    }

    /**
     * Gets a {@linkplain Position position} instance with all axis and rotation values set to {@code 0}.
     *
     * @return the position
     * @since 1.0
     */
    public static @NonNull Position zero() {
        return ZERO;
    }

    private static float toMinecraftLikeDegrees(float value) {
        value %= 360;
        return value >= 180
                ? value - 360
                : (value < -180 ? value + 360 : value);
    }
}