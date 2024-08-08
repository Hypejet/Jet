package net.hypejet.jet.protocol.packet.server.play;

import net.hypejet.jet.protocol.packet.server.ServerPlayPacket;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * Represents a {@linkplain ServerPlayPacket server play packet}, which responds with command suggestions, which
 * were requested by a client.
 *
 * @param transactionId an identifier of the transaction, sent by a client request
 * @param start a start position of the text to replace
 * @param length a length of the text to replace
 * @param suggestions the suggestions
 * @since 1.0
 * @author Codestech
 * @see ServerPlayPacket
 */
public record ServerCommandSuggestionsResponsePlayPacket(int transactionId, int start, int length,
                                                         @NonNull Collection<Suggestion> suggestions)
        implements ServerPlayPacket {
    /**
     * Constructs the {@linkplain ServerCommandSuggestionsResponsePlayPacket command suggestions response play packet}.
     *
     * @param transactionId an identifier of the transaction, sent by a client request
     * @param start a start position of the text to replace
     * @param length a length of the text to replace
     * @param suggestions the suggestions
     * @since 1.0
     */
    public ServerCommandSuggestionsResponsePlayPacket {
        suggestions = List.copyOf(suggestions);
    }

    /**
     * Represents a suggestion of a {@linkplain ServerCommandSuggestionsResponsePlayPacket command suggestions
     * response play packet}.
     *
     * @param text a text of the suggestion
     * @param tooltip a tooltip of the suggestion, which is displayed when the suggestion is being hovered
     *                on a client, {@code null} if none
     * @since 1.0
     * @author Codestech
     */
    public record Suggestion(@NonNull String text, @Nullable Component tooltip) {}
}