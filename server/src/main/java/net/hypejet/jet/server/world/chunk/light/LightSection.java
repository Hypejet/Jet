package net.hypejet.jet.server.world.chunk.light;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.world.chunk.light.storage.LightStorage;
import net.hypejet.jet.server.world.chunk.light.update.LightStorageUpdate;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Set;

/**
 * Represents light data of block states stored in an associated
 * {@linkplain net.hypejet.jet.server.world.chunk.section.ChunkSection a chunk section}.
 *
 * @param skyLightStorage a light storage of skylight of block states of the section
 * @param blockLightStorage a light storage of block light of block states of the section
 * @since 1.0
 * @see net.hypejet.jet.server.world.chunk.section.ChunkSection
 */
public record LightSection(@NonNull LightStorage skyLightStorage, @NonNull LightStorage blockLightStorage) {
    /**
     * Constructs the {@linkplain LightSection light section}.
     *
     * @param skyLightStorage a light storage of skylight of block states of the section
     * @param blockLightStorage a light storage of block light of block states of the section
     * @since 1.0
     */
    public LightSection {
        NullabilityUtil.requireNonNull(skyLightStorage, "skylight storage");
        NullabilityUtil.requireNonNull(blockLightStorage, "block light storage");
    }

    /**
     * Creates a new {@linkplain LightSection light section} with updates specified that should be applied to
     * the skylight storage.
     *
     * @param updates the updates
     * @return the new light section
     * @since 1.0
     */
    public @NonNull LightSection withSkyLightUpdates(@NonNull LightStorageUpdate @NonNull ... updates) {
        return this.withUpdates(Set.of(updates), Set.of());
    }

    /**
     * Creates a new {@linkplain LightSection light section} with updates specified that should be applied to
     * the block light storage.
     *
     * @param updates the updates
     * @return the new light section
     * @since 1.0
     */
    public @NonNull LightSection withBlockLightUpdates(@NonNull LightStorageUpdate @NonNull ... updates) {
        return this.withUpdates(Set.of(), Set.of(updates));
    }

    /**
     * Creates a new {@linkplain LightSection light section} with updates specified that should be applied to
     * the skylight storage and to the block light storage.
     *
     * @param skyLightStorageUpdates updates that should be applied to the skylight storage
     * @param blockLightStorageUpdates updates that should be applied to the block light storage
     * @return the new light section
     * @since 1.0
     */
    public @NonNull LightSection withUpdates(@NonNull Collection<LightStorageUpdate> skyLightStorageUpdates,
                                             @NonNull Collection<LightStorageUpdate> blockLightStorageUpdates) {
        NullabilityUtil.requireNonNull(skyLightStorageUpdates, "sky light storage updates");
        NullabilityUtil.requireNonNull(blockLightStorageUpdates, "block light storage updates");

        LightStorage newSkyLightStorage = this.skyLightStorage.withUpdates(skyLightStorageUpdates);
        LightStorage newBlockLightStorage = this.blockLightStorage.withUpdates(blockLightStorageUpdates);

        if (newSkyLightStorage == this.skyLightStorage && newBlockLightStorage == this.blockLightStorage)
            return this;
        return new LightSection(newSkyLightStorage, newBlockLightStorage);
    }
}