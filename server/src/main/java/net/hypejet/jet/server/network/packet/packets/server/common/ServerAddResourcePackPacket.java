package net.hypejet.jet.server.network.packet.packets.server.common;

import java.util.Objects;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

/**
 * Represents {@linkplain ServerPacket a server packet}, which requests a client to download a resource pack.
 *
 * @param uniqueId a unique identifier of the resource pack
 * @param url a download url of the resource pack
 * @param hash an SHA-1 hash of the resource pack file
 * @param forced whether the resource pack is required
 * @param prompt a message shown on a request screen, {@code null} if none
 * @since 1.0
 * @see ServerPacket
 */
public record ServerAddResourcePackPacket(@NonNull UUID uniqueId, @NonNull String url, @NonNull String hash,
                                          boolean forced, @Nullable Component prompt) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerAddResourcePackPacket server add resource pack packet}.
     *
     * @param uniqueId a unique identifier of the resource pack
     * @param url a download url of the resource pack
     * @param hash an SHA-1 hash of the resource pack file
     * @param forced whether the resource pack is required
     * @param prompt a message shown on a request screen, {@code null} if none
     * @since 1.0
     */
    public ServerAddResourcePackPacket {
        Objects.requireNonNull(uniqueId, "unique id");
        Objects.requireNonNull(url, "url");
        Objects.requireNonNull(hash, "hash");
    }
}