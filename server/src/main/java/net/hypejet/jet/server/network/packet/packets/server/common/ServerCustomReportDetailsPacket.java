package net.hypejet.jet.server.network.packet.packets.server.common;

import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.util.game.crash.CrashReportDetails;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents {@linkplain ServerPacket a server packet} that adds details that are included in crash reports on the
 * client.
 *
 * @param details the details
 * @since 1.0
 * @see CrashReportDetails
 * @see ServerPacket
 */
public record ServerCustomReportDetailsPacket(@NonNull Collection<CrashReportDetails> details)
        implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerCustomReportDetailsPacket custom report details configuration packet}.
     *
     * @param details the details
     * @since 1.0
     */
    public ServerCustomReportDetailsPacket {
        details = List.copyOf(Objects.requireNonNull(details, "details"));
    }
}