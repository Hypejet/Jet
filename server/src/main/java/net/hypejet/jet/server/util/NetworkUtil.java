package net.hypejet.jet.server.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.nbt.BinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.nbt.NBTComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.value.qual.IntRange;
import org.jetbrains.annotations.Range;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * A utility used for serializing data allowed by Minecraft protocol, but unsupported by default
 * via {@link ByteBuf byte buf}.
 *
 * @since 1.0
 * @author Codestech
 */
public final class NetworkUtil {

    private static final byte SEGMENT_BITS = 0x7F;
    private static final int CONTINUE_BIT = 0x80;

    private static final short MAX_STRING_SIZE = 32767;

    private NetworkUtil() {}

    /**
     * Reads a variable-length integer from a {@link ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @return the variable-length integer
     * @throws IllegalArgumentException if the integer is bigger than maximum allowed
     * @since 1.0
     */
    public static int readVarInt(@NonNull ByteBuf buf) {
        int value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = buf.readByte();
            value |= (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;

            if (position >= 32) throw new IllegalArgumentException("VarInt is bigger than maximum allowed");
        }

        return value;
    }

    /**
     * Writes a variable-length integer to a {@link ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @param value the variable-length integer
     * @since 1.0
     */
    public static void writeVarInt(@NonNull ByteBuf buf, int value) {
        while (true) {
            if ((value & ~SEGMENT_BITS) == 0) {
                buf.writeByte(value);
                return;
            }

            buf.writeByte((value & SEGMENT_BITS) | CONTINUE_BIT);
            value >>>= 7;
        }
    }

    /**
     * Reads a variable-length long from a {@link ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @since 1.0
     * @throws IllegalArgumentException if the long is bigger than maximum allowed
     * @return the variable-length long
     */
    public static long readVarLong(@NonNull ByteBuf buf) {
        long value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = buf.readByte();
            value |= (long) (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;

            if (position >= 64) throw new IllegalArgumentException("VarLong is bigger than maximum allowed");
        }

        return value;
    }

    /**
     * Writes a variable-length long to a {@link ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @param value the variable-length long
     * @since 1.0
     */
    public static void writeVarLong(@NonNull ByteBuf buf, long value) {
        while (true) {
            if ((value & ~((long) SEGMENT_BITS)) == 0) {
                buf.writeByte((int) value);
                return;
            }

            buf.writeByte((int) ((value & SEGMENT_BITS) | CONTINUE_BIT));
            value >>>= 7;
        }
    }

    /**
     * Reads a string from a {@link ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @return the string
     * @since 1.0
     */
    public static @NonNull String readString(@NonNull ByteBuf buf) {
        return readString(buf, MAX_STRING_SIZE);
    }

    /**
     * Reads a string from a {@link ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @param maxLength a maximum length of the string
     * @return the string
     * @since 1.0
     */
    public static @NonNull String readString(@NonNull ByteBuf buf, @IntRange(to = MAX_STRING_SIZE) int maxLength) {
        int length = readVarInt(buf);

        if (length < 0 || length > maxLength) {
            throw new IllegalArgumentException("Invalid length of a string - " + length + ". Maximum allowed length" +
                    " is " + maxLength);
        }

        if (!buf.isReadable(length))
            throw new IllegalArgumentException("A buffer does not contain at least " + length + " readable bytes");

        String string = buf.toString(buf.readerIndex(), length, StandardCharsets.UTF_8);
        buf.skipBytes(length);

        return string;
    }

    /**
     * Writes as string to a {@link ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @param value the string
     * @since 1.0
     */
    public static void writeString(@NonNull ByteBuf buf, @NonNull String value) {
        writeVarInt(buf, ByteBufUtil.utf8Bytes(value));
        buf.writeCharSequence(value, StandardCharsets.UTF_8);
    }

    /**
     * Writes as string to a {@link ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @param value the string
     * @param maxLength a maximum length of the string
     * @since 1.0
     */
    public static void writeString(@NonNull ByteBuf buf, @NonNull String value,
                                   @Range(from = 0, to = MAX_STRING_SIZE) int maxLength) {
        int length = ByteBufUtil.utf8Bytes(value);

        if (length > maxLength) {
            throw new IllegalArgumentException("A length of the string - " + length + " - is higher than the " +
                    "maximum length allowed - " + maxLength);
        }

        writeVarInt(buf, length);
        buf.writeCharSequence(value, StandardCharsets.UTF_8);
    }

    /**
     * Reads a {@linkplain Component component} serialized with json from a {@linkplain ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @return the component
     * @since 1.0
     */
    public static @NonNull Component readJsonComponent(@NonNull ByteBuf buf) {
        return GsonComponentSerializer.gson().deserialize(readString(buf));
    }

    /**
     * Writes a json text component to a {@linkplain ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @param value the json text component
     * @since 1.0
     */
    public static void writeJsonComponent(@NonNull ByteBuf buf, @NonNull Component value) {
        writeString(buf, GsonComponentSerializer.gson().serialize(value));
    }

    /**
     * Reads a {@linkplain Component component} serialized to a {@linkplain BinaryTag binary tag} from
     * a {@linkplain ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @return the component
     * @since 1.0
     * @see BinaryTag
     */
    public static @NonNull Component readComponent(@NonNull ByteBuf buf) {
        return NBTComponentSerializer.nbt().deserialize(BinaryTagCodec.instance().read(buf));
    }

    /**
     * Writes a {@linkplain Component component} as a {@linkplain BinaryTag binary tag} to
     * a {@linkplain ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @param value the component
     * @since 1.0
     * @see BinaryTag
     */
    public static void writeComponent(@NonNull ByteBuf buf, @NonNull Component value) {
        BinaryTagCodec.instance().write(buf, NBTComponentSerializer.nbt().serialize(value));
    }

    /**
     * Reads a {@linkplain UUID unique identifier} form a {@linkplain ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @return the unique identifier
     * @since 1.0
     */
    public static @NonNull UUID readUniqueId(@NonNull ByteBuf buf) {
        return new UUID(buf.readLong(), buf.readLong());
    }

    /**
     * Writes a {@linkplain UUID unique identifier} to a {@linkplain ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @param value the unique identifier
     * @since 1.0
     */
    public static void writeUniqueId(@NonNull ByteBuf buf, @NonNull UUID value) {
        buf.writeLong(value.getMostSignificantBits());
        buf.writeLong(value.getLeastSignificantBits());
    }

    /**
     * Reads a {@linkplain Collection collection} for a {@linkplain ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @param codec a codec reading elements of the collection
     * @return the collection
     * @param <T> the type of elements in the collection
     * @since 1.0
     */
    public static <T> @NonNull Collection<T> readCollection(@NonNull ByteBuf buf, @NonNull NetworkCodec<T> codec) {
        int length = readVarInt(buf);
        List<T> list = new ArrayList<>(length);

        for (int i = 0; i < length; i++) {
            list.add(codec.read(buf));
        }

        return List.copyOf(list);
    }

    /**
     * Writes a {@linkplain Collection collection} into a {@linkplain ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @param codec a codec writing elements of the collection
     * @param value the collection
     * @param <T> the type of elements in the collection
     * @since 1.0
     */
    public static <T> void writeCollection(@NonNull ByteBuf buf, @NonNull NetworkCodec<T> codec,
                                           @NonNull Collection<T> value) {
        writeVarInt(buf, value.size());
        value.forEach(element -> codec.write(buf, element));
    }

    /**
     * Reads a byte array from a {@linkplain ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @return the byte array
     * @since 1.0
     */
    public static byte @NonNull [] readByteArray(@NonNull ByteBuf buf) {
        return readByteArray(buf, Integer.MAX_VALUE);
    }

    /**
     * Reads a byte array from a {@linkplain ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @param maxLength a max length of the array allowed
     * @return the byte array
     * @since 1.0
     */
    public static byte @NonNull [] readByteArray(@NonNull ByteBuf buf, int maxLength) {
        int length = readVarInt(buf);

        if (length > maxLength) {
            throw new IllegalArgumentException("The array is longer than maximum allowed " +
                    "(" + length + " > " + maxLength + ").");
        }

        byte[] array = new byte[length];
        buf.readBytes(array);

        return array;
    }

    /**
     * Writes a byte array to a {@linkplain ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @param value the byte array
     * @since 1.0
     */
    public static void writeByteArray(@NonNull ByteBuf buf, byte @NonNull [] value) {
        writeByteArray(buf, value, Integer.MAX_VALUE);
    }

    /**
     * Writes a byte array to a {@linkplain ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @param value the byte array
     * @param maxLength a max length of the array allowed
     * @since 1.0
     */
    public static void writeByteArray(@NonNull ByteBuf buf, byte @NonNull [] value, int maxLength) {
        writeVarInt(buf, value.length);
        buf.writeBytes(value);
    }

    /**
     * Reads a variable-length integer array from a {@linkplain ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @return the integer array
     * @since 1.0
     */
    public static int @NonNull [] readVarIntArray(@NonNull ByteBuf buf) {
        int[] array = new int[readVarInt(buf)];
        for (int index = 0; index < array.length; index++) {
            array[index] = readVarInt(buf);
        }
        return array;
    }

    /**
     * Writes a variable-length integer array to a {@linkplain ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @param value the integer array
     * @since 1.0
     */
    public static void writeVarIntArray(@NonNull ByteBuf buf, int @NonNull [] value) {
        writeVarInt(buf, value.length);
        for (int integer : value) {
            writeVarInt(buf, integer);
        }
    }

    /**
     * Reads all remaining bytes from a {@linkplain ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @return the remaining bytes
     * @since 1.0
     */
    public static byte @NonNull [] readRemainingBytes(@NonNull ByteBuf buf) {
        return readBytes(buf, buf.readableBytes());
    }

    /**
     * Reads bytes from a {@linkplain ByteBuf byte buf}.
     *
     * @param buf the byte buf
     * @param length an amount of bytes to read
     * @return the bytes
     * @since 1.0
     */
    public static byte @NonNull [] readBytes(@NonNull ByteBuf buf, int length) {
        byte[] bytes = new byte[length];
        buf.readBytes(bytes);
        return bytes;
    }
}