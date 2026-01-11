package net.hypejet.jet.server.registry.codecs.world.coordinate;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.world.coordinate.Vector;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.DoubleBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * A {@linkplain BinaryTagCodec binary-tag codec} of {@linkplain Vector vectors}.
 *
 * @since 1.0
 * @see Vector
 * @see BinaryTagCodec
 */
@NullMarked
public final class VectorBinaryTagCodec implements BinaryTagCodec<Vector> {
    /**
     * An instance of the {@linkplain VectorBinaryTagCodec vector binary-tag codec}.
     *
     * @since 1.0
     */
    public static final VectorBinaryTagCodec INSTANCE = new VectorBinaryTagCodec();

    private VectorBinaryTagCodec() {}

    @Override
    public Vector decode(BinaryTag binaryTag) {
        if (!(binaryTag instanceof ListBinaryTag listTag))
            throw new IllegalArgumentException("The encoded tag must be of double-list type to decode it to a vector");
        if (listTag.size() != 3) {
            throw new IllegalArgumentException(
                    "The list binary tag representing a vector must contain exactly 3 elements"
            );
        }
        return new Vector(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
    }

    @Override
    public BinaryTag encode(Vector value) {
        return ListBinaryTag.listBinaryTag(BinaryTagTypes.DOUBLE, List.of(
                DoubleBinaryTag.doubleBinaryTag(value.x()),
                DoubleBinaryTag.doubleBinaryTag(value.y()),
                DoubleBinaryTag.doubleBinaryTag(value.z())
        ));
    }
}