package net.hypejet.jet.server.network.codec.game.settings;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.data.codecs.util.mapper.Mapper;
import net.hypejet.jet.entity.Entity.Hand;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.entity.player.Player.ChatMode;
import net.hypejet.jet.entity.player.Player.ParticleStatus;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.mapper.MapperNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads {@linkplain Player.Settings player settings}.
 *
 * @since 1.0
 * @author Codestech
 * @see Player.Settings
 * @see NetworkReader
 */
public final class PlayerSettingsReader implements NetworkReader<Player.Settings> {

    private static final MapperNetworkCodec<ChatMode, Integer> CHAT_MODE_CODEC = new MapperNetworkCodec<>(
            Mapper.builder(ChatMode.class, int.class)
                    .register(Player.ChatMode.ENABLED, 0)
                    .register(Player.ChatMode.COMMANDS_ONLY, 1)
                    .register(Player.ChatMode.HIDDEN, 2)
                    .build(),
            VarIntNetworkCodec.INSTANCE
    );

    private static final MapperNetworkCodec<Hand, Integer> HAND_CODEC = new MapperNetworkCodec<>(
            Mapper.builder(Hand.class, int.class)
                    .register(Hand.LEFT, 0)
                    .register(Hand.RIGHT, 1)
                    .build(),
            VarIntNetworkCodec.INSTANCE
    );

    private static final MapperNetworkCodec<ParticleStatus, Integer> PARTICLE_STATUS_CODEC = new MapperNetworkCodec<>(
            Mapper.builder(ParticleStatus.class, int.class)
                    .register(ParticleStatus.ALL, 0)
                    .register(ParticleStatus.DECREASED, 1)
                    .register(ParticleStatus.MINIMAL, 2)
                    .build(),
            VarIntNetworkCodec.INSTANCE
    );

    /**
     * An instance of {@linkplain PlayerSettingsReader a player settings reader}.
     *
     * @since 1.0
     */
    public static final PlayerSettingsReader INSTANCE = new PlayerSettingsReader();

    private PlayerSettingsReader() {}

    @Override
    public Player.@NonNull Settings read(@NonNull ByteBuf buf) {
        return new Player.Settings(
                LocaleNetworkReader.INSTANCE.read(buf), buf.readByte(), CHAT_MODE_CODEC.read(buf), buf.readBoolean(),
                SkinPartCollectionNetworkReader.INSTANCE.read(buf), HAND_CODEC.read(buf), buf.readBoolean(),
                buf.readBoolean(), PARTICLE_STATUS_CODEC.read(buf)
        );
    }
}