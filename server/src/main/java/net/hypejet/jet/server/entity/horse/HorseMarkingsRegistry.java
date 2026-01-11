package net.hypejet.jet.server.entity.horse;

import net.hypejet.jet.entity.horse.HorseMarkings;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.kyori.adventure.util.Index;
import org.jspecify.annotations.NullMarked;

import java.util.Map;

/**
 * A registry of {@linkplain HorseMarkings horse markings}.
 *
 * @since 1.0
 * @see HorseMarkings
 */
@NullMarked
public final class HorseMarkingsRegistry {

    private static final Index<HorseMarkings, Integer> HORSE_MARKINGS = IndexUtil.fromMap(Map.of(
            0, HorseMarkings.NONE,
            1, HorseMarkings.WHITE,
            2, HorseMarkings.WHITE_FIELD,
            3, HorseMarkings.WHITE_DOTS,
            4, HorseMarkings.BLACK_DOTS
    ));

    private HorseMarkingsRegistry() {}

    /**
     * Gets registered {@linkplain HorseMarkings horse markings} with the specified numeric identifier.
     *
     * @param id the numeric identifier
     * @return the horse markings
     * @since 1.0
     */
    public static HorseMarkings horseMarkings(int id) {
        return HORSE_MARKINGS.keyOrThrow(id);
    }

    /**
     * Gets a numeric identifier of the specified registered {@linkplain HorseMarkings horse markings}.
     *
     * @param markings the horse markings whose numeric identifier should be returned
     * @return the numeric identifier
     * @since 1.0
     */
    public static int horseMarkingsId(HorseMarkings markings) {
        return HORSE_MARKINGS.valueOrThrow(markings);
    }
}