package net.hypejet.jet.data.json.test;

import net.hypejet.jet.data.json.model.event.JsonGameEvent;
import org.junit.jupiter.api.Test;
/**
 * Represents a GSON conversion test of game-event-related objects.
 *
 * @since 1.0
 */
final class GameEventTest {
    @Test
    void testGameEvent() {
        TestUtil.test(new JsonGameEvent(34));
    }
}