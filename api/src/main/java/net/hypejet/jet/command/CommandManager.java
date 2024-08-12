package net.hypejet.jet.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;

/**
 * Represents a manager, which managers registration of Minecraft commands.
 *
 * @since 1.0
 * @author Codestech
 * @see LiteralCommandNode
 */
public interface CommandManager {
    /**
     * Registers a command.
     *
     * @param node the command
     * @since 1.0
     */
    void register(@NonNull LiteralCommandNode<CommandSource> node);

    /**
     * Gets a registered command by a name.
     *
     * @param name the name
     * @return the commands
     * @since 1.0
     */
    @Nullable LiteralCommandNode<CommandSource> get(@NonNull String name);

    /**
     * Gets a {@linkplain Collection collection} of commands, which have been registered.
     *
     * @return the collection
     * @since 1.0
     */
    @NonNull Collection<LiteralCommandNode<CommandSource>> commands();
}
