package net.hypejet.jet.world.chunk;

import net.kyori.adventure.nbt.BinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a Minecraft block entity.
 *
 * @param x an {@code X} coordinate of the block entity, relative to the chunk, which they are in
 * @param z an {@code Y} coordinate of the block entity, relative to the chunk, which they are in
 * @param y a height, relative to the world
 * @param type a type of the block entity
 * @param data a data of the block entity, without the position data
 * @since 1.0
 * @author Codestech
 */
public record BlockEntity(byte x, byte z, short y, int type, @NonNull BinaryTag data) {}