package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.world.coordinate.Vector;
import net.hypejet.jet.world.coordinate.floats.FloatVector;
import net.hypejet.jet.world.particle.Particle;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * A {@linkplain ServerPacket server packet} spawning a {@linkplain Particle particle}.
 *
 * @param overrideLimiter whether the particle should spawn even if it is going to be spawned
 *                        in a long distance from the player or the player has selected
 *                        minimal particle spawning strategy in settings
 * @param alwaysShow whether the particle should have a chance of being displayed even when
 *                   player has selected minimal or decreased particle spawning strategy in settings
 * @param position a position to spawn the particle at
 * @param offset size of a cuboid region (centered with the particle original position) that
 *               the particle should spawn in, 1 unit represents 6 blocks
 * @param maxSpeed speed that the particle should move with
 * @param count the number of times that the particle should be spawned
 * @param particle the particle to spawn
 * @since 1.0
 * @see Particle
 * @see ServerPacket
 */
@NullMarked
public record ServerSpawnParticlePacket(
        boolean overrideLimiter, boolean alwaysShow, Vector position,
        FloatVector offset, float maxSpeed, int count, Particle particle
) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerSpawnParticlePacket server spawn particle packet}.
     *
     * @param overrideLimiter whether the particle should spawn even if it is going to be spawned
     *                        in a long distance from the player or the player has selected
     *                        minimal particle spawning strategy in settings
     * @param alwaysShow whether the particle should have a chance of being displayed even when
     *                   player has selected minimal or decreased particle spawning strategy in settings
     * @param position a position to spawn the particle at
     * @param offset size of a cuboid region (centered with the particle original position) that
     *               the particle should spawn in, 1 unit represents 6 blocks
     * @param maxSpeed speed that the particle should move with
     * @param count the number of times that the particle should be spawned
     * @param particle the particle to spawn
     * @since 1.0
     */
    public ServerSpawnParticlePacket {
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(offset, "offset");
        Objects.requireNonNull(particle, "particle");
    }
}