package net.hypejet.jet.server.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import net.hypejet.jet.buffer.NetworkBuffer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.value.qual.IntRange;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public final class NetworkBufferImpl extends ReadOnlyNetworkBufferImpl implements NetworkBuffer {

    public NetworkBufferImpl(@NonNull ByteBuf buf) {
        super(buf);
    }

    @Override
    public void writeBoolean(boolean value) {
        this.buf.writeBoolean(value);
    }

    @Override
    public void writeByte(byte value) {
        this.buf.writeByte(value);
    }

    @Override
    public void writeUnsignedByte(@IntRange(from = 0, to = 255) short value) {
        this.buf.writeByte(value);
    }

    @Override
    public void writeShort(short value) {
        this.buf.writeShort(value);
    }

    @Override
    public void writeUnsignedShort(@IntRange(from = 0, to = 65535) int value) {
        this.buf.writeShort(value);
    }

    @Override
    public void writeInt(int value) {
        this.buf.writeInt(value);
    }

    @Override
    public void writeLong(long value) {
        this.buf.writeLong(value);
    }

    @Override
    public void writeFloat(float value) {
        this.buf.writeFloat(value);
    }

    @Override
    public void writeDouble(double value) {
        this.buf.writeDouble(value);
    }

    @Override
    public void writeString(@NonNull String value) {
        this.writeVarInt(ByteBufUtil.utf8Bytes(value));
        this.buf.writeCharSequence(value, StandardCharsets.UTF_8);
    }

    @Override
    public void writeTextComponent(@NonNull Component value) {
        // TODO
    }

    @Override
    public void writeJsonTextComponent(@NonNull Component value) {
        this.writeString(GsonComponentSerializer.gson().serialize(value));
    }

    @Override
    public void writeVarInt(int value) {
        while (true) {
            if ((value & ~SEGMENT_BITS) == 0) {
                this.buf.writeByte(value);
                return;
            }

            this.buf.writeByte((value & SEGMENT_BITS) | CONTINUE_BIT);
            value >>>= 7;
        }
    }

    @Override
    public void writeVarLong(long value) {
        while (true) {
            if ((value & ~((long) SEGMENT_BITS)) == 0) {
                this.buf.writeByte((int) value);
                return;
            }

            this.buf.writeByte((int) ((value & SEGMENT_BITS) | CONTINUE_BIT));
            value >>>= 7;
        }
    }

    @Override
    public void writeUniqueId(@NonNull UUID value) {
        this.buf.writeLong(value.getMostSignificantBits());
        this.buf.writeLong(value.getLeastSignificantBits());
    }
}
