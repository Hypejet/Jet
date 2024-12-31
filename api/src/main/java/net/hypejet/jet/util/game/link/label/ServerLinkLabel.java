package net.hypejet.jet.util.game.link.label;

import net.hypejet.jet.util.game.link.ServerLink;

/**
 * Represents a label displayed as the {@linkplain ServerLink server link}.
 *
 * <p>This interface is not sealed, since it depends on Minecraft. Adding another implementation could break switch
 * cases for example./p>
 *
 * @since 1.0
 * @see ServerLink
 */
public interface ServerLinkLabel {}