package net.hypejet.jet.data.generator.adpater;

import com.google.common.primitives.ImmutableIntArray;
import net.hypejet.jet.data.json.model.poi.JsonPoiType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

import java.util.Set;

/**
 * Represents something converting {@linkplain PoiType point of interest types} to a Jet data equivalent.
 *
 * @since 1.0
 * @see PoiType
 */
public final class PoiTypeAdapter {

    private PoiTypeAdapter() {}

    /**
     * Converts the specified {@linkplain PoiType point of interest type} to a Jet data equivalent.
     *
     * @param type the point of interest type to convert
     * @return the converted point of interest type
     * @since 1.0
     */
    public static @NonNull JsonPoiType convert(@NonNull PoiType type) {
        return new JsonPoiType(convertMatchingStates(type.matchingStates()), type.maxTickets(), type.validRange());
    }

    private static @NonNull ImmutableIntArray convertMatchingStates(@NonNull Set<BlockState> matchingStates) {
        ImmutableIntArray.Builder matchingStateIdsBuilder = ImmutableIntArray.builder(matchingStates.size());
        matchingStates.forEach(state -> matchingStateIdsBuilder.add(Block.getId(state)));
        return matchingStateIdsBuilder.build();
    }
}