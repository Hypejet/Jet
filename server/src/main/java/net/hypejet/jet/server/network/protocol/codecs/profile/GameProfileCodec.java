package net.hypejet.jet.server.network.protocol.codecs.profile;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.profile.GameProfile;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain GameProfile game profile}.
 *
 * @since 1.0
 * @author Codestech
 * @see GameProfile
 * @see NetworkCodec
 */
public final class GameProfileCodec implements NetworkCodec<GameProfile> {

    private static final GameProfileCodec INSTANCE = new GameProfileCodec();

    private GameProfileCodec() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public @NonNull GameProfile read(@NonNull ByteBuf buf) {
        return new GameProfile(
                NetworkUtil.readUniqueId(buf),
                NetworkUtil.readString(buf),
                buf.readBoolean() ? NetworkUtil.readString(buf) : null
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull GameProfile object) {
        NetworkUtil.writeUniqueId(buf, object.uniqueId());
        NetworkUtil.writeString(buf, object.username());

        String signature = object.signature();
        buf.writeBoolean(signature != null);

        if (signature != null) {
            NetworkUtil.writeString(buf, signature);
        }
    }

    /**
     * Gets an instance of the {@linkplain GameProfileCodec game profile properties codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull GameProfileCodec instance() {
        return INSTANCE;
    }
}
