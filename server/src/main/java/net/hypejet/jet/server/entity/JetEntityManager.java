package net.hypejet.jet.server.entity;

import net.hypejet.jet.entity.EntityManager;
import net.hypejet.jet.entity.EntityType;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.world.coordinate.Position;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;
import java.util.UUID;

/**
 * An implementation of the {@linkplain EntityManager entity manager}.
 *
 * @since 1.0
 * @see EntityManager
 */
@NullMarked
public final class JetEntityManager implements EntityManager {

    private final JetMinecraftServer server;

    /**
     * Constructs the {@linkplain JetEntityManager entity manager implementation}.
     *
     * @param server a server that the created entities should belong to
     * @since 1.0
     */
    public JetEntityManager(JetMinecraftServer server) {
        this.server = Objects.requireNonNull(server, "server");
    }

    @Override
    public JetEntity createEntity(Holder.Reference<EntityType> entityType) {
        return this.createEntity(entityType, UUID.randomUUID());
    }

    @Override
    public JetEntity createEntity(Holder.Reference<EntityType> entityType, UUID uniqueId) {
        return this.createEntity(entityType, uniqueId, Position.zero());
    }

    @Override
    public JetEntity createEntity(Holder.Reference<EntityType> entityType, UUID uniqueId, Position position) {
        return new JetEntity(entityType, uniqueId, position, this.server);
    }
}