package net.hypejet.jet.server.world.chunk.factory.light;

import java.util.Objects;
import net.hypejet.jet.server.world.chunk.light.storage.AbstractLightStorage;
import net.hypejet.jet.server.world.chunk.light.storage.EmptyLightStorage;
import net.hypejet.jet.util.array.NibbleArray;
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
    public @NonNull AbstractLightStorage createDirect(@NonNull NibbleArray array) {
        Objects.requireNonNull(array, "array");
        return AbstractLightStorage.create(array);
    }

    @Override
    public @NonNull EmptyLightStorage emptyLightStorage() {
        return EmptyLightStorage.INSTANCE;
    }
}
