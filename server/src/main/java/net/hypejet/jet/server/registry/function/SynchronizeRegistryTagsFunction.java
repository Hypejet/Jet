package net.hypejet.jet.server.registry.function;

import net.hypejet.jet.server.network.packet.packets.server.common.ServerUpdateTagsPacket;
import net.hypejet.jet.server.registry.JetMinecraftRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A function synchronizing tags of {@linkplain JetMinecraftRegistry registries} with the client.
 *
 * <p>This allows doing additional checks if the synchronization is not needed,
 * for example if initial tag synchronization has not been sent yet.</p>
 *
 * @since 1.0
 * @see JetMinecraftRegistry
 */
@FunctionalInterface
public interface SynchronizeRegistryTagsFunction {
    /**
     * Synchronizes tags of {@linkplain JetMinecraftRegistry registries} with the client.
     *
     * @param packet a packet to synchronize the registries with
     * @since 1.0
     */
    void synchronizeTags(@NonNull ServerUpdateTagsPacket packet);
}