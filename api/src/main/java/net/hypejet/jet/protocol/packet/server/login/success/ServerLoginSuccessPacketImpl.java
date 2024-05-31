package net.hypejet.jet.protocol.packet.server.login.success;

import net.hypejet.jet.buffer.NetworkBuffer;
import net.hypejet.jet.player.profile.properties.GameProfileProperties;
import net.hypejet.jet.protocol.ProtocolState;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

/**
 * Represents an implementation of {@linkplain ServerLoginSuccessPacket login success packet}.
 *
 * @param uniqueId a unique identifier of a player logging in
 * @param username a username of a player logging in
 * @param properties a collection of {@linkplain GameProfileProperties game profile properties} of a player logging in
 * @param strictErrorHandling whether a client should disconnect on invalid server data
 * @since 1.0
 * @author Codestech
 * @see ServerLoginSuccessPacket
 */
record ServerLoginSuccessPacketImpl(@NonNull UUID uniqueId, @NonNull String username,
                                    @NonNull Collection<GameProfileProperties> properties,
                                    boolean strictErrorHandling) implements ServerLoginSuccessPacket {
    @Override
    public int getPacketId() {
        return 2;
    }

    @Override
    public void write(@NonNull NetworkBuffer buffer) {
        buffer.writeUniqueId(this.uniqueId);
        buffer.writeString(this.username);
        buffer.writeCollection(this.properties);
        buffer.writeBoolean(this.strictErrorHandling);
    }

    /**
     * Represents an implementation of {@linkplain ServerLoginSuccessPacket.Builder login success packet builder}.
     *
     * @since 1.0
     * @author Codestech
     * @see ServerLoginSuccessPacket.Builder
     */
    static final class Builder implements ServerLoginSuccessPacket.Builder {

        private UUID uniqueId = UUID.randomUUID();
        private String username = "";
        private Collection<GameProfileProperties> properties = Collections.emptySet();
        private boolean strictErrorHandling;

        @Override
        public ServerLoginSuccessPacket.@NonNull Builder uniqueId(@NonNull UUID uniqueId) {
            this.uniqueId = uniqueId;
            return this;
        }

        @Override
        public ServerLoginSuccessPacket.@NonNull Builder username(@NonNull String username) {
            this.username = username;
            return this;
        }

        @Override
        public ServerLoginSuccessPacket.@NonNull Builder properties(
                @NonNull Collection<GameProfileProperties> properties
        ) {
            this.properties = properties;
            return this;
        }

        @Override
        public ServerLoginSuccessPacket.@NonNull Builder strictErrorHandling(boolean strictErrorHandling) {
            this.strictErrorHandling = strictErrorHandling;
            return this;
        }

        @Override
        public @NonNull ServerLoginSuccessPacket build() {
            return new ServerLoginSuccessPacketImpl(this.uniqueId, this.username, this.properties,
                    this.strictErrorHandling);
        }
    }
}