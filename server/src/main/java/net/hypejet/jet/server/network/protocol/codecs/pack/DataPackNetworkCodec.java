package net.hypejet.jet.server.network.protocol.codecs.pack;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.pack.DataPack;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain DataPack data pack}.
 *
 * @since 1.0
 * @author Codestech
 * @see DataPack
 * @see NetworkCodec
 */
public final class DataPackNetworkCodec implements NetworkCodec<DataPack> {

    private static final DataPackNetworkCodec INSTANCE = new DataPackNetworkCodec();

    private DataPackNetworkCodec() {}

    @Override
    public @NonNull DataPack read(@NonNull ByteBuf buf) {
        return new DataPack(NetworkUtil.readString(buf), NetworkUtil.readString(buf), NetworkUtil.readString(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull DataPack object) {
        NetworkUtil.writeString(buf, object.namespace());
        NetworkUtil.writeString(buf, object.identifier());
        NetworkUtil.writeString(buf, object.version());
    }

    /**
     * Gets an instance of the {@linkplain DataPackNetworkCodec data pack network codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull DataPackNetworkCodec instance() {
        return INSTANCE;
    }
}