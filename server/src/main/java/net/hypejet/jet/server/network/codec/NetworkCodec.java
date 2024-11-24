package net.hypejet.jet.server.network.codec;

/**
 * Represents a combination of {@linkplain NetworkReader a network reader}
 * and {@linkplain NetworkWriter a network writer}.
 *
 * @param <T> a type of the object
 * @since 1.0
 * @author Codestech
 */
public interface NetworkCodec<T> extends NetworkReader<T>, NetworkWriter<T> {}