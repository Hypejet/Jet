package net.hypejet.jet.server.network.buffer;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.buffer.codec.NetworkCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.common.value.qual.IntRange;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Represents a container of data allowed by Minecraft protocol.
 *
 * @since 1.0
 * @author Codestech
 */
public final class NetworkBuffer {

    private final ByteBuf buf;

    /**
     * Constructs a {@link NetworkBuffer network buffer}.
     *
     * @param buf a netty's {@link ByteBuf byte buf} to read data from
     * @since 1.0
     */
    public NetworkBuffer(@NonNull ByteBuf buf) {
        this.buf = buf;
    }

    /**
     * Reads a boolean from the buffer.
     *
     * @return the boolean
     * @since 1.0
     */
    public boolean readBoolean() {
        return this.buf.readBoolean();
    }

    /**
     * Writes a boolean to the buffer.
     *
     * @param value the boolean
     * @since 1.0
     */
    public void writeBoolean(boolean value) {
        this.buf.writeBoolean(value);
    }

    /**
     * Reads a byte from the buffer.
     *
     * @return the byte
     * @since 1.0
     */
    public byte readByte() {
        return this.buf.readByte();
    }

    /**
     * Writes a byte to the buffer.
     *
     * @param value the byte
     * @since 1.0
     */
    public void writeByte(byte value) {
        this.buf.writeByte(value);
    }

    /**
     * Reads an unsigned byte from the buffer.
     *
     * @return the unsigned byte
     * @since 1.0
     */
    public @IntRange(from = 0, to = 255) short readUnsignedByte() {
        return this.buf.readUnsignedByte();
    }

    /**"
     * Writes an unsigned byte to the buffer.
     *
     * @param value the unsigned byte
     * @since 1.0
     */
    public void writeUnsignedByte(@IntRange(from = 0, to = 255) short value) {
        this.buf.writeByte(value);
    }

    /**
     * Reads a short from the buffer.
     *
     * @return teh short
     * @since 1.0
     */
    public short readShort() {
        return this.buf.readShort();
    }

    /**
     * Writes a short to the buffer.
     *
     * @param value the short
     * @since 1.0
     */
    public void writeShort(short value) {
        this.buf.writeShort(value);
    }

    /**
     * Reads an unsigned short from the buffer.
     *
     * @return the unsigned short
     * @since 1.0
     */
    public @IntRange(from = 0, to = 65535) int readUnsignedShort() {
        return this.buf.readUnsignedShort();
    }

    /**
     * Writes an unsigned short to the buffer.
     *
     * @param value the unsigned short
     * @since 1.0
     */
    public void writeUnsignedShort(@IntRange(from = 0, to = 65535) int value) {
        this.buf.writeShort(value);
    }

    /**
     * Reads an integer from the buffer.
     *
     * @return the integer
     * @since 1.0
     */
    public int readInt() {
        return this.buf.readInt();
    }

    /**
     * Writes an integer to the buffer.
     *
     * @param value the integer
     * @since 1.0
     */
    public void writeInt(int value) {
        this.buf.writeInt(value);
    }

    /**
     * Reads a long from the buffer.
     *
     * @return the long
     * @since 1.0
     */
    public long readLong() {
        return this.buf.readLong();
    }

    /**
     * Writes a long to the buffer.
     *
     * @param value the long
     * @since 1.0
     */
    public void writeLong(long value) {
        this.buf.writeLong(value);
    }

    /**
     * Reads a float from the buffer.
     *
     * @return the float
     * @since 1.0
     */
    public float readFloat() {
        return this.buf.readFloat();
    }

    /**
     * Writes a float to the buffer.
     *
     * @param value the float
     * @since 1.0
     */
    public void writeFloat(float value) {
        this.buf.writeFloat(value);
    }

    /**
     * Reads a double from the buffer.
     *
     * @return the double
     * @since 1.0
     */
    public double readDouble() {
        return this.buf.readDouble();
    }

    /**
     * Writes a double to the buffer.
     *
     * @param value the double
     * @since 1.0
     */
    public void writeDouble(double value) {
        this.buf.writeDouble(value);
    }

    /**
     * Reads a string from the buffer.
     *
     * @return the string
     * @since 1.0
     */
    public @NonNull String readString() {
        return NetworkUtil.readString(this.buf);
    }

    /**
     * Writes a string to the buffer.
     *
     * @param value the string
     * @since 1.0
     */
    public void writeString(@NonNull String value) {
        NetworkUtil.writeString(this.buf, value);
    }

    /**
     * Reads a text component from the buffer.
     *
     * @return the text component
     * @since 1.0
     */
    public @NonNull Component readTextComponent() {
        return null; // TODO, not implemented yet, because kyori adventure does not have an NBT serializer *yet*
    }

    /**
     * Writes a text component to the buffer.
     *
     * @param value the text component
     * @since 1.0
     */
    public void writeTextComponent(@NonNull Component value) {
        // TODO, not implemented yet, because kyori adventure does not have an NBT serializer *yet*
    }

    /**
     * Reads a json text component from the buffer.
     *
     * @return the json text component
     * @since 1.0
     */
    public @NonNull Component readJsonTextComponent() {
        return GsonComponentSerializer.gson().deserialize(this.readString());
    }

    /**
     * Writes a json text component to the buffer.
     *
     * @param value the json text component
     * @since 1.0
     */
    public void writeJsonTextComponent(@NonNull Component value) {
        this.writeString(GsonComponentSerializer.gson().serialize(value));
    }

    /**
     * Reads a variable-length integer from the buffer.
     *
     * @return the variable-length integer
     * @since 1.0
     */
    public int readVarInt() {
        return NetworkUtil.readVarInt(this.buf);
    }

    /**
     * Writes a variable-length integer to the buffer.
     *
     * @param value the variable-length integer
     * @since 1.0
     */
    public void writeVarInt(int value) {
        NetworkUtil.writeVarInt(this.buf, value);
    }

    /**
     * Reads a variable-length long from the buffer.
     *
     * @return the variable-length long
     * @since 1.0
     */
    public long readVarLong() {
        return NetworkUtil.readVarLong(this.buf);
    }

    /**
     * Writes a variable-length long to the buffer.
     *
     * @param value the variable-length long
     * @since 1.0
     */
    public void writeVarLong(long value) {
        NetworkUtil.writeVarLong(this.buf, value);
    }

    /**
     * Reads an unique identifier from the buffer.
     *
     * @return the unique identifier
     * @since 1.0
     */
    public @NonNull UUID readUniqueId() {
        return new UUID(this.buf.readLong(), this.buf.readLong());
    }

    /**
     * Writes an unique identifier to the buffer.
     *
     * @param value the unique identifier
     * @since 1.0
     */
    public void writeUniqueId(@NonNull UUID value) {
        this.buf.writeLong(value.getMostSignificantBits());
        this.buf.writeLong(value.getLeastSignificantBits());
    }

    /**
     * Gets whether this buffer contains equal or more elements than a specified number.
     *
     * @param length the number
     * @return true if this buffer contains equal or more elements than a specified number, false otherwise
     * @since 1.0
     */
    public boolean isReadable(int length) {
        return this.buf.isReadable(length);
    }

    /**
     * Converts this buffer to a byte array.
     *
     * @return the byte array
     * @since 1.0
     */
    public byte @NonNull [] toByteArray() {
        int readerIndex = this.buf.readerIndex();
        this.buf.resetReaderIndex();

        byte[] array = new byte[this.buf.readableBytes()];
        this.buf.readBytes(array);

        this.buf.readerIndex(readerIndex);
        return array;
    }

    /**
     * Reads an identifier from the buffer.
     *
     * @return the identifier
     * @since 1.0
     */
    public @NonNull Key readIdentifier() {
        return Key.key(this.readString());
    }

    /**
     * Writes an identifier to the buffer.
     *
     * @param identifier the identifier
     * @since 1.0
     */
    public void writeIdentifier(@NonNull Key identifier) {
        this.writeString(identifier.asString());
    }

    /**
     * Reads a byte array from the buffer.
     *
     * @return the byte array
     * @throws IllegalArgumentException if an amount of remaining bytes is lower than a string size
     * @since 1.0
     */
    public byte @NonNull [] readByteArray() {
        return this.readByteArray(true);
    }

    /**
     * Writes a byte array to the buffer.
     *
     * @param value the byte array
     * @since 1.0
     */
    public void writeByteArray(byte @NonNull [] value) {
        this.writeByteArray(value, true);
    }

    /**
     * Writes a byte array to the buffer.
     *
     * @param writeLength whether a length of the array should be written
     * @param value the byte array
     * @since 1.0
     */
    public void writeByteArray(byte @NonNull [] value, boolean writeLength) {
        if (writeLength) {
            this.writeVarInt(value.length);
        }

        for (byte element : value) {
            this.writeByte(element);
        }
    }

    /**
     * Reads an optional string from the buffer.
     *
     * @return the optional string, which may be null
     * @since 1.0
     */
    public @Nullable String readOptionalString() {
        if (this.readBoolean()) return this.readString();
        return null;
    }


    /**
     * Writes an optional string to the buffer.
     *
     * @param value the optional string, which may be null
     * @since 1.0
     */
    public void writeOptionalString(@Nullable String value) {
        boolean isPresent = value != null;
        this.writeBoolean(isPresent);
        if (isPresent) this.writeString(value);
    }

    /**
     * Reads a collection of {@linkplain T objects} from the buffer with a {@linkplain NetworkCodec network codec}.
     *
     * @param codec the network codec
     * @return the collection
     * @param <T> a type of the objects
     * @since 1.0
     */
    public <T> @NonNull Collection<T> readCollection(@NonNull NetworkCodec<T> codec) {
        int size = this.readVarInt();
        List<T> list = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            list.add(codec.read(this));
        }

        return List.copyOf(list);
    }

    /**
     * Writes a collection of {@linkplain T objects} to the buffer with a {@linkplain NetworkCodec network codec}.
     *
     * @param codec the network coded
     * @param collection the collection
     * @param <T> a type of the objects
     * @since 1.0
     */
    public <T> void writeCollection(@NonNull NetworkCodec<T> codec, @NonNull Collection<T> collection) {
        int size = collection.size();
        this.writeVarInt(size);

        if (size > 0) {
            collection.forEach(object -> codec.write(this, object));
        }
    }

    /**
     * Reads a byte array from the buffer. If the {@code readLength} is false, the array size will be based on
     * remaining buffer size.
     *
     * @param readLength whether the length should be read
     * @return the byte array
     * @since 1.0
     */
    public byte @NonNull [] readByteArray(boolean readLength) {
        byte[] array;

        if (readLength) {
            int length = this.readVarInt();
            if (!this.isReadable(length))
                throw new IllegalArgumentException("An amount of remaining bytes is lower than a string size");
            array = new byte[length];
        } else {
            array = new byte[this.buf.readableBytes()];
        }

        this.buf.readBytes(array);
        return array;
    }

    /**
     * Writes another buffer to this buffer.
     *
     * @param another the another buffer
     * @since 1.0
     */
    public void write(@NonNull NetworkBuffer another) {
        this.buf.writeBytes(another.buf);
    }
}
