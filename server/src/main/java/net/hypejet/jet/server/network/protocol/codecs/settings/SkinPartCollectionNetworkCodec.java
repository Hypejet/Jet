package net.hypejet.jet.server.network.protocol.codecs.settings;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a collection
 * of {@linkplain Player.SkinPart player skin parts}.
 *
 * @since 1.0
 * @author Codestech
 * @see Player.SkinPart
 * @see Collection
 * @see NetworkCodec
 */
public final class SkinPartCollectionNetworkCodec implements NetworkCodec<Collection<Player.SkinPart>> {

    private static final SkinPartCollectionNetworkCodec INSTANCE = new SkinPartCollectionNetworkCodec();

    private static final EnumMap<Player.SkinPart, Byte> skinBitMasks = new EnumMap<>(Player.SkinPart.class);

    static {
        skinBitMasks.put(Player.SkinPart.CAPE, (byte) 0x01);
        skinBitMasks.put(Player.SkinPart.JACKET, (byte) 0x02);
        skinBitMasks.put(Player.SkinPart.LEFT_SLEEVE, (byte) 0x04);
        skinBitMasks.put(Player.SkinPart.RIGHT_SLEEVE, (byte) 0x08);
        skinBitMasks.put(Player.SkinPart.LEFT_PANTS, (byte) 0x10);
        skinBitMasks.put(Player.SkinPart.RIGHT_PANTS, (byte) 0x20);
        skinBitMasks.put(Player.SkinPart.HAT, (byte) 0x40);
    }

    private SkinPartCollectionNetworkCodec() {}

    @Override
    public @NonNull Collection<Player.SkinPart> read(@NonNull ByteBuf buf) {
        byte skinPartsByte = buf.readByte();
        List<Player.SkinPart> skinParts = new ArrayList<>();

        for (Player.SkinPart part : Player.SkinPart.values()) {
            if ((skinPartsByte & skinBitMasks.get(part)) != 0) {
                skinParts.add(part);
            }
        }

        return List.copyOf(skinParts);
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Collection<Player.SkinPart> object) {
        byte skinPartsByte = 0;
        for (Player.SkinPart part : object) {
            skinPartsByte = (byte) (skinPartsByte | skinBitMasks.get(part));
        }
        buf.writeByte(skinPartsByte);
    }

    /**
     * Gets an instance of {@linkplain SkinPartCollectionNetworkCodec skin part collection network codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull SkinPartCollectionNetworkCodec instance() {
        return INSTANCE;
    }
}