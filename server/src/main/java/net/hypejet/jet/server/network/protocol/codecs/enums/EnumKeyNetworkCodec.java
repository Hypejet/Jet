package net.hypejet.jet.server.network.protocol.codecs.enums;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.key.PackedKeyNetworkCodec;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents {@linkplain NetworkCodec a network codec}, which reads and writes enum entries
 * from/to {@linkplain Key a key}.
 *
 * @param <E> a type of the enum entries
 * @since 1.0
 * @author Codestech
 * @see Enum
 * @see Key
 * @see NetworkCodec
 */
public final class EnumKeyNetworkCodec<E extends Enum<E>> implements NetworkCodec<E> {

    private final EnumMap<E, Key> enumToKeyMap;
    private final Map<Key, E> keyToEnumMap = new HashMap<>();

    private EnumKeyNetworkCodec(@NonNull EnumMap<E, Key> enumToKeyMap) {
        this.enumToKeyMap = new EnumMap<>(enumToKeyMap);
        this.enumToKeyMap.forEach((enumEntry, key) -> this.keyToEnumMap.put(key, enumEntry));
    }

    @Override
    public @NonNull E read(@NonNull ByteBuf buf) {
        Key key = PackedKeyNetworkCodec.INSTANCE.read(buf);
        E enumEntry = this.keyToEnumMap.get(key);

        if (enumEntry == null)
            throw new IllegalArgumentException("Could not find an enum entry for: " + key);

        return enumEntry;
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull E object) {
        Key key = this.enumToKeyMap.get(object);
        if (key == null)
            throw new IllegalArgumentException("Could not find a key for: " + object);
        PackedKeyNetworkCodec.INSTANCE.write(buf, key);
    }

    /**
     * Creates a builder of the {@linkplain EnumKeyNetworkCodec enum key network codec}.
     *
     * @param enumClass a class of the enum of the enum entries that the codec should read and write
     * @return the builder
     * @param <E> a type of the enum of the enum entries that the codec should read and write
     * @since 1.0
     */
    public static <E extends Enum<E>> @NonNull Builder<E> builder(@NonNull Class<E> enumClass) {
        return new Builder<>(enumClass);
    }

    /**
     * Represents a builder of the {@linkplain EnumKeyNetworkCodec enum key network codec}.
     *
     * @param <E> a type of the enum
     * @since 1.0
     * @see EnumKeyNetworkCodec
     */
    public static final class Builder<E extends Enum<E>> {

        private final EnumMap<E, Key> enumToKeyMap;

        private Builder(@NonNull Class<E> enumClass) {
            this.enumToKeyMap = new EnumMap<>(enumClass);
        }

        /**
         * Registers an enum entry and a key of it.
         *
         * @param enumEntry the enum entry
         * @param key the key
         * @return this builder
         * @since 1.0
         */
        public @NonNull Builder<E> add(@NonNull E enumEntry, @NonNull Key key) {
            this.enumToKeyMap.put(enumEntry, key);
            return this;
        }

        /**
         * Builds {@linkplain EnumKeyNetworkCodec an enum key network codec} using values registered in this builder.
         *
         * @return the codec
         * @since 1.0
         */
        public @NonNull EnumKeyNetworkCodec<E> build() {
            return new EnumKeyNetworkCodec<>(this.enumToKeyMap);
        }
    }
}