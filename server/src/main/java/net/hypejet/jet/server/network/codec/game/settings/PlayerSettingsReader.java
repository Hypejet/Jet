package net.hypejet.jet.server.network.codec.game.settings;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.entity.Entity.Hand;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.entity.player.Player.ChatMode;
import net.hypejet.jet.entity.player.Player.ParticleStatus;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.server.network.codec.index.IndexNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.util.index.IndexUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads {@linkplain Player.Settings player settings}.
 *
 * @since 1.0
 * @see Player.Settings
 * @see NetworkReader
 */
public final class PlayerSettingsReader implements NetworkReader<Player.Settings> {

    private static final IndexNetworkCodec<ChatMode, Integer> CHAT_MODE_CODEC = new IndexNetworkCodec<>(
            IndexUtil.fromMap(Map.of(
                    0, ChatMode.ENABLED,
                    1, ChatMode.COMMANDS_ONLY,
                    2, ChatMode.HIDDEN
            )),
            VarIntNetworkCodec.INSTANCE
    );

    private static final IndexNetworkCodec<Hand, Integer> HAND_CODEC = new IndexNetworkCodec<>(
            IndexUtil.fromMap(Map.of(
                    0, Hand.LEFT,
                    1, Hand.RIGHT
            )),
            VarIntNetworkCodec.INSTANCE
    );

    private static final IndexNetworkCodec<ParticleStatus, Integer> PARTICLE_STATUS_CODEC = new IndexNetworkCodec<>(
            IndexUtil.fromMap(Map.of(
                    0, ParticleStatus.ALL,
                    1, ParticleStatus.DECREASED,
                    2, ParticleStatus.MINIMAL
            )),
            VarIntNetworkCodec.INSTANCE
    );

    /**
     * An instance of the {@linkplain PlayerSettingsReader player settings reader}.
     *
     * @since 1.0
     */
    public static final PlayerSettingsReader INSTANCE = new PlayerSettingsReader();

    private PlayerSettingsReader() {}

    @Override
    public Player.@NonNull Settings read(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager) {
        return new Player.Settings(
                LocaleNetworkReader.INSTANCE.read(buf, registryManager),
                buf.readByte(),
                CHAT_MODE_CODEC.read(buf, registryManager),
                buf.readBoolean(),
                SkinPartCollectionNetworkReader.INSTANCE.read(buf, registryManager),
                HAND_CODEC.read(buf, registryManager), buf.readBoolean(),
                buf.readBoolean(), PARTICLE_STATUS_CODEC.read(buf, registryManager)
        );
    }
}