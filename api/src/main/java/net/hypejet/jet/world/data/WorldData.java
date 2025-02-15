package net.hypejet.jet.world.data;

/**
 * Represents an additional data of {@linkplain net.hypejet.jet.world.World a world}, which is not used by default
 * by the server, but is sent to clients.
 *
 * @param seed a seed used for generation of the world
 * @param flat whether the world is flat
 * @param seaLevel a sea level of the world
 * @since 1.0
 * @see WorldData
 */
public record WorldData(long seed, boolean flat, int seaLevel) {}