package net.hypejet.jet.link.label;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerLinkLabel server link label} that displays a {@linkplain Component component}.
 *
 * @param component the component to display
 * @since 1.0
 * @author Codestech
 * @see ServerLinkLabel
 */
public record ComponentLabel(@NonNull Component component) implements ServerLinkLabel {}