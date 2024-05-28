package net.hypejet.jet.server.buffer;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.buffer.ReadOnlyNetworkBuffer;
import net.hypejet.jet.server.util.NetworkUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.value.qual.IntRange;

import java.util.UUID;

public sealed class ReadOnlyNetworkBufferImpl implements ReadOnlyNetworkBuffer permits NetworkBufferImpl {

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
        return NetworkUtil.readString(this.buf);
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
        return NetworkUtil.readVarInt(this.buf);
    }

    @Override
    public long readVarLong() {
        return NetworkUtil.readVarLong(this.buf);
    }

    @Override
    public @NonNull UUID readUniqueId() {
        return new UUID(this.buf.readLong(), this.buf.readLong());
    }
}