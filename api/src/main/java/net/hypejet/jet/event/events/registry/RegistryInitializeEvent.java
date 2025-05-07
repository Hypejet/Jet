package net.hypejet.jet.event.events.registry;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents an event, which is called when a registry is being initialized.
 *
 * @param <E> a type of entries of the registry that is being initialized
 * @since 1.0
 */
public final class RegistryInitializeEvent<E> {

    private final Key key;
    private final Class<E> entryClass;

    private final Map<Key, E> entryMap = new HashMap<>();
    private final Map<Key, E> unmodifiableEntryMapView = Collections.unmodifiableMap(this.entryMap);

    /**
     * Constructs the {@linkplain RegistryInitializeEvent registry initialize event}.
     *
     * @param key a key of the registry
     * @param entryClass a class of entries of the registry
     * @since 1.0
     */
    public RegistryInitializeEvent(@NonNull Key key, @NonNull Class<E> entryClass) {
        this.key = NullabilityUtil.requireNonNull(key, "key");
        this.entryClass = NullabilityUtil.requireNonNull(entryClass, "entry class");
    }

    /**
     * Gets {@linkplain Key a key} of the registry.
     *
     * @return the identifier
     * @since 1.0
     */
    public @NonNull Key key() {
        return this.key;
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
     * Gets an unmodifiable view of {@linkplain Map a map} of entries, which have been registered during this event.
     *
     * @return the map
     * @since 1.0
     */
    public @NonNull Map<Key, E> entryMap() {
        return this.unmodifiableEntryMapView;
    }

    /**
     * Registers {@linkplain E an entry} that should be added to the registry.
     *
     * @param key a key that the entry should be associated with
     * @param entry the entry
     * @throws IllegalArgumentException if an entry associated with the same key has been already registered or
     *                                  the {@linkplain #entryClass entry class} is not assignable from a class of
     *                                  the entry specified
     * @since 1.0
     */
    public void register(@NonNull Key key, @NonNull E entry) {
        NullabilityUtil.requireNonNull(key, "key");
        NullabilityUtil.requireNonNull(entry, "entry");

        if (this.entryMap.containsKey(key)) {
            throw new IllegalArgumentException(String.format(
                    "An entry with key of %s has been already registered",
                    key
            ));
        }

        if (!this.entryClass.isAssignableFrom(entry.getClass())) {
            throw new IllegalArgumentException(String.format(
                    "The entry specified is not an instance of \"%s\"",
                    this.entryClass.getSimpleName()
            ));
        }

        this.entryMap.put(key, entry);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegistryInitializeEvent<?> event)) return false;
        return Objects.equals(this.key, event.key)
                && Objects.equals(this.entryClass, event.entryClass)
                && Objects.equals(this.entryMap, event.entryMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.key, this.entryClass, this.entryMap);
    }

    @Override
    public String toString() {
        return "RegistryInitializeEvent{" +
                "identifier=" + this.key +
                ", entryClass=" + this.entryClass +
                ", entryMap=" + this.entryMap +
                '}';
    }
}