package net.hypejet.jet.server.util.array;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a holder of a long array, which is intended be unmodifiable. In order to ensure that, the array is
 * cloned during construction and every time it is being got a clone is returned.
 *
 * @param array the array
 * @since 1.0
 */
public record UnmodifiableLongArray(long @NonNull [] array) {
    /**
     * Constructs the {@linkplain UnmodifiableLongArray unmodifiable long array}.
     *
     * @param array the array
     * @since 1.0
     */
    public UnmodifiableLongArray {
        array = NullabilityUtil.requireNonNull(array, "array").clone();
    }

    @Override
    public long @NonNull [] array() {
        return this.array.clone();
    }

    // --- Override equals and hashCode since records do not compare primitive arrays by contents of them ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnmodifiableLongArray otherArray)) return false;
        return Objects.deepEquals(this.array, otherArray.array);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.array);
    }
}