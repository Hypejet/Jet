package net.hypejet.jet.server.test.network;

import net.hypejet.jet.network.PlayerConnectionState;
import net.hypejet.jet.server.network.ProtocolState;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of {@linkplain ProtocolState protocol state} to {@linkplain PlayerConnectionState player
 * connection state} conversion.
 *
 * @since 1.0
 */
public final class ProtocolStateTest {
    @Test
    public void testConnectionStateConversion() {
        test(ProtocolState.PLAY, PlayerConnectionState.PLAY);
        test(ProtocolState.CONFIGURATION, PlayerConnectionState.CONFIGURATION);
        test(ProtocolState.LOGIN, PlayerConnectionState.LOGIN);
        test(ProtocolState.STATUS, PlayerConnectionState.STATUS);
        test(ProtocolState.HANDSHAKE, PlayerConnectionState.HANDSHAKE);
    }

    private static void test(@NonNull ProtocolState protocolState, @NonNull PlayerConnectionState connectionState) {
        Assertions.assertSame(protocolState.toConnectionState(), connectionState);
    }
}