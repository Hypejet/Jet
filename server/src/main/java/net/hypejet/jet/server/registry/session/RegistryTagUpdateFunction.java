package net.hypejet.jet.server.registry.session;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a function updating registry tags.
 *
 * <p>This allows to do additional checks before tags are updated.</p>
 *
 * @since 1.0
 * @author Codestech
 */
@FunctionalInterface
public interface RegistryTagUpdateFunction {
    /**
     * Performs a tag update using a task specified.
     *
     * @param tagUpdateTask the tag update
     * @since 1.0
     */
    void updateTags(@NonNull Runnable tagUpdateTask);
}