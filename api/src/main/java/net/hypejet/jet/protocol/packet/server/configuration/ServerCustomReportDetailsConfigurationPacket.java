package net.hypejet.jet.protocol.packet.server.configuration;

import net.hypejet.jet.protocol.packet.server.ServerConfigurationPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.List;

/**
 * Represents a {@linkplain ServerConfigurationPacket server configuration packet} that adds another details to
 * the crash reports on client.
 *
 * @param detailEntries entries of the details
 * @since 1.0
 * @author Codestech
 * @see Details
 * @see ServerConfigurationPacket
 */
public record ServerCustomReportDetailsConfigurationPacket(@NonNull Collection<Details> detailEntries)
        implements ServerConfigurationPacket {
    /**
     * Constructs the {@linkplain ServerCustomReportDetailsConfigurationPacket custom report details configuration
     * packet}.
     *
     * @param detailEntries entries of the details
     * @since 1.0
     */
    public ServerCustomReportDetailsConfigurationPacket {
        detailEntries = List.copyOf(detailEntries);
    }

    /**
     * Represents a details entry used by the {@linkplain ServerCustomReportDetailsConfigurationPacket custom report
     * details configuration packet}.
     *
     * @param title a title of the entry
     * @param description a description of the entry
     * @since 1.0
     * @author Codestech
     * @see ServerCustomReportDetailsConfigurationPacket
     */
    public record Details(@NonNull String title, @NonNull String description) {}
}