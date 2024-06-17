package net.hypejet.jet.protocol.packet.server.login;

import net.hypejet.jet.protocol.packet.server.ServerLoginPacket;
import net.hypejet.jet.protocol.properties.GameProfileProperties;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.UUID;

/**
 * Represents a {@linkplain ServerLoginPacket server login packet}, which is sent when a server successfully logs in a
 * player.
 *
 * @param uniqueId a unique identifier of a player that is logged in
 * @param username a username of a player that is logged in
 * @param properties a {@linkplain GameProfileProperties game profile properties} of a player that is logged in
 * @param strictErrorHandling whether a player logging in should disconnect if this packet is incorrect
 * @since 1.0
 * @author Codestech
 * @see ServerLoginPacket
 */
public record ServerLoginSuccessLoginPacket(@NonNull UUID uniqueId, @NonNull String username,
                                            @NonNull Collection<GameProfileProperties> properties,
                                            boolean strictErrorHandling) implements ServerLoginPacket {}