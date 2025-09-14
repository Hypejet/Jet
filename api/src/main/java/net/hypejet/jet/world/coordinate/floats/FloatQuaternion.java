package net.hypejet.jet.world.coordinate.floats;

/**
 * A quaternion represented by {@code float} values.
 *
 * @param w the rotation angle of this quaternion
 * @param x the {@code X} axis value of the imaginary part of this quaternion
 * @param y the {@code Y} axis value of the imaginary part of this quaternion
 * @param z the {@code Z} axis value of the imaginary part of this quaternion
 * @since 1.0
 */
public record FloatQuaternion(float w, float x, float y, float z) {}