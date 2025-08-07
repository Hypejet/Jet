package net.hypejet.jet.server.network.packet.packets.server.play;

import com.mojang.brigadier.arguments.ArgumentType;
import java.util.Objects;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Set;

/**
 *
 * Represents {@linkplain ServerPacket a server packet}, which lists commands, which are available on the server.
 *
 * @param rootNode a command node containing all commands registered on the server
 * @since 1.0
 */
public record ServerDeclareCommandsPlayPacket(@NonNull RootNode rootNode) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerDeclareCommandsPlayPacket server declare commands play packet}.
     *
     * @param rootNode a command node containing all commands registered on the server
     * @since 1.0
     */
    public ServerDeclareCommandsPlayPacket {
        Objects.requireNonNull(rootNode, "root node");
    }

    /**
     * Represents a part of a Minecraft command.
     *
     * <p>{@link #equals(Object)}, {@link #hashCode()}, {@link #toString()} methods are not implemented,
     * since it is hard - if not impossible - to do while children can redirect to their parents.</p>
     *
     * @since 1.0
     * @see ServerDeclareCommandsPlayPacket
     */
    public static sealed abstract class Node {

        private final Node redirect;
        private final boolean executable;

        private @MonotonicNonNull Set<Node> children;

        private Node(@Nullable Node redirect, boolean executable) {
            this.redirect = redirect;
            this.executable = executable;
        }

        /**
         * Gets a command node that this node redirects to.
         *
         * @return the command node, {@code null} if this node does not redirect to any node
         * @since 1.0
         */
        public final @Nullable Node redirect() {
            return this.redirect;
        }

        /**
         * Gets whether the node stack to this point constitutes to a valid command.
         *
         * @return {@code true} if the node stack constitutes to a valid command, {@code false} otherwise
         * @since 1.0
         */
        public final boolean executable() {
            return this.executable;
        }

        /**
         * A children of the command node.
         *
         * @return the children
         * @since 1.0
         */
        public final @NonNull Set<Node> children() {
            if (this.children == null)
                this.initializeChildren(Set.of());
            return this.children;
        }

        /**
         * Initializes children of the command node.
         *
         * @param children the children
         * @throws IllegalStateException if the children nodes have been already initialized
         * @since 1.0
         */
        public final void initializeChildren(@NonNull Collection<Node> children) {
            if (this.children != null)
                throw new IllegalStateException("The children nodes have been already initialized");
            this.children = Set.copyOf(children);
        }
    }

    /**
     * Represents {@linkplain Node a command node}, which is a root of a command node tree.
     *
     * @since 1.0
     * @see Node
     */
    public static final class RootNode extends Node {
        /**
         * Constructs the {@linkplain RootNode root node}.
         *
         * @param redirect a command node that the node should redirect to, {@code null} if this node should not
         *                 redirect to any node
         * @param executable whether the node stack to this point constitutes to a valid command
         * @since 1.0
         */
        public RootNode(@Nullable Node redirect, boolean executable) {
            super(redirect, executable);
        }
    }

    /**
     * Represents {@linkplain Node a command node}, which requires a literal string to be typed to execute the node.
     *
     * @since 1.0
     * @see Node
     */
    public static final class LiteralNode extends Node {

        private final String name;

        /**
         * Constructs the {@linkplain LiteralNode literal node}.
         *
         * @param redirect a command node that the node should redirect to, {@code null} if this node should
         *                 not redirect to any node
         * @param executable whether the node stack to this point constitutes to a valid command
         * @param name the literal string
         * @since 1.0
         */
        public LiteralNode(@Nullable Node redirect, boolean executable, @NonNull String name) {

            super(redirect, executable);
            this.name = Objects.requireNonNull(name, "name");
        }

        /**
         * Gets the literal string.
         *
         * @return the literal string
         * @since 1.0
         */
        public @NonNull String name() {
            return this.name;
        }
    }

    /**
     * Represents {@linkplain Node a node}, which executes with an argument typed by a command source.
     *
     * @since 1.0
     */
    public static final class ArgumentNode extends Node {

        private final String name;

        private final ArgumentType<?> argumentType;
        private final SuggestionsType suggestionsType;

        /**
         * Constructs the {@linkplain ArgumentNode argument node}.
         *
         * @param redirect a command node that the node should redirect to, {@code null} if this node should
         *                 not redirect to any node
         * @param executable whether the node stack to this point constitutes to a valid command
         * @param name a name of the argument
         * @param argumentType a type of the argument
         * @param suggestionsType a type of suggesting of the argument
         * @since 1.0
         */
        public ArgumentNode(@Nullable Node redirect, boolean executable, @NonNull String name,
                            @NonNull ArgumentType<?> argumentType, @Nullable SuggestionsType suggestionsType) {
            super(redirect, executable);
            this.name = Objects.requireNonNull(name, "name");
            this.argumentType = Objects.requireNonNull(argumentType, "argument type");
            this.suggestionsType = suggestionsType;
        }

        /**
         * Gets a name of the argument.
         *
         * @return the name
         * @since 1.0
         */
        public @NonNull String name() {
            return this.name;
        }

        /**
         * Gets a type of the argument.
         *
         * @return the type
         * @since 1.0
         */
        public @NonNull ArgumentType<?> argumentType() {
            return this.argumentType;
        }

        /**
         * Gets a type of suggesting of the argument.
         *
         * @return the type
         * @since 1.0
         */
        public @Nullable SuggestionsType suggestionsType() {
            return this.suggestionsType;
        }
    }

    /**
     * Represents a method how suggestions of an argument are displayed.
     *
     * @since 1.0
     * @see Node
     */
    public enum SuggestionsType {
        /**
         * A suggestions type, which asks a server for suggestions.
         *
         * @since 1.0
         */
        ASK_SERVER,
        /**
         * A suggestions type, which displays all recipes registered.
         *
         * @since 1.0
         */
        ALL_RECIPES,
        /**
         * A suggestions type, which displays all sounds available.
         *
         * @since 1.0
         */
        AVAILABLE_SOUNDS,
        /**
         * A suggestions type, which displays all entities, which can be summoned.
         *
         * @since 1.0
         */
        SUMMONABLE_ENTITIES
    }

    /**
     * Represents a function, which initializes children of {@linkplain Node a node}.
     *
     * <p>This allows to use node before children initialization.</p>
     *
     * @since 1.0
     * @see Node
     */
    @FunctionalInterface
    public interface ChildrenInitializer {
        /**
         * Gets a children that should be initialized in {@linkplain Node a node}.
         *
         * @param node the node
         * @return the children
         * @since 1.0
         */
        @NonNull Collection<Node> children(@NonNull Node node);
    }
}