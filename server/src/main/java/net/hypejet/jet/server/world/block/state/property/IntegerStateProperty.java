package net.hypejet.jet.server.world.block.state.property;

import net.hypejet.jet.util.range.RangeUtil;
import org.jspecify.annotations.NullMarked;

/**
 * A {@linkplain StateProperty state property} accepting {@linkplain Integer integer} values.
 *
 * @since 1.0
 * @see Integer
 * @see StateProperty
 */
@NullMarked
public final class IntegerStateProperty extends StateProperty<Integer> {

    private final int min;
    private final int max;

    /**
     * Constructs the {@linkplain IntegerStateProperty integer state property}.
     *
     * @param name the name that the state property should have
     * @param min a minimum integer value that the state property should accept
     * @param max a maximum integer value that the state property should accept
     * @since 1.0
     */
    public IntegerStateProperty(String name, int min, int max) {
        super(name, Integer.class);
        this.min = min;
        this.max = max;
    }

    @Override
    public String valueToString(Integer value) {
        RangeUtil.ensureInRange(this.min, this.max, value);
        return value.toString();
    }

    @Override
    public Integer valueFromString(String valueString) {
        int value = Integer.parseInt(valueString);
        RangeUtil.ensureInRange(this.min, this.max, value);
        return value;
    }
}