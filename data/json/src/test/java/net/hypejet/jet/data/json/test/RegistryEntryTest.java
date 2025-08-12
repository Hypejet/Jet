package net.hypejet.jet.data.json.test;

import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.entry.JsonRegistryEntry;
import net.hypejet.jet.data.json.model.feature.JsonKnownPack;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

import java.util.Set;

/**
 * Represents a GSON conversion test of registry-entry-related objects.
 *
 * @since 1.0
 */
final class RegistryEntryTest {
    @Test
    void testCustomNamespaceKnownPack() {
        TestUtil.test(new JsonKnownPack("hypejet", "pack", "2.0"));
    }

    @Test
    public void testMinecraftNamespaceKnownPack() {
        TestUtil.test(new JsonKnownPack("minecraft", "vanilla", "1.21.8"));
    }

    @Test
    void testMinimalRegistryEntry() {
        TestUtil.test(
                new JsonRegistryEntry<>(Key.key("glowstone"), 12342, Set.of(), null),
                new TypeToken<>() {}
        );
    }

    @Test
    void testMaximalRegistryEntry() {
        TestUtil.test(
                new JsonRegistryEntry<>(
                        Key.key("hypejet", "a-test"),
                        "A test entry!",
                        Set.of(),
                        new JsonKnownPack("hypejet", "jet-pack", "2.0")
                ),
                new TypeToken<>() {}
        );
    }
}