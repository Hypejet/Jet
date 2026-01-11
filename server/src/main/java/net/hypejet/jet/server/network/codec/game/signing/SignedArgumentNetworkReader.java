package net.hypejet.jet.server.network.codec.game.signing;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.aggregate.array.bytes.FixedByteArrayNetworkReader;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.util.game.signing.SignedArgument;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkReader network-reader} reading {@linkplain SignedArgument signed arguments}.
 *
 * @since 1.0
 * @see SignedArgument
 * @see NetworkReader
 */
public final class SignedArgumentNetworkReader implements NetworkReader<SignedArgument> {

    private static final FixedByteArrayNetworkReader SIGNATURE_READER = new FixedByteArrayNetworkReader(256);

    /**
     * An instance of the {@linkplain SignedArgumentNetworkReader signed argument network-reader}.
     *
     * @since 1.0
     */
    public static final SignedArgumentNetworkReader INSTANCE = new SignedArgumentNetworkReader();

    private SignedArgumentNetworkReader() {}

    @Override
    public @NonNull SignedArgument read(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager) {
        return new SignedArgument(
                StringNetworkCodec.MAX_16_INSTANCE.read(buf, registryManager),
                SIGNATURE_READER.read(buf, registryManager)
        );
    }
}