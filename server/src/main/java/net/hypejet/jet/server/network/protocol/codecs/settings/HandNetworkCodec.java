package net.hypejet.jet.server.network.protocol.codecs.settings;

import io.netty.buffer.ByteBuf;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.EnumMap;

/**"
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain Entity.Hand entity hand},
 *
 * @since 1.0
 * @author Codestech
 * @see Entity.Hand
 * @see NetworkCodec
 */
public final class HandNetworkCodec implements NetworkCodec<Entity.Hand> {

    private static final HandNetworkCodec INSTANCE = new HandNetworkCodec();

    private static final IntObjectMap<Entity.Hand> idToHandMap = new IntObjectHashMap<>();
    private static final EnumMap<Entity.Hand, Integer> handToIdMap = new EnumMap<>(Entity.Hand.class);

    static {
        idToHandMap.put(0, Entity.Hand.LEFT);
        idToHandMap.put(1, Entity.Hand.RIGHT);
        idToHandMap.forEach((id, hand) -> handToIdMap.put(hand, id));
    }

    private HandNetworkCodec() {}

    @Override
    public Entity.@NonNull Hand read(@NonNull ByteBuf buf) {
        int handIdentifier = NetworkUtil.readVarInt(buf);
        Entity.Hand hand = idToHandMap.get(handIdentifier);

        if (hand == null) {
            throw new IllegalArgumentException("Unknown entity hand: " + handIdentifier);
        }

        return hand;
    }

    @Override
    public void write(@NonNull ByteBuf buf, Entity.@NonNull Hand object) {
        NetworkUtil.writeVarInt(buf, handToIdMap.get(object));
    }

    /**
     * Gets an instance of {@linkplain HandNetworkCodec hand network codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull HandNetworkCodec instance() {
        return INSTANCE;
    }
}