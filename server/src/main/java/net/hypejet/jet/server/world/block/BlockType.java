package net.hypejet.jet.server.world.block;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.data.model.server.registry.registries.block.state.BlockState;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a type of Minecraft block.
 *
 * @since 1.0
 */
public final class BlockType {

    private final BlockState defaultState;
    private final Set<Key> requiredFeatureFlags;
    private final Map<Map<String, String>, BlockState> possibleStates;

    /**
     * Constructs the {@linkplain BlockType block type}.
     *
     * @param defaultBlockState a default block state that blocks of the block type should have
     * @param requiredFeatureFlags required feature flags that are required to enable the block type
     * @param possibleBlockStates blocks states that blocks of the block type should be able to use
     * @since 1.0
     */
    public BlockType(@NonNull BlockState defaultBlockState, @NonNull Set<Key> requiredFeatureFlags,
                     @NonNull Set<BlockState> possibleBlockStates) {
        NullabilityUtil.requireNonNull(defaultBlockState, "default block state");
        NullabilityUtil.requireNonNull(requiredFeatureFlags, "required feature flags");
        NullabilityUtil.requireNonNull(possibleBlockStates, "possible state entries");

        Map<Map<String, String>, BlockState> possibleStates = new HashMap<>();
        for (BlockState state : possibleBlockStates)
            possibleStates.put(state.properties(), state);

        this.defaultState = defaultBlockState;
        this.requiredFeatureFlags = Set.copyOf(requiredFeatureFlags);
        this.possibleStates = Map.copyOf(possibleStates);
    }

    /**
     * Gets a default {@linkplain BlockState block state} that should be used for creating blocks of this type.
     *
     * @return the block state
     * @since 1.0
     */
    public @NonNull BlockState defaultState() {
        return this.defaultState;
    }

    /**
     * Gets {@linkplain Set a set} of required feature flags that are required to enable this block type.
     *
     * @return the set
     * @since 1.0
     */
    public @NonNull Set<Key> requiredFeatureFlags() {
        return this.requiredFeatureFlags;
    }

    /**
     * Gets {@linkplain Collection a collection} of {@linkplain BlockState block states} that blocks of this block type
     * should be able to use.
     *
     * @return the collection
     * @since 1.0
     */
    public @NonNull Collection<BlockState> possibleStates() {
        return this.possibleStates.values();
    }

    /**
     * Gets {@linkplain BlockState a block state} associated with this block type with properties specified.
     *
     * @param properties the properties
     * @return the block state
     * @since 1.0
     */
    public @NonNull BlockState state(@NonNull Map<String, String> properties) {
        NullabilityUtil.requireNonNull(properties, "properties");
        BlockState blockState = this.possibleStates.get(properties);

        if (blockState == null) {
            throw new IllegalArgumentException(String.format(
                    "Could not find a block state for properties specified (%s)",
                    properties
            ));
        }

        return blockState;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BlockType blockType)) return false;
        return Objects.equals(this.defaultState, blockType.defaultState)
                && Objects.equals(this.requiredFeatureFlags, blockType.requiredFeatureFlags)
                && Objects.equals(this.possibleStates, blockType.possibleStates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.defaultState, this.requiredFeatureFlags, this.possibleStates);
    }

    @Override
    public String toString() {
        return "BlockType{" +
                "defaultState=" + this.defaultState +
                ", requiredFeatureFlags=" + this.requiredFeatureFlags +
                ", states=" + this.possibleStates +
                '}';
    }
}