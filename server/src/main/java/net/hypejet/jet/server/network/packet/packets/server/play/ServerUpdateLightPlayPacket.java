package net.hypejet.jet.server.network.packet.packets.server.play;

import java.util.Objects;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.server.world.chunk.light.LightSerializationData;
import net.hypejet.jet.world.coordinate.chunk.ChunkPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ServerPacket a server packet}, which updates light data
 * of {@linkplain net.hypejet.jet.world.chunk.Chunk a chunk} at {@linkplain ChunkPosition a chunk position} specified.
 *
 * @param position the chunk position
 * @param data a light serialization data, which contains light updates
 * @since 1.0
 * @see ServerPacket
 */
public record ServerUpdateLightPlayPacket(@NonNull ChunkPosition position, @NonNull LightSerializationData data)
        implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerUpdateLightPlayPacket server update light play packet}.
     *
     * @param position the chunk position
     * @param data a light serialization data, which contains light updates
     * @since 1.0
     */
    public ServerUpdateLightPlayPacket {
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(data, "data");
    }
}