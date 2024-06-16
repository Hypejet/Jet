package net.hypejet.jet.server.entity.player;

import net.hypejet.jet.entity.EntityType;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.protocol.connection.PlayerConnection;
import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.hypejet.jet.server.entity.JetEntity;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Represents an implementation of {@linkplain Player player}.
 *
 * @since 1.0
 * @author Codestech
 * @see Player
 * @see JetEntity
 */
public final class JetPlayer extends JetEntity implements Player {

    private final String username;
    private final PlayerConnection connection;

    /**
     * Constructs a {@linkplain JetPlayer player}.
     *
     * @param uniqueId an unique identifier of the player
     * @param username a username of the player
     * @param connection a connection of the player
     * @since 1.0
     */
    public JetPlayer(@NonNull UUID uniqueId, @NonNull String username, @NonNull PlayerConnection connection) {
        super(EntityType.PLAYER, uniqueId);

        this.username = username;
        this.connection = connection;
    }

    @Override
    public @NotNull String username() {
        return this.username;
    }

    @Override
    public @NotNull PlayerConnection connection() {
        return this.connection;
    }

    @Override
    public void sendPacket(@NotNull ServerPacket packet) {
        this.connection.sendPacket(packet);
    }

    @Override
    public void disconnect(@NotNull Component reason) {
        this.connection.disconnect(reason);
    }

    @Override
    public void applyPointers(Pointers.@NonNull Builder pointers) {
        pointers.withStatic(Identity.NAME, this.username);
    }
}