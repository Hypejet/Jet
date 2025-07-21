package net.hypejet.jet.data.generator.adpater;

import net.hypejet.jet.data.json.model.block.JsonBlockEntityType;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents something converting {@linkplain BlockEntityType block entity types} to a Jet data equivalent.
 *
 * @since 1.0
 * @see BlockEntityType
 */
public final class BlockEntityTypeAdapter {

    private static final Field VALID_BLOCK_FIELD;

    static {
        try {
            VALID_BLOCK_FIELD = BlockEntityType.class.getDeclaredField("validBlocks");
            VALID_BLOCK_FIELD.setAccessible(true);
        } catch (NoSuchFieldException exception) {
            throw new RuntimeException(exception);
        }
    }

    private BlockEntityTypeAdapter() {}

    /**
     * Converts the specified {@linkplain BlockEntityType block entity type} to a Jet data equivalent.
     *
     * @param type the block entity type to convert
     * @param registryAccess access to all Minecraft registries
     * @return the converted block entity type
     * @since 1.0
     */
    public static @NonNull JsonBlockEntityType convert(@NonNull BlockEntityType<?> type,
                                                       @NonNull RegistryAccess registryAccess) {
        try {
            Set<?> validBlocks = (Set<?>) VALID_BLOCK_FIELD.get(type);

            Registry<Block> blockRegistry = registryAccess.lookupOrThrow(Registries.BLOCK);
            Set<Key> convertedValidBlocks = new HashSet<>();

            for (Object element : validBlocks) {
                if (!(element instanceof Block block))
                    throw new IllegalStateException("The valid blocks set contains a non-block object");

                ResourceLocation location = blockRegistry.getKey(block);
                if (location == null)
                    throw new IllegalStateException("A registry element does not have a resource location");

                convertedValidBlocks.add(KeyAdapter.convert(location));
            }

            return new JsonBlockEntityType(convertedValidBlocks);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }
}