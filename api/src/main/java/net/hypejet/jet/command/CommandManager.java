package net.hypejet.jet.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import net.hypejet.concurrency.collection.CollectionAcquisition;
import net.hypejet.concurrency.object.notnull.NotNullObjectAcquisition;
import net.hypejet.concurrency.primitive.booleans.BooleanAcquisition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents something that manages registration of Minecraft commands.
 *
 * @since 1.0
 * @see LiteralCommandNode
 */
public interface CommandManager {
    /**
     * Registers a command.
     *
     * @param node a literal command node representing the command
     * @since 1.0
     */
    void register(@NonNull LiteralCommandNode<CommandSource> node);

    /**
     * Unregisters a command, does nothing if the command has not been registered.
     *
     * @param name a name of the command
     */
    void unregister(@NonNull String name);

    /**
     * Creates {@linkplain BooleanAcquisition a boolean acquisition}, whose state represents whether a command
     * with a name specified has been registered.
     *
     * @param name a name of the command
     * @return the boolean acquisition, whose state is {@code true} if a command with the name specified has been
     *         registered, {@code false} otherwise
     */
    @NonNull BooleanAcquisition isRegistered(@NonNull String name);

    /**
     * Creates {@linkplain NotNullObjectAcquisition an not-null object acquisition} holding
     * {@linkplain LiteralCommandNode a literal command node} representing a command, which was registered in this
     * command manager.
     *
     * <p>The {@linkplain NotNullObjectAcquisition#get() not-null object acquisition get method} throws
     * {@linkplain IllegalArgumentException an illegal argument exception} if no command with the name specified has
     * been registered.</p>
     *
     * @param name a name of the command
     * @return the object acquisition
     * @since 1.0
     */
    @NonNull NotNullObjectAcquisition<LiteralCommandNode<CommandSource>> get(@NonNull String name);

    /**
     * Creates {@linkplain CollectionAcquisition a collection acquisition} of commands, which have been registered
     * in this command manager.
     *
     * @return the collection
     * @since 1.0
     */
    @NonNull CollectionAcquisition<LiteralCommandNode<CommandSource>, ?> commands();
}