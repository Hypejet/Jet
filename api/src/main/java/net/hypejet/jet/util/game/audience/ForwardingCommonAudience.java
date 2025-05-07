package net.hypejet.jet.util.game.audience;

import net.hypejet.jet.util.game.crash.CrashReportDetails;
import net.hypejet.jet.util.game.link.ServerLink;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Represents a combination of {@linkplain CommonAudience a common audience}
 * and {@linkplain ForwardingAudience a forwarding audience}.
 *
 * @since 1.0
 * @see ForwardingAudience
 * @see CommonAudience
 */
@FunctionalInterface
public interface ForwardingCommonAudience extends CommonAudience, ForwardingAudience {
    /**
     * Gets {@linkplain Iterable an iterable} of {@linkplain CommonAudience common audiences} that this audience
     * should forward to.
     *
     * @return the iterable
     * @since 1.0
     */
    @Override
    @NotNull Iterable<? extends CommonAudience> audiences();

    @Override
    default void setCustomLinks(@NonNull Collection<ServerLink> links) {
        this.forEachCommonAudience(audience -> audience.setCustomLinks(links));
    }

    @Override
    default void setCustomReportDetails(@NonNull Collection<CrashReportDetails> details) {
        this.forEachCommonAudience(audience -> audience.setCustomReportDetails(details));
    }

    @Override
    default void disconnect(@NonNull Component reason) {
        this.forEachCommonAudience(audience -> audience.disconnect(reason));
    }

    @Override
    default void ping(int identifier) {
        this.forEachCommonAudience(audience -> audience.ping(identifier));
    }

    @Override
    default void sendPluginMessage(@NonNull Key key, byte @NonNull [] data) {
        this.forEachCommonAudience(audience -> audience.sendPluginMessage(key, data));
    }

    @Override
    default void requestCookie(@NonNull Key key) {
        this.forEachCommonAudience(audience -> audience.requestCookie(key));
    }

    @Override
    default void storeCookie(@NonNull Key key, byte @NonNull [] data) {
        this.forEachCommonAudience(audience -> audience.storeCookie(key, data));
    }

    @Override
    default void transfer(@NonNull String address, int port) {
        this.forEachCommonAudience(audience -> audience.transfer(address, port));
    }

    @Override
    default void sendResourcePacks(@NotNull ResourcePackRequest request) {
        this.forEachCommonAudience(audience -> audience.sendResourcePacks(request));
    }

    @Override
    default void removeResourcePacks(@NotNull UUID id, @NotNull UUID @NotNull ... others) {
        this.forEachCommonAudience(audience -> audience.removeResourcePacks(id, others));
    }

    @Override
    default void clearResourcePacks() {
        this.forEachCommonAudience(CommonAudience::clearResourcePacks);
    }

    private void forEachCommonAudience(@NonNull Consumer<? super CommonAudience> action) {
        this.audiences().forEach(action);
    }
}