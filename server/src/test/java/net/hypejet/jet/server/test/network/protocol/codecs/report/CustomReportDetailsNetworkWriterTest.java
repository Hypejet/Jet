package net.hypejet.jet.server.test.network.protocol.codecs.report;

import net.hypejet.jet.protocol.packet.server.configuration.ServerCustomReportDetailsConfigurationPacket.Details;
import net.hypejet.jet.server.network.protocol.codecs.report.CustomReportDetailsNetworkWriter;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of a {@linkplain CustomReportDetailsNetworkWriter custom report details
 * network codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see CustomReportDetailsNetworkWriter
 */
public final class CustomReportDetailsNetworkWriterTest {
    @Test
    public void test() {
        NetworkCodecTestUtil.test(CustomReportDetailsNetworkWriter.instance(), new Details(
                "a-title", "some-very-very-very-long-description-of-a-crash-report"
        ));
    }
}