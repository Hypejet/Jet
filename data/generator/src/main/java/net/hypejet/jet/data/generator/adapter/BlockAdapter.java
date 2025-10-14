package net.hypejet.jet.data.generator.adapter;

import com.google.common.primitives.ImmutableIntArray;
import net.hypejet.jet.data.json.model.block.JsonBlock;
import net.hypejet.jet.data.json.model.block.state.property.JsonStateProperty;
import net.hypejet.jet.data.json.model.block.state.property.JsonEnumStatePropertyValueType;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerState;
import net.minecraft.world.level.block.entity.vault.VaultState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BambooLeaves;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BellAttachType;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.ComparatorMode;
import net.minecraft.world.level.block.state.properties.CreakingHeartState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.block.state.properties.TestBlockMode;
import net.minecraft.world.level.block.state.properties.Tilt;
import net.minecraft.world.level.block.state.properties.WallSide;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents something converting {@linkplain Block blocks} to a Jet data equivalent.
 *
 * @since 1.0
 * @see Block
 */
public final class BlockAdapter {

    private static final Map<Class<?>, JsonEnumStatePropertyValueType> ENUM_VALUE_TYPES = Map.ofEntries(
            Map.entry(Direction.Axis.class, JsonEnumStatePropertyValueType.AXIS),
            Map.entry(Direction.class, JsonEnumStatePropertyValueType.DIRECTION),
            Map.entry(FrontAndTop.class, JsonEnumStatePropertyValueType.FRONT_AND_TOP),
            Map.entry(AttachFace.class, JsonEnumStatePropertyValueType.ATTACHMENT_TYPE),
            Map.entry(BellAttachType.class, JsonEnumStatePropertyValueType.BELL_ATTACHMENT_TYPE),
            Map.entry(WallSide.class, JsonEnumStatePropertyValueType.WALL_CONNECTION_TYPE),
            Map.entry(RedstoneSide.class, JsonEnumStatePropertyValueType.REDSTONE_CONNECTION_TYPE),
            Map.entry(DoubleBlockHalf.class, JsonEnumStatePropertyValueType.DOUBLE_BLOCK_HALF),
            Map.entry(Half.class, JsonEnumStatePropertyValueType.VERTICAL_OCCUPANCY_TYPE),
            Map.entry(RailShape.class, JsonEnumStatePropertyValueType.RAIL_SHAPE),
            Map.entry(BedPart.class, JsonEnumStatePropertyValueType.BED_PART),
            Map.entry(ChestType.class, JsonEnumStatePropertyValueType.CHEST_CONNECTION_TYPE),
            Map.entry(ComparatorMode.class, JsonEnumStatePropertyValueType.COMPARATOR_MODE),
            Map.entry(DoorHingeSide.class, JsonEnumStatePropertyValueType.DOOR_HINGE_SIDE),
            Map.entry(NoteBlockInstrument.class, JsonEnumStatePropertyValueType.NOTE_BLOCK_INSTRUMENT),
            Map.entry(PistonType.class, JsonEnumStatePropertyValueType.PISTON_TYPE),
            Map.entry(SlabType.class, JsonEnumStatePropertyValueType.SLAB_OCCUPANCY_TYPE),
            Map.entry(StairsShape.class, JsonEnumStatePropertyValueType.STAIRS_SHAPE),
            Map.entry(StructureMode.class, JsonEnumStatePropertyValueType.STRUCTURE_BLOCK_MODE),
            Map.entry(BambooLeaves.class, JsonEnumStatePropertyValueType.BAMBOO_LEAVES_TYPE),
            Map.entry(Tilt.class, JsonEnumStatePropertyValueType.TILT),
            Map.entry(DripstoneThickness.class, JsonEnumStatePropertyValueType.DRIPSTONE_THICKNESS),
            Map.entry(SculkSensorPhase.class, JsonEnumStatePropertyValueType.SCULK_SENSOR_PHASE),
            Map.entry(TrialSpawnerState.class, JsonEnumStatePropertyValueType.TRIAL_SPAWNER_STATE),
            Map.entry(VaultState.class, JsonEnumStatePropertyValueType.VAULT_STATE),
            Map.entry(CreakingHeartState.class, JsonEnumStatePropertyValueType.CREAKING_HEART_STATE),
            Map.entry(TestBlockMode.class, JsonEnumStatePropertyValueType.TEST_BLOCK_MODE)
    );

    private static final Field INTEGER_PROPERTY_MIN_FIELD;
    private static final Field INTEGER_PROPERTY_MAX_FIELD;

    static {
        try {
            INTEGER_PROPERTY_MIN_FIELD = IntegerProperty.class.getDeclaredField("min");
            INTEGER_PROPERTY_MAX_FIELD = IntegerProperty.class.getDeclaredField("max");
            INTEGER_PROPERTY_MIN_FIELD.setAccessible(true);
            INTEGER_PROPERTY_MAX_FIELD.setAccessible(true);
        } catch (NoSuchFieldException exception) {
            throw new RuntimeException(exception);
        }
    }

    private BlockAdapter() {}

    /**
     * Converts the specified {@linkplain Block block} to a Jet data equivalent.
     *
     * @param block the block to convert
     * @return the converted block
     * @since 1.0
     */
    public static @NonNull JsonBlock convert(@NonNull Block block) {
        StateDefinition<Block, BlockState> blockStateDefinition = block.getStateDefinition();

        Map<String, JsonStateProperty> stateProperties = new HashMap<>();
        for (Property<?> property : blockStateDefinition.getProperties())
            stateProperties.put(property.getName(), convertStateProperty(property));

        ImmutableIntArray.Builder possibleStateIdsBuilder = ImmutableIntArray.builder();
        for (BlockState state : blockStateDefinition.getPossibleStates())
            possibleStateIdsBuilder.add(Block.getId(state));

        return new JsonBlock(
                KeyAdapter.convertSet(FeatureFlags.REGISTRY.toNames(block.requiredFeatures())),
                Block.getId(block.defaultBlockState()),
                possibleStateIdsBuilder.build(),
                stateProperties
        );
    }

    private static <T extends Comparable<T>> @NonNull JsonStateProperty convertStateProperty(
            @NonNull Property<T> property
    ) {
        return switch (property) {
            case BooleanProperty ignored -> JsonStateProperty.Boolean.INSTANCE;
            case IntegerProperty integerProperty -> {
                try {
                    int min = INTEGER_PROPERTY_MIN_FIELD.getInt(integerProperty);
                    int max = INTEGER_PROPERTY_MAX_FIELD.getInt(integerProperty);
                    yield new JsonStateProperty.Integer(min, max);
                } catch (IllegalAccessException exception) {
                    throw new RuntimeException(exception);
                }
            }
            case EnumProperty<?> ignored -> {
                Class<T> valueClass = property.getValueClass();
                JsonEnumStatePropertyValueType valueType = ENUM_VALUE_TYPES.get(valueClass);

                if (valueType == null) {
                    throw new IllegalStateException(
                            "No state property value type was registered for class: " + valueClass.getName()
                    );
                }

                Set<String> acceptedValues = new HashSet<>();
                for (T possibleValue : property.getPossibleValues())
                    acceptedValues.add(property.getName(possibleValue));
                yield new JsonStateProperty.Enum(valueType, acceptedValues);
            }
            default -> throw new IllegalStateException("Unknown property: " + property.getClass().getName());
        };
    }
}