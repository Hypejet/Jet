package net.hypejet.jet.server.network.codec.packet.client.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.aggregate.array.bytes.FixedByteArrayNetworkReader;
import net.hypejet.jet.server.network.codec.game.signing.SeenMessagesNetworkReader;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientSignedChatMessagePlayPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkReader network-reader}
 * of {@linkplain ClientSignedChatMessagePlayPacket signed chat message play packets}.
 *
 * @since 1.0
 * @see ClientSignedChatMessagePlayPacket
 * @see NetworkReader
 */
public final class ClientSignedChatMessagePlayPacketReader
        implements NetworkReader<ClientSignedChatMessagePlayPacket> {

    private static final StringNetworkCodec MESSAGE_CODEC = StringNetworkCodec.create(256);
    private static final FixedByteArrayNetworkReader SIGNATURE_READER = new FixedByteArrayNetworkReader(256);

    /**
     * An instance of
     * the {@linkplain ClientSignedChatMessagePlayPacketReader client signed chat message play packet-reader}.
     *
     * @since 1.0
     */
    public static final ClientSignedChatMessagePlayPacketReader
            INSTANCE = new ClientSignedChatMessagePlayPacketReader();

    private ClientSignedChatMessagePlayPacketReader() {}

    @Override
    public @NonNull ClientSignedChatMessagePlayPacket read(@NonNull ByteBuf buf,
                                                           @NonNull JetRegistryManager registryManager) {
        return new ClientSignedChatMessagePlayPacket(
                MESSAGE_CODEC.read(buf, registryManager),
                buf.readLong(),
                buf.readLong(),
                NetworkUtil.readOptional(SIGNATURE_READER, buf, registryManager),
                SeenMessagesNetworkReader.INSTANCE.read(buf, registryManager)
        );
    }
}