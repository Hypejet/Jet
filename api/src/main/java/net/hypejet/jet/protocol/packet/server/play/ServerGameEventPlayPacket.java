package net.hypejet.jet.protocol.packet.server.play;

import net.hypejet.jet.protocol.packet.server.ServerPlayPacket;
import net.hypejet.jet.world.GameEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerPlayPacket server play packet}, which triggers a {@linkplain GameEvent game event}.
 *
 * @param gameEvent the game event to trigger
 * @since 1.0
 * @author Codestech
 * @see GameEvent
 */
public record ServerGameEventPlayPacket(@NonNull GameEvent gameEvent) implements ServerPlayPacket {}