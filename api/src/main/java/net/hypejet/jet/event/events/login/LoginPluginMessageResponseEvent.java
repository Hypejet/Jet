package net.hypejet.jet.event.events.login;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.session.login.LoginManager;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents {@linkplain LoginEvent a login event}, which is called when a client sends a response to a requested
 * plugin message during a login state.
 *
 * @param loginManager a login manager of the login state
 * @param requestIdentifier an identifier created when the plugin message was requested
 * @param successful whether the client understood the plugin message
 * @param data a data that was sent by the client, {@code null} if none
 * @since 1.0
 */
public record LoginPluginMessageResponseEvent(@NonNull LoginManager loginManager, int requestIdentifier,
                                              boolean successful, @Nullable UnmodifiableByteArray data)
        implements LoginEvent {
    /**
     * Constructs the {@linkplain LoginPluginMessageResponseEvent plugin message response event}.
     *
     * @param loginManager a login manager of the login state
     * @param requestIdentifier an identifier created when the plugin message was requested
     * @param successful whether the client understood the plugin message
     * @param data a data that was sent by the client, {@code null} if none
     * @since 1.0
     */
    public LoginPluginMessageResponseEvent(@NonNull LoginManager loginManager,
                                           int requestIdentifier, boolean successful, byte @Nullable [] data) {
        this(loginManager, requestIdentifier, successful, data == null ? null : new UnmodifiableByteArray(data));
    }

    /**
     * Constructs the {@linkplain LoginPluginMessageResponseEvent plugin message response event}.
     *
     * @param loginManager a login manager of the login state
     * @param requestIdentifier an identifier created when the plugin message was requested
     * @param successful whether the client understood the plugin message
     * @param data a data that was sent by the client, {@code null} if none
     * @since 1.0
     */
    public LoginPluginMessageResponseEvent {
        NullabilityUtil.requireNonNull(loginManager, "login manager");
    }
}