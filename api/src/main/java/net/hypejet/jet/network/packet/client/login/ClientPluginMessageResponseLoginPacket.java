package net.hypejet.jet.network.packet.client.login;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.network.packet.client.ClientPacket;
import net.hypejet.jet.network.packet.server.login.ServerPluginMessageRequestLoginPacket;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents {@linkplain ClientPacket a client packet}, which is a response of client for a plugin message request
 * sent by a server.
 *
 * @param messageId a numeric identifier of the plugin message request
 * @param successful whether the client understood the plugin message
 * @param data a response data, {@code null} if none
 * @since 1.0
 * @author Codestech
 * @see ServerPluginMessageRequestLoginPacket
 * @see ClientPacket
 */
public record ClientPluginMessageResponseLoginPacket(int messageId, boolean successful,
                                                     @Nullable UnmodifiableByteArray data) implements ClientPacket {
    /**
     * Constructs the {@linkplain ClientPluginMessageResponseLoginPacket client plugin message response login packet}.
     *
     * @param messageId a numeric identifier of the plugin message request
     * @param successful whether the client understood the plugin message
     * @param data a response data, {@code null} if none
     * @since 1.0
     */
    public ClientPluginMessageResponseLoginPacket(int messageId, boolean successful, byte @Nullable [] data) {
        this(messageId, successful, data == null ? null : new UnmodifiableByteArray(data));
    }

    /**
     * Constructs the {@linkplain ClientPluginMessageResponseLoginPacket client plugin message response login packet}.
     *
     * @param messageId a numeric identifier of the plugin message request
     * @param successful whether the client understood the plugin message
     * @param data a response data, {@code null} if none
     * @since 1.0
     */
    public ClientPluginMessageResponseLoginPacket {
        NullabilityUtil.requireNonNull(data, "data");
    }
}