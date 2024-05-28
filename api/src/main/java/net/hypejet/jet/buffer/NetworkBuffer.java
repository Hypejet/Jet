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
public interface NetworkBuffer {
    /**
     * Reads a boolean from the buffer.
     *
     * @return the boolean
     * @since 1.0
     */
    boolean readBoolean();

    /**
     * Writes a boolean to the buffer.
     *
     * @param value the boolean
     * @since 1.0
     */
    void writeBoolean(boolean value);

    /**
     * Reads a byte from the buffer.
     *
     * @return the byte
     * @since 1.0
     */
    byte readByte();

    /**
     * Writes a byte to the buffer.
     *
     * @param value the byte
     * @since 1.0
     */
    void writeByte(byte value);

    /**
     * Reads an unsigned byte from the buffer.
     *
     * @return the unsigned byte
     * @since 1.0
     */
    @IntRange(from = 0, to = 255)
    short readUnsignedByte();

    /**
     * Writes an unsigned byte to the buffer.
     *
     * @param value the unsigned byte
     * @since 1.0
     */
    void writeUnsignedByte(@IntRange(from = 0, to = 255) short value);

    /**
     * Reads a short from the buffer.
     *
     * @return the short
     * @since 1.0
     */
    short readShort();

    /**
     * Writes a short to the buffer.
     *
     * @param value the short
     * @since 1.0
     */
    void writeShort(short value);

    /**
     * Reads an unsigned short from the buffer.
     *
     * @return the unsigned short
     * @since 1.0
     */
    @IntRange(from = 0, to = 65535) int readUnsignedShort();

    /**
     * Writes an unsigned short to the buffer.
     *
     * @param value the unsigned short
     * @since 1.0
     */
    void writeUnsignedShort(@IntRange(from = 0, to = 65535) int value);

    /**
     * Reads an integer from the buffer.
     *
     * @return the integer
     * @since 1.0
     */
    int readInt();

    /**
     * Writes an integer to the buffer.
     *
     * @param value the integer
     * @since 1.0
     */
    void writeInt(int value);

    /**
     * Reads a long from the buffer.
     *
     * @return the long
     * @since 1.0
     */
    long readLong();

    /**
     * Writes a long to the buffer.
     *
     * @param value the long
     * @since 1.0
     */
    void writeLong(long value);

    /**
     * Reads a float from the buffer.
     *
     * @return the float
     * @since 1.0
     */
    float readFloat();

    /**
     * Writes a float to the buffer.
     *
     * @param value the float
     * @since 1.0
     */
    void writeFloat(float value);

    /**
     * Reads a double from the buffer.
     *
     * @return the double
     * @since 1.0
     */
    double readDouble();

    /**
     * Writes a double to the buffer.
     *
     * @param value the double
     * @since 1.0
     */
    void writeDouble(double value);

    /**
     * Reads a string from the buffer.
     *
     * @return the string
     * @since 1.0
     */
    @NonNull
    String readString();

    /**
     * Writes a string to the buffer.
     *
     * @param value the string
     * @since 1.0
     */
    void writeString(@NonNull String value);

    /**
     * Reads a text component, which is serialized as an NBT tag.
     *
     * @return the text component
     */
    @NonNull Component readTextComponent();

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
     * @return the text component
     * @since 1.0
     */
    @NonNull Component readJsonTextComponent();

    /**
     * Reads a text component, which is serialized as a json string.
     *
     * @param value the text component
     * @since 1.0
     */
    void writeJsonTextComponent(@NonNull Component value);


    /**
     * Reads a variable-length integer from the buffer.
     *
     * @return the variable-length integer
     * @since 1.0
     */
    int readVarInt();

    /**
     * Writes a variable-length integer to the buffer.
     *
     * @param value the variable-length integer
     * @since 1.0
     */
    void writeVarInt(int value);

    /**
     * Reads a variable-length long from the buffer.
     *
     * @return the variable-length long
     * @since 1.0
     */
    long readVarLong();

    /**
     * Writes a variable-length long to the buffer.
     *
     * @param value the variable-length long
     * @since 1.0
     */
    void writeVarLong(long value);

    /**
     * Reads an unique id from the buffer.
     *
     * @return the unique id
     * @since 1.0
     */
    @NonNull UUID readUniqueId();

    /**
     * Writes a unique id to the buffer.
     *
     * @param value the unique id
     * @since 1.0
     */
    void writeUniqueId(@NonNull UUID value);

    /**
     * Checks whether an amount of bytes is higher or equal to remaining bytes.
     *
     * @param length the amount of bytes
     * @return true if an amount of bytes is higher or equal to remaining bytes, false otherwise
     * @since 1.0
     */
    boolean isReadable(int length);

    /**
     * Reads an identifier from the buffer.
     *
     * @return the identifier
     * @since 1.0
     */
    default @NonNull Key readIdentifier() {
        String identifier = this.readString();
        if (!Key.parseable(identifier)) throw identifierNotParseable(identifier);
        // Use another method to create a key to avoid warning in IDE, which are already solved
        return Key.key(identifier, Key.DEFAULT_SEPARATOR);
    }

    /**
     * Writes an identifier to the buffer.
     *
     * @param identifier the identifier
     * @since 1.0
     */
    default void writeIdentifier(@NonNull Key identifier) {
        this.writeString(identifier.asString());
    }

    /**
     * Reads a byte array from the buffer.
     *
     * @return the byte array
     * @throws IllegalArgumentException if an amount of remaining bytes is lower than a string size
     * @since 1.0
     */
    default byte @NonNull [] readByteArray() {
        int length = this.readVarInt();
        byte[] array = new byte[length];

        if (!this.isReadable(length))
            throw new IllegalArgumentException("An amount of remaining bytes is lower than a string size");

        for (int i = 0; i < length; i++) {
            array[i] = this.readByte();
        }

        return array;
    }

    /**
     * Writes a byte array to the buffer.
     *
     * @param value the byte array
     * @since 1.0
     */
    default void writeByteArray(byte @NonNull [] value) {
        this.writeVarInt(value.length);

        for (byte element : value) {
            this.writeByte(element);
        }
    }

    private static @NonNull RuntimeException identifierNotParseable(@NonNull String identifier) {
        return new IllegalArgumentException("An identifier of \"" + identifier + "\" contains illegal characters");
    }
}