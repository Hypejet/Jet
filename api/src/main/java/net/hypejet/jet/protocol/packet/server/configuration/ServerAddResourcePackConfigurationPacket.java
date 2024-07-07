package net.hypejet.jet.protocol.packet.server.configuration;

import net.hypejet.jet.protocol.packet.server.ServerConfigurationPacket;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

/**
 * Represents a {@linkplain ServerConfigurationPacket server configuration packet}, which is sent when it wants
 * a client to download and load a resource pack.
 *
 * @param uniqueId an unique identifier of the resource pack
 * @param url a download url of the resource pack
 * @param hash a SHA-1 hash of the resource pack file
 * @param forced whether the resource pack is required
 * @param prompt a prompt making the client accept or decline the resource pack, {@code null} if a default prompt
 *               should be used
 * @since 1.0
 * @author Codestech
 * @see ServerConfigurationPacket
 */
public record ServerAddResourcePackConfigurationPacket(
        @NonNull UUID uniqueId, @NonNull String url, @NonNull String hash, boolean forced, @Nullable Component prompt
) implements ServerConfigurationPacket {}