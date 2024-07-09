package net.hypejet.jet.server.network.protocol.packet.client.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.play.ClientActionPlayPacket;
import net.hypejet.jet.protocol.packet.client.play.ClientActionPlayPacket.Action;
import net.hypejet.jet.server.network.protocol.codecs.enums.EnumVarIntCodec;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientActionPlayPacket action play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientActionPlayPacket
 * @see ClientPacketCodec
 */
public final class ClientActionPlayPacketCodec extends ClientPacketCodec<ClientActionPlayPacket> {

    private static final int MIN_JUMP_BOOST = 0;
    private static final int MAX_JUMP_BOOST = 100;

    private static final EnumVarIntCodec<Action> actionCodec = EnumVarIntCodec.builder(Action.class)
            .add(Action.START_SNEAKING, 0)
            .add(Action.STOP_SNEAKING, 1)
            .add(Action.LEAVE_BED, 2)
            .add(Action.START_SPRINTING, 3)
            .add(Action.STOP_SPRINTING, 4)
            .add(Action.START_JUMPING_WITH_HORSE, 5)
            .add(Action.STOP_JUMPING_WITH_HORSE, 6)
            .add(Action.OPEN_VEHICLE_INVENTORY, 7)
            .add(Action.START_FLYING_WITH_ELYTRA, 8)
            .build();

    /**
     * Constructs the {@linkplain ClientActionPlayPacketCodec action play packet codec}.
     *
     * @since 1.0
     */
    public ClientActionPlayPacketCodec() {
        super(ClientPacketIdentifiers.PLAY_ACTION, ClientActionPlayPacket.class);
    }

    @Override
    public @NonNull ClientActionPlayPacket read(@NonNull ByteBuf buf) {
        int entityId = NetworkUtil.readVarInt(buf);
        Action action = actionCodec.read(buf);
        int jumpBoost = NetworkUtil.readVarInt(buf);

        if (jumpBoost > MAX_JUMP_BOOST || jumpBoost < MIN_JUMP_BOOST)
            throw new IllegalArgumentException("Invalid jump boost");

        return new ClientActionPlayPacket(entityId, action, jumpBoost);
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientActionPlayPacket object) {
        int jumpBoost = object.jumpBoost();

        if (jumpBoost > MAX_JUMP_BOOST || jumpBoost < MIN_JUMP_BOOST)
            throw new IllegalArgumentException("Invalid jump boost");

        NetworkUtil.writeVarInt(buf, object.entityId());
        actionCodec.write(buf, object.action());
        NetworkUtil.writeVarInt(buf, jumpBoost);
    }

    @Override
    public void handle(@NonNull ClientActionPlayPacket packet, @NonNull SocketPlayerConnection connection) {
        // TODO
    }
}