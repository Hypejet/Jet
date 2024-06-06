package net.hypejet.jet.server.configuration.serializer;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ObjectSerializer object serializer} allowing for serialization
 * of {@linkplain Component components}.
 *
 * @since 1.0
 * @author Codestech
 * @see Component
 * @see ObjectSerializer
 */
public final class ComponentObjectSerializer implements ObjectSerializer<Component> {

    private static final MiniMessage SERIALIZER = MiniMessage.miniMessage();

    @Override
    public boolean supports(@NonNull Class<? super Component> type) {
        return Component.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull Component object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.setValue(SERIALIZER.serialize(object));
    }

    @Override
    public Component deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        return SERIALIZER.deserialize(data.getValue(String.class));
    }
}