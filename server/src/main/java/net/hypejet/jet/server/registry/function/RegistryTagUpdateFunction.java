package net.hypejet.jet.server.registry.function;

import net.hypejet.jet.server.network.packet.packets.server.common.ServerUpdateTagsPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a function updating registry tags.
 *
 * <p>This allows to do additional checks before tags are updated.</p>
 *
 * @since 1.0
 */
@FunctionalInterface
public interface RegistryTagUpdateFunction {
    /**
     * Performs a tag update using a packet specified.
     *
     * @param packet the packet, which represents the tag update
     * @since 1.0
     */
    void updateTags(@NonNull ServerUpdateTagsPacket packet);
}