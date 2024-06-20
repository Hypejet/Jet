package net.hypejet.jet.server.network.protocol.packet.client.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.protocol.packet.client.configuration.ClientInformationConfigurationPacket;
import net.hypejet.jet.server.network.protocol.codecs.settings.ChatModeNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.settings.HandNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.settings.LocaleNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.settings.SkinPartCollectionNetworkCodec;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.client.codec.ClientPacketCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Locale;

/**
 * Represents a {@linkplain ClientPacketCodec packet codec}, which reads and writes
 * a {@linkplain ClientInformationConfigurationPacket client information configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientInformationConfigurationPacket
 * @see ClientPacketCodec
 */
public final class ClientInformationConfigurationPacketCodec
        extends ClientPacketCodec<ClientInformationConfigurationPacket> {
    /**
     * Constructs a {@linkplain ClientInformationConfigurationPacketCodec client information configuration packet
     * codec}.
     *
     * @since 1.0
     */
    public ClientInformationConfigurationPacketCodec() {
        super(ClientPacketIdentifiers.CONFIGURATION_CLIENT_INFORMATION, ClientInformationConfigurationPacket.class);
    }

    @Override
    public @NonNull ClientInformationConfigurationPacket read(@NonNull ByteBuf buf) {
        Locale locale = LocaleNetworkCodec.instance().read(buf);

        byte viewDistance = buf.readByte();
        Player.ChatMode chatMode = ChatModeNetworkCodec.instance().read(buf);
        boolean chatColorsEnabled = buf.readBoolean();

        Collection<Player.SkinPart> skinParts = SkinPartCollectionNetworkCodec.instance().read(buf);

        Entity.Hand hand = HandNetworkCodec.instance().read(buf);
        boolean enableTextFiltering = buf.readBoolean();
        boolean allowServerListings = buf.readBoolean();

        return new ClientInformationConfigurationPacket(locale, viewDistance, chatMode, chatColorsEnabled, skinParts,
                hand, enableTextFiltering, allowServerListings);
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientInformationConfigurationPacket object) {
        LocaleNetworkCodec.instance().write(buf, object.locale());
        buf.writeByte(object.viewDistance());
        ChatModeNetworkCodec.instance().write(buf, object.chatMode());
        buf.writeBoolean(object.chatColorsEnabled());
        SkinPartCollectionNetworkCodec.instance().write(buf, object.enabledSkinParts());
        HandNetworkCodec.instance().write(buf, object.mainHand());
        buf.writeBoolean(object.textFilteringEnabled());
        buf.writeBoolean(object.allowServerListings());
    }

    @Override
    public void handle(@NonNull ClientInformationConfigurationPacket packet,
                       @NonNull SocketPlayerConnection connection) {
        // TODO
    }
}