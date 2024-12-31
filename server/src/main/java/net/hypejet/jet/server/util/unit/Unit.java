package net.hypejet.jet.server.util.unit;

/**
 * Represents {@linkplain Object an object} that has no fields and allows only to use one instance.
 *
 * <p>An example of usage is {@linkplain java.util.concurrent.Future a future} whose value is unused.</p>
 *
 * @since 1.0
 */
public final class Unit {
    /**
     * An instance of the unit class.
     *
     * @since 1.0
     */
    public static final Unit INSTANCE = new Unit();

    // Units cannot be constructed
    private Unit() {}
}