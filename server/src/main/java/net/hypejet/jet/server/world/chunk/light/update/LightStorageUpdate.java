package net.hypejet.jet.server.world.chunk.light.update;

import net.hypejet.jet.server.world.chunk.light.storage.LightStorage;

/**
 * Represents an update of {@linkplain LightStorage light storage}.
 *
 * @param x a section-relative {@code X} value of coordinate that the light should be updated at
 * @param y a section-relative {@code Y} value of coordinate that the light should be updated at
 * @param z a section-relative {@code Z} value of coordinate that the light should be updated at
 * @param value a value that the light value at the coordinate specified should be updated with
 * @since 1.0
 * @see LightStorage
 */
public record LightStorageUpdate(byte x, byte y, byte z, byte value) {}