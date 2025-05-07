package net.hypejet.jet.server.test.world.chunk.light;

import net.hypejet.jet.server.world.chunk.light.JetLightSection;
import net.hypejet.jet.server.world.chunk.light.storage.EmptyLightStorage;
import net.hypejet.jet.server.world.chunk.light.update.LightStorageUpdate;
import net.hypejet.jet.server.world.chunk.palette.type.ChunkPaletteType;
import net.hypejet.jet.server.world.coordinate.chunk.palette.relative.ChunkPaletteRelativePosition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

/**
 * Represents a test of {@linkplain JetLightSection a light section implementation}.
 *
 * @since 1.0
 * @see JetLightSection
 */
public final class JetLightSectionTest {
    @Test
    public void testRedundantUpdate() {
        JetLightSection section = new JetLightSection(EmptyLightStorage.INSTANCE, EmptyLightStorage.INSTANCE);

        Set<LightStorageUpdate> storageUpdates = Set.of(new LightStorageUpdate(
                new ChunkPaletteRelativePosition((byte) 0, (byte) 0, (byte) 0, ChunkPaletteType.BLOCK_STATE),
                (byte) 0
        ));

        JetLightSection updatedSection = section.withUpdates(storageUpdates, storageUpdates);
        Assertions.assertSame(section, updatedSection);
    }

    @Test
    public void testUpdate() {
        JetLightSection section = new JetLightSection(EmptyLightStorage.INSTANCE, EmptyLightStorage.INSTANCE);

        JetLightSection updatedSection = section.withUpdates(
                Set.of(new LightStorageUpdate(
                        new ChunkPaletteRelativePosition((byte) 6, (byte) 1, (byte) 3, ChunkPaletteType.BLOCK_STATE),
                        (byte) 0
                )),
                Set.of(new LightStorageUpdate(
                        new ChunkPaletteRelativePosition((byte) 2, (byte) 0, (byte) 4, ChunkPaletteType.BLOCK_STATE),
                        (byte) 0
                ))
        );

        Assertions.assertNotEquals(section, updatedSection);
        Assertions.assertNotEquals(section.skyLightStorage(), updatedSection.skyLightStorage());
        Assertions.assertNotEquals(section.blockLightStorage(), updatedSection.blockLightStorage());
    }
}