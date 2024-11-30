package net.hypejet.jet.network.packet.client.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.network.packet.client.ClientPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacket a client packet}, which invokes a command sent by a client.
 *
 * @param commandString the unparsed chat command
 * @since 1.0
 * @author Codestech
 * @see ClientPacket
 */
public record ClientChatCommandPlayPacket(@NonNull String commandString) implements ClientPacket {
    /**
     * Constructs the {@linkplain ClientChatCommandPlayPacket client chat command play packet}.
     *
     * @param commandString the unparsed chat command
     * @since 1.0
     */
    public ClientChatCommandPlayPacket {
        NullabilityUtil.requireNonNull(commandString, "command string");
    }
}