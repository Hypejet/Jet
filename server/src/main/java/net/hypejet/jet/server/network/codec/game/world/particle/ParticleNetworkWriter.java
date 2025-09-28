package net.hypejet.jet.server.network.codec.game.world.particle;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.registry.keys.ParticleTypeKeys;
import net.hypejet.jet.registry.reference.RegistryReference;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.particle.block.BlockParticleAdditionalNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.particle.color.ColorParticleAdditionalNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.particle.item.ItemParticleAdditionalNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.particle.scalable.dust.DustParticleAdditionalNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.particle.scalable.dust.DustParticleTransitionAdditionalNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.particle.sculk.SculkChargeParticleAdditionalNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.particle.shriek.ShriekParticleAdditionalNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.particle.trail.TrailParticleAdditionalNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.particle.vibration.VibrationParticleAdditionalNetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.world.particle.JetParticle;
import net.hypejet.jet.server.world.particle.block.JetBlockParticle;
import net.hypejet.jet.server.world.particle.color.JetColorParticle;
import net.hypejet.jet.server.world.particle.item.JetItemParticle;
import net.hypejet.jet.server.world.particle.scalable.dust.JetDustParticle;
import net.hypejet.jet.server.world.particle.scalable.dust.JetDustTransitionParticle;
import net.hypejet.jet.server.world.particle.sculk.JetSculkChargeParticle;
import net.hypejet.jet.server.world.particle.shriek.JetShriekParticle;
import net.hypejet.jet.server.world.particle.trail.JetTrailParticle;
import net.hypejet.jet.server.world.particle.vibration.JetVibrationParticle;
import net.hypejet.jet.world.particle.ParticleType;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

import java.util.Map;
import java.util.Objects;

/**
 * A {@linkplain NetworkWriter network writer} of {@linkplain JetParticle particles}.
 *
 * @since 1.0
 * @see JetParticle
 * @see NetworkWriter
 */
@NullMarked
public final class ParticleNetworkWriter implements NetworkWriter<JetParticle> {

    private static final ParticleWriterType<JetBlockParticle> BLOCK_PARTICLE_WRITER_TYPE = new ParticleWriterType<>(
            JetBlockParticle.class,
            BlockParticleAdditionalNetworkWriter.INSTANCE
    );

    private static final ParticleWriterType<JetColorParticle> COLOR_PARTICLE_WRITER_TYPE = new ParticleWriterType<>(
            JetColorParticle.class,
            ColorParticleAdditionalNetworkWriter.INSTANCE
    );

    private static final Map<Key, ParticleWriterType<?>> WRITER_TYPES = Map.ofEntries(
            Map.entry(ParticleTypeKeys.BLOCK, BLOCK_PARTICLE_WRITER_TYPE),
            Map.entry(ParticleTypeKeys.BLOCK_MARKER, BLOCK_PARTICLE_WRITER_TYPE),
            Map.entry(ParticleTypeKeys.ENTITY_EFFECT, COLOR_PARTICLE_WRITER_TYPE),
            Map.entry(ParticleTypeKeys.FALLING_DUST, BLOCK_PARTICLE_WRITER_TYPE),
            Map.entry(ParticleTypeKeys.TINTED_LEAVES, COLOR_PARTICLE_WRITER_TYPE),
            Map.entry(ParticleTypeKeys.DUST_PILLAR, BLOCK_PARTICLE_WRITER_TYPE),
            Map.entry(ParticleTypeKeys.BLOCK_CRUMBLE, BLOCK_PARTICLE_WRITER_TYPE),
            Map.entry(
                    ParticleTypeKeys.DUST,
                    new ParticleWriterType<>(
                            JetDustParticle.class,
                            DustParticleAdditionalNetworkWriter.INSTANCE
                    )
            ),
            Map.entry(
                    ParticleTypeKeys.DUST_COLOR_TRANSITION,
                    new ParticleWriterType<>(
                            JetDustTransitionParticle.class,
                            DustParticleTransitionAdditionalNetworkWriter.INSTANCE
                    )
            ),
            Map.entry(
                    ParticleTypeKeys.SCULK_CHARGE,
                    new ParticleWriterType<>(
                            JetSculkChargeParticle.class,
                            SculkChargeParticleAdditionalNetworkWriter.INSTANCE
                    )
            ),
            Map.entry(
                    ParticleTypeKeys.ITEM,
                    new ParticleWriterType<>(
                            JetItemParticle.class,
                            ItemParticleAdditionalNetworkWriter.INSTANCE
                    )
            ),
            Map.entry(
                    ParticleTypeKeys.VIBRATION,
                    new ParticleWriterType<>(
                            JetVibrationParticle.class,
                            VibrationParticleAdditionalNetworkWriter.INSTANCE
                    )
            ),
            Map.entry(
                    ParticleTypeKeys.TRAIL,
                    new ParticleWriterType<>(
                            JetTrailParticle.class,
                            TrailParticleAdditionalNetworkWriter.INSTANCE
                    )
            ),
            Map.entry(
                    ParticleTypeKeys.SHRIEK,
                    new ParticleWriterType<>(
                            JetShriekParticle.class,
                            ShriekParticleAdditionalNetworkWriter.INSTANCE
                    )
            )
    );

    private ParticleNetworkWriter() {}

    @Override
    public void write(ByteBuf buf, JetRegistryManager registryManager, JetParticle object) {
        Holder.Reference<ParticleType> particleTypeReference = object.particleType();

        VarIntNetworkCodec.INSTANCE.write(
                buf, registryManager,
                registryManager.registry(RegistryReference.PARTICLE_TYPE).indexOf(particleTypeReference)
        );

        ParticleWriterType<?> writerType = WRITER_TYPES.get(particleTypeReference.key());
        if (writerType != null) writeAdditional(writerType, buf, registryManager, object);
    }

    private static <P extends JetParticle> void writeAdditional(ParticleWriterType<P> writerType,
                                                                ByteBuf buf,
                                                                JetRegistryManager registryManager,
                                                                JetParticle particle) {
        Class<P> particleClass = writerType.particleClass();
        if (!particleClass.isAssignableFrom(particle.getClass())) {
            throw new IllegalArgumentException(String.format(
                    "The particle with %s type must be of %s class to encode it",
                    particle.particleType().key(),
                    particleClass.getName()
            ));
        }
        writerType.networkWriter().write(buf, registryManager, particleClass.cast(particle));
    }

    /**
     * A type of how {@linkplain JetParticle particles} containing additional data of certain type should be encoded.
     *
     * @param particleClass the class of particles whose serialization is handled by the specified network writer
     * @param networkWriter the network writer that the additional particle fields should be written with
     * @param <P> the type of particles whose serialization is handled by the specified network writer
     * @since 1.0
     * @see JetParticle
     */
    private record ParticleWriterType<P extends JetParticle>(Class<P> particleClass, NetworkWriter<P> networkWriter) {
        /**
         * Constructs the {@linkplain ParticleWriterType particle writer type}.
         *
         * @param particleClass the class of particles whose serialization
         *                      should be handled by the specified network writer
         * @param networkWriter the network writer that the additional particle fields should be written with
         * @since 1.0
         */
        private ParticleWriterType {
            Objects.requireNonNull(particleClass, "particle class");
            Objects.requireNonNull(networkWriter, "network writer");
        }
    }
}