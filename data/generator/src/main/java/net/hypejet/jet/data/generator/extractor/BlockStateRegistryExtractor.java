package net.hypejet.jet.data.generator.extractor;

import net.hypejet.jet.data.generator.adapter.KeyAdapter;
import net.hypejet.jet.data.json.entry.JsonRegistryEntry;
import net.hypejet.jet.data.json.model.block.state.JsonBlockState;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a {@linkplain RegistryExtractor registry extractor} extracting {@linkplain BlockState block states}
 * from the Minecraft block state registry and converting them to a Jet data equivalent.
 *
 * @since 1.0
 * @see BlockState
 * @see RegistryExtractor
 */
@NullMarked
public final class BlockStateRegistryExtractor implements RegistryExtractor<JsonBlockState> {

    /**
     * An instance of the {@linkplain BlockStateRegistryExtractor block state registry extractor}.
     *
     * @since 1.0
     */
    public static final BlockStateRegistryExtractor INSTANCE = new BlockStateRegistryExtractor();

    private BlockStateRegistryExtractor() {}

    @Override
    public List<JsonRegistryEntry<JsonBlockState>> extract(RegistryAccess registryAccess) {
        Registry<Block> blockRegistry = registryAccess.lookupOrThrow(Registries.BLOCK);
        List<JsonRegistryEntry<JsonBlockState>> entries = new ArrayList<>();

        for (BlockState blockState : Block.BLOCK_STATE_REGISTRY) {
            ResourceLocation location = blockRegistry.getKey(blockState.getBlock());
            if (location == null) {
                throw new IllegalStateException(
                        "A block associated with a block state does not have a resource location in a block registry"
                );
            }

            entries.add(new JsonRegistryEntry<>(
                    KeyAdapter.convert(location),
                    new JsonBlockState(
                            stringProperties(blockState),
                            blockState.isAir(),
                            !blockState.getFluidState().isEmpty(),
                            blockState.blocksMotion(),
                            blockState.getBlock() instanceof LeavesBlock
                    ),
                    Set.of(),
                    null
            ));
        }

        return List.copyOf(entries);
    }

    @Override
    public Class<JsonBlockState> valueClass() {
        return JsonBlockState.class;
    }

    /**
     * Creates a {@linkplain Map map} associating {@linkplain Property state property} names with string
     * representations of their values assigned in the specified {@linkplain BlockState block state}.
     *
     * @param blockState the block state that the property map should be created for
     * @return the created property map
     * @since 1.0
     */
    public static Map<String, String> stringProperties(BlockState blockState) {
        Map<String, String> properties = new HashMap<>();

        for (Map.Entry<Property<?>, Comparable<?>> entry : blockState.getValues().entrySet()) {
            Property<?> property = entry.getKey();
            properties.put(property.getName(), propertyValueString(property, entry.getValue()));
        }

        return Map.copyOf(properties);
    }

    private static <T extends Comparable<T>> String propertyValueString(Property<T> property, Comparable<?> value) {
        return property.getName(property.getValueClass().cast(value));
    }
}