package net.hypejet.jet.server.network.session.pack;

import net.hypejet.concurrency.collection.CollectionAcquisition;
import net.hypejet.concurrency.collection.set.HashSetAcquirable;
import net.hypejet.concurrency.collection.set.SetAcquirable;
import net.hypejet.concurrency.map.MapAcquirable;
import net.hypejet.concurrency.map.MapAcquisition;
import net.hypejet.concurrency.map.hashmap.HashMapAcquirable;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerAddResourcePackPacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerRemoveResourcePackPacket;
import net.hypejet.jet.server.util.game.audience.PacketReceivingAudience;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.resource.ResourcePackInfo;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.resource.ResourcePackStatus;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents something that manages resource packs for {@linkplain SocketPlayerConnection a player connection}.
 *
 * @since 1.0
 * @see SocketPlayerConnection
 */
public final class ResourcePackHandler {

    private final MapAcquirable<UUID, ResourcePackRequest, ?> pendingPacks = new HashMapAcquirable<>();
    private final SetAcquirable<UUID> appliedPacks = new HashSetAcquirable<>();

    private final SocketPlayerConnection connection;

    /**
     * Constructs the {@linkplain ResourcePackHandler resource pack handler}.
     *
     * @param connection a player connection that the resource pack management should be done for
     * @since 1.0
     */
    public ResourcePackHandler(@NonNull SocketPlayerConnection connection) {
        this.connection = Objects.requireNonNull(connection, "connection");
    }

    /**
     * Performs {@linkplain ResourcePackRequest a resource pack request} specified.
     *
     * @param request the resource pack request
     * @since 1.0
     */
    public void request(@NonNull ResourcePackRequest request) {
        Objects.requireNonNull(request, "request");
        try (MapAcquisition<UUID, ResourcePackRequest, ?> pendingAcquisition = this.pendingPacks.acquireWrite()) {
            if (request.replace())
                this.clear();

            try (CollectionAcquisition<UUID, ?> appliedPacksAcquisition = this.appliedPacks.acquireRead()) {
                Map<UUID, ResourcePackRequest> pendingPacks = pendingAcquisition.map();
                Collection<UUID> appliedPacks = appliedPacksAcquisition.collection();

                for (ResourcePackInfo pack : request.packs()) {
                    UUID uniqueId = pack.id();

                    if (pendingPacks.containsKey(uniqueId)) {
                        throw new IllegalArgumentException(String.format(
                                "A resource pack with unique identifier of %s has already been scheduled for loading",
                                uniqueId
                        ));
                    }

                    if (appliedPacks.contains(uniqueId)) {
                        throw new IllegalArgumentException(String.format(
                                "A resource pack with unique identifier of %s has already been applied",
                                uniqueId
                        ));
                    }

                    pendingAcquisition.map().put(pack.id(), request);
                    this.connection.sendPacket(new ServerAddResourcePackPacket(
                            uniqueId, pack.uri().toString(), pack.hash(), request.required(), request.prompt()
                    ));
                }
            }
        }
    }

    /**
     * Requests a client associated with the {@linkplain PacketReceivingAudience packet-receiving audience} to remove
     * resource packs with {@linkplain UUID unique identifiers} specified.
     *
     * @param packs the unique identifiers of the resource packs
     * @since 1.0
     */
    public void remove(@NonNull Collection<UUID> packs) {
        Objects.requireNonNull(packs, "packs");
        if (packs.isEmpty()) return;

        try (
                MapAcquisition<UUID, ResourcePackRequest, ?> pendingAcquisition = this.pendingPacks.acquireWrite();
                CollectionAcquisition<UUID, ?> appliedPacksAcquisition = this.appliedPacks.acquireWrite()
        ) {
            Map<UUID, ResourcePackRequest> pendingPacks = pendingAcquisition.map();
            Collection<UUID> appliedPacks = appliedPacksAcquisition.collection();

            if (packs.containsAll(appliedPacks) && packs.containsAll(pendingPacks.keySet())) {
                this.clear();
                return;
            }

            for (UUID pack : packs) {
                if (pendingPacks.remove(pack) == null && !appliedPacks.remove(pack)) continue;
                this.connection.sendPacket(new ServerRemoveResourcePackPacket(pack));
            }
        }
    }

    /**
     * Requests a client associated with the {@linkplain PacketReceivingAudience packet-receiving audience} to remove
     * all resource packs that were requested to be loaded.
     *
     * @since 1.0
     */
    public void clear() {
        try (
                MapAcquisition<UUID, ResourcePackRequest, ?> pendingAcquisition = this.pendingPacks.acquireWrite();
                CollectionAcquisition<UUID, ?> appliedPacksAcquisition = this.appliedPacks.acquireWrite()
        ) {
            Map<UUID, ResourcePackRequest> pendingPacks = pendingAcquisition.map();
            Collection<UUID> appliedPacks = appliedPacksAcquisition.collection();

            if (pendingPacks.isEmpty() && appliedPacks.isEmpty())
                return;

            pendingPacks.clear();
            appliedPacks.clear();

            this.connection.sendPacket(new ServerRemoveResourcePackPacket(null));
        }
    }

    /**
     * Handles a status of loading a resource pack.
     *
     * @param uniqueId a unique identifier of the resource pack
     * @param status the status
     * @param audience an audience associated with the resource pack loading
     * @since 1.0
     */
    public void handleState(@NonNull UUID uniqueId, @NonNull ResourcePackStatus status, @NonNull Audience audience) {
        Objects.requireNonNull(uniqueId, "unique identifier");
        Objects.requireNonNull(status, "status");
        Objects.requireNonNull(audience, "audience");

        try (MapAcquisition<UUID, ResourcePackRequest, ?> acquisition = this.pendingPacks.acquireWrite()) {
            Map<UUID, ResourcePackRequest> requests = acquisition.map();

            ResourcePackRequest request = requests.get(uniqueId);
            if (request == null) return;

            if (!status.intermediate())
                requests.remove(uniqueId);

            if (status == ResourcePackStatus.SUCCESSFULLY_LOADED) {
                try (CollectionAcquisition<UUID, ?> appliedAcquisition = this.appliedPacks.acquireWrite()) {
                    appliedAcquisition.collection().add(uniqueId);
                }
            }

            request.callback().packEventReceived(uniqueId, status, audience);
        }
    }
}