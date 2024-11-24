package net.hypejet.jet.server.test.command.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.hypejet.jet.server.command.argument.ArgumentCodecRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of a {@linkplain ArgumentCodecRegistry argument codec registry}.
 *
 * @since 1.0
 * @author Codestech
 * @see ArgumentCodecRegistry
 */
public final class ArgumentCodecMinecraftRegistryTest {
    @Test
    public void testByClass() {
        Assertions.assertNotNull(ArgumentCodecRegistry.codec(StringArgumentType.class));
        Assertions.assertNull(ArgumentCodecRegistry.codec(String.class));
    }

    @Test
    public void testById() {
        Assertions.assertNotNull(ArgumentCodecRegistry.codec(0));
        Assertions.assertNull(ArgumentCodecRegistry.codec(9));
    }
}