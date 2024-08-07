package net.hypejet.jet.server.network.protocol.codecs.enums;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.identifier.IdentifierNetworkCodec;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes enum entries from/to an identifier.
 *
 * @param <E> a type of the enum entries
 * @since 1.0
 * @author Codestech
 * @see Enum
 * @see Key
 * @see NetworkCodec
 */
public final class EnumIdentifierCodec<E extends Enum<E>> implements NetworkCodec<E> {

    private final EnumMap<E, Key> enumToIdentifierMap;
    private final Map<Key, E> identifierToEnumMap = new HashMap<>();

    private EnumIdentifierCodec(@NonNull EnumMap<E, Key> enumToIdentifierMap) {
        this.enumToIdentifierMap = new EnumMap<>(enumToIdentifierMap);
        this.enumToIdentifierMap.forEach((enumEntry, key) -> this.identifierToEnumMap.put(key, enumEntry));
    }

    @Override
    public @NonNull E read(@NonNull ByteBuf buf) {
        Key key = IdentifierNetworkCodec.instance().read(buf);
        E enumEntry = this.identifierToEnumMap.get(key);

        if (enumEntry == null)
            throw new IllegalArgumentException("Could not find an enum entry for: " + key);

        return enumEntry;
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull E object) {
        Key key = this.enumToIdentifierMap.get(object);
        if (key == null)
            throw new IllegalArgumentException("Could not find a key for: " + object);
        IdentifierNetworkCodec.instance().write(buf, key);
    }

    /**
     * Creates an {@linkplain Builder enum identifier codec builder}.
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
     * Represents a builder of {@linkplain EnumIdentifierCodec enum identifier codec}.
     *
     * @param <E> a type of the enum
     * @since 1.0
     * @see EnumIdentifierCodec
     */
    public static final class Builder<E extends Enum<E>> {

        private final EnumMap<E, Key> enumToIdentifierMap;

        private Builder(@NonNull Class<E> enumClass) {
            this.enumToIdentifierMap = new EnumMap<>(enumClass);
        }

        /**
         * Registers an enum entry and an identifier of it.
         *
         * @param enumEntry the enum entry
         * @param identifier the identifier
         * @return this builder
         * @since 1.0
         */
        public @NonNull Builder<E> add(@NonNull E enumEntry, @NonNull Key identifier) {
            this.enumToIdentifierMap.put(enumEntry, identifier);
            return this;
        }

        /**
         * Builds an {@linkplain EnumIdentifierCodec enum identifier codec} using values registered in this builder.
         *
         * @return the codec
         * @since 1.0
         */
        public @NonNull EnumIdentifierCodec<E> build() {
            return new EnumIdentifierCodec<>(this.enumToIdentifierMap);
        }
    }
}