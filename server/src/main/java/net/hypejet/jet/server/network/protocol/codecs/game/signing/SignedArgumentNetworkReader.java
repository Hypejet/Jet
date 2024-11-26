package net.hypejet.jet.server.network.protocol.codecs.game.signing;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.collection.CollectionNetworkReader;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import net.hypejet.jet.signing.SignedArgument;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which {@linkplain SignedArgument a signed argument}.
 *
 * @since 1.0
 * @author Codestech
 * @see SignedArgument
 * @see NetworkReader
 */
public final class SignedArgumentNetworkReader implements NetworkReader<SignedArgument> {

    private static final int ARGUMENT_SIGNATURE_LENGTH = 256;

    /**
     * An instance of {@linkplain SignedArgumentNetworkReader a signed argument network reader}.
     *
     * @since 1.0
     */
    public static final SignedArgumentNetworkReader INSTANCE = new SignedArgumentNetworkReader();

    /**
     * An instance of {@linkplain CollectionNetworkReader a collection network reader}, which reads elements
     * with a type of {@linkplain SignedArgument signed argument}.
     */
    public static final CollectionNetworkReader<SignedArgument> COLLECTION_READER =
            new CollectionNetworkReader<>(INSTANCE);

    private SignedArgumentNetworkReader() {}

    @Override
    public @NonNull SignedArgument read(@NonNull ByteBuf buf) {
        return new SignedArgument(
                StringNetworkCodec.MAX_16_INSTANCE.read(buf),
                NetworkUtil.readBytes(buf, ARGUMENT_SIGNATURE_LENGTH)
        );
    }
}