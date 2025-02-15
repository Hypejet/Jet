package net.hypejet.jet.server.network.codec.game.player.spawn;

import com.google.common.hash.Hashing;
import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.entity.player.spawn.PlayerSpawnInfo;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.key.PackedKeyNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import net.hypejet.jet.server.util.game.gamemode.GameModeUtil;
import net.hypejet.jet.world.data.WorldData;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer} of {@linkplain PlayerSpawnInfo a player spawn info}.
 *
 * @since 1.0
 * @see PlayerSpawnInfo
 * @see NetworkWriter
 */
public final class PlayerSpawnInfoNetworkWriter implements NetworkWriter<PlayerSpawnInfo> {
    /**
     * An instance of the {@linkplain PlayerSpawnInfoNetworkWriter player spawn info network writer}.
     *
     * @since 1.0
     */
    public static final PlayerSpawnInfoNetworkWriter INSTANCE = new PlayerSpawnInfoNetworkWriter();

    private PlayerSpawnInfoNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull PlayerSpawnInfo object) {
        VarIntNetworkCodec.INSTANCE.write(buf, object.dimensionTypeIdentifier());
        PackedKeyNetworkCodec.INSTANCE.write(buf, object.dimensionTypeKey());

        WorldData worldData = object.worldData();
        buf.writeLong(Hashing.sha256().hashLong(worldData.seed()).asLong());

        buf.writeByte(GameModeUtil.identifierOf(object.gameMode()));
        buf.writeByte(GameModeUtil.nullableIdentifierOf(object.previousGameMode()));

        buf.writeBoolean(false); // The debug field, we always write false since it makes no sense to use with Jet
        buf.writeBoolean(worldData.flat());

        NetworkUtil.writeOptional(object.lastDeathLocation(), DeathLocationNetworkWriter.INSTANCE, buf);

        VarIntNetworkCodec.INSTANCE.write(buf, object.portalCooldown());
        VarIntNetworkCodec.INSTANCE.write(buf, worldData.seaLevel());
    }
}