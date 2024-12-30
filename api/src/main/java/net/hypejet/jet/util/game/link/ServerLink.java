package net.hypejet.jet.util.game.link;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.util.game.link.label.ServerLinkLabel;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a server link, that a Minecraft client displays in a menu available in a pause-menu.
 *
 * @param label a label of the link
 * @param url an url that the link forwards to
 * @since 1.0
 * @see ServerLinkLabel
 */
public record ServerLink(@NonNull ServerLinkLabel label, @NonNull String url) {
    /**
     * Constructs the {@linkplain ServerLink server link}.
     *
     * @param label a label of the link
     * @param url an url that the link forwards to
     * @since 1.0
     */
    public ServerLink {
        NullabilityUtil.requireNonNull(label, "label");
        NullabilityUtil.requireNonNull(url, "URL");
    }
}