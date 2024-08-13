package net.hypejet.jet.registry;

import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * Represents a Minecraft registry.
 *
 * @param <E> a type of entries in this registry
 * @since 1.0
 * @author Codestech
 */
public interface Registry<E> {
    /**
     * Gets an {@linkplain Key identifier} of this {@linkplain Registry registry}.
     *
     * @return the identifier
     * @since 1.0
     */
    @NonNull Key registryIdentifier();

    /**
     * Gets a {@linkplain Class class} of entries of this {@linkplain Registry registry}.
     *
     * @return the class
     * @since 1.0
     */
    @NonNull Class<E> entryClass();

    /**
     * Gets a registry entry by an identifier of it.
     *
     * @param identifier the identifier
     * @return the registry entry
     * @since 1.0
     */
    @Nullable E get(@NonNull Key identifier);

    /**
     * Gets a registry entry by a numeric identifier of it.
     *
     * @param numericIdentifier the numeric identifier
     * @return the registry entry
     * @since 1.0
     */
    @Nullable E get(int numericIdentifier);

    /**
     * Gets whether a registry entry specified was registered in this {@linkplain Registry registry}.
     *
     * @param entry the registry entry
     * @return {@code true} if the registry entry was registered in this registry, {@code false} otherwise
     * @since 1.0
     */
    boolean isRegistered(@NonNull E entry);

    /**
     * Gets a {@linkplain Key identifier} of a registry entry registered in this {@linkplain Registry registry}.
     *
     * @param entry the registry entry
     * @return the identifier
     * @since 1.0
     */
    @NonNull Key identifierOf(@NonNull E entry);

    /**
     * Gets a numeric identifier of a registry entry registered in this {@link Registry registry}.
     *
     * @param entry the registry entry
     * @return a numeric identifier of the registry entry
     * @since 1.0
     */
    int numericIdentifierOf(@NonNull E entry);

    /**
     * Gets a {@linkplain Collection collection} of all entries registered in this {@linkplain Registry registry}.
     *
     * @return the collection
     * @since 1.0
     */
    @NonNull Collection<E> entries();

    /**
     * Gets a {@linkplain List list} of all entries registered in this {@linkplain Registry registry}.
     *
     * <p>The entries are sorted by their numeric identifier.</p>
     *
     * @return the list
     * @since 1.0
     */
    @NonNull List<E> sortedEntries();
}