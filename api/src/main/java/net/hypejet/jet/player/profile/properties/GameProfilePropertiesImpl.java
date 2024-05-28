package net.hypejet.jet.player.profile.properties;

import net.hypejet.jet.buffer.NetworkBuffer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

/**
 * Represents an implementation of {@link GameProfileProperties game profile properties}.
 *
 * @param uniqueId an unique identifier of a Minecraft player
 * @param username a username of a Minecraft player
 * @param signature an optional signature of a Minecraft player
 * @since 1.0
 * @author Codestech
 * @see GameProfileProperties
 */
record GameProfilePropertiesImpl(@NonNull UUID uniqueId, @NonNull String username,
                                 @Nullable String signature) implements GameProfileProperties {
    @Override
    public void write(@NonNull NetworkBuffer buffer) {
        buffer.writeUniqueId(this.uniqueId);
        buffer.writeString(this.username);
        buffer.writeOptionalString(this.signature);
    }

    /**
     * Represents an implementation of {@link GameProfileProperties.Builder game profile properties builder}.
     *
     * @since 1.0
     * @author Codestech
     * @see GameProfileProperties.Builder
     */
    static final class Builder implements GameProfileProperties.Builder {

        private UUID uniqueId = UUID.randomUUID();
        private String username = "";
        private String signature = null;

        @Override
        public GameProfileProperties.@NonNull Builder uniqueId(@NonNull UUID uniqueId) {
            this.uniqueId = uniqueId;
            return this;
        }

        @Override
        public GameProfileProperties.@NonNull Builder username(@NonNull String username) {
            this.username = username;
            return this;
        }

        @Override
        public GameProfileProperties.@NonNull Builder signature(@Nullable String signature) {
            this.signature = signature;
            return this;
        }

        @Override
        public @NonNull GameProfileProperties build() {
            return new GameProfilePropertiesImpl(this.uniqueId, this.username, this.signature);
        }
    }
}