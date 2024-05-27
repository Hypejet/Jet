package net.hypejet.jet.server.buffer;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.buffer.ReadOnlyNetworkBuffer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.value.qual.IntRange;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public sealed class ReadOnlyNetworkBufferImpl implements ReadOnlyNetworkBuffer permits NetworkBufferImpl {

    protected static final byte SEGMENT_BITS = 0x7F;
    protected static final int CONTINUE_BIT = 0x80;

    protected static final short MAX_STRING_SIZE = 32767;

    protected final ByteBuf buf;

    public ReadOnlyNetworkBufferImpl(@NonNull ByteBuf buf) {
        this.buf = buf;
    }

    @Override
    public boolean readBoolean() {
        return this.buf.readBoolean();
    }

    @Override
    public byte readByte() {
        return this.buf.readByte();
    }

    @Override
    public @IntRange(from = 0, to = 255) short readUnsignedByte() {
        return this.buf.readUnsignedByte();
    }

    @Override
    public short readShort() {
        return this.buf.readShort();
    }

    @Override
    public @IntRange(from = 0, to = 65535) int readUnsignedShort() {
        return this.buf.readUnsignedShort();
    }

    @Override
    public int readInt() {
        return this.buf.readInt();
    }

    @Override
    public long readLong() {
        return this.buf.readLong();
    }

    @Override
    public float readFloat() {
        return this.buf.readFloat();
    }

    @Override
    public double readDouble() {
        return this.buf.readDouble();
    }

    @Override
    public @NonNull String readString() {
        int length = this.readVarInt();

        if (length < 0 || length > MAX_STRING_SIZE)
            throw new IllegalArgumentException("Invalid length of a string - " + length);

        //if (this.buf.isReadable(length))
            //throw new IllegalArgumentException("A buffer does not contain at least " + length + " readable bytes");

        String string = this.buf.toString(this.buf.readerIndex(), length, StandardCharsets.UTF_8);
        this.buf.skipBytes(length);

        return string;
    }

    @Override
    public @NonNull Component readTextComponent() {
        return null; // TODO
    }

    @Override
    public @NonNull Component readJsonTextComponent() {
        return GsonComponentSerializer.gson().deserialize(this.readString());
    }

    @Override
    public int readVarInt() {
        int value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = this.buf.readByte();
            value |= (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;

            if (position >= 32) throw new IllegalArgumentException("VarInt is too big");
        }

        return value;
    }

    @Override
    public long readVarLong() {
        long value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = readByte();
            value |= (long) (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;

            if (position >= 64) throw new IllegalArgumentException("VarLong is too big");
        }

        return value;
    }

    @Override
    public @NonNull UUID readUniqueId() {
        return new UUID(this.buf.readLong(), this.buf.readLong());
    }
}