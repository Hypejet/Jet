package net.hypejet.jet.data.json.model.position;

/**
 * A position of Minecraft block.
 *
 * @param x an {@code X} axis value of the block position
 * @param y an {@code Y} axis value of the block position
 * @param z an {@code Z} axis value of the block position
 * @since 1.0
 */
public record JsonBlockPosition(int x, int y, int z) {}