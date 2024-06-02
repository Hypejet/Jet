package net.hypejet.jet.server;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.event.node.EventNode;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an implementation of {@linkplain MinecraftServer minecraft server}.
 *
 * @since 1.0
 * @author Codestech
 */
public final class JetMinecraftServer implements MinecraftServer {

    private final EventNode<Object> eventNode = EventNode.create();

    @Override
    public @NonNull EventNode<Object> eventNode() {
        return this.eventNode;
    }
}