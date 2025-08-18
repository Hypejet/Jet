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
import net.hypejet.concurrency.object.notnull.NotNullObjectAcquisition;
import net.hypejet.jet.command.CommandManager;
import net.hypejet.jet.command.CommandSource;
import net.hypejet.jet.event.events.command.CommandExecuteEvent;
import net.hypejet.jet.event.events.command.CommandExecutionFailureEvent;
import net.hypejet.jet.event.events.command.CommandPreExecuteEvent;
import net.hypejet.jet.event.events.command.CommandPreParseEvent;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.server.command.exceptions.CommandParseException;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.entity.player.PlayerList;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerDeclareCommandsPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerDeclareCommandsPlayPacket.ArgumentNode;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerDeclareCommandsPlayPacket.LiteralNode;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerDeclareCommandsPlayPacket.Node;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerDeclareCommandsPlayPacket.RootNode;
import net.hypejet.jet.server.network.session.Session;
import net.hypejet.jet.server.network.session.task.PlaySessionTask;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * An implementation of the {@linkplain CommandManager command manager}.
 *
 * @since 1.0
 * @see CommandManager
 */
public final class JetCommandManager implements CommandManager {

    private static final IllegalArgumentException NOT_LITERAL_EXCEPTION
            = new IllegalArgumentException("The root command node contained non-literal command node");
    private static final char COMMAND_PREFIX = '/';

    private static final Logger LOGGER = LoggerFactory.getLogger(JetCommandManager.class);

    private final EventNode<Object> eventNode;
    private final PlayerList playerList;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final CommandDispatcher<CommandSource> dispatcher = new CommandDispatcher<>();

    /**
     * Constructs the {@linkplain JetCommandManager command manager}.
     *
     * @param eventNode an event node that command events should be called in
     * @param playerList a player list of the server that the command manager is being constructed for
     * @since 1.0
     */
    public JetCommandManager(@NonNull EventNode<Object> eventNode, @NonNull PlayerList playerList) {
        this.eventNode = Objects.requireNonNull(eventNode, "event node");
        this.playerList = Objects.requireNonNull(playerList, "player list");
    }

    @Override
    public void register(@NonNull LiteralCommandNode<CommandSource> node) {
        try {
            this.lock.writeLock().lock();
            this.dispatcher.getRoot().addChild(node);
            this.broadcastCommandUpdate();
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public @Nullable LiteralCommandNode<CommandSource> unregister(@NonNull String name) {
        try {
            this.lock.writeLock().lock();
            RootCommandNode<CommandSource> rootNode = this.dispatcher.getRoot();

            CommandNode<CommandSource> node = rootNode.getChild(name);
            if (node == null) return null;

            rootNode.removeChildByName(name);
            this.broadcastCommandUpdate();

            if (!(node instanceof LiteralCommandNode<CommandSource> castNode))
                throw NOT_LITERAL_EXCEPTION;
            return castNode;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public @Nullable LiteralCommandNode<CommandSource> get(@NonNull String name) {
        try {
            this.lock.readLock().lock();
            RootCommandNode<CommandSource> rootNode = this.dispatcher.getRoot();

            CommandNode<CommandSource> node = rootNode.getChild(name);
            if (node == null) return null;

            if (!(rootNode.getChild(name) instanceof LiteralCommandNode<CommandSource> castNode))
                throw NOT_LITERAL_EXCEPTION;
            return castNode;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public @NonNull Collection<LiteralCommandNode<CommandSource>> commands() {
        try {
            this.lock.readLock().lock();
            Set<LiteralCommandNode<CommandSource>> nodes = new HashSet<>();

            for (CommandNode<CommandSource> child : this.dispatcher.getRoot().getChildren()) {
                if (!(child instanceof LiteralCommandNode<CommandSource> castNode))
                    throw NOT_LITERAL_EXCEPTION;
                nodes.add(castNode);
            }

            return Set.copyOf(nodes);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    /**
     * Executes a command with the specified input.
     *
     * @param input the command input
     * @param source the command source that sent the specified command input
     * @since 1.0
     */
    public void execute(@NonNull String input, @NonNull CommandSource source) {
        try {
            this.lock.readLock().lock();

            CommandPreParseEvent preParseEvent = new CommandPreParseEvent(source, input);
            this.eventNode.call(preParseEvent);
            if (preParseEvent.isCancelled()) return;

            input = preParseEvent.getInput();
            ParseResults<CommandSource> parseResults = this.parse(new StringReader(input), source);

            CommandPreExecuteEvent preExecuteEvent = new CommandPreExecuteEvent(source, input, parseResults);
            this.eventNode.call(preExecuteEvent);
            if (preExecuteEvent.isCancelled()) return;

            int executionResult;

            try {
                executionResult = this.dispatcher.execute(parseResults);
            } catch (CommandSyntaxException exception) {
                this.eventNode.call(new CommandExecutionFailureEvent(source, exception));
                return;
            } catch (Throwable throwable) {
                LOGGER.error("An error occurred while executing a command", throwable);
                return;
            }

            this.eventNode.call(new CommandExecuteEvent(source, input, parseResults, executionResult));
        } finally {
            this.lock.readLock().unlock();
        }
    }

    /**
     * A {@linkplain CompletableFuture completable future} whose result are
     * {@linkplain Suggestions suggestions} for the command input string specified.
     *
     * @param input the command input string to create the suggestions for
     * @param source the source which typed the specified command input
     * @return the completable future
     * @since 1.0
     */
    public @NonNull CompletableFuture<Suggestions> suggest(@NonNull String input, @NonNull CommandSource source) {
        try {
            this.lock.readLock().lock();

            StringReader reader = new StringReader(input);
            if (reader.canRead() && reader.peek() == COMMAND_PREFIX)
                reader.skip();

            ParseResults<CommandSource> parseResults = this.parse(reader, source);
            return this.dispatcher.getCompletionSuggestions(parseResults);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    /**
     * Initializes commands from this {@linkplain JetCommandManager command manager}
     * for the specified {@linkplain PlaySessionTask play session task}.
     *
     * @param sessionTask the play session task tha the commands should be initialized for
     * @since 1.0
     */
    public void initializeCommands(@NonNull PlaySessionTask sessionTask) {
        try {
            this.lock.readLock().lock();
            sessionTask.sendCommands(this.createDeclarationPacket(), true);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    private void broadcastCommandUpdate() {
        ServerDeclareCommandsPlayPacket declarationPacket = this.createDeclarationPacket();
        for (JetPlayer player : this.playerList.players()) {
            try (NotNullObjectAcquisition<Session> sessionAcquisition = player.connection().acquireSessionRead()) {
                if (!(sessionAcquisition.get().sessionTask() instanceof PlaySessionTask playSessionTask)) return;
                playSessionTask.sendCommands(declarationPacket, false);
            }
        }
    }

    private @NonNull ServerDeclareCommandsPlayPacket createDeclarationPacket() {
        if (!(getOrConvert(this.dispatcher.getRoot(), new IdentityHashMap<>()) instanceof RootNode rootNode))
            throw new IllegalStateException("The converted node is not a root node");
        return new ServerDeclareCommandsPlayPacket(rootNode);
    }

    private @NonNull ParseResults<CommandSource> parse(@NonNull StringReader reader, @NonNull CommandSource source) {
        try {
            return this.dispatcher.parse(reader, source);
        } catch (Throwable throwable) {
            // A throwable can be thrown during parsing while checking if the command source can use the command node
            throw new CommandParseException("An error occurred while parsing the command input", throwable);
        }
    }

    private static @NonNull Node getOrConvert(@NonNull CommandNode<?> node,
                                              @NonNull IdentityHashMap<CommandNode<?>, Node> nodes) {
        if (nodes.containsKey(node))
            return nodes.get(node);

        CommandNode<?> unconvertedRedirect = node.getRedirect();
        Node redirect = null;

        if (unconvertedRedirect != null)
            redirect = getOrConvert(unconvertedRedirect, nodes);

        // Put the node before children initialization to allow for children redirecting to it
        Node convertedNode = convertNode(node, redirect);
        nodes.put(node, convertedNode);

        Set<Node> children = new HashSet<>();
        node.getChildren().forEach(child -> children.add(getOrConvert(child, nodes)));
        convertedNode.initializeChildren(children);

        return convertedNode;
    }

    private static @NonNull Node convertNode(@NonNull CommandNode<?> node, @Nullable Node redirect) {
        String name = node.getName();
        boolean executable = node.getCommand() != null;
        return switch (node) {
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
    }
}