package net.hypejet.jet.buffer;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.value.qual.IntRange;

import java.util.UUID;

/**
 * Represents a container of data allowed by Minecraft protocol.
 *
 * @since 1.0
 * @author Codestech
 */
public interface NetworkBuffer extends ReadOnlyNetworkBuffer {
    /**
     * Writes a boolean to the buffer.
     *
     * @param value the boolean
     * @since 1.0
     */
    void writeBoolean(boolean value);

    /**
     * Writes a byte to the buffer.
     *
     * @param value the byte
     * @since 1.0
     */
    void writeByte(byte value);

    /**
     * Writes an unsigned byte to the buffer.
     *
     * @param value the unsigned byte
     * @since 1.0
     */
    void writeUnsignedByte(@IntRange(from = 0, to = 255) short value);

    /**
     * Writes a short to the buffer.
     *
     * @param value the short
     * @since 1.0
     */
    void writeShort(short value);

    /**
     * Writes an unsigned short to the buffer.
     *
     * @param value the unsigned short
     * @since 1.0
     */
    void writeUnsignedShort(@IntRange(from = 0, to = 65535) int value);

    /**
     * Writes an integer to the buffer.
     *
     * @param value the integer
     * @since 1.0
     */
    void writeInt(int value);

    /**
     * Writes a long to the buffer.
     *
     * @param value the long
     * @since 1.0
     */
    void writeLong(long value);

    /**
     * Writes a float to the buffer.
     *
     * @param value the float
     * @since 1.0
     */
    void writeFloat(float value);

    /**
     * Writes a double to the buffer.
     *
     * @param value the double
     * @since 1.0
     */
    void writeDouble(double value);

    /**
     * Writes a string to the buffer.
     *
     * @param value the string
     * @since 1.0
     */
    void writeString(@NonNull String value);

    /**
     * Writes a text component, which is serialized as an NBT tag.
     *
     * @param value the text component
     * @since 1.0
     */
    void writeTextComponent(@NonNull Component value);

    /**
     * Reads a text component, which is serialized as a json string.
     *
     * @param value the text component
     * @since 1.0
     */
    void writeJsonTextComponent(@NonNull Component value);

    /**
     * Writes a variable-length integer to the buffer.
     *
     * @param value the variable-length integer
     * @since 1.0
     */
    void writeVarInt(int value);

    /**
     * Writes a variable-length long to the buffer.
     *
     * @param value the variable-length long
     * @since 1.0
     */
    void writeVarLong(long value);

    /**
     * Writes a unique id to the buffer.
     *
     * @param value the unique id
     * @since 1.0
     */
    void writeUniqueId(@NonNull UUID value);

    /**
     * Writes an identifier to the buffer.
     *
     * @param identifier the identifier
     * @since 1.0
     */
    default void writeIdentifier(@NonNull Key identifier) {
        this.writeString(identifier.asString());
    }
}