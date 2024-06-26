package net.hypejet.jet.link;

import net.hypejet.jet.link.label.ServerLinkLabel;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a server link, that a Minecraft client will display in a menu available on a pause-menu.
 *
 * @param label a label of the link
 * @param url an url that the link forwards to
 * @since 1.0
 * @author Codestech
 * @see ServerLinkLabel
 * @see net.hypejet.jet.protocol.packet.server.configuration.ServerCustomLinksConfigurationPacket
 */
public record ServerLink(@NonNull ServerLinkLabel label, @NonNull String url) {}