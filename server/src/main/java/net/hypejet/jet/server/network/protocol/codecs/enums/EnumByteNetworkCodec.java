package net.hypejet.jet.server.network.protocol.codecs.enums;

import io.netty.buffer.ByteBuf;
import io.netty.util.collection.ByteObjectHashMap;
import io.netty.util.collection.ByteObjectMap;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.EnumMap;
import java.util.Objects;

/**
 * Represents {@linkplain NetworkCodec a network codec}, which reads/writes an enum from/to a byte.
 *
 * @param <E> a type of the enum
 * @since 1.0
 * @author Codestech
 * @see Builder
 */
public final class EnumByteNetworkCodec<E extends Enum<E>> implements NetworkCodec<E> {

    private final EnumMap<E, Byte> enumToByteMap;
    private final ByteObjectMap<E> byteToEnumMap;

    private EnumByteNetworkCodec(@NonNull Class<E> enumClass, @NonNull ByteObjectMap<E> entries) {
        this.enumToByteMap = new EnumMap<>(enumClass);
        this.byteToEnumMap = entries;
        entries.forEach((key, value) -> this.enumToByteMap.put(value, key));
    }

    @Override
    public @NonNull E read(@NonNull ByteBuf buf) {
        Objects.requireNonNull(buf, "The byte buf must not be null");

        byte enumEntryId = buf.readByte();
        E enumEntry = this.byteToEnumMap.get(enumEntryId);

        if (enumEntry == null)
            throw new IllegalArgumentException("Could not find an enum entry with identifier of: " + enumEntryId);

        return enumEntry;
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull E object) {
        Objects.requireNonNull(buf, "The byte buf must not be null");
        Objects.requireNonNull(object, "The object must not be null");

        Byte enumEntryId = this.enumToByteMap.get(object);
        if (enumEntryId == null)
            throw new IllegalArgumentException("Could not find an identifier for an enum entry of: " + object);

        buf.writeByte(enumEntryId);
    }

    /**
     * Creates a builder of the {@linkplain EnumByteNetworkCodec enum byte network codec}.
     *
     * @param <E> a type of the enum that the codec should encode and decode
     * @return the builder
     * @since 1.0
     */
    public static <E extends Enum<E>> @NonNull Builder<E> builder(@NonNull Class<E> enumClass) {
        return new Builder<>(enumClass);
    }

    /**
     * Represents a builder of the {@linkplain EnumByteNetworkCodec enum byte network codec}.
     *
     * @param <E> a type of the enum that the codec should encode and decode
     * @since 1.0
     * @author Codestech
     * @see EnumByteNetworkCodec
     */
    public static class Builder<E extends Enum<E>> {

        private final Class<E> enumClass;
        private final ByteObjectMap<E> map = new ByteObjectHashMap<>();

        private Builder(@NonNull Class<E> enumClass) {
            this.enumClass = enumClass;
        }

        /**
         * Adds an enum entry and byte representation of it.
         *
         * @param entry the enum entry
         * @param value the byte representation of the enum entry
         * @return this builder
         * @since 1.0
         */
        public @NonNull Builder<E> add(@NonNull E entry, byte value) {
            this.map.put(value, entry);
            return this;
        }

        /**
         * Builds {@linkplain EnumByteNetworkCodec an enum byte network codec} using values registered in this builder.
         *
         * @return the codec
         * @since 1.0
         */
        public @NonNull EnumByteNetworkCodec<E> build() {
            return new EnumByteNetworkCodec<>(this.enumClass, this.map);
        }
    }
}