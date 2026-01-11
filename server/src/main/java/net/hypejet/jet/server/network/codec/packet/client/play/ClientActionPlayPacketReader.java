package net.hypejet.jet.server.network.codec.packet.client.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.index.IndexNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientActionPlayPacket;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientActionPlayPacket.Action;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.util.index.IndexUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientActionPlayPacket an action play packet}.
 *
 * @since 1.0
 * @see ClientActionPlayPacket
 * @see NetworkReader
 */
public final class ClientActionPlayPacketReader implements NetworkReader<ClientActionPlayPacket> {

    /**
     * An instance of the {@linkplain ClientActionPlayPacket client action play packet reader}.
     *
     * @since 1.0
     */
    public static final ClientActionPlayPacketReader INSTANCE = new ClientActionPlayPacketReader();

    private static final int MIN_JUMP_BOOST = 0;
    private static final int MAX_JUMP_BOOST = 100;

    private static final IndexNetworkCodec<Action, Integer> ACTION_CODEC = new IndexNetworkCodec<>(
            IndexUtil.fromMap(Map.of(
                    0, Action.START_SNEAKING,
                    1, Action.STOP_SNEAKING,
                    2, Action.LEAVE_BED,
                    3, Action.START_SPRINTING,
                    4, Action.STOP_SPRINTING,
                    5, Action.START_JUMPING_WITH_HORSE,
                    6, Action.STOP_JUMPING_WITH_HORSE,
                    7, Action.OPEN_VEHICLE_INVENTORY,
                    8, Action.START_FLYING_WITH_ELYTRA
            )),
            VarIntNetworkCodec.INSTANCE
    );

    private ClientActionPlayPacketReader() {}

    @Override
    public @NonNull ClientActionPlayPacket read(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager) {
        int entityId = VarIntNetworkCodec.INSTANCE.read(buf, registryManager);
        Action action = ACTION_CODEC.read(buf, registryManager);

        int jumpBoost = VarIntNetworkCodec.INSTANCE.read(buf, registryManager);
        if (jumpBoost > MAX_JUMP_BOOST || jumpBoost < MIN_JUMP_BOOST) {
            throw new IllegalArgumentException(String.format(
                    "The jump boost is out of allowed range, got %d while maximum allowed is %d and minimum allowed" +
                            " is %d",
                    jumpBoost, MAX_JUMP_BOOST, MIN_JUMP_BOOST
            ));
        }

        return new ClientActionPlayPacket(entityId, action, jumpBoost);
    }
}