package net.hypejet.jet.server.network.protocol.codecs.settings;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Locale;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain Locale locale}.
 *
 * @since 1.0
 * @author Codestech
 * @see Locale
 * @see NetworkCodec
 */
public final class LocaleNetworkCodec implements NetworkCodec<Locale> {

    private static final String LOCALE_TAG_DELIMITER = "-";
    private static final String MOJANG_LOCALE_TAG_DELIMITER = "_";

    private static final StringNetworkCodec LOCALE_CODEC = StringNetworkCodec.create(16);
    private static final LocaleNetworkCodec INSTANCE = new LocaleNetworkCodec();

    private LocaleNetworkCodec() {}

    @Override
    public @NonNull Locale read(@NonNull ByteBuf buf) {
        return Locale.forLanguageTag(LOCALE_CODEC.read(buf)
                .replace(MOJANG_LOCALE_TAG_DELIMITER, LOCALE_TAG_DELIMITER));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Locale object) {
        LOCALE_CODEC.write(buf, object.toLanguageTag().replace(LOCALE_TAG_DELIMITER, MOJANG_LOCALE_TAG_DELIMITER));
    }

    /**
     * Gets an instance of {@linkplain LocaleNetworkCodec locale network codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull LocaleNetworkCodec instance() {
        return INSTANCE;
    }
}