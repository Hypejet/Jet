package net.hypejet.jet.world.coordinate;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain Coordinate coordinate} with no specific use-case or additional fields.
 *
 * @param x an {@code X} axis value of the vector
 * @param y an {@code Y} axis value of the vector
 * @param z an {@code Z} axis value of the vector
 * @since 1.0
 * @see Coordinate
 */
public record Vector(double x, double y, double z) implements Coordinate<Vector> {
    /**
     * An instance of the {@linkplain Vector vector} with all axis values set to {@code 0}.
     *
     * @since 1.0
     */
    private static final Vector ZERO = new Vector(0, 0, 0);

    @Override
    public @NonNull Vector withValues(double x, double y, double z) {
        return new Vector(x, y, z);
    }

    /**
     * Creates a {@linkplain Vector vector} using values from the specified {@linkplain Coordinate coordinate}.
     *
     * @param coordinate the vector to create the vector from
     * @return the created vector, or the specified coordinate if it is a vector
     * @since 1.0
     */
    public static @NonNull Vector from(@NonNull Coordinate<?> coordinate) {
        if (coordinate instanceof Vector vector) return vector;
        return create(coordinate.x(), coordinate.y(), coordinate.z());
    }

    /**
     * Creates a {@linkplain Vector vector} with the specified {@code X}, {@code Y} and {@code Z} axis values.
     *
     * @param x the {@code X} axis value that the vector should have
     * @param y the {@code Y} axis value that the vector should have
     * @param z the {@code Z} axis value that the vector should have
     * @return the created vector, or {@link #zero()} if all the specified values are equal to {@code 0}
     * @since 1.0
     */
    public static @NonNull Vector create(double x, double y, double z) {
        if (x == ZERO.x && y == ZERO.y && z == ZERO.z) return ZERO;
        return new Vector(x, y, z);
    }

    /**
     * Gets a {@linkplain Vector vector} instance with all axis values set to {@code 0}.
     *
     * @return the vector
     * @since 1.0
     */
    public static @NonNull Vector zero() {
        return ZERO;
    }
}