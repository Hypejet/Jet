package net.hypejet.jet.server.network.protocol.codecs.settings;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.entity.Entity.Hand;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.entity.player.Player.ChatMode;
import net.hypejet.jet.entity.player.Player.ParticleStatus;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.protocol.codecs.enums.EnumVarIntNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Locale;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads {@linkplain Player.Settings player settings}.
 *
 * @since 1.0
 * @author Codestech
 * @see Player.Settings
 * @see NetworkReader
 */
public final class PlayerSettingsReader implements NetworkReader<Player.Settings> {

    private static final EnumVarIntNetworkCodec<ChatMode> CHAT_MODE_CODEC = EnumVarIntNetworkCodec.builder(ChatMode.class)
            .add(Player.ChatMode.ENABLED, 0)
            .add(Player.ChatMode.COMMANDS_ONLY, 1)
            .add(Player.ChatMode.HIDDEN, 2)
            .build();

    private static final EnumVarIntNetworkCodec<Hand> HAND_CODEC = EnumVarIntNetworkCodec.builder(Hand.class)
            .add(Hand.LEFT, 0)
            .add(Hand.RIGHT, 1)
            .build();

    private static final EnumVarIntNetworkCodec<ParticleStatus> PARTICLE_STATUS_CODEC = EnumVarIntNetworkCodec
            .builder(ParticleStatus.class)
            .add(ParticleStatus.ALL, 0)
            .add(ParticleStatus.DECREASED, 1)
            .add(ParticleStatus.MINIMAL, 2)
            .build();

    /**
     * An instance of {@linkplain PlayerSettingsReader a player settings reader}.
     *
     * @since 1.0
     */
    public static final PlayerSettingsReader INSTANCE = new PlayerSettingsReader();

    private PlayerSettingsReader() {}

    @Override
    public Player.@NonNull Settings read(@NonNull ByteBuf buf) {
        Locale locale = LocaleNetworkReader.INSTANCE.read(buf);

        byte viewDistance = buf.readByte();
        ChatMode chatMode = CHAT_MODE_CODEC.read(buf);
        boolean chatColorsEnabled = buf.readBoolean();

        Collection<Player.SkinPart> skinParts = SkinPartCollectionNetworkReader.INSTANCE.read(buf);

        Hand hand = HAND_CODEC.read(buf);
        boolean enableTextFiltering = buf.readBoolean();
        boolean allowServerListings = buf.readBoolean();

        ParticleStatus particleStatus = PARTICLE_STATUS_CODEC.read(buf);

        return new Player.Settings(locale, viewDistance, chatMode, chatColorsEnabled, skinParts, hand,
                enableTextFiltering, allowServerListings, particleStatus);
    }
}