package net.hypejet.jet.server.network.protocol.codecs.signing;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.bitset.FixedBitSetNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.signing.SeenMessages;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.BitSet;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes {@linkplain SeenMessages seen
 * messages}.
 *
 * @since 1.0
 * @author Codesteh
 * @see SeenMessages
 * @see NetworkCodec
 */
public final class SeenMessagesNetworkCodec implements NetworkCodec<SeenMessages> {

    private static final int ACKNOWLEDGED_LENGTH = 20;
    private static final NetworkCodec<BitSet> ACKNOWLEDGED_CODEC = FixedBitSetNetworkCodec.codec(ACKNOWLEDGED_LENGTH);

    private static final SeenMessagesNetworkCodec INSTANCE = new SeenMessagesNetworkCodec();

    private SeenMessagesNetworkCodec() {}

    @Override
    public @NonNull SeenMessages read(@NonNull ByteBuf buf) {
        return new SeenMessages(VarIntNetworkCodec.instance().read(buf), ACKNOWLEDGED_CODEC.read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull SeenMessages object) {
        VarIntNetworkCodec.instance().write(buf, object.messageCount());
        ACKNOWLEDGED_CODEC.write(buf, object.acknowledged());
    }

    /**
     * Gets an instance of the {@linkplain SeenMessagesNetworkCodec seen messages network codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull SeenMessagesNetworkCodec instance() {
        return INSTANCE;
    }
}