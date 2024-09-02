package net.hypejet.jet.protocol.packet.client.play;

import net.hypejet.jet.data.model.coordinate.BlockPosition;
import net.hypejet.jet.protocol.packet.client.ClientPlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPlayPacket client play packet}, which is received when a client requests
 * a {@linkplain net.kyori.adventure.nbt.CompoundBinaryTag compound binary tag} of a block entity for a command
 * created by pressing {@code F3 + I} on the client.
 *
 * @param transactionId an identifier of the transaction
 * @param blockPosition a position of the block
 * @since 1.0
 * @author Codestech
 * @see ClientPlayPacket
 */
public record ClientQueryBlockEntityTagPacket(int transactionId, @NonNull BlockPosition blockPosition)
        implements ClientPlayPacket {}