package net.hypejet.jet.data.json.test;

import net.hypejet.jet.data.json.model.sound.JsonSoundEvent;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;
/**
 * Represents a GSON conversion test of sound-event-related objects.
 *
 * @since 1.0
 */
final class SoundEventTest {
    @Test
    void testSoundEventWithNoFixedRange() {
        TestUtil.test(new JsonSoundEvent(Key.key("block.place"), null));
    }

    @Test
    void testSoundEventWithFixedRange() {
        TestUtil.test(new JsonSoundEvent(Key.key("hypejet", "custom.sound"), 34F));
    }
}