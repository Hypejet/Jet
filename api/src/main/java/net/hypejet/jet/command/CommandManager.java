package net.hypejet.jet.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Collection;

/**
 * Something managing registration of Minecraft commands.
 *
 * @since 1.0
 * @see LiteralCommandNode
 */
public interface CommandManager {
    /**
     * Registers a command. If a command with the same name already exists, the commands are merged.
     *
     * @param node a literal command node that should represent the command
     * @since 1.0
     */
    void register(@NonNull LiteralCommandNode<CommandSource> node);

    /**
     * Unregisters a command with the specified name.
     *
     * @param name the name of the command to unregister
     * @return a literal command node representing the removed command, {@code null} if no command
     *         with the specified name was registered in this command manager
     * @since 1.0
     */
    @Nullable LiteralCommandNode<CommandSource> unregister(@NonNull String name);

    /**
     * Gets a {@linkplain LiteralCommandNode literal command node} of a command with the specified name.
     *
     * @param name the command name
     * @return the command node, {@code null} if no command with the specified name was registered
     * @since 1.0
     */
    @Nullable LiteralCommandNode<CommandSource> get(@NonNull String name);

    /**
     * Gets a copy of a {@linkplain Collection collection} of {@linkplain LiteralCommandNode literal command nodes}
     * representing commands registered in this {@linkplain CommandManager command manager}.
     *
     * @return the collection
     * @since 1.0
     */
    @NonNull Collection<LiteralCommandNode<CommandSource>> commands();
}