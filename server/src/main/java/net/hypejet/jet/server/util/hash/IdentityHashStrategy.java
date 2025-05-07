package net.hypejet.jet.server.util.hash;

import it.unimi.dsi.fastutil.Hash;

/**
 * Represents {@linkplain Hash.Strategy a hash strategy}, which provides hash code via
 * {@linkplain System#identityHashCode(Object) an identity hash code method} and returns {@code true}
 * when {@link #equals(Object, Object)} is called only when both values have the same memory references.
 *
 * @since 1.0
 * @see Hash.Strategy
 */
public final class IdentityHashStrategy implements Hash.Strategy<Object> {

    /**
     * An instance of the {@linkplain IdentityHashStrategy identity hash strategy}.
     *
     * @since 1.0
     */
    public static final IdentityHashStrategy INSTANCE = new IdentityHashStrategy();

    private IdentityHashStrategy() {}

    @Override
    public int hashCode(Object o) {
        return System.identityHashCode(o);
    }

    @Override
    public boolean equals(Object a, Object b) {
        return a == b;
    }
}