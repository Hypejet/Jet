package net.hypejet.jet.event.events.pluginmessage;

import net.hypejet.jet.network.PlayerConnection;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents an event called when a client associated with {@linkplain PlayerConnection a player connection} sends
 * a message with custom data
 *
 * @param connection the connection
 * @param key a key to distinguish the custom message
 * @param data the custom data
 * @since 1.0
 * @see PlayerConnection
 */
public record PluginMessageEvent(@NonNull PlayerConnection connection, @NonNull Key key,
                                 @NonNull UnmodifiableByteArray data) {
    /**
     * Constructs the {@linkplain PluginMessageEvent plugin message event}.
     *
     * @param connection the connection
     * @param key a key to distinguish the custom message
     * @param data the custom data
     * @since 1.0
     */
    public PluginMessageEvent {
        Objects.requireNonNull(connection, "connection");
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(data, "data");
    }
}