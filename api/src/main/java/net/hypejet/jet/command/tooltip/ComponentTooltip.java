package net.hypejet.jet.command.tooltip;

import com.mojang.brigadier.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@linkplain Message message}, which contains a {@linkplain Component component}.
 *
 * @param component the component
 * @since 1.0
 * @author Codestech
 * @see Message
 */
public record ComponentTooltip(@NonNull Component component) implements Message, ComponentLike {
    @Override
    public String getString() {
        return PlainTextComponentSerializer.plainText().serialize(this.component);
    }

    @Override
    public @NotNull Component asComponent() {
        return this.component;
    }
}