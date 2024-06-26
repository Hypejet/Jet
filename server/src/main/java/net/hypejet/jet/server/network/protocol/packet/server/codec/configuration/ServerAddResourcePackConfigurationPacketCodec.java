package net.hypejet.jet.server.network.protocol.packet.server.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerAddResourcePackConfigurationPacket;
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

    private static final int MAX_URL_LENGTH = 32767;
    private static final int MAX_HASH_LENGTH = 40;

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
        return new ServerAddResourcePackConfigurationPacket(
                NetworkUtil.readUniqueId(buf),
                NetworkUtil.readString(buf, MAX_URL_LENGTH),
                NetworkUtil.readString(buf, MAX_HASH_LENGTH),
                buf.readBoolean(),
                buf.readBoolean() ? NetworkUtil.readComponent(buf) : null
        );
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerAddResourcePackConfigurationPacket object) {
        NetworkUtil.writeUniqueId(buf, object.uniqueId());
        NetworkUtil.writeString(buf, object.url(), MAX_URL_LENGTH);
        NetworkUtil.writeString(buf, object.hash(), MAX_HASH_LENGTH);
        buf.writeBoolean(object.forced());

        Component prompt = object.prompt();
        buf.writeBoolean(prompt != null);

        if (prompt != null) {
            NetworkUtil.writeComponent(buf, prompt);
        }
    }
}