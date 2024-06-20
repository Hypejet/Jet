package net.hypejet.jet.server.network.protocol.packet.client.codec.configuration;

import io.netty.buffer.ByteBuf;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.hypejet.jet.protocol.packet.client.configuration.ClientResourcePackResponseConfigurationPacket;
import net.hypejet.jet.protocol.packet.client.configuration.ClientResourcePackResponseConfigurationPacket.Result;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.EnumMap;
import java.util.UUID;

/**
 * Represents a {@linkplain ClientPacketCodec client packet codec}, which reads and writes
 * a {@linkplain ClientResourcePackResponseConfigurationPacket resource pack response configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientResourcePackResponseConfigurationPacket
 * @see ClientPacketCodec
 */
public final class ClientResourcePackResponseConfigurationPacketCodec
        extends ClientPacketCodec<ClientResourcePackResponseConfigurationPacket> {

    private static final IntObjectMap<Result> idToResultMap = new IntObjectHashMap<>();
    private static final EnumMap<Result, Integer> resultToIdMap = new EnumMap<>(Result.class);

    static {
        idToResultMap.put(0, Result.SUCCESS);
        idToResultMap.put(1, Result.DECLINED);
        idToResultMap.put(2, Result.FAILED_TO_DOWNLOAD);
        idToResultMap.put(3, Result.ACCEPTED);
        idToResultMap.put(4, Result.DOWNLOADED);
        idToResultMap.put(5, Result.INVALID_URL);
        idToResultMap.put(6, Result.FAILED_TO_RELOAD);
        idToResultMap.put(7, Result.DISCARDED);
        idToResultMap.forEach((id, result) -> resultToIdMap.put(result, id));
    }

    /**
     * Constructs a {@linkplain ClientResourcePackResponseConfigurationPacketCodec resource pack response configuration
     * packet codec}.
     *
     * @since 1.0
     */
    public ClientResourcePackResponseConfigurationPacketCodec() {
        super(ClientPacketIdentifiers.CONFIGURATION_RESOURCE_PACK_RESPONSE,
                ClientResourcePackResponseConfigurationPacket.class);
    }

    @Override
    public @NonNull ClientResourcePackResponseConfigurationPacket read(@NonNull ByteBuf buf) {
        UUID uniqueId = NetworkUtil.readUniqueId(buf);

        int resultIdentifier = NetworkUtil.readVarInt(buf);
        Result result = idToResultMap.get(resultIdentifier);

        if (result == null) {
            throw new IllegalStateException("Unknown resource pack result: " + resultIdentifier);
        }

        return new ClientResourcePackResponseConfigurationPacket(uniqueId, result);
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientResourcePackResponseConfigurationPacket object) {
        NetworkUtil.writeUniqueId(buf, object.uniqueId());
        NetworkUtil.writeVarInt(buf, resultToIdMap.get(object.result()));
    }

    @Override
    public void handle(@NonNull ClientResourcePackResponseConfigurationPacket packet,
                       @NonNull SocketPlayerConnection connection) {
        // TODO
    }
}