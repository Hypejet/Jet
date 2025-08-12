package net.hypejet.jet.server.network.codec.game.signing;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.aggregate.bitset.FixedBitSetNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.util.game.signing.SeenMessages;
import net.hypejet.jet.util.bitset.UnmodifiableBitSet;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.BitSet;

/**
 * A {@linkplain NetworkReader network reader} of {@linkplain SeenMessages seen messages}.
 *
 * @since 1.0
 * @see SeenMessages
 * @see NetworkReader
 */
public final class SeenMessagesNetworkReader implements NetworkReader<SeenMessages> {

    private static final NetworkReader<BitSet> ACKNOWLEDGED_CODEC = new FixedBitSetNetworkCodec(20);

    /**
     * An instance of the {@linkplain SeenMessagesNetworkReader seen messages network-reader}.
     *
     * @since 1.0
     */
    public static final SeenMessagesNetworkReader INSTANCE = new SeenMessagesNetworkReader();

    private SeenMessagesNetworkReader() {}

    @Override
    public @NonNull SeenMessages read(@NonNull ByteBuf buf) {
        return new SeenMessages(
                VarIntNetworkCodec.INSTANCE.read(buf),
                new UnmodifiableBitSet(ACKNOWLEDGED_CODEC.read(buf)),
                buf.readByte()
        );
    }
}