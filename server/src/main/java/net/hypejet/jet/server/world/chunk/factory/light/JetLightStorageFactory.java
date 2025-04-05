package net.hypejet.jet.server.world.chunk.factory.light;

import net.hypejet.jet.server.world.chunk.light.storage.AbstractLightStorage;
import net.hypejet.jet.server.world.chunk.light.storage.EmptyLightStorage;
import net.hypejet.jet.world.chunk.factory.light.LightStorageFactory;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an implementation of {@linkplain LightStorageFactory a light storage factory}.
 *
 * @since 1.0
 * @see LightStorageFactory
 */
public final class JetLightStorageFactory implements LightStorageFactory {
    /**
     * An instance of the {@linkplain JetLightStorageFactory light storage factory implementation}.
     *
     * @since 11.0
     */
    public static final JetLightStorageFactory INSTANCE = new JetLightStorageFactory();

    private JetLightStorageFactory() {}

    @Override
    public @NonNull AbstractLightStorage createDirect(byte @NonNull [] lightValues) {
        return AbstractLightStorage.create(lightValues);
    }

    @Override
    public @NonNull EmptyLightStorage emptyLightStorage() {
        return EmptyLightStorage.INSTANCE;
    }
}
