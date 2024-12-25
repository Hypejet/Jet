package net.hypejet.jet.server.network.codec.game.signing;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.aggregate.bitset.FixedBitSetNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.signing.SeenMessages;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.BitSet;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads {@linkplain SeenMessages seen messages}.
 *
 * @since 1.0
 * @see SeenMessages
 * @see NetworkReader
 */
public final class SeenMessagesNetworkReader implements NetworkReader<SeenMessages> {

    private static final int ACKNOWLEDGED_LENGTH = 20;
    private static final NetworkReader<BitSet> ACKNOWLEDGED_CODEC = new FixedBitSetNetworkCodec(ACKNOWLEDGED_LENGTH);

    /**
     * An instance of the {@linkplain SeenMessagesNetworkReader seen messages network reader}.
     *
     * @since 1.0
     */
    public static final SeenMessagesNetworkReader INSTANCE = new SeenMessagesNetworkReader();

    private SeenMessagesNetworkReader() {}

    @Override
    public @NonNull SeenMessages read(@NonNull ByteBuf buf) {
        return new SeenMessages(VarIntNetworkCodec.INSTANCE.read(buf), ACKNOWLEDGED_CODEC.read(buf));
    }
}