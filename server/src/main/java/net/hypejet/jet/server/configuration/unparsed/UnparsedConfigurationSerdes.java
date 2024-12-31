package net.hypejet.jet.server.configuration.unparsed;

import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.SerdesRegistry;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import net.hypejet.jet.server.configuration.unparsed.serializer.KeySerializer;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain OkaeriSerdesPack an okaeri serdes pack}, which contains
 * {@linkplain eu.okaeri.configs.serdes.ObjectSerializer object serializers} and
 * {@linkplain eu.okaeri.configs.serdes.ObjectTransformer object transformers} allowing for serialization
 * of the {@linkplain UnparsedServerConfiguration unparsed server configuration}.
 *
 * @since 1.0
 * @see UnparsedServerConfiguration
 * @see OkaeriSerdesPack
 * @see eu.okaeri.configs.serdes.ObjectSerializer
 * @see eu.okaeri.configs.serdes.ObjectTransformer
 */
public final class UnparsedConfigurationSerdes implements OkaeriSerdesPack {
    @Override
    public void register(@NonNull SerdesRegistry registry) {
        registry.register(new SerdesCommons());
        registry.register(new KeySerializer());
    }
}