package net.hypejet.jet.util.game.link.label;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents {@linkplain ServerLinkLabel a server link label} that displays {@linkplain Component a component}.
 *
 * @param component the component to display
 * @since 1.0
 * @see ServerLinkLabel
 */
public record ComponentLabel(@NonNull Component component) implements ServerLinkLabel {
    /**
     * Constructs the {@linkplain ComponentLabel component label}.
     *
     * @param component the component to display
     * @since 1.0
     */
    public ComponentLabel {
        Objects.requireNonNull(component, "component");
    }
}