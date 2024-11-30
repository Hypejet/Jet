package net.hypejet.jet.util.bitset;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.BitSet;

/**
 * Represents a wrapper of {@linkplain BitSet a bitset}, which is intended be unmodifiable. In order to ensure that,
 * the bitset is cloned during construction and every time it is being got a clone is returned.
 *
 * @param bitSet the bitset
 * @since 1.0
 * @author Codestech
 */
public record UnmodifiableBitSet(@NonNull BitSet bitSet) {
    /**
     * Constructs the {@linkplain UnmodifiableBitSet unmodifiable bitset}.
     *
     * @param bitSet the bitset
     * @since 1.0
     */
    public UnmodifiableBitSet {
        bitSet = (BitSet) NullabilityUtil.requireNonNull(bitSet, "bitset").clone();
    }

    @Override
    public @NonNull BitSet bitSet() {
        return (BitSet) this.bitSet.clone();
    }
}