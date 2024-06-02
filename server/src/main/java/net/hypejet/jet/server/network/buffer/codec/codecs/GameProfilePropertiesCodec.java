package net.hypejet.jet.server.network.buffer.codec.codecs;

import net.hypejet.jet.player.profile.properties.GameProfileProperties;
import net.hypejet.jet.server.network.buffer.NetworkBuffer;
import net.hypejet.jet.server.network.buffer.codec.NetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which writes or reads a {@linkplain GameProfileProperties game
 * profile properties}.
 *
 * @since 1.0
 * @author Codestech
 * @see GameProfileProperties
 * @see NetworkCodec
 */
public final class GameProfilePropertiesCodec implements NetworkCodec<GameProfileProperties> {

    private static final GameProfilePropertiesCodec INSTANCE = new GameProfilePropertiesCodec();

    private GameProfilePropertiesCodec() {}

    @Override
    public void write(@NonNull NetworkBuffer buffer, @NonNull GameProfileProperties object) {
        buffer.writeUniqueId(object.uniqueId());
        buffer.writeString(object.username());
        buffer.writeOptionalString(object.signature());
    }

    @Override
    public @NonNull GameProfileProperties read(@NonNull NetworkBuffer buffer) {
        return GameProfileProperties.builder()
                .uniqueId(buffer.readUniqueId())
                .username(buffer.readString())
                .signature(buffer.readOptionalString())
                .build();
    }

    /**
     * Gets an instance of {@linkplain GameProfilePropertiesCodec game profile properties codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull GameProfilePropertiesCodec instance() {
        return INSTANCE;
    }
}
