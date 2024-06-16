package net.hypejet.jet.server.network.protocol.codecs.profile;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.properties.GameProfileProperties;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain GameProfileProperties
 * game profile properties}.
 *
 * @since 1.0
 * @author Codestech
 * @see GameProfileProperties
 * @see NetworkCodec
 */
public final class GameProfilePropertiesCodec implements NetworkCodec<GameProfileProperties> {

    private static final GameProfilePropertiesCodec INSTANCE = new GameProfilePropertiesCodec();

    private GameProfilePropertiesCodec() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public @NonNull GameProfileProperties read(@NonNull ByteBuf buf) {
        return new GameProfileProperties(
                NetworkUtil.readUniqueId(buf),
                NetworkUtil.readString(buf),
                buf.readBoolean() ? NetworkUtil.readString(buf) : null
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull GameProfileProperties object) {
        NetworkUtil.writeUniqueId(buf, object.uniqueId());
        NetworkUtil.writeString(buf, object.username());

        String signature = object.signature();
        buf.writeBoolean(signature != null);

        if (signature != null) {
            NetworkUtil.writeString(buf, signature);
        }
    }

    /**
     * Gets an instance of the {@linkplain GameProfilePropertiesCodec game profile properties codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull GameProfilePropertiesCodec instance() {
        return INSTANCE;
    }
}
