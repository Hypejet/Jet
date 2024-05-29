package net.hypejet.jet.server.buffer;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.buffer.NetworkBuffer;
import net.hypejet.jet.server.util.NetworkUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.value.qual.IntRange;

import java.util.UUID;

/**
 * Represents an implementation of {@link NetworkBuffer network buffer}, which reads data from
 * netty's {@link ByteBuf byte buf}.
 *
 * @since 1.0
 * @author Codestech
 */
public final class NetworkBufferImpl implements NetworkBuffer {

    private final ByteBuf buf;

    /**
     * Constructs a {@link NetworkBufferImpl network buffer}.
     *
     * @param buf a netty's {@link ByteBuf byte buf} to read data from
     * @since 1.0
     */
    public NetworkBufferImpl(@NonNull ByteBuf buf) {
        this.buf = buf;
    }

    @Override
    public boolean readBoolean() {
        return this.buf.readBoolean();
    }

    @Override
    public void writeBoolean(boolean value) {
        this.buf.writeBoolean(value);
    }

    @Override
    public byte readByte() {
        return this.buf.readByte();
    }

    @Override
    public void writeByte(byte value) {
        this.buf.writeByte(value);
    }

    @Override
    public @IntRange(from = 0, to = 255) short readUnsignedByte() {
        return this.buf.readUnsignedByte();
    }

    @Override
    public void writeUnsignedByte(@IntRange(from = 0, to = 255) short value) {
        this.buf.writeByte(value);
    }

    @Override
    public short readShort() {
        return this.buf.readShort();
    }

    @Override
    public void writeShort(short value) {
        this.buf.writeShort(value);
    }

    @Override
    public @IntRange(from = 0, to = 65535) int readUnsignedShort() {
        return this.buf.readUnsignedShort();
    }

    @Override
    public void writeUnsignedShort(@IntRange(from = 0, to = 65535) int value) {
        this.buf.writeShort(value);
    }

    @Override
    public int readInt() {
        return this.buf.readInt();
    }

    @Override
    public void writeInt(int value) {
        this.buf.writeInt(value);
    }

    @Override
    public long readLong() {
        return this.buf.readLong();
    }

    @Override
    public void writeLong(long value) {
        this.buf.writeLong(value);
    }

    @Override
    public float readFloat() {
        return this.buf.readFloat();
    }

    @Override
    public void writeFloat(float value) {
        this.buf.writeFloat(value);
    }

    @Override
    public double readDouble() {
        return this.buf.readDouble();
    }

    @Override
    public void writeDouble(double value) {
        this.buf.writeDouble(value);
    }

    @Override
    public @NonNull String readString() {
        return NetworkUtil.readString(this.buf);
    }

    @Override
    public void writeString(@NonNull String value) {
        NetworkUtil.writeString(this.buf, value);
    }

    @Override
    public @NonNull Component readTextComponent() {
        return null; // TODO, not implemented yet, because kyori adventure does not have an NBT serializer *yet*
    }

    @Override
    public void writeTextComponent(@NonNull Component value) {
        // TODO, not implemented yet, because kyori adventure does not have an NBT serializer *yet*
    }

    @Override
    public @NonNull Component readJsonTextComponent() {
        return GsonComponentSerializer.gson().deserialize(this.readString());
    }

    @Override
    public void writeJsonTextComponent(@NonNull Component value) {
        this.writeString(GsonComponentSerializer.gson().serialize(value));
    }

    @Override
    public int readVarInt() {
        return NetworkUtil.readVarInt(this.buf);
    }

    @Override
    public void writeVarInt(int value) {
        NetworkUtil.writeVarInt(this.buf, value);
    }

    @Override
    public long readVarLong() {
        return NetworkUtil.readVarLong(this.buf);
    }

    @Override
    public void writeVarLong(long value) {
        NetworkUtil.writeVarLong(this.buf, value);
    }

    @Override
    public @NonNull UUID readUniqueId() {
        return new UUID(this.buf.readLong(), this.buf.readLong());
    }

    @Override
    public void writeUniqueId(@NonNull UUID value) {
        this.buf.writeLong(value.getMostSignificantBits());
        this.buf.writeLong(value.getLeastSignificantBits());
    }

    @Override
    public boolean isReadable(int length) {
        return this.buf.isReadable(length);
    }

    @Override
    public byte @NonNull [] toByteArray() {
        return this.buf.array();
    }

    @Override
    public byte @NonNull [] readByteArray(boolean readLength) {
        byte[] array;

        if (readLength) {
            int length = this.readVarInt();
            if (!this.isReadable(length))
                throw new IllegalArgumentException("An amount of remaining bytes is lower than a string size");
            array = new byte[length];
        } else {
            array = new byte[this.buf.readableBytes()];
        }

        for (int i = 0; i < array.length; i++) {
            array[i] = this.readByte();
        }

        return array;
    }
}
