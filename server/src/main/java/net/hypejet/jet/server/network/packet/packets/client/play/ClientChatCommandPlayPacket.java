package net.hypejet.jet.server.network.packet.packets.client.play;

import java.util.Objects;
import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacket a client packet}, which invokes a command sent by a client.
 *
 * @param commandString the unparsed chat command
 * @since 1.0
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
        Objects.requireNonNull(commandString, "command string");
    }
}