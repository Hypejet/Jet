package net.hypejet.jet.signing;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an argument of a Minecraft command, which was signed by a client.
 *
 * @param name a name of the argument
 * @param signature a signature of the argument
 * @since 1.0
 * @author Codestech
 */
public record SignedArgument(@NonNull String name, byte @NonNull [] signature) {
    /**
     * Constructs the {@linkplain SignedArgument signed argument}.
     *
     * <p>The signature is being copied to prevent modifications of the array.</p>
     *
     * @param name a name of the argument
     * @param signature a signature of the argument
     * @since 1.0
     */
    public SignedArgument {
        signature = signature.clone();
    }

    /**
     * Gets a signature, which the client signed the argument with.
     *
     * <p>The array returned is a copy to prevents modifications of the original array.</p>
     *
     * @return the signature
     * @since 1.0
     */
    @Override
    public byte @NonNull [] signature() {
        return this.signature.clone();
    }
}