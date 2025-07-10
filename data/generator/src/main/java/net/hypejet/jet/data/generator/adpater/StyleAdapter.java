package net.hypejet.jet.data.generator.adpater;

import net.kyori.adventure.nbt.TagStringIO;
import net.kyori.adventure.text.serializer.nbt.NBTComponentSerializer;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Style;
import org.jspecify.annotations.NonNull;

import java.io.IOException;

/**
 * Represents something converting {@linkplain Style Minecraft styles} to an adventure equivalent.
 *
 * @since 1.0
 * @see Style
 */
public final class StyleAdapter {

    private StyleAdapter() {}

    /**
     * Converts the specified {@linkplain Style style} to an adventure equivalent.
     *
     * @param style the style to convert
     * @return the converted style
     * @since 1.0
     */
    public static net.kyori.adventure.text.format.@NonNull Style convert(@NonNull Style style) {
        return Style.Serializer.CODEC.encodeStart(NbtOps.INSTANCE, style)
                .map(Tag::toString)
                .map(tagString -> {
                    try {
                        return TagStringIO.tagStringIO().asCompound(tagString);
                    } catch (IOException exception) {
                        throw new RuntimeException(exception);
                    }
                })
                .map(NBTComponentSerializer.nbt()::deserializeStyle)
                .getOrThrow();
    }
}