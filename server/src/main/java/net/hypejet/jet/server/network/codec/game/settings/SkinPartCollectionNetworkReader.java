package net.hypejet.jet.server.network.codec.game.settings;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.server.network.codec.NetworkReader;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads {@linkplain Collection a collection}
 * of {@linkplain Player.SkinPart player skin parts}.
 *
 * @since 1.0
 * @see Player.SkinPart
 * @see Collection
 * @see NetworkReader
 */
public final class SkinPartCollectionNetworkReader implements NetworkReader<Collection<Player.SkinPart>> {

    private static final EnumMap<Player.SkinPart, Byte> SKIN_BIT_MASKS = new EnumMap<>(Player.SkinPart.class);

    /**
     * An instance of the {@linkplain SkinPartCollectionNetworkReader skin part collection network reader}.
     *
     * @since 1.0
     */
    public static final SkinPartCollectionNetworkReader INSTANCE = new SkinPartCollectionNetworkReader();

    static {
        SKIN_BIT_MASKS.put(Player.SkinPart.CAPE, (byte) 0x01);
        SKIN_BIT_MASKS.put(Player.SkinPart.JACKET, (byte) 0x02);
        SKIN_BIT_MASKS.put(Player.SkinPart.LEFT_SLEEVE, (byte) 0x04);
        SKIN_BIT_MASKS.put(Player.SkinPart.RIGHT_SLEEVE, (byte) 0x08);
        SKIN_BIT_MASKS.put(Player.SkinPart.LEFT_PANTS, (byte) 0x10);
        SKIN_BIT_MASKS.put(Player.SkinPart.RIGHT_PANTS, (byte) 0x20);
        SKIN_BIT_MASKS.put(Player.SkinPart.HAT, (byte) 0x40);
    }

    private SkinPartCollectionNetworkReader() {}

    @Override
    public @NonNull Collection<Player.SkinPart> read(@NonNull ByteBuf buf) {
        byte skinPartsByte = buf.readByte();
        List<Player.SkinPart> skinParts = new ArrayList<>();

        for (Player.SkinPart part : Player.SkinPart.values()) {
            if ((skinPartsByte & SKIN_BIT_MASKS.get(part)) != 0) {
                skinParts.add(part);
            }
        }

        return List.copyOf(skinParts);
    }
}