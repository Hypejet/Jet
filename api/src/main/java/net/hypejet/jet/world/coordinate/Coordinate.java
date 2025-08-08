package net.hypejet.jet.world.coordinate;

import net.hypejet.jet.world.World;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A position in a {@linkplain World world}.
 *
 * @param <C> the type of this coordinate
 * @see World
 */
public sealed interface Coordinate<C extends Coordinate<C>> permits BlockPosition, Position, Vector {
    /**
     * Gets a value of {@code X} axis of this {@linkplain Coordinate coordinate}.
     *
     * @return the {@code X} axis value
     * @since 1.0
     */
    double x();

    /**
     * Gets a value of {@code Y} axis of this {@linkplain Coordinate coordinate}.
     *
     * @return the {@code Y} axis value
     * @since 1.0
     */
    double y();

    /**
     * Gets a value of {@code Z} axis of this {@linkplain Coordinate coordinate}.
     *
     * @return the {@code Z} axis value
     * @since 1.0
     */
    double z();

    /**
     * Creates a copy of this {@linkplain Coordinate coordinate} with the specified {@code X}, {@code Y} and {@code Z}
     * axis values.
     *
     * @param x the {@code X} axis value that the coordinate should have
     * @param y the {@code Y} axis value that the coordinate should have
     * @param z the {@code Z} axis value that the coordinate should have
     * @return the copied coordinate
     * @since 1.0
     */
    @NonNull C withValues(double x, double y, double z);

    /**
     * Creates a copy of this {@linkplain Coordinate coordinate} with {@code X}, {@code Y} and {@code Z} axis values
     * of the specified {@linkplain Coordinate coordinate}.
     *
     * @param other the coordinate to whose axis values should be used for the coordinate copy creation
     * @return the copied coordinate
     * @since 1.0
     */
    default @NonNull C withValues(@NonNull Coordinate<?> other) {
        return this.withValues(other.x(), other.y(), other.z());
    }

    /**
     * Gets the rounded down value of {@code X} axis of this {@linkplain Coordinate coordinate}.
     *
     * @return the rounded down {@code X} axis value
     * @since 1.0
     */
    default int blockX() {
        return (int) Math.floor(this.x());
    }

    /**
     * Gets the rounded down value of {@code Y} axis of this {@linkplain Coordinate coordinate}.
     *
     * @return the rounded down {@code Y} axis value
     * @since 1.0
     */
    default int blockY() {
        return (int) Math.floor(this.y());
    }

    /**
     * Gets the rounded down value of {@code Z} axis of this {@linkplain Coordinate coordinate}.
     *
     * @return the rounded down {@code Z} axis value
     * @since 1.0
     */
    default int blockZ() {
        return (int) Math.floor(this.z());
    }

    /**
     * Creates a copy of this {@linkplain Coordinate coordinate} with {@code X}, {@code Y} and {@code Z} axis values
     * summed with the specified values.
     *
     * @param x an addend to sum the {@code X} axis value with
     * @param y an addend to sum the {@code Y} axis value with
     * @param z an addend to sum the {@code Z} axis value with
     * @return the copied coordinate
     * @since 1.0
     */
    default @NonNull C add(double x, double y, double z) {
        return this.withValues(this.x() + x, this.y() + y, this.z() + z);
    }

    /**
     * Creates a copy of this {@linkplain Coordinate coordinate} with {@code X}, {@code Y} and {@code Z} axis values
     * subtracted by the specified values.
     *
     * @param x a subtrahend to subtract the {@code X} axis value with
     * @param y a subtrahend to subtract the {@code Y} axis value with
     * @param z a subtrahend to subtract the {@code Z} axis value with
     * @return the copied coordinate
     * @since 1.0
     */
    default @NonNull C subtract(double x, double y, double z) {
        return this.withValues(this.x() - x, this.y() - y, this.z() - z);
    }

    /**
     * Creates a copy of this {@linkplain Coordinate coordinate} with {@code X}, {@code Y} and {@code Z} axis values
     * multiplied by the specified values.
     *
     * @param x a multiplier to multiply the {@code X} axis value with
     * @param y a multiplier to multiply the {@code Y} axis value with
     * @param z a multiplier to multiply the {@code Z} axis value with
     * @return the copied coordinate
     * @since 1.0
     */
    default @NonNull C multiply(double x, double y, double z) {
        return this.withValues(this.x() * x, this.y() * y, this.z() * z);
    }

    /**
     * Creates a copy of this {@linkplain Coordinate coordinate} with {@code X}, {@code Y} and {@code Z} axis values
     * divided by the specified values.
     *
     * @param x a divisor to divide the {@code X} axis value with
     * @param y a divisor to divide the {@code Y} axis value with
     * @param z a divisor to divide the {@code Z} axis value with
     * @return the copied coordinate
     * @since 1.0
     */
    default @NonNull C divide(double x, double y, double z) {
        return this.withValues(this.x() / x, this.y() / y, this.z() / z);
    }
}