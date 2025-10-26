package net.hypejet.jet.server.network.codec.game.settings;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Locale;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads {@linkplain Locale a locale}.
 *
 * @since 1.0
 * @see Locale
 * @see NetworkReader
 */
public final class LocaleNetworkReader implements NetworkReader<Locale> {

    private static final String LOCALE_TAG_DELIMITER = "-";
    private static final String MOJANG_LOCALE_TAG_DELIMITER = "_";

    private static final StringNetworkCodec LOCALE_CODEC = StringNetworkCodec.MAX_16_INSTANCE;

    /**
     * An instance of the {@linkplain LocaleNetworkReader locale network reader}.
     *
     * @since 1.0
     */
    public static final LocaleNetworkReader INSTANCE = new LocaleNetworkReader();

    private LocaleNetworkReader() {}

    @Override
    public @NonNull Locale read(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager) {
        return Locale.forLanguageTag(
                LOCALE_CODEC.read(buf, registryManager).replace(MOJANG_LOCALE_TAG_DELIMITER, LOCALE_TAG_DELIMITER)
        );
    }
}