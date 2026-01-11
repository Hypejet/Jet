package net.hypejet.jet.server.network.codec.packet.server.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.codec.other.UUIDNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.login.ServerLoginSuccessLoginPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.util.NetworkUtil;
import net.hypejet.jet.session.login.profile.GameProfileProperty;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerLoginSuccessLoginPacket a login success login packet}.
 *
 * @since 1.0
 * @see ServerLoginSuccessLoginPacket
 * @see NetworkWriter
 */
public final class ServerLoginSuccessLoginPacketWriter implements NetworkWriter<ServerLoginSuccessLoginPacket> {

    /**
     * An instance of the {@linkplain ServerLoginSuccessLoginPacketWriter server login success login packet writer}.
     *
     * @since 1.0
     */
    public static final ServerLoginSuccessLoginPacketWriter INSTANCE = new ServerLoginSuccessLoginPacketWriter();

    private static final CollectionNetworkWriter<GameProfileProperty>
            GAME_PROFILE_PROPERTIES_WRITER = new CollectionNetworkWriter<>(new GameProfilePropertyWriter());

    private ServerLoginSuccessLoginPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager,
                      @NonNull ServerLoginSuccessLoginPacket object) {
        UUIDNetworkCodec.INSTANCE.write(buf, registryManager, object.uniqueId());
        StringNetworkCodec.MAX_16_INSTANCE.write(buf, registryManager, object.username());
        GAME_PROFILE_PROPERTIES_WRITER.write(buf, registryManager, object.properties());
    }

    /**
     * Represents {@linkplain NetworkWriter a network writer}, which writes
     * {@linkplain GameProfileProperty a game profile property}.
     *
     * @since 1.0
     * @see GameProfileProperty
     * @see NetworkWriter
     */
    private static final class GameProfilePropertyWriter implements NetworkWriter<GameProfileProperty> {
        @Override
        public void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager,
                          @NonNull GameProfileProperty object) {
            StringNetworkCodec.INSTANCE.write(buf, registryManager, object.name());
            StringNetworkCodec.INSTANCE.write(buf, registryManager, object.value());
            NetworkUtil.writeOptional(object.signature(), StringNetworkCodec.INSTANCE, buf, registryManager);
        }
    }
}