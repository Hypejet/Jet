package net.hypejet.jet;

import net.hypejet.jet.event.node.EventNode;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an interface for managing a Minecraft server.
 *
 * @since 1.0
 * @author Codestech
 */
public interface MinecraftServer {
    /**
     * Gets a main {@linkplain EventNode event node}, on which all events are called.
     *
     * @return the event node
     * @since 1.0
     */
    @NonNull EventNode<Object> eventNode();
}