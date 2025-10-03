package net.hypejet.jet.server.world.block.state.property;

import net.hypejet.jet.server.util.index.IndexUtil;
import net.kyori.adventure.util.Index;
import org.jspecify.annotations.NullMarked;

import java.util.Map;
import java.util.Objects;

/**
 * A {@linkplain StateProperty state property} accepting
 * {@linkplain Object object} values with static set of instances.
 *
 * @param <V> the type of objects accepted by this state property
 * @since 1.0
 * @see Object
 * @see StateProperty
 */
@NullMarked
public final class EnumLikeStateProperty<V> extends StateProperty<V> {

    private final Index<String, V> valueStrings;

    /**
     * Constructs the {@linkplain EnumLikeStateProperty enum-like state property}.
     *
     * @param name the name that the state property should have
     * @param objectClass the class of values that the state property should accept
     * @param values a map associating values that should be accepted
     *               by the state property with their string representations
     * @since 1.0
     */
    public EnumLikeStateProperty(String name, Class<V> objectClass, Map<V, String> values) {
        super(name, objectClass);
        this.valueStrings = IndexUtil.fromMap(Objects.requireNonNull(values, "values"));
    }

    @Override
    public String valueToString(V value) {
        String valueString = this.valueStrings.key(value);
        if (valueString == null)
            throw new IllegalArgumentException("Unknown value: " + value);
        return valueString;
    }

    @Override
    public V valueFromString(String valueString) {
        V value = this.valueStrings.value(valueString);
        if (value == null)
            throw new IllegalArgumentException("Unknown string-represented value: " + valueString);
        return value;
    }
}