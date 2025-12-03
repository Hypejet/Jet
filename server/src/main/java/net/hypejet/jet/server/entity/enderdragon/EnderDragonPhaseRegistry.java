package net.hypejet.jet.server.entity.enderdragon;

import net.hypejet.jet.entity.enderdragon.EnderDragonPhase;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.kyori.adventure.util.Index;
import org.jspecify.annotations.NullMarked;

import java.util.Map;

/**
 * A registry of {@linkplain EnderDragonPhase ender dragon phases}.
 *
 * @since 1.0
 * @see EnderDragonPhase
 */
@NullMarked
public final class EnderDragonPhaseRegistry {

    private static final Index<EnderDragonPhase, Integer> PHASES = IndexUtil.fromMap(Map.ofEntries(
            Map.entry(0, EnderDragonPhase.CIRCLING),
            Map.entry(1, EnderDragonPhase.STRAFING),
            Map.entry(2, EnderDragonPhase.FLYING_TO_PORTAL_TO_LAND),
            Map.entry(3, EnderDragonPhase.LANDING_ON_PORTAL),
            Map.entry(4, EnderDragonPhase.TAKING_OFF_FROM_PORTAL),
            Map.entry(5, EnderDragonPhase.BREATH_ATTACK),
            Map.entry(6, EnderDragonPhase.SEARCHING_FOR_PLAYER),
            Map.entry(7, EnderDragonPhase.ROARING),
            Map.entry(8, EnderDragonPhase.CHARGING_PLAYER),
            Map.entry(9, EnderDragonPhase.FLYING_TO_PORTAL_TO_DIE),
            Map.entry(10, EnderDragonPhase.HOVERING)
    ));

    private EnderDragonPhaseRegistry() {}

    /**
     * Gets a registered {@linkplain EnderDragonPhase ender dragon phase} by its numeric identifier.
     *
     * @param id the identifier
     * @return the ender dragon phase
     * @since 1.0
     */
    public static EnderDragonPhase phaseById(int id) {
        return PHASES.keyOrThrow(id);
    }

    /**
     * Gets a numeric identifier of the specified registered {@linkplain EnderDragonPhase ender dragon phase}.
     *
     * @param phase the ender dragon phase whose identifier should be returned
     * @return the numeric identifier of the specified ender dragon phase
     * @since 1.0
     */
    public static int phaseId(EnderDragonPhase phase) {
        return PHASES.valueOrThrow(phase);
    }
}