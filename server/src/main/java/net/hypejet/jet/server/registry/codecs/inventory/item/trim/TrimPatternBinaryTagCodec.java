package net.hypejet.jet.server.registry.codecs.inventory.item.trim;

import net.hypejet.jet.inventory.item.trim.TrimPattern;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.util.codec.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.ComponentBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.KeyBinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.booleanValue;
import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain TrimPattern trim patterns}.
 *
 * @since 1.0
 * @see TrimPattern
 * @see BinaryTagCodec
 */
@NullMarked
public final class TrimPatternBinaryTagCodec implements BinaryTagCodec<TrimPattern> {

    private static final String ASSET_FIELD = "asset_id";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String DECAL_FIELD = "decal";

    /**
     * An instance of the {@linkplain TrimPatternBinaryTagCodec trim-pattern binary tag codec}.
     *
     * @since 1.0
     */
    public static final TrimPatternBinaryTagCodec INSTANCE = new TrimPatternBinaryTagCodec();

    private TrimPatternBinaryTagCodec() {}

    @Override
    public TrimPattern decode(BinaryTag binaryTag, JetMinecraftServer server) {
        if (binaryTag instanceof CompoundBinaryTag compound) {
            return new TrimPattern(
                    KeyBinaryTagCodec.INSTANCE.decode(requiredTag(ASSET_FIELD, compound), server),
                    ComponentBinaryTagCodec.INSTANCE.decode(requiredTag(DESCRIPTION_FIELD, compound), server),
                    booleanValue(requiredTag(DECAL_FIELD, compound, BinaryTagTypes.BYTE))
            );
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag must be of compound type to decode it to a trim pattern"
            );
        }
    }

    @Override
    public BinaryTag encode(TrimPattern value, JetMinecraftServer server) {
        return CompoundBinaryTag.builder()
                .put(ASSET_FIELD, KeyBinaryTagCodec.INSTANCE.encode(value.asset(), server))
                .put(DESCRIPTION_FIELD, ComponentBinaryTagCodec.INSTANCE.encode(value.description(), server))
                .putBoolean(DECAL_FIELD, value.decal())
                .build();
    }
}