package net.hypejet.jet.server.network.packet;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a serialized Minecraft packet.
 *
 * @param identifier an identifier of the packet
 * @param body a serialized body of the packet
 * @since 1.0
 */
public record RawPacket(int identifier, @NonNull UnmodifiableByteArray body) {
    /**
     * Constructs the {@linkplain RawPacket raw packet}.
     *
     * @param identifier an identifier of the packet
     * @param body a serialized body of the packet
     * @since 1.0
     */
    public RawPacket(int identifier, byte @NonNull [] body) {
        this(identifier, new UnmodifiableByteArray(body));
    }

    /**
     * Constructs the {@linkplain RawPacket raw packet}.
     *
     * @param identifier an identifier of the packet
     * @param body a serialized body of the packet
     * @since 1.0
     */
    public RawPacket {
        NullabilityUtil.requireNonNull(body, "body");
    }
}