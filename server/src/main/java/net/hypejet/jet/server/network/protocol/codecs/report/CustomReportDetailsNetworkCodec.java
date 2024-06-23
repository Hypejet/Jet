package net.hypejet.jet.server.network.protocol.codecs.report;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerCustomReportDetailsConfigurationPacket.Details;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.util.NetworkUtil;
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

    private static final CustomReportDetailsNetworkCodec INSTANCE = new CustomReportDetailsNetworkCodec();

    private static final int MAX_TITLE_LENGTH = 128;
    private static final int MAX_DESCRIPTION_LENGTH = 4096;

    private CustomReportDetailsNetworkCodec() {}

    @Override
    public @NonNull Details read(@NonNull ByteBuf buf) {
        return new Details(NetworkUtil.readString(buf, MAX_TITLE_LENGTH),
                NetworkUtil.readString(buf, MAX_DESCRIPTION_LENGTH));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Details object) {
        NetworkUtil.writeString(buf, object.title(), MAX_TITLE_LENGTH);
        NetworkUtil.writeString(buf, object.description(), MAX_DESCRIPTION_LENGTH);
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