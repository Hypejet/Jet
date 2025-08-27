package net.hypejet.jet.util.game.audience;

import net.hypejet.jet.util.game.crash.CrashReportDetails;
import net.hypejet.jet.util.game.link.ServerLink;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

/**
 * Represents a common complement to {@linkplain Audience an audience}.
 *
 * @since 1.0
 * @see Audience
 */
public interface CommonAudience extends Audience {
    /**
     * Sets {@linkplain ServerLink server links} specified to be displayed on a menu screen of clients associated
     * with this audience.
     *
     * @param links the server links
     * @since 1.0
     */
    void setCustomLinks(@NonNull Collection<ServerLink> links);

    /**
     * Sets {@linkplain CrashReportDetails crash report details} specified to be included in crash reports of clients
     * associated with this audience if they crash.
     *
     * @param details the crash report details
     * @since 1.0
     */
    void setCustomReportDetails(@NonNull Collection<CrashReportDetails> details);

    /**
     * Disconnects clients associated with this audience with a reason specified.
     *
     * @param reason the reason
     * @since 1.0
     */
    void disconnect(@NonNull Component reason);

    /**
     * Sends a ping to clients associated with this audience.
     *
     * @param identifier an identifier that the ping should have
     * @since 1.0
     */
    void ping(int identifier);

    /**
     * Sends a custom message to clients associated with this audience.
     *
     * @param key a key that the custom message should have
     * @param data a data that the custom message should have
     * @since 1.0
     */
    void sendPluginMessage(@NonNull Key key, byte @NonNull [] data);

    /**
     * Requests clients associated with this audience to send data of a cookie with {@linkplain Key a key} specified,
     * which is stored on the clients.
     *
     * @param key the key
     * @since 1.0
     */
    void requestCookie(@NonNull Key key);

    /**
     * Requests a client associated with this audience to store a cookie with {@linkplain Key a key} specified.
     *
     * @param key the key
     * @param data a data that the cookie should have
     * @since 1.0
     */
    void storeCookie(@NonNull Key key, byte @NonNull [] data);

    /**
     * Requests clients associated with this audience to transfer to another Minecraft server.
     *
     * @param address an address of the server that the clients should transfer to
     * @param port a port of the server that the clients should transfer to
     * @since 1.0
     */
    void transfer(@NonNull String address, int port);

    @Override
    void sendResourcePacks(@NotNull ResourcePackRequest request);

    @Override
    void removeResourcePacks(@NotNull UUID id, @NotNull UUID @NotNull ... others);

    @Override
    void clearResourcePacks();
}