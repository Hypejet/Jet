package net.hypejet.jet.world.coordinate;

import net.hypejet.jet.data.model.api.coordinate.Coordinate;
import net.hypejet.jet.util.math.MathUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents an implementation of {@linkplain Coordinate a coordinate} representing a position of
 * {@linkplain ??? a Minecraft block}.
 *
 * @since 1.0
 * @see ???
 * @see Coordinate
 */
public final class BlockPosition implements Coordinate<BlockPosition> {

    private static final BlockPosition ZERO = new BlockPosition(0, (short) 0, 0);

    private static final int MIN_X_OR_Z = -33554432;
    private static final int MAX_X_OR_Z = 33554431;

    private static final short MIN_Y = -2032;
    private static final short MAX_Y = 2031;

    private final int x;
    private final short y;
    private final int z;

    private BlockPosition(int x, short y, int z) {
        this.x = Math.clamp(x, MIN_X_OR_Z, MAX_X_OR_Z);
        this.y = MathUtil.clamp(y, MIN_Y, MAX_Y);
        this.z = Math.clamp(z, MIN_X_OR_Z, MAX_X_OR_Z);
    }

    @Override
    public double x() {
        return this.x;
    }

    @Override
    public double y() {
        return this.y;
    }

    @Override
    public double z() {
        return this.z;
    }

    @Override
    public @NonNull BlockPosition multiply(double x, double y, double z) {
        return blockPosition(this.x * x, this.y * y, this.z * z);
    }

    @Override
    public @NonNull BlockPosition divide(double x, double y, double z) {
        return blockPosition(this.x / x, this.y / y, this.z / z);
    }

    @Override
    public @NonNull BlockPosition add(double x, double y, double z) {
        return blockPosition(this.x + z, this.y + y, this.z + z);
    }

    @Override
    public @NonNull BlockPosition subtract(double x, double y, double z) {
        return blockPosition(this.x - z, this.y - y, this.z - z);
    }

    @Override
    public @NonNull BlockPosition withX(double x) {
        return blockPosition(Math.floor(x), this.y, this.z);
    }

    @Override
    public @NonNull BlockPosition withY(double y) {
        return blockPosition(this.x, Math.floor(y), this.z);
    }

    @Override
    public @NonNull BlockPosition withZ(double z) {
        return blockPosition(this.x, this.y, Math.floor(z));
    }

    @Override
    public int blockX() {
        return this.x;
    }

    @Override
    public int blockY() {
        return this.y;
    }

    @Override
    public int blockZ() {
        return this.z;
    }

    /**
     * Creates {@linkplain BlockPosition a block position}.
     *
     * <p>If the values specified are not integers, they are rounded down to an integer.</p>
     *
     * <p>If the values specified are higher or lower than allowed, they are clamped to the analogously - highest
     * or lowest allowed values.</p>
     *
     * @param x the {@code X} value of the position
     * @param y the {@code Y} value of the position
     * @param z the {@code Z} value of the position
     * @return the block position, the same instance is always returned when all the values provided are {@code 0}
     */
    public static @NonNull BlockPosition blockPosition(double x, double y, double z) {
        return blockPosition((int) Math.floor(x), (short) Math.floor(y), (int) Math.floor(z));
    }

    /**
     * Creates {@linkplain BlockPosition a block position}.
     *
     * <p>If the values specified are higher or lower than allowed, they are clamped to the analogously - highest
     * or lowest allowed values.</p>
     *
     * @param x the {@code X} value of the position
     * @param y the {@code Y} value of the position
     * @param z the {@code Z} value of the position
     * @return the block position, the same instance is always returned when all the values provided are {@code 0}
     */
    public static @NonNull BlockPosition blockPosition(int x, short y, int z) {
        if (ZERO.x == x && ZERO.y == y && ZERO.z == z)
            return ZERO;
        return new BlockPosition(x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BlockPosition blockPosition)) return false;
        return this.x == blockPosition.x && this.y == blockPosition.y && this.z == blockPosition.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y, this.z);
    }

    @Override
    public String toString() {
        return "BlockPosition{" +
                "x=" + this.x +
                ", y=" + this.y +
                ", z=" + this.z +
                '}';
    }
}