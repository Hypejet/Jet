package net.hypejet.jet.server.configuration.serializer;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import net.hypejet.jet.data.model.pack.DataPack;
import net.hypejet.jet.data.model.utils.NullabilityUtil;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an {@linkplain ObjectSerializer object serializer}, which deserializes and serializes
 * a {@linkplain DataPack data pack}.
 *
 * @since 1.0
 * @author Codestech
 * @see DataPack
 * @see ObjectSerializer
 */
public final class DataPackSerializer implements ObjectSerializer<DataPack> {

    private static final String KEY_FIELD = "key";
    private static final String VERSION_FIELD = "version";

    @Override
    public boolean supports(@NonNull Class<? super DataPack> type) {
        return DataPack.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull DataPack object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add(KEY_FIELD, object.key());
        data.add(VERSION_FIELD, object.version());
    }

    @Override
    public DataPack deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        return new DataPack(NullabilityUtil.requireNonNull(data.get(KEY_FIELD, Key.class), KEY_FIELD),
                NullabilityUtil.requireNonNull(data.get(VERSION_FIELD, String.class), VERSION_FIELD));
    }
}