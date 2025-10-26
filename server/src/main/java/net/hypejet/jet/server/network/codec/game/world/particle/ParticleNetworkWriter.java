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
import net.hypejet.jet.world.particle.Particle;
import net.hypejet.jet.world.particle.ParticleType;
import net.hypejet.jet.world.particle.block.BlockParticle;
import net.hypejet.jet.world.particle.color.ColorParticle;
import net.hypejet.jet.world.particle.item.ItemParticle;
import net.hypejet.jet.world.particle.scalable.dust.DustParticle;
import net.hypejet.jet.world.particle.scalable.dust.DustTransitionParticle;
import net.hypejet.jet.world.particle.sculk.SculkChargeParticle;
import net.hypejet.jet.world.particle.shriek.ShriekParticle;
import net.hypejet.jet.world.particle.simple.SimpleParticle;
import net.hypejet.jet.world.particle.trail.TrailParticle;
import net.hypejet.jet.world.particle.vibration.VibrationParticle;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

import java.util.Map;
import java.util.Objects;

/**
 * A {@linkplain NetworkWriter network writer} of {@linkplain Particle particles}.
 *
 * @since 1.0
 * @see Particle
 * @see NetworkWriter
 */
@NullMarked
public final class ParticleNetworkWriter implements NetworkWriter<Particle> {

    private static final ParticleWriterType<BlockParticle> BLOCK_PARTICLE_WRITER_TYPE = new ParticleWriterType<>(
            BlockParticle.class,
            BlockParticleAdditionalNetworkWriter.INSTANCE
    );

    private static final ParticleWriterType<ColorParticle> COLOR_PARTICLE_WRITER_TYPE = new ParticleWriterType<>(
            ColorParticle.class,
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
                            DustParticle.class, DustParticleAdditionalNetworkWriter.INSTANCE
                    )
            ),
            Map.entry(
                    ParticleTypeKeys.DUST_COLOR_TRANSITION,
                    new ParticleWriterType<>(
                            DustTransitionParticle.class, DustParticleTransitionAdditionalNetworkWriter.INSTANCE
                    )
            ),
            Map.entry(
                    ParticleTypeKeys.SCULK_CHARGE,
                    new ParticleWriterType<>(
                            SculkChargeParticle.class, SculkChargeParticleAdditionalNetworkWriter.INSTANCE
                    )
            ),
            Map.entry(
                    ParticleTypeKeys.ITEM,
                    new ParticleWriterType<>(
                            ItemParticle.class, ItemParticleAdditionalNetworkWriter.INSTANCE
                    )
            ),
            Map.entry(
                    ParticleTypeKeys.VIBRATION,
                    new ParticleWriterType<>(
                            VibrationParticle.class, VibrationParticleAdditionalNetworkWriter.INSTANCE
                    )
            ),
            Map.entry(
                    ParticleTypeKeys.TRAIL,
                    new ParticleWriterType<>(
                            TrailParticle.class, TrailParticleAdditionalNetworkWriter.INSTANCE
                    )
            ),
            Map.entry(
                    ParticleTypeKeys.SHRIEK,
                    new ParticleWriterType<>(
                            ShriekParticle.class, ShriekParticleAdditionalNetworkWriter.INSTANCE
                    )
            )
    );

    /**
     * An instance of the {@linkplain ParticleNetworkWriter particle network writer}.
     *
     * @since 1.0
     */
    public static final ParticleNetworkWriter INSTANCE = new ParticleNetworkWriter();

    private ParticleNetworkWriter() {}

    @Override
    public void write(ByteBuf buf, JetRegistryManager registryManager, Particle object) {
        Holder.Reference<ParticleType> particleTypeReference = object.particleType();

        VarIntNetworkCodec.INSTANCE.write(
                buf, registryManager,
                registryManager.registry(RegistryReference.PARTICLE_TYPE).indexOf(particleTypeReference)
        );

        ParticleWriterType<?> writerType = WRITER_TYPES.get(particleTypeReference.key());
        if (writerType == null) {
            if (!(object instanceof SimpleParticle)) {
                throw new IllegalArgumentException(String.format(
                        "Particles without additional data must use %s implementation",
                        SimpleParticle.class.getName()
                ));
            }
        } else {
            writeAdditional(writerType, buf, registryManager, object);
        }
    }

    private static <P extends Particle> void writeAdditional(ParticleWriterType<P> writerType, ByteBuf buf,
                                                             JetRegistryManager registryManager, Particle particle) {
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
     * A type of how {@linkplain Particle particles} containing additional data of certain type should be encoded.
     *
     * @param particleClass the class of particles whose serialization is handled by the specified network writer
     * @param networkWriter the network writer that the additional particle fields should be written with
     * @param <P> the type of particles whose serialization is handled by the specified network writer
     * @since 1.0
     * @see Particle
     */
    private record ParticleWriterType<P extends Particle>(Class<P> particleClass, NetworkWriter<P> networkWriter) {
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