package net.hypejet.jet.server.network.packet.packets.client.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import net.hypejet.jet.world.coordinate.BlockPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacket a client packet} requesting block entity data of a block
 * at a position specified.
 *
 * @param transactionId an identifier of the request, server should respond with the same identifier
 * @param blockPosition a position of the block
 * @since 1.0
 * @author Codestech
 * @see ClientPacket
 */
public record ClientQueryBlockEntityTagPacket(int transactionId, @NonNull BlockPosition blockPosition)
        implements ClientPacket {
    /**
     * Constructs the {@linkplain ClientQueryBlockEntityTagPacket client query block entity tag packet}.
     *
     * @param transactionId an identifier of the request, server should respond with the same identifier
     * @param blockPosition a position of the block
     * @since 1.0
     */
    public ClientQueryBlockEntityTagPacket {
        NullabilityUtil.requireNonNull(blockPosition, "block position");
    }
}