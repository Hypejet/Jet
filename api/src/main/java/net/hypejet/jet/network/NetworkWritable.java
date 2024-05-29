package net.hypejet.jet.network;

import net.hypejet.jet.buffer.NetworkBuffer;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a Minecraft protocol object, which can be written into a {@linkplain NetworkBuffer network buffer}.
 *
 * @since 1.0
 * @author Codestech
 * @see NetworkBuffer
 */
public interface NetworkWritable {
    /**
     * WRites the object to a {@linkplain NetworkBuffer network buffer}.
     *
     * @param buffer the network buffer
     * @since 1.0
     */
    void write(@NonNull NetworkBuffer buffer);
}