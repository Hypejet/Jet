package net.hypejet.jet.server.network.codec.game.player.spawn;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.entity.player.spawn.DeathLocation;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.key.KeyNetworkCodec;
import net.hypejet.jet.server.network.codec.game.world.coordinate.BlockPositionNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain DeathLocation a death location}.
 *
 * @since 1.0
 * @see DeathLocation
 * @see NetworkWriter
 */
public final class DeathLocationNetworkWriter implements NetworkWriter<DeathLocation> {
    /**
     * An instance of the {@linkplain DeathLocationNetworkWriter death location network writer}.
     *
     * @since 1.0
     */
    public static final DeathLocationNetworkWriter INSTANCE = new DeathLocationNetworkWriter();

    private DeathLocationNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull DeathLocation object) {
        KeyNetworkCodec.INSTANCE.write(buf, object.deathDimensionTypeKey());
        BlockPositionNetworkCodec.INSTANCE.write(buf, object.deathPosition());
    }
}