package net.hypejet.jet.world.block.vault;

import org.jspecify.annotations.NullMarked;

/**
 * The state of a vault block.
 *
 * <p>This is not an enum, since it depends on Minecraft.
 * Adding an enum entry could break enum switch cases for example./p>
 *
 * @since 1.0
 */
@NullMarked
public final class VaultState {
    /**
     * A {@linkplain VaultState vault state} indicating that the vault is not
     * holding a reward because has already been used by all nearby players.
     *
     * @since 1.0
     */
    public static final VaultState INACTIVE = new VaultState("inactive");

    /**
     * A {@linkplain VaultState vault state} indicating that the vault is holding
     * reward because there are nearby players that have not used the vault.
     *
     * @since 1.0
     */
    public static final VaultState ACTIVE = new VaultState("active");

    /**
     * A {@linkplain VaultState vault state} indicating that the vault
     * is being unlocked and is about to eject the held reward.
     *
     * @since 1.0
     */
    public static final VaultState UNLOCKING = new VaultState("unlocking");

    /**
     * A {@linkplain VaultState vault state} indicating that the vault is ejecting the held reward
     *
     * @since 1.0
     */
    public static final VaultState EJECTING = new VaultState("ejecting");

    private final String name;

    private VaultState(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "VaultState{" +
                "name='" + this.name + '\'' +
                '}';
    }
}