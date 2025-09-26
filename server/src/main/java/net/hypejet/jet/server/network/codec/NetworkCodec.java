package net.hypejet.jet.server.network.codec;

/**
 * A combination of {@linkplain NetworkReader network reader} and {@linkplain NetworkWriter network writer}.
 *
 * @param <T> a type of the object
 * @since 1.0
 * @see NetworkReader
 * @see NetworkWriter
 */
public interface NetworkCodec<T> extends NetworkReader<T>, NetworkWriter<T> {}