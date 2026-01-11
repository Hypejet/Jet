package net.hypejet.jet.world.block.spawner;

import org.jspecify.annotations.NullMarked;

/**
 * The state of a trial spawner block.
 *
 * <p>This is not an enum, since it depends on Minecraft.
 * Adding an enum entry could break enum switch cases for example./p>
 *
 * @since 1.0
 */
@NullMarked
public final class TrialSpawnerState {
    /**
     * A {@linkplain TrialSpawnerState trial spawner state} indicating
     * that the trial spawner is currently not spawning mobs.
     *
     * @since 1.0
     */
    public static final TrialSpawnerState INACTIVE = new TrialSpawnerState("inactive");

    /**
     * A {@linkplain TrialSpawnerState trial spawner state} indicating that
     * the trial spawner is ready to spawn mobs, but there are no players nearby.
     *
     * @since 1.0
     */
    public static final TrialSpawnerState WAITING_FOR_PLAYERS = new TrialSpawnerState("waiting_for_players");

    /**
     * A {@linkplain TrialSpawnerState trial spawner state}
     * indicating that the trial spawner is currently spawning mobs.
     *
     * @since 1.0
     */
    public static final TrialSpawnerState ACTIVE = new TrialSpawnerState("active");

    /**
     * A {@linkplain TrialSpawnerState trial spawner state}
     * indicating that the trial spawner is ready to eject a reward.
     *
     * @since 1.0
     */
    public static final TrialSpawnerState AWAITING_REWARD_EJECTION = new TrialSpawnerState("awaiting_reward_ejection");

    /**
     * A {@linkplain TrialSpawnerState trial spawner state} indicating
     * that the trial spawner is currently ejecting a reward.
     *
     * @since 1.0
     */
    public static final TrialSpawnerState EJECTING_REWARD = new TrialSpawnerState("ejecting_reward");

    /**
     * A {@linkplain TrialSpawnerState trial spawner state} indicating that
     * the trial spawner is on a cooldown between reward ejection and activation.
     *
     * @since 1.0
     */
    public static final TrialSpawnerState COOLDOWN = new TrialSpawnerState("cooldown");

    private final String name;

    private TrialSpawnerState(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TrailSpawnerState{" +
                "name='" + this.name + '\'' +
                '}';
    }
}