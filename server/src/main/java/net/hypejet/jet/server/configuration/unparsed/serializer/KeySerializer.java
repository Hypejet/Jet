package net.hypejet.jet.server.configuration.unparsed.serializer;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ObjectSerializer an object serializer}, which deserializes and serializes
 * {@linkplain Key a key}.
 *
 * @since 1.0
 * @see Key
 * @see ObjectSerializer
 */
public final class KeySerializer implements ObjectSerializer<Key> {
    @Override
    public boolean supports(@NonNull Class<? super Key> type) {
        return Key.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull Key object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.setValue(object.asString());
    }

    @Override
    public Key deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        return Key.key(NullabilityUtil.requireNonNull(data.getValue(String.class), "value"));
    }
}