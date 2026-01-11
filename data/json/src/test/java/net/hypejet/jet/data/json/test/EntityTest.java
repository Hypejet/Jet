package net.hypejet.jet.data.json.test;

import net.hypejet.jet.data.json.model.entity.JsonEntityType;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

import java.util.Set;
/**
 * Represents a GSON conversion test of entity-related objects.
 *
 * @since 1.0
 */
final class EntityTest {
    @Test
    void testEntityType() {
        TestUtil.test(new JsonEntityType(Set.of(Key.key("vanilla"), Key.key("some-update")), 300, true, true, true));
    }
}
