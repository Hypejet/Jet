package net.hypejet.jet.protocol.properties;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

/**
 * Represents properties of a Minecraft game profile, retrieved from the Mojang API.
 *
 * @param uniqueId a unique identifier of the profile
 * @param username a username of the profile
 * @param signature a signature of the profile, which is optional
 * @since 1.0
 * @author Codestech
 */
public record GameProfileProperties(@NonNull UUID uniqueId, @NonNull String username, @Nullable String signature) {}