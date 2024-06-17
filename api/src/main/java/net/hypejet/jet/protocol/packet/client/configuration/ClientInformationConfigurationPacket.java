package net.hypejet.jet.protocol.packet.client.configuration;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.protocol.packet.client.ClientConfigurationPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Locale;
import java.util.Set;

/**
 * Represents a {@linkplain ClientConfigurationPacket client configuration packet} sent by a client to inform
 * the server about settings of a player.
 *
 * @param locale a locale of the player
 * @param viewDistance a view distance of the player
 * @param chatMode a chat mode of the player
 * @param chatColorsEnabled whether the chat colors are enabled by the player
 * @param enabledSkinParts parts of a skin of the player, which should be enabled
 * @param mainHand a main hand of the player
 * @param textFilteringEnabled whether the text filtering on signs and written books is enabled
 * @param allowServerListings whether the player should be listed on players lists
 * @since 1.0
 * @author Codestech
 * @see ClientConfigurationPacket
 */
public record ClientInformationConfigurationPacket(
        @NonNull Locale locale, byte viewDistance, Player.@NonNull ChatMode chatMode, boolean chatColorsEnabled,
        @NonNull Collection<Player.SkinPart> enabledSkinParts, Entity.@NonNull Hand mainHand, boolean textFilteringEnabled,
        boolean allowServerListings
) implements ClientConfigurationPacket {
    /**
     * Constructs the {@linkplain ClientInformationConfigurationPacket information configuration packet}.
     *
     * @param locale a locale of the player
     * @param viewDistance a view distance of the player
     * @param chatMode a chat mode of the player
     * @param chatColorsEnabled whether the chat colors are enabled by the player
     * @param enabledSkinParts parts of a skin of the player, which should be enabled
     * @param mainHand a main hand of the player
     * @param textFilteringEnabled whether the text filtering on signs and written books is enabled
     * @param allowServerListings whether the player should be listed on players lists
     * @since 1.0
     */
    public ClientInformationConfigurationPacket {
        enabledSkinParts = Set.copyOf(enabledSkinParts);
    }
}