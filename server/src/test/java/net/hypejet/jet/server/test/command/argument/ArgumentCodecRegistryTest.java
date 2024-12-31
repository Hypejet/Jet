package net.hypejet.jet.server.test.command.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.hypejet.jet.server.command.argument.ArgumentWriterRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of {@linkplain ArgumentWriterRegistry an argument codec registry}.
 *
 * @since 1.0
 * @see ArgumentWriterRegistry
 */
public final class ArgumentCodecRegistryTest {
    @Test
    public void testByClass() {
        Assertions.assertNotNull(ArgumentWriterRegistry.writer(StringArgumentType.class));
        Assertions.assertNull(ArgumentWriterRegistry.writer(String.class));
    }
}