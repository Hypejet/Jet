package net.hypejet.jet.server.registry.writers.registry.wolf;

import net.hypejet.jet.data.model.api.registries.wolf.WolfBiomes;
import net.hypejet.jet.server.registry.writers.key.PackedKeyBinaryTagWriter;
import net.hypejet.jet.server.registry.writers.key.TagKeyBinaryTagWriter;
import net.hypejet.jet.server.util.codec.Writer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents {@linkplain Writer a writer}, which writes {@linkplain WolfBiomes wolf biomes} into
 * {@linkplain BinaryTag a binary tag}.
 *
 * @since 1.0
 * @see WolfBiomes
 * @see BinaryTag
 * @see Writer
 */
public final class WolfBiomesBinaryTagWriter implements Writer<WolfBiomes, BinaryTag> {
    /**
     * An instance of the {@linkplain WolfBiomesBinaryTagWriter wolf biomes binary tag writer}.
     *
     * @since 1.0
     */
    public static final WolfBiomesBinaryTagWriter INSTANCE = new WolfBiomesBinaryTagWriter();

    private WolfBiomesBinaryTagWriter() {}

    @Override
    public @NonNull BinaryTag write(@NonNull WolfBiomes object) {
        return switch (object) {
            case WolfBiomes.SingleBiome biomes -> PackedKeyBinaryTagWriter.INSTANCE.write(biomes.key());
            case WolfBiomes.TaggedBiomes biomes -> TagKeyBinaryTagWriter.INSTANCE.write(biomes.key());
            case WolfBiomes.Biomes biomes -> {
                List<BinaryTag> tags = new ArrayList<>();
                for (Key key : biomes.keys())
                    tags.add(PackedKeyBinaryTagWriter.INSTANCE.write(key));
                yield ListBinaryTag.from(tags);
            }
        };
    }
}