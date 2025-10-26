package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.component.ComponentNetworkWriter;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerPlayerListHeaderAndFooterPlayPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerPlayerListHeaderAndFooterPlayPacket a player list header and footer play packet}.
 *
 * @since 1.0
 * @see ServerPlayerListHeaderAndFooterPlayPacket
 * @see NetworkWriter
 */
public final class ServerPlayerListHeaderAndFooterPlayPacketWriter
        implements NetworkWriter<ServerPlayerListHeaderAndFooterPlayPacket> {

    /**
     * An instance of the {@linkplain ServerPlayerListHeaderAndFooterPlayPacket server player list header and footer
     * play packet writer}.
     *
     * @since 1.0
     */
    public static final ServerPlayerListHeaderAndFooterPlayPacketWriter
            INSTANCE = new ServerPlayerListHeaderAndFooterPlayPacketWriter();

    private ServerPlayerListHeaderAndFooterPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager,
                      @NonNull ServerPlayerListHeaderAndFooterPlayPacket object) {
        ComponentNetworkWriter.INSTANCE.write(buf, registryManager, object.headerText());
        ComponentNetworkWriter.INSTANCE.write(buf, registryManager, object.footerText());
    }
}