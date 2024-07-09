package net.hypejet.jet.server.network.protocol.codecs.enums;

import io.netty.buffer.ByteBuf;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.EnumMap;
import java.util.Objects;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads an enum from a variable-length integer and writes
 * it to the variable-length integer.
 *
 * @param <E> a type of the enum
 * @since 1.0
 * @author Codestech
 * @see Builder
 * @see NetworkUtil#readVarInt(ByteBuf)
 * @see NetworkUtil#writeVarInt(ByteBuf, int)
 */
public final class EnumVarIntCodec<E extends Enum<E>> implements NetworkCodec<E> {

    private final EnumMap<E, Integer> enumToIntMap;
    private final IntObjectMap<E> intToEnumMap;

    private EnumVarIntCodec(@NonNull Class<E> enumClass, @NonNull IntObjectMap<E> entries) {
        this.enumToIntMap = new EnumMap<>(enumClass);
        this.intToEnumMap = entries;
        entries.forEach((key, value) -> this.enumToIntMap.put(value, key));
    }

    @Override
    public @NonNull E read(@NonNull ByteBuf buf) {
        Objects.requireNonNull(buf, "The byte buf must not be null");

        int enumEntryId = NetworkUtil.readVarInt(buf);
        E enumEntry = this.intToEnumMap.get(enumEntryId);

        if (enumEntry == null)
            throw new IllegalArgumentException("Could not find an enum entry with identifier of: " + enumEntryId);

        return enumEntry;
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull E object) {
        Objects.requireNonNull(buf, "The byte buf must not be null");
        Objects.requireNonNull(object, "The object must not be null");

        Integer enumEntryId = this.enumToIntMap.get(object);
        if (enumEntryId == null)
            throw new IllegalArgumentException("Could not find an identifier for an enum entry of: " + object);

        NetworkUtil.writeVarInt(buf, enumEntryId);
    }

    /**
     * Creates a builder of the {@linkplain EnumVarIntCodec enum var-int codec}.
     *
     * @param <E> a type of the enum that the codec should encode and decode
     * @return the builder
     * @since 1.0
     */
    public static <E extends Enum<E>> @NonNull Builder<E> builder(@NonNull Class<E> enumClass) {
        return new Builder<>(enumClass);
    }

    /**
     * Represents a builder of the {@linkplain EnumVarIntCodec enum variable-length integer codec}.
     *
     * @param <E> a type of the enum that the codec should encode and decode
     * @since 1.0
     * @author Codestech
     * @see EnumVarIntCodec
     */
    public static class Builder<E extends Enum<E>> {

        private final Class<E> enumClass;
        private final IntObjectMap<E> map = new IntObjectHashMap<>();

        private Builder(@NonNull Class<E> enumClass) {
            this.enumClass = enumClass;
        }

        /**
         * Adds an enum entry and integer representation of it.
         *
         * @param entry the enum entry
         * @param integer the integer representation the enum entry
         * @return this builder
         * @since 1.0
         */
        public @NonNull Builder<E> add(@NonNull E entry, int integer) {
            this.map.put(integer, entry);
            return this;
        }

        /**
         * Builds the {@linkplain EnumVarIntCodec enum variable-length integer codec}.
         *
         * @return the codec
         * @since 1.0
         */
        public @NonNull EnumVarIntCodec<E> build() {
            return new EnumVarIntCodec<>(this.enumClass, this.map);
        }
    }
}