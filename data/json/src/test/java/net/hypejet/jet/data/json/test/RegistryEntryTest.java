package net.hypejet.jet.data.json.test;

import com.google.gson.reflect.TypeToken;
import net.hypejet.jet.data.json.entry.JsonRegistryEntry;
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
    void testCustomNamespaceFeaturePack() {
        TestUtil.test(new JsonRegistryEntry.FeaturePack("hypejet", "pack", "2.0"));
    }

    @Test
    public void testMinecraftNamespaceFeaturePack() {
        TestUtil.test(new JsonRegistryEntry.FeaturePack("minecraft", "vanilla", "1.21.8"));
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
                        new JsonRegistryEntry.FeaturePack("hypejet", "jet-pack", "2.0")
                ),
                new TypeToken<>() {}
        );
    }
}