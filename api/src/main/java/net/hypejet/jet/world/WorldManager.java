package net.hypejet.jet.world;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.UUID;

/**
 * Represents a manager, which creates, gets and unregisters {@linkplain World worlds}.
 *
 * @since 1.0
 * @author Codestech
 * @see World
 */
public interface WorldManager {
    /**
     * Creates a {@linkplain World world} with a random {@linkplain UUID unique identifier} assigned and registers it.
     *
     * @return the world
     * @since 1.0
     */
    @NonNull World createWorld();

    /**
     * Creates a {@linkplain World world} with a {@linkplain UUID unique identifier} specified and registers it.
     *
     * @param uniqueId the unique identifier
     * @return the world
     * @since 1.0
     * @throws IllegalArgumentException if a world with the unique identifier specified already exists
     */
    @NonNull World createWorld(@NonNull UUID uniqueId);

    /**
     * Registers a {@linkplain World world}.
     *
     * @param world the world to register
     * @throws IllegalArgumentException if the world has been already registered, a world with the same unique
     *                                  identifier is registered or the world is not a valid Jet world
     * @since 1.0
     */
    void registerWorld(@NonNull World world);

    /**
     * Gets a registered {@linkplain World world} by a {@linkplain UUID unique identifier}.
     *
     * @param uniqueId the unique identifier
     * @return the world, {@code null} if there is no a world with the unique identifier specified
     * @since 1.0
     */
    @Nullable World getWorld(@NonNull UUID uniqueId);

    /**
     * Unregisters a {@linkplain World world}.
     *
     * @param world the world to unregister
     * @return {@code true} if the world has been unregistered successfully, {@code false} otherwise
     * @since 1.0
     * @throws IllegalArgumentException if the world is not a valid Jet world
     */
    boolean unregisterWorld(@NonNull World world);

    /**
     * Gets {@linkplain World worlds} registered via this world manager.
     *
     * @return the worlds
     * @since 1.0
     */
    @NonNull Collection<World> worlds();
}