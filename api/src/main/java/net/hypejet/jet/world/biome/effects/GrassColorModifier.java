package net.hypejet.jet.world.biome.effects;

import net.hypejet.jet.data.model.api.registries.biome.Biome;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A modifier that affects the final grass color of {@linkplain Biome biomes}.
 *
 * <p>This is not an enum since it depends on Minecraft. Adding new entries could break switch cases for example.</p>
 *
 * @since 1.0
 * @see Biome
 */
public final class GrassColorModifier {
    /**
     * A modifier that does not modify the resulting grass color, therefore it is static throughout the biome.
     *
     * @since 1.0
     */
    public static final GrassColorModifier NONE = new GrassColorModifier("none");

    /**
     * A modifier that makes the resulting grass color darker and less saturated.
     *
     * @since 1.0
     */
    public static final GrassColorModifier DARK_FOREST = new GrassColorModifier("darK_forest");

    /**
     * A modifier that overrides the resulting grass color with fixed values randomly distributed throughout
     * the biome.
     *
     * @since 1.0
     */
    public static final GrassColorModifier SWAMP = new GrassColorModifier("swamp");

    private final String name;

    private GrassColorModifier(@NonNull String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    @Override
    public String toString() {
        return "GrassColorModifier{" +
                "name='" + this.name + '\'' +
                '}';
    }
}