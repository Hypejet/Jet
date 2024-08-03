package net.hypejet.jet.protocol.packet.client.play;

import net.hypejet.jet.protocol.packet.client.ClientPlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPlayPacket client play packet}, which requests a command suggestions from a server.
 *
 * @param transactionId an identifier of the transaction, the server responds with the same identifier
 * @param text all current typed text of the command on the client, which is behind the cursor
 * @since 1.0
 * @author Codestech
 * @see ClientPlayPacket
 */
public record ClientCommandSuggestionsRequestPlayPacket(int transactionId, @NonNull String text)
        implements ClientPlayPacket {}