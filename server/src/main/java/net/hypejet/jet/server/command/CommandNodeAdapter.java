package net.hypejet.jet.server.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.hypejet.jet.protocol.packet.server.play.ServerDeclareCommandsPlayPacket.ArgumentNode;
import net.hypejet.jet.protocol.packet.server.play.ServerDeclareCommandsPlayPacket.ChildrenInitializer;
import net.hypejet.jet.protocol.packet.server.play.ServerDeclareCommandsPlayPacket.LiteralNode;
import net.hypejet.jet.protocol.packet.server.play.ServerDeclareCommandsPlayPacket.Node;
import net.hypejet.jet.protocol.packet.server.play.ServerDeclareCommandsPlayPacket.RootNode;
import net.hypejet.jet.protocol.packet.server.play.ServerDeclareCommandsPlayPacket.SuggestionsType;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

/**
 * Represents a utility for converting {@linkplain CommandNode command nodes} to {@linkplain Node nodes}.
 *
 * @since 1.0
 * @author Codestech
 * @see CommandNode
 * @see Node
 */
public final class CommandNodeAdapter {

    private CommandNodeAdapter() {}

    /**
     * Converts a {@linkplain RootCommandNode root command node} to a {@linkplain RootNode root node}.
     *
     * @param commandNode the converted node
     * @return the root node
     * @throws IllegalArgumentException if the was not converted to a root node
     * @since 1.0
     */
    public static @NonNull RootNode convert(@NonNull RootCommandNode<?> commandNode) {
        Node node = getOrConvert(commandNode, new IdentityHashMap<>());
        if (!(node instanceof RootNode rootNode))
            throw new IllegalArgumentException("The node converted is not a root node");
        return rootNode;
    }

    /**
     * Gets a converted {@linkplain Node node} from a node map.
     *
     * <p>If it is not present the {@linkplain CommandNode command node} is being converted
     * via {@link #convert(CommandNode, IdentityHashMap, ChildrenInitializer)} and put into the node map.</p>
     *
     * @param node the command node to get or convert
     * @param nodes a map of nodes, which have been already converted
     * @return the got or converted node
     * @since 1.0
     */
    private static @NonNull Node getOrConvert(@NonNull CommandNode<?> node,
                                              @NonNull IdentityHashMap<CommandNode<?>, Node> nodes) {
        if (nodes.containsKey(node)) return nodes.get(node);
        return convert(node, nodes, convertedNode -> {
            // Put the node before children initialization to allow for children redirecting to it
            nodes.put(node, convertedNode);

            List<Node> children = new ArrayList<>();
            node.getChildren().forEach(child -> children.add(getOrConvert(child, nodes)));
            return List.copyOf(children);
        });
    }

    /**
     * Converts a {@linkplain CommandNode command node} into a {@linkplain Node node}.
     *
     * @param node the command node
     * @param nodes a map of nodes, which have been already converted
     * @param childrenInitializer an initializer, which initializes children of the converted node
     * @return the converted node
     * @since 1.0
     */
    private static @NonNull Node convert(@NonNull CommandNode<?> node,
                                         @NonNull IdentityHashMap<CommandNode<?>, Node> nodes,
                                         @NonNull ChildrenInitializer childrenInitializer) {
        CommandNode<?> unconvertedRedirect = node.getRedirect();
        Node redirect = null;

        if (unconvertedRedirect != null)
            redirect = getOrConvert(unconvertedRedirect, nodes);

        String name = node.getName();
        boolean executable = node.getCommand() != null;

        return switch (node) {
            case RootCommandNode<?> ignored -> new RootNode(childrenInitializer, redirect, executable);
            case LiteralCommandNode<?> ignored -> new LiteralNode(childrenInitializer, redirect, executable, name);
            case ArgumentCommandNode<?, ?> argumentNode -> {
                ArgumentType<?> argumentType = argumentNode.getType();

                SuggestionsType suggestionsType = null;
                if (argumentNode.getCustomSuggestions() != null)
                    suggestionsType = SuggestionsType.ASK_SERVER;

                yield new ArgumentNode(childrenInitializer, redirect, executable, name, argumentType, suggestionsType);
            }
            default -> throw new IllegalStateException("Unknown command node: " + node);
        };
    }
}