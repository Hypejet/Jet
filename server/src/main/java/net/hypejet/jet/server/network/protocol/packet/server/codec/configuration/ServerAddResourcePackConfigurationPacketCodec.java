package net.hypejet.jet.server.network.protocol.packet.server.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerAddResourcePackConfigurationPacket;
import net.hypejet.jet.server.network.protocol.codecs.component.ComponentNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.UUIDNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ServerAddResourcePackConfigurationPacket add resource pack configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerAddResourcePackConfigurationPacket
 * @see PacketCodec
 */
public final class ServerAddResourcePackConfigurationPacketCodec
        extends PacketCodec<ServerAddResourcePackConfigurationPacket> {

    private static final StringNetworkCodec HASH_CODEC = StringNetworkCodec.create(40);

    /**
     * Constructs the {@linkplain ServerAddResourcePackConfigurationPacket add resource pack configuration packet}.
     *
     * @since 1.0
     */
    public ServerAddResourcePackConfigurationPacketCodec() {
        super(ServerPacketIdentifiers.CONFIGURATION_ADD_RESOURCE_PACK, ServerAddResourcePackConfigurationPacket.class);
    }

    @Override
    public @NonNull ServerAddResourcePackConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ServerAddResourcePackConfigurationPacket(UUIDNetworkCodec.instance().read(buf),
                StringNetworkCodec.instance().read(buf), HASH_CODEC.read(buf),
                buf.readBoolean(), buf.readBoolean() ? ComponentNetworkCodec.instance().read(buf) : null
        );
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerAddResourcePackConfigurationPacket object) {
        UUIDNetworkCodec.instance().write(buf, object.uniqueId());
        StringNetworkCodec.instance().write(buf, object.url());

        HASH_CODEC.write(buf, object.hash());
        buf.writeBoolean(object.forced());

        Component prompt = object.prompt();
        buf.writeBoolean(prompt != null);

        if (prompt != null) {
            ComponentNetworkCodec.instance().write(buf, object.prompt());
        }
    }
}