package net.hypejet.jet.data.json.model.poi;

import com.google.common.primitives.ImmutableIntArray;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A type of Minecraft point of interest type.
 *
 * @param matchingStateIds identifiers of block states that this point of interest type can be used with
 * @param maxTickets a maximum number of entities that can be associated with a point of interest of this type
 * @param validRange a maximum distance within entities can reach blocks that this point of interest type matches with
 * @since 1.0
 */
public record JsonPoiType(@NonNull ImmutableIntArray matchingStateIds, int maxTickets, int validRange) {
    /**
     * Constructs the {@linkplain JsonPoiType point of interest type}.
     *
     * @param matchingStateIds identifiers of block states that this point of interest type can be used with
     * @param maxTickets a maximum number of entities that can be associated with a point of interest of this type
     * @param validRange a maximum distance within entities can reach blocks that this point of interest type matches with
     * @since 1.0
     */
    public JsonPoiType {
        Objects.requireNonNull(matchingStateIds, "matching state ids");
    }
}