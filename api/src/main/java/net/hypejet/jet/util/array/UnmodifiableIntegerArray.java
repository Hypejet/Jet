package net.hypejet.jet.util.array;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a wrapper of an integer array, which is intended be unmodifiable. In order to ensure that, the array is
 * cloned during construction and every time it is being got a clone is returned.
 *
 * @param array the array
 * @since 1.0
 */
public record UnmodifiableIntegerArray(int @NonNull [] array) {
    /**
     * Constructs the {@linkplain UnmodifiableIntegerArray unmodifiable integer array}.
     *
     * @param array the array
     * @since 1.0
     */
    public UnmodifiableIntegerArray {
        array = NullabilityUtil.requireNonNull(array, "array").clone();
    }

    @Override
    public int @NonNull [] array() {
        return this.array.clone();
    }

    // --- Override equals and hashCode since records do not compare primitive arrays by contents of them ---

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UnmodifiableIntegerArray otherArray)) return false;
        return Objects.deepEquals(this.array, otherArray.array);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.array);
    }
}