package net.hypejet.jet.server.network.packet.packets.client.play;

import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents {@linkplain ClientPacket a client packet}, which requests command suggestions from a server.
 *
 * @param transactionId an identifier of the request, the server responds with the same identifier
 * @param text all current typed text of the command on the client, which is behind a cursor
 * @since 1.0
 * @see ClientPacket
 */
public record ClientCommandSuggestionsRequestPlayPacket(int transactionId, @NonNull String text)
        implements ClientPacket {
    /**
     * Constructs the {@linkplain ClientCommandSuggestionsRequestPlayPacket client command suggestions request play
     * packet}.
     *
     * @param transactionId an identifier of the request, the server responds with the same identifier
     * @param text all current typed text of the command on the client, which is behind a cursor
     * @since 1.0
     */
    public ClientCommandSuggestionsRequestPlayPacket {
        Objects.requireNonNull(text, "text");
    }
}