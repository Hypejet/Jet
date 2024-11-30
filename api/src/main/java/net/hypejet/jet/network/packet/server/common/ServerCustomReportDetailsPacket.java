package net.hypejet.jet.network.packet.server.common;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.network.packet.server.ServerPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.List;

/**
 * Represents {@linkplain ServerPacket a server packet} that adds details that are included in crash reports
 * on the client.
 *
 * @param details the details
 * @since 1.0
 * @author Codestech
 * @see Details
 * @see ServerPacket
 */
public record ServerCustomReportDetailsPacket(@NonNull Collection<Details> details) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerCustomReportDetailsPacket custom report details configuration packet}.
     *
     * @param details the details
     * @since 1.0
     */
    public ServerCustomReportDetailsPacket {
        details = List.copyOf(NullabilityUtil.requireNonNull(details, "details"));
    }

    /**
     * Represents details of a crash report.
     *
     * @param title a title of the details
     * @param description a description of the details
     * @since 1.0
     * @see ServerCustomReportDetailsPacket
     */
    public record Details(@NonNull String title, @NonNull String description) {
        /**
         * Constructs the {@linkplain Details details}.
         *
         * @param title a title of the details
         * @param description a description of the details
         * @since 1.0
         */
        public Details {
            NullabilityUtil.requireNonNull(title, "title");
            NullabilityUtil.requireNonNull(description, "description");
        }
    }
}