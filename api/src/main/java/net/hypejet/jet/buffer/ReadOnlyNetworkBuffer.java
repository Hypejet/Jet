package net.hypejet.jet.buffer;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.value.qual.IntRange;

import java.util.UUID;

/**
 * Represents a read-only container of data allowed in Minecraft protocol.
 *
 * @since 1.0
 * @author Codestech
 */
public interface ReadOnlyNetworkBuffer {
    /**
     * Reads a boolean from the buffer.
     *
     * @return the boolean
     * @since 1.0
     */
    boolean readBoolean();

    /**
     * Reads a byte from the buffer.
     *
     * @return the byte
     * @since 1.0
     */
    byte readByte();

    /**
     * Reads an unsigned byte from the buffer.
     *
     * @return the unsigned byte
     * @since 1.0
     */
    @IntRange(from = 0, to = 255)
    short readUnsignedByte();

    /**
     * Reads a short from the buffer.
     *
     * @return the short
     * @since 1.0
     */
    short readShort();

    /**
     * Reads an unsigned short from the buffer.
     *
     * @return the unsigned short
     * @since 1.0
     */
    @IntRange(from = 0, to = 65535) int readUnsignedShort();

    /**
     * Reads an integer from the buffer.
     *
     * @return the integer
     * @since 1.0
     */
    int readInt();

    /**
     * Reads a long from the buffer.
     *
     * @return the long
     * @since 1.0
     */
    long readLong();

    /**
     * Reads a float from the buffer.
     *
     * @return the float
     * @since 1.0
     */
    float readFloat();

    /**
     * Reads a double from the buffer.
     *
     * @return the double
     * @since 1.0
     */
    double readDouble();

    /**
     * Reads a string from the buffer.
     *
     * @return the string
     * @since 1.0
     */
    @NonNull
    String readString();

    /**
     * Reads a text component, which is serialized as an NBT tag.
     *
     * @return the text component
     */
    @NonNull Component readTextComponent();

    /**
     * Reads a text component, which is serialized as a json string.
     *
     * @return the text component
     * @since 1.0
     */
    @NonNull Component readJsonTextComponent();

    /**
     * Reads a variable-length integer from the buffer.
     *
     * @return the variable-length integer
     * @since 1.0
     */
    int readVarInt();

    /**
     * Reads a variable-length long from the buffer.
     *
     * @return the variable-length long
     * @since 1.0
     */
    long readVarLong();

    /**
     * Reads an unique id from the buffer.
     *
     * @return the unique id
     * @since 1.0
     */
    @NonNull UUID readUniqueId();

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

    private static @NonNull RuntimeException identifierNotParseable(@NonNull String identifier) {
        return new IllegalArgumentException("An identifier of \"" + identifier + "\" contains illegal characters");
    }
}