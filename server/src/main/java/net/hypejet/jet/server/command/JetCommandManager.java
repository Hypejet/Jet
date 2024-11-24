package net.hypejet.jet.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.hypejet.jet.command.CommandManager;
import net.hypejet.jet.command.CommandSource;
import net.hypejet.jet.event.command.CommandExecuteEvent;
import net.hypejet.jet.event.command.CommandExecutionFailureEvent;
import net.hypejet.jet.event.command.CommandPreExecuteEvent;
import net.hypejet.jet.event.command.CommandPreParseEvent;
import net.hypejet.jet.event.node.EventNode;
import net.hypejet.jet.protocol.packet.server.play.ServerDeclareCommandsPlayPacket;
import net.hypejet.jet.server.JetMinecraftServer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents an implementation of the {@linkplain CommandManager command manager}.
 *
 * @since 1.0
 * @author Codestech
 * @see CommandManager
 */
public final class JetCommandManager implements CommandManager {

    public static final char COMMAND_PREFIX = '/';

    private static final Logger LOGGER = LoggerFactory.getLogger(JetCommandManager.class);

    private final JetMinecraftServer server;

    private final CommandDispatcher<CommandSource> dispatcher = new CommandDispatcher<>();
    private final ReentrantReadWriteLock nodeLock = new ReentrantReadWriteLock();

    /**
     * Constructs the {@linkplain JetCommandManager command manager}.
     *
     * @param server a server, on which the commands should be managed
     * @since 1.0
     */
    public JetCommandManager(@NonNull JetMinecraftServer server) {
        this.server = server;
    }

    @Override
    public void register(@NonNull LiteralCommandNode<CommandSource> node) {
        try {
            this.nodeLock.writeLock().lock();
            this.dispatcher.getRoot().addChild(node);
        } finally {
            this.nodeLock.writeLock().unlock();
        }
    }

    @Override
    public @Nullable LiteralCommandNode<CommandSource> get(@NonNull String name) {
        try {
            this.nodeLock.readLock().lock();

            CommandNode<CommandSource> node = this.dispatcher.getRoot().getChild(name);
            if (node == null) return null;

            if (!(this.dispatcher.getRoot().getChild(name) instanceof LiteralCommandNode<CommandSource> literalNode))
                throw notLiteralException();

            return literalNode;
        } finally {
            this.nodeLock.readLock().unlock();
        }
    }

    @Override
    public @NonNull Collection<LiteralCommandNode<CommandSource>> commands() {
        try {
            this.nodeLock.readLock().lock();
            Set<LiteralCommandNode<CommandSource>> nodes = new HashSet<>();

            for (CommandNode<CommandSource> child : this.dispatcher.getRoot().getChildren()) {
                if (!(child instanceof LiteralCommandNode<CommandSource> literalNode))
                    throw notLiteralException();
                nodes.add(literalNode);
            }

            return Set.copyOf(nodes);
        } finally {
            this.nodeLock.readLock().unlock();
        }
    }

    /**
     * Executes a command.
     *
     * @param input a raw command string input
     * @param source a sender of the command input
     * @since 1.0
     */
    public void execute(@NonNull String input, @NonNull CommandSource source) {
        try {
            this.nodeLock.readLock().lock();
            EventNode<Object> eventNode = this.server.eventNode();

            CommandPreParseEvent preParseEvent = new CommandPreParseEvent(source, input);
            eventNode.call(preParseEvent);
            if (preParseEvent.isCancelled()) return;

            input = preParseEvent.getInput();
            ParseResults<CommandSource> parseResults;

            try {
                parseResults = this.dispatcher.parse(input, source);
            } catch (Throwable throwable) {
                // A throwable can be thrown during parsing while checking if a source can use a command node
                LOGGER.error("An error occurred while parsing a command", throwable);
                return;
            }

            CommandPreExecuteEvent preExecuteEvent = new CommandPreExecuteEvent(source, input, parseResults);
            eventNode.call(preExecuteEvent);
            if (preParseEvent.isCancelled()) return;

            int executionResult;

            try {
                executionResult = this.dispatcher.execute(parseResults);
            } catch (Throwable throwable) {
                if (throwable instanceof CommandSyntaxException exception) {
                    eventNode.call(new CommandExecutionFailureEvent(source, exception));
                    return;
                }

                LOGGER.error("An error occurred while executing a command", throwable);
                return;
            }

            eventNode.call(new CommandExecuteEvent(source, input, parseResults, executionResult));
        } finally {
            this.nodeLock.readLock().unlock();
        }
    }

    public @NonNull CompletableFuture<Suggestions> suggest(@NonNull String input, @NonNull CommandSource source) {
        StringReader reader = new StringReader(input);
        if (reader.canRead() && reader.peek() == COMMAND_PREFIX)
            reader.skip();

        ParseResults<CommandSource> parseResults;

        try {
            parseResults = this.dispatcher.parse(reader, source);
        } catch (Throwable throwable) {
            // A throwable can be thrown during parsing while checking if a source can use a command node
            LOGGER.error("An error occurred while parsing a command", throwable);
            return CompletableFuture.failedFuture(throwable);
        }

        return this.dispatcher.getCompletionSuggestions(parseResults);
    }

    /**
     * Create a {@linkplain ServerDeclareCommandsPlayPacket declare commands play packet}
     * using {@linkplain CommandNode command nodes} registered into this {@linkplain CommandManager command manager}.
     *
     * @return the packet
     * @since 1.0
     */
    public @NonNull ServerDeclareCommandsPlayPacket createDeclarationPacket() {
        return new ServerDeclareCommandsPlayPacket(CommandNodeAdapter.convert(this.dispatcher.getRoot()));
    }

    private static @NonNull IllegalArgumentException notLiteralException() {
        return new IllegalArgumentException("Root command node cannot contain command nodes other than literals");
    }
}
