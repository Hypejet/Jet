package net.hypejet.jet.protocol.packet.server.play;

import com.mojang.brigadier.arguments.ArgumentType;
import net.hypejet.jet.protocol.packet.server.ServerPlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a {@linkplain ServerPlayPacket server play packet}, which lists commands, which are available on the
 * server for a client.
 *
 * @param rootNode
 * @since 1.0
 * @author Codestech
 */
public record ServerDeclareCommandsPlayPacket(@NonNull RootNode rootNode) implements ServerPlayPacket {
    /**
     * Represents a part of a Minecraft command.
     *
     * <p>{@link #equals(Object)}, {@link #hashCode()}, {@link #toString()} methods are not implemented,
     * since it is hard if not impossible to do while children can redirect to their parents.</p>
     *
     * @since 1.0
     * @author Codestech
     * @see ServerDeclareCommandsPlayPacket
     */
    public sealed interface Node {
        /**
         * A children of the command node.
         *
         * @return the children
         * @since 1.0
         */
        @NonNull Collection<Node> children();

        /**
         * Gets a command node that this node redirects to.
         *
         * @return the command node, {@code null} if this node does not redirect to any node
         * @since 1.0
         */
        @Nullable Node redirect();

        /**
         * Gets whether the node stack to this point constitutes to a valid command.
         *
         * @return {@code true} if the node stack constitutes to a valid command, {@code false} otherwise
         * @since 1.0
         */
        boolean executable();
    }

    /**
     * Represents a main {@linkplain Node node}, which is a root of a command node tree.
     *
     * @since 1.0
     * @author Codestech
     * @see Node
     */
    public static final class RootNode implements Node {

        private final Collection<Node> children;
        private final Node redirect;
        private final boolean executable;

        /**
         * Constructs the {@linkplain RootNode root node}.
         *
         * @param children a children of the command node
         * @param redirect a command node that the node should redirect to, {@code null} if this node should not
         *                 redirect to any node
         * @param executable whether the node stack to this point constitutes to a valid command
         * @since 1.0
         */
        public RootNode(@NonNull Collection<Node> children, @Nullable Node redirect, boolean executable) {
            this.children = List.copyOf(Objects.requireNonNull(children, "The children must not be null"));
            this.redirect = redirect;
            this.executable = executable;
        }

        /**
         * Constructs the {@linkplain RootNode root node}.
         *
         * @param childrenInitializer an initializer of children of the command node
         * @param redirect a command node that the node should redirect to, {@code null} if this node should not
         *                 redirect to any node
         * @param executable whether the node stack to this point constitutes to a valid command
         * @since 1.0
         */
        public RootNode(@NonNull ChildrenInitializer childrenInitializer, @Nullable Node redirect,
                        boolean executable) {
            Collection<Node> children = childrenInitializer.children(this);
            this.children = List.copyOf(Objects.requireNonNull(children, "The children must not be null"));
            this.redirect = redirect;
            this.executable = executable;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NonNull Collection<Node> children() {
            return this.children;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @Nullable Node redirect() {
            return this.redirect;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean executable() {
            return this.executable;
        }
    }

    /**
     * Represents a {@linkplain Node node}, which requires a literal string to be typed to execute the node.
     *
     * @since 1.0
     * @author Codestech
     * @see Node
     */
    public static final class LiteralNode implements Node {

        private final Collection<Node> children;
        private final Node redirect;
        private final boolean executable;
        private final String name;

        /**
         * Constructs the {@linkplain LiteralNode literal node}.
         *
         * @param children a children of the command node
         * @param redirect a command node that the node should redirect to, {@code null} if this node should not redirect
         *                 to any node
         * @param executable whether the node stack to this point constitutes to a valid command
         * @param name the literal string
         * @since 1.0
         */
        public LiteralNode(@NonNull Collection<Node> children, @Nullable Node redirect,
                           boolean executable, @NonNull String name) {
            this.children = List.copyOf(Objects.requireNonNull(children, "The children must not be null"));
            this.redirect = redirect;
            this.executable = executable;
            this.name = Objects.requireNonNull(name, "The name must not be null");
        }

        /**
         * Constructs the {@linkplain LiteralNode literal node}.
         *
         * @param childrenInitializer an initializer of children of the command node
         * @param redirect a command node that the node should redirect to, {@code null} if this node should not redirect
         *                 to any node
         * @param executable whether the node stack to this point constitutes to a valid command
         * @param name the literal string
         * @since 1.0
         */
        public LiteralNode(@NonNull ChildrenInitializer childrenInitializer, @Nullable Node redirect,
                           boolean executable, @NonNull String name) {
            Collection<Node> children = childrenInitializer.children(this);
            this.children = List.copyOf(Objects.requireNonNull(children, "The children must not be null"));
            this.redirect = redirect;
            this.executable = executable;
            this.name = Objects.requireNonNull(name, "The name must not be null");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NonNull Collection<Node> children() {
            return this.children;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @Nullable Node redirect() {
            return this.redirect;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean executable() {
            return this.executable;
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
     * Represents a {@linkplain Node node}, which executes with an argument typed by a command source.
     *
     * @since 1.0
     * @author Codestech
     */
    public static final class ArgumentNode implements Node {

        private final Collection<Node> children;
        private final Node redirect;
        private final boolean executable;

        private final String name;

        private final ArgumentType<?> argumentType;
        private final SuggestionsType suggestionsType;

        /**
         * Constructs the {@linkplain ArgumentNode argument node}.
         *
         * @param children a children of the command node
         * @param redirect a command node that the node should redirect to, {@code null} if this node should not redirect
         *                 to any node
         * @param executable whether the node stack to this point constitutes to a valid command
         * @param name a name of the argument
         * @param argumentType a type of the argument
         * @param suggestionsType a type of suggesting of the argument
         * @since 1.0
         */
        public ArgumentNode(@NonNull Collection<Node> children, @Nullable Node redirect, boolean executable,
                            @NonNull String name, @NonNull ArgumentType<?> argumentType,
                            @Nullable SuggestionsType suggestionsType) {
            this.children = List.copyOf(Objects.requireNonNull(children, "The children must not be null"));
            this.redirect = redirect;
            this.executable = executable;
            this.name = Objects.requireNonNull(name, "The name must not be null");
            this.argumentType = Objects.requireNonNull(argumentType, "The argument type must not be null");
            this.suggestionsType = suggestionsType;
        }

        /**
         * Constructs the {@linkplain ArgumentNode argument node}.
         *
         * @param childrenInitializer a children of the command node
         * @param redirect a command node that the node should redirect to, {@code null} if this node should not redirect
         *                 to any node
         * @param executable whether the node stack to this point constitutes to a valid command
         * @param name a name of the argument
         * @param argumentType a type of the argument
         * @param suggestionsType a type of suggesting of the argument
         * @since 1.0
         */
        public ArgumentNode(@NonNull ChildrenInitializer childrenInitializer, @Nullable Node redirect,
                            boolean executable, @NonNull String name, @NonNull ArgumentType<?> argumentType,
                            @Nullable SuggestionsType suggestionsType) {
            Collection<Node> children = childrenInitializer.children(this);
            this.children = List.copyOf(Objects.requireNonNull(children, "The children must not be null"));
            this.redirect = redirect;
            this.executable = executable;
            this.name = Objects.requireNonNull(name, "The name must not be null");
            this.argumentType = Objects.requireNonNull(argumentType, "The argument type must not be null");
            this.suggestionsType = suggestionsType;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NonNull Collection<Node> children() {
            return this.children;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @Nullable Node redirect() {
            return this.redirect;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean executable() {
            return this.executable;
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
     * @author Codestech
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
     * Represents a function, which initializes children of a {@linkplain Node node}.
     *
     * <p>This allows to use node before children initialization.</p>
     *
     * @since 1.0
     * @author Codestech
     * @see Node
     */
    @FunctionalInterface
    public interface ChildrenInitializer {
        /**
         * Gets a children that should be initialized in a {@linkplain Node node}.
         *
         * @param node the node
         * @return the children
         * @since 1.0
         */
        @NonNull Collection<Node> children(@NonNull Node node);
    }
}