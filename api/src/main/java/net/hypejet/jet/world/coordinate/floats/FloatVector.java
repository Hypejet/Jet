package net.hypejet.jet.world.coordinate.floats;

/**
 * A vector represented {@code float} coordinate values.
 *
 * @param x the {@code X} axis value of coordinates of this vector
 * @param y the {@code Y} axis value of coordinates of this vector
 * @param z the {@code Z} axis value of coordinates of this vector
 * @since 1.0
 */
public record FloatVector(float x, float y, float z) {
    /**
     * An instance of the {@linkplain FloatVector float vector} with all axis values set to {@code 0}.
     *
     * @since 1.0
     */
    public static final FloatVector ZERO = new FloatVector(0f, 0f, 0f);

    /**
     * Creates a {@linkplain FloatVector float vector} with
     * the specified {@code X}, {@code Y} and {@code Z} axis values.
     *
     * @param x the {@code X} axis value that the float vector should have
     * @param y the {@code Y} axis value that the float vector should have
     * @param z the {@code Z} axis value that the float vector should have
     * @return the created float vector, or {@link #ZERO} if all the specified values are equal to {@code 0}
     * @since 1.0
     */
    public static FloatVector floatVector(float x, float y, float z) {
        if (ZERO.x == x && ZERO.y == y && ZERO.z == z) return ZERO;
        return new FloatVector(x, y, z);
    }
}