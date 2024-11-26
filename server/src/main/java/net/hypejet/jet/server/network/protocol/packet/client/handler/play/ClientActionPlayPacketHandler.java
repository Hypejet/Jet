package net.hypejet.jet.server.network.protocol.packet.client.handler.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.data.codecs.util.mapper.Mapper;
import net.hypejet.jet.protocol.packet.client.play.ClientActionPlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientActionPlayPacket.Action;
import net.hypejet.jet.server.network.protocol.codecs.mapper.MapperNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.SessionTask;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientActionPlayPacket an action play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientActionPlayPacket
 * @see ClientPacketHandler
 */
public final class ClientActionPlayPacketHandler implements ClientPacketHandler<ClientActionPlayPacket> {

    private static final int MIN_JUMP_BOOST = 0;
    private static final int MAX_JUMP_BOOST = 100;

    private static final MapperNetworkCodec<Action, Integer> ACTION_CODEC = new MapperNetworkCodec<>(
            Mapper.builder(Action.class, int.class)
                    .register(Action.START_SNEAKING, 0)
                    .register(Action.STOP_SNEAKING, 1)
                    .register(Action.LEAVE_BED, 2)
                    .register(Action.START_SPRINTING, 3)
                    .register(Action.STOP_SPRINTING, 4)
                    .register(Action.START_JUMPING_WITH_HORSE, 5)
                    .register(Action.STOP_JUMPING_WITH_HORSE, 6)
                    .register(Action.OPEN_VEHICLE_INVENTORY, 7)
                    .register(Action.START_FLYING_WITH_ELYTRA, 8)
                    .build(),
            VarIntNetworkCodec.INSTANCE
    );

    @Override
    public @NonNull ClientActionPlayPacket read(@NonNull ByteBuf buf) {
        int entityId = VarIntNetworkCodec.INSTANCE.read(buf);
        Action action = ACTION_CODEC.read(buf);
        int jumpBoost = VarIntNetworkCodec.INSTANCE.read(buf);

        if (jumpBoost > MAX_JUMP_BOOST || jumpBoost < MIN_JUMP_BOOST)
            throw new IllegalArgumentException("Invalid jump boost");

        return new ClientActionPlayPacket(entityId, action, jumpBoost);
    }

    @Override
    public void handle(@NonNull ClientActionPlayPacket packet, @NonNull SessionTask sessionTask) {
        // TODO
    }
}