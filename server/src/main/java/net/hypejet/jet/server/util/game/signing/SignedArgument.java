package net.hypejet.jet.server.util.game.signing;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an argument of a Minecraft command, which was signed by a client.
 *
 * @param name a name of the argument
 * @param signature a signature of the argument
 * @since 1.0
 */
public record SignedArgument(@NonNull String name, @NonNull UnmodifiableByteArray signature) {
    /**
     * Constructs the {@linkplain SignedArgument signed argument}.
     *
     * @param name a name of the argument
     * @param signature a signature of the argument
     * @since 1.0
     */
    public SignedArgument(@NonNull String name, byte @NonNull [] signature) {
        this(name, new UnmodifiableByteArray(signature));
    }

    /**
     * Constructs the {@linkplain SignedArgument signed argument}.
     *
     * @param name a name of the argument
     * @param signature a signature of the argument
     * @since 1.0
     */
    public SignedArgument {
        NullabilityUtil.requireNonNull(name, "name");
        NullabilityUtil.requireNonNull(signature, "signature");
    }
}