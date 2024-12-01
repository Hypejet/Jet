package net.hypejet.jet.event.events.pluginmessage;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.network.PlayerConnection;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents an event, which is called when a client sends a response to a requested plugin message.
 *
 * <p>Note that calling {@linkplain PluginMessageEvent a plugin message event} does not trigger this.</p>
 *
 * @param playerConnection a player connection attached to the client
 * @param requestIdentifier an identifier created when the plugin message was requested
 * @param successful whether the client understood the plugin message
 * @param data a data that was sent by the client, {@code null} if none
 * @since 1.0
 * @author Codestech
 */
public record PluginMessageResponseEvent(@NonNull PlayerConnection playerConnection, int requestIdentifier,
                                         boolean successful, @Nullable UnmodifiableByteArray data) {
    /**
     * Constructs the {@linkplain PluginMessageResponseEvent plugin message response event}.
     *
     * @param playerConnection a player connection attached to the client
     * @param requestIdentifier an identifier created when the plugin message was requested
     * @param successful whether the client understood the plugin message
     * @param data a data that was sent by the client, {@code null} if none
     * @since 1.0
     */
    public PluginMessageResponseEvent(@NonNull PlayerConnection playerConnection, int requestIdentifier,
                                      boolean successful, byte @Nullable [] data) {
        this(playerConnection, requestIdentifier, successful, data == null ? null : new UnmodifiableByteArray(data));
    }

    /**
     * Constructs the {@linkplain PluginMessageResponseEvent plugin message response event}.
     *
     * @param playerConnection a player connection attached to the client
     * @param requestIdentifier an identifier created when the plugin message was requested
     * @param successful whether the client understood the plugin message
     * @param data a data that was sent by the client, {@code null} if none
     * @since 1.0
     */
    public PluginMessageResponseEvent {
        NullabilityUtil.requireNonNull(playerConnection, "player connection");
    }
}