package net.hypejet.jet.server.registry.session;

import net.hypejet.jet.protocol.packet.server.configuration.ServerUpdateTagsConfigurationPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a function, which synchronizes registry tag updates with a client.
 *
 * @since 1.0
 * @author Codestech
 */
@FunctionalInterface
public interface RegistryTagsUpdater {
    /**
     * Synchronizes the tags.
     *
     * @param tagRegistry a tag registry containing the changes to synchronize
     * @since 1.0
     */
    void synchronizeTags(ServerUpdateTagsConfigurationPacket.@NonNull TagRegistry tagRegistry);
}