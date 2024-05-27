package net.hypejet.jet.protocol.packet.clientbound.login;

import net.hypejet.jet.buffer.NetworkBuffer;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.clientbound.ClientBoundPacket;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@link ClientBoundPacket client-bound packet} sent by a server to disconnect a player.
 *
 * @since 1.0
 * @author Codestech
 */
public final class DisconnectPacket extends ClientBoundPacket {

    private final Component reason;

    /**
     * Constructs a {@link DisconnectPacket disconnect packet}.
     *
     * @param reason a reason why of the disconnection
     * @since 1.0
     */
    public DisconnectPacket(@NonNull Component reason) {
        super(0, ProtocolState.LOGIN);
        this.reason = reason;
    }

    @Override
    public void write(@NonNull NetworkBuffer buffer) {
        buffer.writeJsonTextComponent(this.reason);
    }
}