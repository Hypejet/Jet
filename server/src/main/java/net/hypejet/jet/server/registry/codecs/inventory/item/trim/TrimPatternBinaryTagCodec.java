package net.hypejet.jet.server.registry.codecs.inventory.item.trim;

import net.hypejet.jet.inventory.item.trim.TrimPattern;
import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
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
    public TrimPattern decode(BinaryTag binaryTag) {
        if (binaryTag instanceof CompoundBinaryTag compound) {
            return new TrimPattern(
                    KeyBinaryTagCodec.INSTANCE.decode(requiredTag(ASSET_FIELD, compound)),
                    ComponentBinaryTagCodec.INSTANCE.decode(requiredTag(DESCRIPTION_FIELD, compound)),
                    booleanValue(requiredTag(DECAL_FIELD, compound, BinaryTagTypes.BYTE))
            );
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag must be of compound type to decode it to a trim pattern"
            );
        }
    }

    @Override
    public BinaryTag encode(TrimPattern value) {
        return CompoundBinaryTag.builder()
                .put(ASSET_FIELD, KeyBinaryTagCodec.INSTANCE.encode(value.asset()))
                .put(DESCRIPTION_FIELD, ComponentBinaryTagCodec.INSTANCE.encode(value.description()))
                .putBoolean(DECAL_FIELD, value.decal())
                .build();
    }
}