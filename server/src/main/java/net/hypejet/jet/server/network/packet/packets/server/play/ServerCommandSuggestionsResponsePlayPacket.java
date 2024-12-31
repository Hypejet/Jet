package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * Represents {@linkplain ServerPacket a server packet}, which responds to a command suggestion request of a client.
 *
 * @param transactionId an identifier of the request
 * @param start a start position of the text to replace
 * @param length a length of the text to replace
 * @param suggestions the suggestions
 * @since 1.0
 * @see ServerPacket
 */
public record ServerCommandSuggestionsResponsePlayPacket(int transactionId, int start, int length,
                                                         @NonNull Collection<Suggestion> suggestions)
        implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerCommandSuggestionsResponsePlayPacket server command suggestions
     * response play packet}.
     *
     * @param transactionId an identifier of the request
     * @param start a start position of the text to replace
     * @param length a length of the text to replace
     * @param suggestions the suggestions
     * @since 1.0
     */
    public ServerCommandSuggestionsResponsePlayPacket {
        suggestions = List.copyOf(NullabilityUtil.requireNonNull(suggestions, "suggestions"));
    }

    /**
     * Represents a suggestion of {@linkplain ServerCommandSuggestionsResponsePlayPacket a command suggestions
     * response play packet}.
     *
     * @param text a text of the suggestion
     * @param tooltip a tooltip of the suggestion, which should be displayed when the suggestion is being hovered
     *                on a client, {@code null} if none
     * @since 1.0
     */
    public record Suggestion(@NonNull String text, @Nullable Component tooltip) {
        /**
         * Constructs the {@linkplain Suggestion suggestion}.
         *
         * @param text a text of the suggestion
         * @param tooltip a tooltip of the suggestion, which should be displayed when the suggestion is being hovered
         *                on a client, {@code null} if none
         * @since 1.0
         */
        public Suggestion {
            NullabilityUtil.requireNonNull(text, "text");
            NullabilityUtil.requireNonNull(tooltip, "tooltip");
        }
    }
}