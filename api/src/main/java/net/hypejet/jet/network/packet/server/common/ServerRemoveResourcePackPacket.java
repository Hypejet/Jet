package net.hypejet.jet.network.packet.server.common;

import net.hypejet.jet.network.packet.server.ServerPacket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

/**
 * Represents {@linkplain ServerPacket a server packet} removing a resource pack from a client.
 *
 * @param uniqueId a unique identifier of the resource pack to remove, or {@code null} to remove all of them
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 */
public record ServerRemoveResourcePackPacket(@Nullable UUID uniqueId) implements ServerPacket {}