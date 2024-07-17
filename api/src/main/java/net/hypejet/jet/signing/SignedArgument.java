package net.hypejet.jet.signing;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.Objects;

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

    /**
     * {@inheritDoc}
     */
    // Override, because records do not compare contents of arrays natively
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof SignedArgument that)) return false;
        return Objects.equals(this.name, that.name) && Objects.deepEquals(this.signature, that.signature);
    }

    /**
     * {@inheritDoc}
     */
    // Override, because records do not compare contents of arrays natively
    @Override
    public int hashCode() {
        return Objects.hash(this.name, Arrays.hashCode(this.signature));
    }
}