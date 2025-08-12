package net.hypejet.jet.session.login.profile;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/**
 * Represents a property of a Minecraft game profile.
 *
 * @param name a name of the property
 * @param value a value of the property
 * @param signature a signature of the property, {@code null} if the property has not been signed
 * @since 1.0
 */
public record GameProfileProperty(@NonNull String name, @NonNull String value, @Nullable String signature) {
    /**
     * Constructs the {@linkplain GameProfileProperty game profile property} with no signature.
     *
     * @param name a name of the property
     * @param value a value of the property
     * @since 1.0
     */
    public GameProfileProperty(@NonNull String name, @NonNull String value) {
        this(name, value, null);
    }

    /**
     * Constructs the {@linkplain GameProfileProperty game profile property}.
     *
     * @param name a name of the property
     * @param value a value of the property
     * @param signature a signature of the property, {@code null} if the property has not been signed
     * @since 1.0
     */
    public GameProfileProperty {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");
    }
}