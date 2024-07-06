package net.hypejet.jet.server.network.protocol.codecs.settings;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.Entity.Hand;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.entity.player.Player.ChatMode;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.enums.EnumVarIntCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Locale;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain Player.Settings player
 * settings}.
 *
 * @since 1.0
 * @author Codestech
 * @see Player.Settings
 * @see NetworkCodec
 */
public final class PlayerSettingsCodec implements NetworkCodec<Player.Settings> {

    private static final PlayerSettingsCodec INSTANCE = new PlayerSettingsCodec();

    private static final EnumVarIntCodec<ChatMode> chatModeCodec = EnumVarIntCodec.builder(ChatMode.class)
            .add(Player.ChatMode.ENABLED, 0)
            .add(Player.ChatMode.COMMANDS_ONLY, 1)
            .add(Player.ChatMode.HIDDEN, 2)
            .build();

    private static final EnumVarIntCodec<Hand> handCodec = EnumVarIntCodec.builder(Hand.class)
            .add(Hand.LEFT, 0)
            .add(Hand.RIGHT, 1)
            .build();

    private PlayerSettingsCodec() {}

    @Override
    public Player.@NonNull Settings read(@NonNull ByteBuf buf) {
        Locale locale = LocaleNetworkCodec.instance().read(buf);

        byte viewDistance = buf.readByte();
        ChatMode chatMode = chatModeCodec.read(buf);
        boolean chatColorsEnabled = buf.readBoolean();

        Collection<Player.SkinPart> skinParts = SkinPartCollectionNetworkCodec.instance().read(buf);

        Hand hand = handCodec.read(buf);
        boolean enableTextFiltering = buf.readBoolean();
        boolean allowServerListings = buf.readBoolean();

        return new Player.Settings(locale, viewDistance, chatMode, chatColorsEnabled, skinParts, hand,
                enableTextFiltering, allowServerListings);
    }

    @Override
    public void write(@NonNull ByteBuf buf, Player.@NonNull Settings object) {
        LocaleNetworkCodec.instance().write(buf, object.locale());
        buf.writeByte(object.viewDistance());
        chatModeCodec.write(buf, object.chatMode());
        buf.writeBoolean(object.chatColorsEnabled());
        SkinPartCollectionNetworkCodec.instance().write(buf, object.enabledSkinParts());
        handCodec.write(buf, object.mainHand());
        buf.writeBoolean(object.textFilteringEnabled());
        buf.writeBoolean(object.allowServerListings());
    }

    /**
     * Gets an instance of the {@linkplain PlayerSettingsCodec player settings codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull PlayerSettingsCodec instance() {
        return INSTANCE;
    }
}