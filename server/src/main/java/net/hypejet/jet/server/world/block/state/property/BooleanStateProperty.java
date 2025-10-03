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
     * Constructs the {@linkplain BooleanStateProperty boolean state property}.
     *
     * @param name the name that the state property should have
     * @since 1.0
     */
    public BooleanStateProperty(String name) {
        super(name, Boolean.class);
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