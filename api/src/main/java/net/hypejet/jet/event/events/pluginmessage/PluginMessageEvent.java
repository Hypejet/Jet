package net.hypejet.jet.event.events.pluginmessage;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an event called when a client sends a message with custom data when {@linkplain Player a player}
 * associated with the client has been initialized.
 *
 * @param player the player
 * @param key a key to distinguish the custom message
 * @param data the custom data
 * @since 1.0
 * @see Player
 */
public record PluginMessageEvent(@NonNull Player player, @NonNull Key key, @NonNull UnmodifiableByteArray data) {
    /**
     * Constructs the {@linkplain PluginMessageEvent plugin message event}.
     *
     * @param player the player
     * @param key a key to distinguish the custom message
     * @param data the custom data
     * @since 1.0
     */
    public PluginMessageEvent(@NonNull Player player, @NonNull Key key, byte @NonNull [] data) {
        this(player, key, new UnmodifiableByteArray(data));
    }

    /**
     * Constructs the {@linkplain PluginMessageEvent plugin message event}.
     *
     * @param player the player
     * @param key a key to distinguish the custom message
     * @param data the custom data
     * @since 1.0
     */
    public PluginMessageEvent {
        NullabilityUtil.requireNonNull(player, "player");
        NullabilityUtil.requireNonNull(key, "key");
        NullabilityUtil.requireNonNull(data, "data");
    }
}