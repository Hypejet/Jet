package net.hypejet.jet.data.generator.level;

import net.minecraft.world.level.entity.LevelCallback;
import org.jspecify.annotations.NullMarked;

/**
 * A {@linkplain LevelCallback level callback} doing nothing.
 *
 * @param <T> the type of values that the callback is consuming
 * @since 1.0
 */
@NullMarked
final class EmptyLevelCallback<T> implements LevelCallback<T> {
    @Override
    public void onCreated(T var1) {}

    @Override
    public void onDestroyed(T var1) {}

    @Override
    public void onTickingStart(T var1) {}

    @Override
    public void onTickingEnd(T var1) {}

    @Override
    public void onTrackingStart(T var1) {}

    @Override
    public void onTrackingEnd(T var1) {}

    @Override
    public void onSectionChange(T var1) {}
}