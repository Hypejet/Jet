package net.hypejet.jet.server.network.protocol.codecs.report;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerCustomReportDetailsConfigurationPacket.Details;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain Details custom report
 * details}.
 *
 * @since 1.0
 * @author Coidestech
 * @see Details
 */
public final class CustomReportDetailsNetworkCodec implements NetworkCodec<Details> {

    private static final StringNetworkCodec TITLE_CODEC = StringNetworkCodec.create(128);
    private static final StringNetworkCodec DESCRIPTION_CODEC = StringNetworkCodec.create(4096);

    private static final CustomReportDetailsNetworkCodec INSTANCE = new CustomReportDetailsNetworkCodec();

    private CustomReportDetailsNetworkCodec() {}

    @Override
    public @NonNull Details read(@NonNull ByteBuf buf) {
        return new Details(TITLE_CODEC.read(buf), DESCRIPTION_CODEC.read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Details object) {
        TITLE_CODEC.write(buf, object.title());
        DESCRIPTION_CODEC.write(buf, object.description());
    }

    /**
     * Gets an instance of the {@linkplain CustomReportDetailsNetworkCodec custom report details network codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull CustomReportDetailsNetworkCodec instance() {
        return INSTANCE;
    }
}