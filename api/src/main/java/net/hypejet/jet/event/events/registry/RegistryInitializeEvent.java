package net.hypejet.jet.event.events.registry;

import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents an event, which is called when a registry is being initialized.
 *
 * @param <E> a type of entries in the registry
 * @since 1.0
 * @author Codestech
 */
public final class RegistryInitializeEvent<E> {

    private final Key identifier;
    private final Class<E> entryClass;

    private final Map<Key, E> entryMap = new HashMap<>();

    /**
     * Constructs the {@linkplain RegistryInitializeEvent registry initialize event}.
     *
     * @param identifier an identifier of the registry
     * @param entryClass a class of entries of the registry
     * @since 1.0
     */
    public RegistryInitializeEvent(@NonNull Key identifier, @NonNull Class<E> entryClass) {
        this.identifier = identifier;
        this.entryClass = entryClass;
    }

    /**
     * Gets an {@linkplain Key identifier} of the registry.
     *
     * @return the identifier
     * @since 1.0
     */
    public @NonNull Key identifier() {
        return this.identifier;
    }

    /**
     * Gets a {@linkplain Class class} of entries of the registry.
     *
     * @return the class
     * @since 1.0
     */
    public @NonNull Class<E> entryClass() {
        return this.entryClass;
    }

    /**
     * Gets a {@linkplain Map map} of entries, which have been registered during this event.
     *
     * @return the map
     * @since 1.0
     */
    public @NonNull Map<Key, E> entryMap() {
        return Map.copyOf(this.entryMap);
    }

    /**
     * Registers an {@linkplain E entry} to be added to the registry.
     *
     * @param identifier an identifier of the entry
     * @param entry the entry
     * @throws IllegalArgumentException if an entry with the same identifier has been already registered or
     *                                  the {@link #entryClass} is not assignable from a class of the entry specified
     * @since 1.0
     */
    public void register(@NonNull Key identifier, @NonNull E entry) {
        if (this.entryMap.containsKey(identifier))
            throw new IllegalArgumentException(String.format(
                    "An entry with identifier %s has been already registered",
                    identifier
            ));

        if (!this.entryClass.isAssignableFrom(entry.getClass()))
            throw new IllegalArgumentException(String.format(
                    "The entry specified is not a valid entry of \"%s\"",
                    this.entryClass.getSimpleName()
            ));

        this.entryMap.put(identifier, entry);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistryInitializeEvent<?> that = (RegistryInitializeEvent<?>) o;
        return Objects.equals(this.identifier, that.identifier)
                && Objects.equals(this.entryClass, that.entryClass)
                && Objects.equals(this.entryMap, that.entryMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.identifier, this.entryClass, this.entryMap);
    }

    @Override
    public String toString() {
        return "RegistryInitializeEvent{" +
                "identifier=" + this.identifier +
                ", entryClass=" + this.entryClass +
                ", entryMap=" + this.entryMap +
                '}';
    }
}