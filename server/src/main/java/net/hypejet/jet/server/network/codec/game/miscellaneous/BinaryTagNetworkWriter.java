package net.hypejet.jet.server.network.codec.game.miscellaneous;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagType;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain BinaryTag a binary tag}.
 *
 * @since 1.0
 * @author Codestech
 * @see BinaryTag
 * @see NetworkWriter
 */
public final class BinaryTagNetworkWriter implements NetworkWriter<BinaryTag> {

    /**
     * An instance of the {@linkplain BinaryTagNetworkWriter binary tag network writer}.
     *
     * @since 1.0
     */
    public static final BinaryTagNetworkWriter INSTANCE = new BinaryTagNetworkWriter();

    private BinaryTagNetworkWriter() {}

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
}