package net.hypejet.jet.server.test.network.protocol.codecs.settings;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.server.network.protocol.codecs.settings.PlayerSettingsCodec;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Set;

/**
 * Represents a test of reading and writing of a {@linkplain PlayerSettingsCodec player settings codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see PlayerSettingsCodec
 */
public final class PlayerSettingsCodecTest {
    @Test
    public void test() {
        NetworkCodecTestUtil.test(PlayerSettingsCodec.instance(), new Player.Settings(
                Locale.US, (byte) 5, Player.ChatMode.COMMANDS_ONLY, false,
                Set.of(Player.SkinPart.HAT, Player.SkinPart.RIGHT_SLEEVE, Player.SkinPart.RIGHT_PANTS),
                Entity.Hand.LEFT, true, false
        ));
    }
}