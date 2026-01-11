package net.hypejet.jet.server.entity.metadata;

import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.hypejet.jet.entity.EntityType;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerEntityMetadataPlayPacket;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Metadata of an {@linkplain JetEntity entity}.
 *
 * @since 1.0
 * @see JetEntity
 */
@NullMarked
public final class EntityMetadata {

    private final int entityId;
    private final Consumer<ServerPacket> updatePacketSender;
    private final IntObjectMap<EntityMetadataValue> defaultValues;

    private final IntObjectMap<EntityMetadataValue> networkValues = new IntObjectHashMap<>();

    /**
     * Constructs the {@linkplain EntityMetadata entity metadata}.
     *
     * @param server the server of the entity that the entity metadata is being constructed for
     * @param entityType the type of the entity that the entity metadata is being constructed for
     * @param entityId numeric identifier of the entity that the entity metadata is being constructed for
     * @param updatePacketSender a consumer consuming metadata update packets and sending them to the entity itself
     *                           and viewers of the entity that the entity metadata is being constructed for
     * @since 1.0
     */
    public EntityMetadata(JetMinecraftServer server, Holder.Reference<EntityType> entityType,
                          int entityId, Consumer<ServerPacket> updatePacketSender) {
        this.entityId = entityId;
        this.updatePacketSender = updatePacketSender;
        this.defaultValues = EntityMetadataDefaults.defaultsFor(server, entityType);
    }

    /**
     * Gets an {@linkplain EntityMetadataValue entity metadata value} set
     * at the specified index in this {@linkplain EntityMetadata entity metadata}.
     *
     * @param index the index of the entity metadata value that should be returned
     * @param valueType the expected class of the entity metadata value
     * @return the entity metadata value
     * @param <V> the expected type of the entity metadata value
     * @throws IllegalArgumentException if the entity metadata value is not of the specified expected class
     * @since 1.0
     */
    public <V extends EntityMetadataValue> V value(int index, Class<V> valueType) {
        EntityMetadataValue value = this.networkValues.get(index);
        if (value == null) value = this.defaultValues.get(index);

        if (value == null) {
            throw new IllegalArgumentException(String.format(
                    "No entity metadata value was defined at %d index",
                    index
            ));
        } else if (!valueType.isInstance(value)) {
            throw new IllegalArgumentException(String.format(
                    "Entity metadata value at index %d is of %s type and cannot be cast to %s",
                    index, value.getClass().getSimpleName(), valueType.getSimpleName()
            ));
        }

        return valueType.cast(value);
    }

    /**
     * Replaces an {@linkplain EntityMetadataValue entity metadata value}
     * at the specified index in this {@linkplain EntityMetadata entity metadata}.
     *
     * @param index the index that the entity metadata value should be replaced at
     * @param valueType the expected class of the current entity metadata value,
     *                  new values should be of the same class
     * @param updatedValueProvider a unary operator consuming the current entity metadata value and providing
     *                             a new entity metadata value that should be present at the specified index
     * @param <V> the expected type of the current entity metadata value and the type of the new entity metadata value
     * @throws IllegalArgumentException if the current entity metadata value or the new entity
     *                                  metadata value is not of the specified expected class
     * @since 1.0
     */
    public <V extends EntityMetadataValue> void value(int index, Class<V> valueType,
                                                      UnaryOperator<V> updatedValueProvider) {
        V currentValue = this.value(index, valueType);
        V newValue = updatedValueProvider.apply(currentValue);

        if (!valueType.isInstance(newValue)) {
            throw new IllegalArgumentException(String.format(
                    "Entity metadata value at index %d expects %s type instead of %s",
                    index, valueType.getSimpleName(), newValue.getClass().getSimpleName()
            ));
        }

        if (currentValue.equals(newValue)) return;

        EntityMetadataValue defaultValue = this.defaultValues.get(index);
        if (defaultValue.equals(newValue)) {
            this.networkValues.remove(index);
        } else {
            this.networkValues.put(index, newValue);
        }

        // TODO: Send updates at the end of entity tick instead, like vanilla does
        this.updatePacketSender.accept(new ServerEntityMetadataPlayPacket(
                this.entityId,
                Set.of(new ServerEntityMetadataPlayPacket.Update(index, newValue))
        ));
    }

    /**
     * Creates a server packet initializing this entity metadata.
     *
     * @return the created initializing server packet, {@code null} if this
     *         entity metadata has no changes from the default entity metadata
     * @since 1.0
     */
    // TODO: Cache the packet
    public @Nullable ServerEntityMetadataPlayPacket createInitializationPacket() {
        if (this.networkValues.isEmpty()) return null;
        return ServerEntityMetadataPlayPacket.create(this.entityId, this.networkValues);
    }
}