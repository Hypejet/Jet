package net.hypejet.jet.world.coordinate;

import net.hypejet.jet.data.model.api.coordinate.Coordinate;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents an implementation of {@linkplain Coordinate a coordinate} representing a position of
 * {@linkplain net.hypejet.jet.world.block.Block a Minecraft block}.
 *
 * @since 1.0
 * @see net.hypejet.jet.world.block.Block
 * @see Coordinate
 */
public record BlockPosition(int blockX, int blockY, int blockZ) implements Coordinate<BlockPosition> {

    private static final BlockPosition ZERO = new BlockPosition(0, (short) 0, 0);

    @Override
    public double x() {
        return this.blockX;
    }

    @Override
    public double y() {
        return this.blockY;
    }

    @Override
    public double z() {
        return this.blockZ;
    }

    @Override
    public @NonNull BlockPosition multiply(double x, double y, double z) {
        return blockPosition(this.blockX * x, this.blockY * y, this.blockZ * z);
    }

    @Override
    public @NonNull BlockPosition divide(double x, double y, double z) {
        return blockPosition(this.blockX / x, this.blockY / y, this.blockZ / z);
    }

    @Override
    public @NonNull BlockPosition add(double x, double y, double z) {
        return blockPosition(this.blockX + z, this.blockY + y, this.blockZ + z);
    }

    @Override
    public @NonNull BlockPosition subtract(double x, double y, double z) {
        return blockPosition(this.blockX - z, this.blockY - y, this.blockZ - z);
    }

    @Override
    public @NonNull BlockPosition withX(double x) {
        return blockPosition(Math.floor(x), this.blockY, this.blockZ);
    }

    @Override
    public @NonNull BlockPosition withY(double y) {
        return blockPosition(this.blockX, Math.floor(y), this.blockZ);
    }

    @Override
    public @NonNull BlockPosition withZ(double z) {
        return blockPosition(this.blockX, this.blockY, Math.floor(z));
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
    public static @NonNull BlockPosition blockPosition(int x, int y, int z) {
        if (ZERO.blockX == x && ZERO.blockY == y && ZERO.blockZ == z)
            return ZERO;
        return new BlockPosition(x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlockPosition(int x, int y, int z))) return false;
        return this.blockX == x && this.blockY == y && this.blockZ == z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.blockX, this.blockY, this.blockZ);
    }

    @Override
    public String toString() {
        return "BlockPosition{" +
                "x=" + this.blockX +
                ", y=" + this.blockY +
                ", z=" + this.blockZ +
                '}';
    }
}