package net.hypejet.jet.server.registry.codecs.registry.wolf;

import net.hypejet.jet.data.model.registry.registries.wolf.WolfBiomes;
import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.identifier.PackedIdentifierBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.identifier.TagIdentifierBinaryTagCodec;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a {@linkplain BinaryTagCodec binary tag codec} which deserializes and serializes
 * a {@linkplain WolfBiomes wolf biomes}.
 *
 * @since 1.0
 * @author Codestech
 * @see WolfBiomes
 * @see BinaryTagCodec
 */
public final class WolfBiomesBinaryTagCodec implements BinaryTagCodec<WolfBiomes> {

    private static final WolfBiomesBinaryTagCodec INSTANCE = new WolfBiomesBinaryTagCodec();

    private WolfBiomesBinaryTagCodec() {}

    @Override
    public @NonNull WolfBiomes read(@NonNull BinaryTag tag) {
        return switch (tag) {
            case StringBinaryTag stringTag -> {
                String value = stringTag.value();
                if (value.startsWith(TagIdentifierBinaryTagCodec.HASH_STRING))
                    yield new WolfBiomes.TaggedBiomes(TagIdentifierBinaryTagCodec.instance().read(stringTag));
                yield new WolfBiomes.SingleBiome(PackedIdentifierBinaryTagCodec.instance().read(stringTag));
            }
            case ListBinaryTag listTag -> {
                List<Key> biomes = new ArrayList<>();
                for (BinaryTag binaryTag : listTag)
                    biomes.add(PackedIdentifierBinaryTagCodec.instance().read(binaryTag));
                yield new WolfBiomes.Biomes(biomes);
            }
            default -> throw new IllegalStateException("Unexpected value: " + tag);
        };
    }

    @Override
    public @NonNull BinaryTag write(@NonNull WolfBiomes object) {
        return switch (object) {
            case WolfBiomes.SingleBiome biomes -> PackedIdentifierBinaryTagCodec.instance().write(biomes.key());
            case WolfBiomes.TaggedBiomes biomes -> TagIdentifierBinaryTagCodec.instance().write(biomes.key());
            case WolfBiomes.Biomes biomes -> {
                List<BinaryTag> tags = new ArrayList<>();
                for (Key key : biomes.keys())
                    tags.add(PackedIdentifierBinaryTagCodec.instance().write(key));
                yield ListBinaryTag.from(tags);
            }
        };
    }

    /**
     * Gets an instance of the {@linkplain WolfBiomesBinaryTagCodec wolf biomes binary tag codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull WolfBiomesBinaryTagCodec instance() {
        return INSTANCE;
    }
}