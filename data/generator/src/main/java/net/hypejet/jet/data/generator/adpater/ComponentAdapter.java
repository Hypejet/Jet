package net.hypejet.jet.data.generator.adpater;

import net.kyori.adventure.nbt.TagStringIO;
import net.kyori.adventure.text.serializer.nbt.NBTComponentSerializer;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import org.jspecify.annotations.NonNull;

import java.io.IOException;

/**
 * Represents something converting {@linkplain Component Minecraft components} to an adventure equivalent.
 *
 * @since 1.0
 * @see Component
 */
public final class ComponentAdapter {

    private ComponentAdapter() {}

    /**
     * Converts the specified {@linkplain Component component} to an adventure equivalent.
     *
     * @param component the component to convert
     * @return the converted component
     * @since 1.0
     */
    public static net.kyori.adventure.text.@NonNull Component convert(@NonNull Component component) {
        return ComponentSerialization.CODEC.encodeStart(NbtOps.INSTANCE, component)
                .map(Tag::toString)
                .map(tagString -> {
                    try {
                        return TagStringIO.tagStringIO().asTag(tagString);
                    } catch (IOException exception) {
                        throw new RuntimeException(exception);
                    }
                })
                .map(NBTComponentSerializer.nbt()::deserialize)
                .getOrThrow();
    }
}