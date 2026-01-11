package net.hypejet.jet.server.util.nbt;

import net.hypejet.jet.server.util.math.MathUtil;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagType;
import net.kyori.adventure.nbt.ByteBinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.FloatBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Utilities for {@linkplain BinaryTag binary-tag} management.
 *
 * @since 1.0
 * @see BinaryTag
 */
public final class BinaryTagUtil {

    private BinaryTagUtil() {}

    /**
     * Gets a required tag from the specified {@linkplain CompoundBinaryTag compound binary tag}.
     *
     * @param name a name of the tag to get
     * @param compound the compound binary tag
     * @return the tag
     * @throws IllegalArgumentException if the tag has not been specified
     * @since 1.0
     */
    public static @NonNull BinaryTag requiredTag(@NonNull String name, @NonNull CompoundBinaryTag compound) {
        BinaryTag tag = compound.get(name);
        if (tag == null)
            throw new IllegalArgumentException(String.format("Tag \"%s\" has not been specified", name));
        return tag;
    }

    /**
     * Gets a required tag from the specified {@linkplain CompoundBinaryTag compound binary tag}.
     *
     * @param name a name of the tag to get
     * @param compound the compound binary tag
     * @param tagType a type of the tag to get
     * @return the tag
     * @param <B> the type of the tag
     * @throws IllegalArgumentException if the tag is not of the specified type or the tag has not been specified
     * @since 1.0
     */
    public static <B extends BinaryTag> @NonNull B requiredTag(@NonNull String name,
                                                               @NonNull CompoundBinaryTag compound,
                                                               @NonNull BinaryTagType<B> tagType) {
        B tag = optionalTag(name, compound, tagType);
        if (tag == null)
            throw new IllegalArgumentException(String.format("Tag \"%s\" has not been specified", name));
        return tag;
    }

    /**
     * Gets an optional tag from the specified {@linkplain CompoundBinaryTag compound binary tag}.
     *
     * @param name a name of the tag to get
     * @param compound the compound binary tag
     * @param tagType a type of the tag to get
     * @return the tag, {@code null} if the tag has not been specified
     * @param <B> the type of the tag
     * @throws IllegalArgumentException if the tag is not of the specified type
     * @since 1.0
     */
    public static <B extends BinaryTag> @Nullable B optionalTag(@NonNull String name,
                                                                @NonNull CompoundBinaryTag compound,
                                                                @NonNull BinaryTagType<B> tagType) {
        BinaryTag tag = compound.get(name);
        if (tag == null) {
            return null;
        } else if (tag.type() == tagType) {
            // noinspection unchecked ; we checked whether the tag type is the specified type
            return (B) tag;
        } else {
            throw new IllegalArgumentException(String.format(
                    "Tag \"%s\" must be of %s type",
                    name, tagType.getClass().getSimpleName()
            ));
        }
    }

    /**
     * Gets a {@code boolean} representation of the specified {@linkplain ByteBinaryTag byte binary tag}.
     *
     * @param tag the byte binary tag to represent as a boolean
     * @return the boolean representation of the specified tag
     * @since 1.0
     */
    public static boolean booleanValue(@NonNull ByteBinaryTag tag) {
        return tag.value() != 0; // != might seem weird, but that is what Minecraft does
    }

    /**
     * Gets the element placed at the specified index in the specified {@linkplain ListBinaryTag list binary tag},
     * casts it to a {@code float}, multiplies it by {@code 255} and rounds it down.
     *
     * @param index the index
     * @param listTag the list binary tag
     * @return the final {@code float} result
     * @throws IllegalArgumentException if the element at the specified index is not of float type
     * @since 1.0
     */
    public static int colorComponent(int index, ListBinaryTag listTag) {
        if (!(listTag.get(index) instanceof FloatBinaryTag floatTag))
            throw new IllegalArgumentException("The binary tag at index " + index + " is not of the float type");
        return MathUtil.floor(floatTag.value() * 255f);
    }
}