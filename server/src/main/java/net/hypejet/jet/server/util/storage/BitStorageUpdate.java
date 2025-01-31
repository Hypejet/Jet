package net.hypejet.jet.server.util.storage;

/**
 * Represents an element replacement of {@linkplain BitStorage a bit storage}.
 *
 * @param elementIndex an index of an element that should be replaced
 * @param newElement the element replacement
 * @since 1.0
 * @see BitStorage
 */
public record BitStorageUpdate(int elementIndex, int newElement) {}