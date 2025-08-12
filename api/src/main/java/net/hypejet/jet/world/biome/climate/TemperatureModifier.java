package net.hypejet.jet.world.biome.climate;

import net.hypejet.jet.world.biome.Biome;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A modifier that affects the final temperature of {@linkplain Biome biomes}.
 *
 * <p>This is not an enum since it depends on Minecraft. Adding new entries could break switch cases for example.</p>
 *
 * @since 1.0
 * @see Biome
 */
public final class TemperatureModifier {
    /**
     * A temperature modifier that does not affect the resulting temperature, therefore it is static throughout
     * the biome (aside from variations depending on height).
     *
     * @since 1.0
     */
    public static final TemperatureModifier NONE = new TemperatureModifier("none");

    /**
     * A temperature modifier which randomly distributes pockets of a warm temperature, making some places
     * have temperature high enough to rain.
     *
     * @since 1.0
     */
    public static final TemperatureModifier FROZEN = new TemperatureModifier("frozen");

    private final String name;

    private TemperatureModifier(@NonNull String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    @Override
    public String toString() {
        return "TemperatureModifier{" +
                "name='" + this.name + '\'' +
                '}';
    }
}