package net.hypejet.jet.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.hypejet.concurrency.collection.CollectionAcquisition;
import net.hypejet.concurrency.object.notnull.NotNullObjectAcquirable;
import net.hypejet.concurrency.object.notnull.NotNullObjectAcquisition;
import net.hypejet.concurrency.primitive.booleans.BooleanAcquisition;
import net.hypejet.jet.command.CommandManager;
import net.hypejet.jet.command.CommandSource;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.event.command.CommandExecuteEvent;
import net.hypejet.jet.event.command.CommandExecutionFailureEvent;
import net.hypejet.jet.event.command.CommandPreExecuteEvent;
import net.hypejet.jet.event.command.CommandPreParseEvent;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.command.exceptions.CommandParseException;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerDeclareCommandsPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerDeclareCommandsPlayPacket.ArgumentNode;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerDeclareCommandsPlayPacket.LiteralNode;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerDeclareCommandsPlayPacket.Node;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerDeclareCommandsPlayPacket.RootNode;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.task.PlaySessionTask;
import net.hypejet.jet.server.util.acquisition.BooleanMappedAcquisition;
import net.hypejet.jet.server.util.acquisition.CollectionMappedAcquisition;
import net.hypejet.jet.server.util.acquisition.NotNullObjectMappedAcquisition;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Represents an implementation of the {@linkplain CommandManager command manager}.
 *
 * @since 1.0
 * @see CommandManager
 */
public final class JetCommandManager implements CommandManager {

    private static final IllegalArgumentException NOT_LITERAL_EXCEPTION
            = new IllegalArgumentException("Root command node cannot contain command nodes other than literals");
    private static final char COMMAND_PREFIX = '/';

    private static final Logger LOGGER = LoggerFactory.getLogger(JetCommandManager.class);

    private final JetMinecraftServer server;
    private final NotNullObjectAcquirable<CommandDispatcher<CommandSource>> dispatcher;

    /**
     * Constructs the {@linkplain JetCommandManager command manager}.
     *
     * @param server a server, on which the commands should be managed
     * @since 1.0
     */
    public JetCommandManager(@NonNull JetMinecraftServer server) {
        this.server = NullabilityUtil.requireNonNull(server, "server");
        this.dispatcher = new NotNullObjectAcquirable<>(new CommandDispatcher<>());
    }

    @Override
    public void register(@NonNull LiteralCommandNode<CommandSource> node) {
        try (
                NotNullObjectAcquisition<CommandDispatcher<CommandSource>>
                        dispatcherAcquisition = this.dispatcher.acquireWrite();
                CollectionAcquisition<JetPlayer, ?> playersAcquisition = this.server.players()
        ) {
            CommandDispatcher<CommandSource> dispatcher = dispatcherAcquisition.get();
            dispatcherAcquisition.get().getRoot().addChild(node);
            updateCommands(playersAcquisition.collection(), dispatcher);
        }
    }

    @Override
    public void unregister(@NonNull String name) {
        try (
                NotNullObjectAcquisition<CommandDispatcher<CommandSource>>
                        dispatcherAcquisition = this.dispatcher.acquireWrite();
                CollectionAcquisition<JetPlayer, ?> playersAcquisition = this.server.players()
        ) {
            CommandDispatcher<CommandSource> dispatcher = dispatcherAcquisition.get();
            dispatcher.getRoot().removeChildByName(name);
            updateCommands(playersAcquisition.collection(), dispatcher);
        }
    }

    @Override
    public @NonNull BooleanAcquisition isRegistered(@NonNull String name) {
        return new BooleanMappedAcquisition<>(
                this.dispatcher.acquireRead(),
                acquisition -> acquisition.get().getRoot().getChild(name) != null
        );
    }

    @Override
    public @NonNull NotNullObjectAcquisition<LiteralCommandNode<CommandSource>> get(@NonNull String name) {
        return new NotNullObjectMappedAcquisition<>(this.dispatcher.acquireRead(), acquisition -> {
            RootCommandNode<CommandSource> rootNode = acquisition.get().getRoot();

            CommandNode<CommandSource> node = rootNode.getChild(name);
            if (node == null) {
                throw new IllegalArgumentException(String.format(
                        "No command node with name of \"%s\" was registered",
                        name
                ));
            }

            if (!(rootNode.getChild(name) instanceof LiteralCommandNode<CommandSource> literalNode))
                throw NOT_LITERAL_EXCEPTION;
            return literalNode;
        });
    }

    @Override
    public @NonNull CollectionAcquisition<LiteralCommandNode<CommandSource>, ?> commands() {
        return new CollectionMappedAcquisition<>(this.dispatcher.acquireRead(), acquisition -> {
            Set<LiteralCommandNode<CommandSource>> nodes = new HashSet<>();

            for (CommandNode<CommandSource> child : acquisition.get().getRoot().getChildren()) {
                if (!(child instanceof LiteralCommandNode<CommandSource> literalNode))
                    throw NOT_LITERAL_EXCEPTION;
                nodes.add(literalNode);
            }

            return Set.copyOf(nodes);
        });
    }

    /**
     * Executes a command.
     *
     * @param input a raw command string input
     * @param source a sender of the command input
     * @since 1.0
     */
    public void execute(@NonNull String input, @NonNull CommandSource source) {
        try (NotNullObjectAcquisition<CommandDispatcher<CommandSource>> acquisition = this.dispatcher.acquireRead()) {
            CommandDispatcher<CommandSource> dispatcher = acquisition.get();
            EventNode<Object> eventNode = this.server.eventNode();

            CommandPreParseEvent preParseEvent = new CommandPreParseEvent(source, input);
            eventNode.call(preParseEvent);
            if (preParseEvent.isCancelled()) return;

            input = preParseEvent.getInput();
            ParseResults<CommandSource> parseResults = parse(new StringReader(input), source, dispatcher);

            CommandPreExecuteEvent preExecuteEvent = new CommandPreExecuteEvent(source, input, parseResults);
            eventNode.call(preExecuteEvent);
            if (preParseEvent.isCancelled()) return;

            int executionResult;

            try {
                executionResult = dispatcher.execute(parseResults);
            } catch (CommandSyntaxException exception) {
                eventNode.call(new CommandExecutionFailureEvent(source, exception));
                return;
            } catch (Throwable throwable) {
                LOGGER.error("An error occurred while executing a command", throwable);
                return;
            }

            eventNode.call(new CommandExecuteEvent(source, input, parseResults, executionResult));
        }
    }

    /**
     * Creates {@linkplain CompletableFuture a completable future}, whose result are
     * {@linkplain Suggestions suggestions} for a raw command string input specified.
     *
     * @param input the raw command string input
     * @param source a sender of the command input
     * @return the completable future
     * @since 1.0
     */
    public @NonNull CompletableFuture<Suggestions> suggest(@NonNull String input, @NonNull CommandSource source) {
        try (NotNullObjectAcquisition<CommandDispatcher<CommandSource>> acquisition = this.dispatcher.acquireRead()) {
            StringReader reader = new StringReader(input);
            if (reader.canRead() && reader.peek() == COMMAND_PREFIX)
                reader.skip();

            CommandDispatcher<CommandSource> dispatcher = acquisition.get();
            ParseResults<CommandSource> parseResults = parse(reader, source, dispatcher);

            return acquisition.get().getCompletionSuggestions(parseResults);
        }
    }

    /**
     * Creates and sends {@linkplain ServerDeclareCommandsPlayPacket a server declare commands play packet}
     * to a player specified.
     *
     * @param player the player to send the packet to
     * @since 1.0
     */
    public void sendDeclarationPacket(@NonNull JetPlayer player) {
        try (NotNullObjectAcquisition<CommandDispatcher<CommandSource>> acquisition = this.dispatcher.acquireRead()) {
            player.sendPacket(createDeclarationPacket(acquisition.get()));
        }
    }

    private static void updateCommands(@NonNull Collection<JetPlayer> playerCollection,
                                       @NonNull CommandDispatcher<CommandSource> dispatcher) {
        if (playerCollection.isEmpty()) return;
        ServerDeclareCommandsPlayPacket declarationPacket = createDeclarationPacket(dispatcher);

        for (JetPlayer player : playerCollection) {
            try (NotNullObjectAcquisition<Session> sessionAcquisition = player.connection().acquireSessionRead()) {
                if (!(sessionAcquisition.get().sessionTask() instanceof PlaySessionTask playSessionTask)) return;
                playSessionTask.updateCommands(declarationPacket);
            }
        }
    }

    private static @NonNull ServerDeclareCommandsPlayPacket createDeclarationPacket(
            @NonNull CommandDispatcher<CommandSource> dispatcher
    ) {
        if (!(getOrConvert(dispatcher.getRoot(), new IdentityHashMap<>()) instanceof RootNode rootNode))
            throw new IllegalArgumentException("The node converted is not a root node");
        return new ServerDeclareCommandsPlayPacket(rootNode);
    }

    private static @NonNull ParseResults<CommandSource> parse(@NonNull StringReader reader,
                                                              @NonNull CommandSource source,
                                                              @NonNull CommandDispatcher<CommandSource> dispatcher) {
        try {
            return dispatcher.parse(reader, source);
        } catch (Throwable throwable) {
            // A throwable can be thrown during parsing while checking if the command source can use the command node
            throw new CommandParseException("An error occurred while parsing a command input", throwable);
        }
    }

    /**
     * Gets a converted {@linkplain Node node} from the node map specified.
     *
     * <p>If it is not present the {@linkplain CommandNode command node} is being converted and put into the node
     * map.</p>
     *
     * @param node the command node to get or convert
     * @param nodes a map of nodes, which have been already converted
     * @return the got or converted node
     * @since 1.0
     */
    private static @NonNull Node getOrConvert(@NonNull CommandNode<?> node,
                                              @NonNull IdentityHashMap<CommandNode<?>, Node> nodes) {
        if (nodes.containsKey(node))
            return nodes.get(node);

        CommandNode<?> unconvertedRedirect = node.getRedirect();
        Node redirect = null;

        if (unconvertedRedirect != null)
            redirect = getOrConvert(unconvertedRedirect, nodes);

        String name = node.getName();
        boolean executable = node.getCommand() != null;

        Node convertedNode = switch (node) {
            case RootCommandNode<?> ignored -> new RootNode(redirect, executable);
            case LiteralCommandNode<?> ignored -> new LiteralNode(redirect, executable, name);
            case ArgumentCommandNode<?, ?> argumentNode -> {
                ArgumentType<?> argumentType = argumentNode.getType();

                ServerDeclareCommandsPlayPacket.SuggestionsType suggestionsType = null;
                if (argumentNode.getCustomSuggestions() != null)
                    suggestionsType = ServerDeclareCommandsPlayPacket.SuggestionsType.ASK_SERVER;

                yield new ArgumentNode(redirect, executable, name, argumentType, suggestionsType);
            }
            default -> throw new IllegalStateException("Unknown command node: " + node);
        };

        // Put the node before children initialization to allow for children redirecting to it
        nodes.put(node, convertedNode);

        Set<Node> children = new HashSet<>();
        node.getChildren().forEach(child -> children.add(getOrConvert(child, nodes)));
        convertedNode.initializeChildren(children);

        return convertedNode;
    }
}
