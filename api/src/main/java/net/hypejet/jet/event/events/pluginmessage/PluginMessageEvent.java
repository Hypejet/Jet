package net.hypejet.jet.event.events.pluginmessage;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an event called when a client sends a plugin message, but it was not requested by a server.
 *
 * @param player a player that the client is attached to
 * @param key a key of the plugin message
 * @param data a data of the plugin message
 * @since 1.0
 * @author Codestech
 * @see Player
 */
public record PluginMessageEvent(@NonNull Player player, @NonNull Key key, @NonNull UnmodifiableByteArray data) {
    /**
     * Constructs the {@linkplain PluginMessageEvent plugin message event}.
     *
     * @param player a player that the client is attached to
     * @param key a key of the plugin message
     * @param data a data of the plugin message
     * @since 1.0
     */
    public PluginMessageEvent(@NonNull Player player, @NonNull Key key, byte @NonNull [] data) {
        this(player, key, new UnmodifiableByteArray(data));
    }

    /**
     * Constructs the {@linkplain PluginMessageEvent plugin message event}.
     *
     * @param player a player that the client is attached to
     * @param key a key of the plugin message
     * @param data a data of the plugin message
     * @since 1.0
     */
    public PluginMessageEvent {
        NullabilityUtil.requireNonNull(player, "player");
        NullabilityUtil.requireNonNull(key, "key");
        NullabilityUtil.requireNonNull(data, "data");
    }
}