package net.hypejet.jet.server.world.block.state.property;

import org.jspecify.annotations.NullMarked;

/**
 * A {@linkplain StateProperty state property} accepting {@linkplain Boolean boolean} values.
 *
 * @since 1.0
 * @see Boolean
 * @see StateProperty
 */
@NullMarked
public final class BooleanStateProperty extends StateProperty<Boolean> {
    /**
     * An instance of the {@linkplain BooleanStateProperty boolean state property}.
     *
     * @since 1.0
     */
    public static final BooleanStateProperty INSTANCE = new BooleanStateProperty();

    private BooleanStateProperty() {
        super(Boolean.class);
    }

    @Override
    public String valueToString(Boolean value) {
        return value.toString();
    }

    @Override
    public Boolean valueFromString(String valueString) {
        return Boolean.parseBoolean(valueString);
    }
}