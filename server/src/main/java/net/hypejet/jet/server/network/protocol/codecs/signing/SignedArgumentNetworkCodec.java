package net.hypejet.jet.server.network.protocol.codecs.signing;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import net.hypejet.jet.signing.SignedArgument;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain SignedArgument signed
 * argument}.
 *
 * @since 1.0
 * @author Codestech
 * @see SignedArgument
 * @see NetworkCodec
 */
public final class SignedArgumentNetworkCodec implements NetworkCodec<SignedArgument> {

    private static final int ARGUMENT_SIGNATURE_LENGTH = 256;
    private static final StringNetworkCodec ARGUMENT_NAME_CODEC = StringNetworkCodec.create(16);

    private static final SignedArgumentNetworkCodec INSTANCE = new SignedArgumentNetworkCodec();

    private SignedArgumentNetworkCodec() {}

    @Override
    public @NonNull SignedArgument read(@NonNull ByteBuf buf) {
        return new SignedArgument(ARGUMENT_NAME_CODEC.read(buf),
                NetworkUtil.readBytes(buf, ARGUMENT_SIGNATURE_LENGTH));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull SignedArgument object) {
        ARGUMENT_NAME_CODEC.write(buf, object.name());
        buf.writeBytes(object.signature());
    }

    /**
     * Gets an instance of the {@linkplain SignedArgumentNetworkCodec signed argument network codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull SignedArgumentNetworkCodec instance() {
        return INSTANCE;
    }
}