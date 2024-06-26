package net.hypejet.jet.link.label;

/**
 * Represents a label displayed as the {@linkplain net.hypejet.jet.link.ServerLink server link}.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.link.ServerLink
 */
public sealed interface ServerLinkLabel permits BuiltinLabel, ComponentLabel {}