package net.hypejet.jet.server.world.chunk.light;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.world.chunk.light.storage.AbstractLightStorage;
import net.hypejet.jet.server.world.chunk.light.update.LightStorageUpdate;
import net.hypejet.jet.world.chunk.light.LightSection;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;

import java.util.Collection;

/**
 * Represents an implementation of {@linkplain LightSection a light section}.
 *
 * @since 1.0
 * @see LightSection
 */
public record JetLightSection(@NonNull AbstractLightStorage skyLightStorage,
                              @NonNull AbstractLightStorage blockLightStorage) implements LightSection {
    /**
     * Constructs the {@linkplain JetLightSection light section implementation}.
     *
     * @param skyLightStorage a light storage of skylight that the section should have
     * @param blockLightStorage a light storage of block light that the section should have
     * @since 1.0
     */
    public JetLightSection {
        NullabilityUtil.requireNonNull(skyLightStorage, "skylight storage");
        NullabilityUtil.requireNonNull(blockLightStorage, "block light storage");
    }

    /**
     * Creates a new {@linkplain JetLightSection light section} with updates specified that should be applied to
     * the skylight storage and to the block light storage.
     *
     * @param skyLightStorageUpdates updates that should be applied to the skylight storage
     * @param blockLightStorageUpdates updates that should be applied to the block light storage
     * @return the new light section
     * @since 1.0
     */
    @Contract(pure = true)
    public @NonNull JetLightSection withUpdates(@NonNull Collection<LightStorageUpdate> skyLightStorageUpdates,
                                                @NonNull Collection<LightStorageUpdate> blockLightStorageUpdates) {
        NullabilityUtil.requireNonNull(skyLightStorageUpdates, "sky light storage updates");
        NullabilityUtil.requireNonNull(blockLightStorageUpdates, "block light storage updates");

        AbstractLightStorage newSkyLightStorage = this.skyLightStorage.withUpdates(skyLightStorageUpdates);
        AbstractLightStorage newBlockLightStorage = this.blockLightStorage.withUpdates(blockLightStorageUpdates);

        if (newSkyLightStorage == this.skyLightStorage && newBlockLightStorage == this.blockLightStorage)
            return this;
        return new JetLightSection(newSkyLightStorage, newBlockLightStorage);
    }
}