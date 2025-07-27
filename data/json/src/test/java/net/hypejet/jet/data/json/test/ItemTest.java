package net.hypejet.jet.data.json.test;

import net.hypejet.jet.data.json.model.item.JsonItem;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

import java.util.Set;
/**
 * Represents a GSON conversion test of item-related objects.
 *
 * @since 1.0
 */
final class ItemTest {
    @Test
    void testItem() {
        TestUtil.test(new JsonItem(Set.of(Key.key("vanilla"), Key.key("hypejet", "additional-items"))));
    }
}