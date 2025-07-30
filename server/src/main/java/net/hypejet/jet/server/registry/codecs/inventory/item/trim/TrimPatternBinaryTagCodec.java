package net.hypejet.jet.server.registry.codecs.inventory.item.trim;

import net.hypejet.jet.inventory.item.trim.TrimPattern;
import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.ComponentBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.KeyBinaryTagCodec;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jetbrains.annotations.NotNull;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.booleanValue;
import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary tag codec} of {@linkplain TrimPattern trim patterns}.
 *
 * @since 1.0
 * @see TrimPattern
 * @see BinaryTagCodec
 */
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
    public @NotNull TrimPattern decode(@NotNull BinaryTag encoded) throws Exception {
        if (encoded instanceof CompoundBinaryTag compound) {
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
    public @NotNull BinaryTag encode(@NotNull TrimPattern decoded) throws Exception {
        return CompoundBinaryTag.builder()
                .put(ASSET_FIELD, KeyBinaryTagCodec.INSTANCE.encode(decoded.asset()))
                .put(DESCRIPTION_FIELD, ComponentBinaryTagCodec.INSTANCE.encode(decoded.description()))
                .putBoolean(DECAL_FIELD, decoded.decal())
                .build();
    }
}