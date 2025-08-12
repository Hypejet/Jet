package net.hypejet.jet.server.network.packet.packets.client.play;

import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

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