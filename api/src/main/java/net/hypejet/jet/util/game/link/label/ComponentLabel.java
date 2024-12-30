package net.hypejet.jet.util.game.link.label;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

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
        NullabilityUtil.requireNonNull(component, "component");
    }
}