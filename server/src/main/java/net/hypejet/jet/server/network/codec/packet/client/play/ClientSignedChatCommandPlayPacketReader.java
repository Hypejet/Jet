package net.hypejet.jet.server.network.codec.packet.client.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkReader;
import net.hypejet.jet.server.network.codec.game.signing.SeenMessagesNetworkReader;
import net.hypejet.jet.server.network.codec.game.signing.SignedArgumentNetworkReader;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientSignedChatCommandPlayPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.util.game.signing.SignedArgument;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkReader network-reader} reading
 * {@linkplain ClientSignedChatCommandPlayPacket signed chat command play packets}.
 *
 * @since 1.0
 * @see ClientSignedChatCommandPlayPacket
 * @see NetworkReader
 */
public final class ClientSignedChatCommandPlayPacketReader
        implements NetworkReader<ClientSignedChatCommandPlayPacket> {

    private static final CollectionNetworkReader<SignedArgument>
            ARGUMENTS_READER = new CollectionNetworkReader<>(8, SignedArgumentNetworkReader.INSTANCE);

    /**
     * An instance of
     * the {@linkplain ClientSignedChatCommandPlayPacketReader client signed chat command play packet-reader}.
     *
     * @since 1.0
     */
    public static final ClientSignedChatCommandPlayPacketReader
            INSTANCE = new ClientSignedChatCommandPlayPacketReader();

    private ClientSignedChatCommandPlayPacketReader() {}

    @Override
    public @NonNull ClientSignedChatCommandPlayPacket read(@NonNull ByteBuf buf,
                                                           @NonNull JetRegistryManager registryManager) {
        return new ClientSignedChatCommandPlayPacket(
                StringNetworkCodec.INSTANCE.read(buf, registryManager),
                buf.readLong(),
                buf.readLong(),
                ARGUMENTS_READER.read(buf, registryManager),
                SeenMessagesNetworkReader.INSTANCE.read(buf, registryManager)
        );
    }
}