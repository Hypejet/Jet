package net.hypejet.jet.server.configuration;

import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.SerdesRegistry;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import net.hypejet.jet.server.configuration.serializer.ComponentObjectSerializer;
import net.hypejet.jet.server.configuration.serializer.DataPackSerializer;
import net.hypejet.jet.server.configuration.serializer.KeySerializer;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain OkaeriSerdesPack okaeri serdes pack}, which contains
 * {@linkplain eu.okaeri.configs.serdes.ObjectSerializer object serializers} and
 * {@linkplain eu.okaeri.configs.serdes.ObjectTransformer object transformers} allowing for serialization
 * of the {@linkplain JetServerConfiguration Jet server configuration}.
 *
 * @since 1.0
 * @author Codestech
 * @see JetServerConfiguration
 * @see OkaeriSerdesPack
 * @see eu.okaeri.configs.serdes.ObjectSerializer
 * @see eu.okaeri.configs.serdes.ObjectTransformer
 */
public final class JetConfigurationSerdes implements OkaeriSerdesPack {

    /**
     * Constructs the {@linkplain JetConfigurationSerdes Jet configuration serdes}.
     *
     * @since 1.0
     */
    JetConfigurationSerdes() {}

    @Override
    public void register(@NonNull SerdesRegistry registry) {
        registry.register(new SerdesCommons());
        registry.register(new ComponentObjectSerializer());
        registry.register(new DataPackSerializer());
        registry.register(new KeySerializer());
    }
}
