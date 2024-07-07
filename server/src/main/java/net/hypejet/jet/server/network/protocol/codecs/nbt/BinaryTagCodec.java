package net.hypejet.jet.server.network.protocol.codecs.nbt;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagType;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain BinaryTag binary tag}.
 *
 * @since 1.0
 * @author Codestech
 * @see BinaryTag
 * @see NetworkCodec
 */
public final class BinaryTagCodec implements NetworkCodec<BinaryTag> {

    private static final Method TAG_TYPE_METHOD; // The method is not exposed :(
    private static final BinaryTagCodec INSTANCE;

    static {
        try {
            TAG_TYPE_METHOD = BinaryTagType.class.getDeclaredMethod("binaryTagType", byte.class);
            INSTANCE = new BinaryTagCodec();
        } catch (NoSuchMethodException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private BinaryTagCodec() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public @NonNull BinaryTag read(@NonNull ByteBuf buf) {
        try {
            boolean accessible = TAG_TYPE_METHOD.canAccess(null);
            if (!accessible) TAG_TYPE_METHOD.setAccessible(true);

            BinaryTagType<?> type = (BinaryTagType<?>) TAG_TYPE_METHOD.invoke(null, buf.readByte());
            BinaryTag tag = type.read(new ByteBufInputStream(buf));

            if (!accessible) TAG_TYPE_METHOD.setAccessible(false);
            return tag;
        } catch (IllegalAccessException | InvocationTargetException | IOException exception) {
            throw new IllegalStateException(exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull BinaryTag object) {
        try {
            BinaryTagType type = object.type();
            buf.writeByte(type.id());
            type.write(object, new ByteBufOutputStream(buf));
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }
    }

    /**
     * Gets an instance of a {@linkplain BinaryTagCodec binary tag codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull BinaryTagCodec instance() {
        return INSTANCE;
    }
}