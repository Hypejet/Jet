package net.hypejet.jet.server.network.codec.packet.client.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientPlayerInputPlayPacket;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientPlayerInputPlayPacket.InputFlag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads
 * {@linkplain ClientPlayerInputPlayPacket a client player input play packet}.
 *
 * @since 1.0
 * @see ClientPlayerInputPlayPacket
 * @see NetworkReader
 */
public final class ClientPlayerInputPlayPacketReader implements NetworkReader<ClientPlayerInputPlayPacket> {
    /**
     * An instance of the {@linkplain ClientPlayerInputPlayPacketReader client player input play packet reader}.
     *
     * @since 1.0
     */
    public static final ClientPlayerInputPlayPacketReader INSTANCE = new ClientPlayerInputPlayPacketReader();

    private static final EnumMap<InputFlag, Byte> INPUT_FLAG_BIT_MASKS = new EnumMap<>(InputFlag.class);

    static {
        INPUT_FLAG_BIT_MASKS.put(InputFlag.MOVING_FORWARD, (byte) 0x01);
        INPUT_FLAG_BIT_MASKS.put(InputFlag.MOVING_BACKWARD, (byte) 0x02);
        INPUT_FLAG_BIT_MASKS.put(InputFlag.MOVING_LEFT, (byte) 0x04);
        INPUT_FLAG_BIT_MASKS.put(InputFlag.MOVING_RIGHT, (byte) 0x08);
        INPUT_FLAG_BIT_MASKS.put(InputFlag.JUMPING, (byte) 0x10);
        INPUT_FLAG_BIT_MASKS.put(InputFlag.SNEAKING, (byte) 0x20);
        INPUT_FLAG_BIT_MASKS.put(InputFlag.SPRINTING, (byte) 0x40);
    }

    private ClientPlayerInputPlayPacketReader() {}

    @Override
    public @NonNull ClientPlayerInputPlayPacket read(@NonNull ByteBuf buf) {
        short packedFlags = buf.readUnsignedByte();
        Set<InputFlag> inputFlags = EnumSet.noneOf(InputFlag.class);

        for (InputFlag flag : InputFlag.values()) {
            Byte bitMask = INPUT_FLAG_BIT_MASKS.get(flag);
            if (bitMask == null) {
                throw new IllegalArgumentException(String.format(
                        "Could not find a bit mask for input flag with name of \"%s\"",
                        flag.name()
                ));
            }

            if ((packedFlags & bitMask) != 0)
                inputFlags.add(flag);
        }

        return new ClientPlayerInputPlayPacket(inputFlags);
    }
}