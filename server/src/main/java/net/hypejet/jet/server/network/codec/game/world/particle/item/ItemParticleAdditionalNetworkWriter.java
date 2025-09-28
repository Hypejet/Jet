package net.hypejet.jet.server.network.codec.game.world.particle.item;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.world.particle.item.JetItemParticle;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer} of additional data of {@linkplain JetItemParticle item particles}.
 *
 * @since 1.0
 * @see JetItemParticle
 * @see NetworkWriter
 */
public final class ItemParticleAdditionalNetworkWriter implements NetworkWriter<JetItemParticle> {
    /**
     * An instance of the {@linkplain ItemParticleAdditionalNetworkWriter item particle additional network writer}.
     *
     * @since 1.0
     */
    public static final ItemParticleAdditionalNetworkWriter INSTANCE = new ItemParticleAdditionalNetworkWriter();

    private ItemParticleAdditionalNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf,
                      @NonNull JetRegistryManager registryManager,
                      @NonNull JetItemParticle object) {
        // TODO: Implement
        throw new UnsupportedOperationException("Item particle writing has not been implemented yet");
    }
}