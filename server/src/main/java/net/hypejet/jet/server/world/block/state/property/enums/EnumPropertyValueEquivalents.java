package net.hypejet.jet.server.world.block.state.property.enums;

import net.hypejet.jet.data.json.model.block.state.property.JsonEnumStatePropertyValueType;
import net.hypejet.jet.world.block.attachment.BellAttachmentType;
import net.hypejet.jet.world.block.attachment.BlockAttachmentType;
import net.hypejet.jet.world.block.bamboo.BambooLeavesType;
import net.hypejet.jet.world.block.bed.BedPart;
import net.hypejet.jet.world.block.comparator.ComparatorMode;
import net.hypejet.jet.world.block.connection.ChestConnectionType;
import net.hypejet.jet.world.block.connection.RedstoneConnectionType;
import net.hypejet.jet.world.block.connection.WallConnectionType;
import net.hypejet.jet.world.block.creaking.CreakingHeartState;
import net.hypejet.jet.world.block.door.DoorHingeSide;
import net.hypejet.jet.world.block.dripstone.DripstoneThickness;
import net.hypejet.jet.world.block.half.DoubleBlockHalf;
import net.hypejet.jet.world.block.noteblock.NoteBlockInstrument;
import net.hypejet.jet.world.block.orientation.BlockVerticalOccupancyType;
import net.hypejet.jet.world.block.orientation.FrontAndTop;
import net.hypejet.jet.world.block.piston.PistonType;
import net.hypejet.jet.world.block.rail.RailShape;
import net.hypejet.jet.world.block.sculk.SculkSensorPhase;
import net.hypejet.jet.world.block.slab.SlabOccupancyType;
import net.hypejet.jet.world.block.spawner.TrialSpawnerState;
import net.hypejet.jet.world.block.stairs.StairsShape;
import net.hypejet.jet.world.block.structure.StructureBlockMode;
import net.hypejet.jet.world.block.test.TestBlockMode;
import net.hypejet.jet.world.block.tilt.Tilt;
import net.hypejet.jet.world.block.vault.VaultState;
import net.hypejet.jet.world.coordinate.axis.Axis;
import net.hypejet.jet.world.direction.Direction;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.Map;

/**
 * Something handling conversion between enums used by Minecraft
 * for block state properties and Jet equivalents of these enums.
 *
 * @since 1.0
 */
@NullMarked
public final class EnumPropertyValueEquivalents {

    private static final Map<JsonEnumStatePropertyValueType, Class<?>> VALUE_TYPE_EQUIVALENTS = Map.ofEntries(
            Map.entry(JsonEnumStatePropertyValueType.AXIS, Axis.class),
            Map.entry(JsonEnumStatePropertyValueType.DIRECTION, Direction.class),
            Map.entry(JsonEnumStatePropertyValueType.FRONT_AND_TOP, FrontAndTop.class),
            Map.entry(JsonEnumStatePropertyValueType.ATTACHMENT_TYPE, BlockAttachmentType.class),
            Map.entry(JsonEnumStatePropertyValueType.BELL_ATTACHMENT_TYPE, BellAttachmentType.class),
            Map.entry(JsonEnumStatePropertyValueType.WALL_CONNECTION_TYPE, WallConnectionType.class),
            Map.entry(JsonEnumStatePropertyValueType.REDSTONE_CONNECTION_TYPE, RedstoneConnectionType.class),
            Map.entry(JsonEnumStatePropertyValueType.DOUBLE_BLOCK_HALF, DoubleBlockHalf.class),
            Map.entry(JsonEnumStatePropertyValueType.VERTICAL_OCCUPANCY_TYPE, BlockVerticalOccupancyType.class),
            Map.entry(JsonEnumStatePropertyValueType.RAIL_SHAPE, RailShape.class),
            Map.entry(JsonEnumStatePropertyValueType.BED_PART, BedPart.class),
            Map.entry(JsonEnumStatePropertyValueType.CHEST_CONNECTION_TYPE, ChestConnectionType.class),
            Map.entry(JsonEnumStatePropertyValueType.COMPARATOR_MODE, ComparatorMode.class),
            Map.entry(JsonEnumStatePropertyValueType.DOOR_HINGE_SIDE, DoorHingeSide.class),
            Map.entry(JsonEnumStatePropertyValueType.NOTE_BLOCK_INSTRUMENT, NoteBlockInstrument.class),
            Map.entry(JsonEnumStatePropertyValueType.PISTON_TYPE, PistonType.class),
            Map.entry(JsonEnumStatePropertyValueType.SLAB_OCCUPANCY_TYPE, SlabOccupancyType.class),
            Map.entry(JsonEnumStatePropertyValueType.STAIRS_SHAPE, StairsShape.class),
            Map.entry(JsonEnumStatePropertyValueType.STRUCTURE_BLOCK_MODE, StructureBlockMode.class),
            Map.entry(JsonEnumStatePropertyValueType.BAMBOO_LEAVES_TYPE, BambooLeavesType.class),
            Map.entry(JsonEnumStatePropertyValueType.TILT, Tilt.class),
            Map.entry(JsonEnumStatePropertyValueType.DRIPSTONE_THICKNESS, DripstoneThickness.class),
            Map.entry(JsonEnumStatePropertyValueType.SCULK_SENSOR_PHASE, SculkSensorPhase.class),
            Map.entry(JsonEnumStatePropertyValueType.TRIAL_SPAWNER_STATE, TrialSpawnerState.class),
            Map.entry(JsonEnumStatePropertyValueType.VAULT_STATE, VaultState.class),
            Map.entry(JsonEnumStatePropertyValueType.CREAKING_HEART_STATE, CreakingHeartState.class),
            Map.entry(JsonEnumStatePropertyValueType.TEST_BLOCK_MODE, TestBlockMode.class)
    );

    private static final Map<Class<?>, Map<String, ?>> STRING_REPRESENTATIONS = new StringRepresentationsBuilder()
            // Axis
            .put(Axis.class, Axis.X, "x")
            .put(Axis.class, Axis.Y, "y")
            .put(Axis.class, Axis.Z, "z")
            // Direction
            .put(Direction.class, Direction.DOWN, "down")
            .put(Direction.class, Direction.UP, "up")
            .put(Direction.class, Direction.NORTH, "north")
            .put(Direction.class, Direction.SOUTH, "south")
            .put(Direction.class, Direction.WEST, "west")
            .put(Direction.class, Direction.EAST, "east")
            // Front-and-top
            .put(FrontAndTop.class, FrontAndTop.DOWN_EAST, "down_east")
            .put(FrontAndTop.class, FrontAndTop.DOWN_NORTH, "down_north")
            .put(FrontAndTop.class, FrontAndTop.DOWN_SOUTH, "down_south")
            .put(FrontAndTop.class, FrontAndTop.DOWN_WEST, "down_west")
            .put(FrontAndTop.class, FrontAndTop.UP_EAST, "up_east")
            .put(FrontAndTop.class, FrontAndTop.UP_NORTH, "up_north")
            .put(FrontAndTop.class, FrontAndTop.UP_SOUTH, "up_south")
            .put(FrontAndTop.class, FrontAndTop.UP_WEST, "up_west")
            .put(FrontAndTop.class, FrontAndTop.WEST_UP, "west_up")
            .put(FrontAndTop.class, FrontAndTop.EAST_UP, "east_up")
            .put(FrontAndTop.class, FrontAndTop.NORTH_UP, "north_up")
            .put(FrontAndTop.class, FrontAndTop.SOUTH_UP, "south_up")
            // Block attachment type
            .put(BlockAttachmentType.class, BlockAttachmentType.FLOOR, "floor")
            .put(BlockAttachmentType.class, BlockAttachmentType.WALL, "wall")
            .put(BlockAttachmentType.class, BlockAttachmentType.CEILING, "ceiling")
            // Bell attachment type
            .put(BellAttachmentType.class, BellAttachmentType.FLOOR, "floor")
            .put(BellAttachmentType.class, BellAttachmentType.CEILING, "ceiling")
            .put(BellAttachmentType.class, BellAttachmentType.SINGLE_WALL, "single_wall")
            .put(BellAttachmentType.class, BellAttachmentType.DOUBLE_WALL, "double_wall")
            // Wall connection type
            .put(WallConnectionType.class, WallConnectionType.NONE, "none")
            .put(WallConnectionType.class, WallConnectionType.LOW, "low")
            .put(WallConnectionType.class, WallConnectionType.TALL, "tall")
            // Redstone connection type
            .put(RedstoneConnectionType.class, RedstoneConnectionType.UP, "up")
            .put(RedstoneConnectionType.class, RedstoneConnectionType.SIDE, "side")
            .put(RedstoneConnectionType.class, RedstoneConnectionType.NONE, "none")
            // Double-block half
            .put(DoubleBlockHalf.class, DoubleBlockHalf.UPPER, "upper")
            .put(DoubleBlockHalf.class, DoubleBlockHalf.LOWER, "lower")
            // Vertical occupancy type
            .put(BlockVerticalOccupancyType.class, BlockVerticalOccupancyType.TOP, "top")
            .put(BlockVerticalOccupancyType.class, BlockVerticalOccupancyType.BOTTOM, "bottom")
            // Rail shape
            .put(RailShape.class, RailShape.NORTH_SOUTH, "north_south")
            .put(RailShape.class, RailShape.NORTH_WEST, "north_west")
            .put(RailShape.class, RailShape.NORTH_EAST, "north_east")
            .put(RailShape.class, RailShape.SOUTH_EAST, "south_east")
            .put(RailShape.class, RailShape.SOUTH_WEST, "south_west")
            .put(RailShape.class, RailShape.EAST_WEST, "east_west")
            .put(RailShape.class, RailShape.ASCENDING_EAST, "ascending_east")
            .put(RailShape.class, RailShape.ASCENDING_WEST, "ascending_west")
            .put(RailShape.class, RailShape.ASCENDING_NORTH, "ascending_north")
            .put(RailShape.class, RailShape.ASCENDING_SOUTH, "ascending_south")
            // Bed part
            .put(BedPart.class, BedPart.HEAD, "head")
            .put(BedPart.class, BedPart.FOOT, "foot")
            // Chest connection type
            .put(ChestConnectionType.class, ChestConnectionType.SINGLE, "single")
            .put(ChestConnectionType.class, ChestConnectionType.LEFT, "left")
            .put(ChestConnectionType.class, ChestConnectionType.RIGHT, "right")
            // Comparator mode
            .put(ComparatorMode.class, ComparatorMode.COMPARE, "compare")
            .put(ComparatorMode.class, ComparatorMode.SUBTRACT, "subtract")
            // Door hinge side
            .put(DoorHingeSide.class, DoorHingeSide.LEFT, "left")
            .put(DoorHingeSide.class, DoorHingeSide.RIGHT, "right")
            // Note-block instrument
            .put(NoteBlockInstrument.class, NoteBlockInstrument.HARP, "harp")
            .put(NoteBlockInstrument.class, NoteBlockInstrument.BASS_DRUM, "basedrum")
            .put(NoteBlockInstrument.class, NoteBlockInstrument.SNARE, "snare")
            .put(NoteBlockInstrument.class, NoteBlockInstrument.HAT, "hat")
            .put(NoteBlockInstrument.class, NoteBlockInstrument.BASS, "bass")
            .put(NoteBlockInstrument.class, NoteBlockInstrument.FLUTE, "flute")
            .put(NoteBlockInstrument.class, NoteBlockInstrument.BELL, "bell")
            .put(NoteBlockInstrument.class, NoteBlockInstrument.GUITAR, "guitar")
            .put(NoteBlockInstrument.class, NoteBlockInstrument.CHIME, "chime")
            .put(NoteBlockInstrument.class, NoteBlockInstrument.XYLOPHONE, "xylophone")
            .put(NoteBlockInstrument.class, NoteBlockInstrument.IRON_XYLOPHONE, "iron_xylophone")
            .put(NoteBlockInstrument.class, NoteBlockInstrument.COW_BELL, "cow_bell")
            .put(NoteBlockInstrument.class, NoteBlockInstrument.DIDGERIDOO, "didgeridoo")
            .put(NoteBlockInstrument.class, NoteBlockInstrument.BIT, "bit")
            .put(NoteBlockInstrument.class, NoteBlockInstrument.BANJO, "banjo")
            .put(NoteBlockInstrument.class, NoteBlockInstrument.PLING, "pling")
            .put(NoteBlockInstrument.class, NoteBlockInstrument.ZOMBIE, "zombie")
            .put(NoteBlockInstrument.class, NoteBlockInstrument.SKELETON, "skeleton")
            .put(NoteBlockInstrument.class, NoteBlockInstrument.CREEPER, "creeper")
            .put(NoteBlockInstrument.class, NoteBlockInstrument.DRAGON, "dragon")
            .put(NoteBlockInstrument.class, NoteBlockInstrument.WITHER_SKELETON, "wither_skeleton")
            .put(NoteBlockInstrument.class, NoteBlockInstrument.PIGLIN, "piglin")
            .put(NoteBlockInstrument.class, NoteBlockInstrument.CUSTOM_HEAD, "custom_head")
            // Piston type
            .put(PistonType.class, PistonType.NORMAL, "normal")
            .put(PistonType.class, PistonType.STICKY, "sticky")
            // Slab occupancy type
            .put(SlabOccupancyType.class, SlabOccupancyType.TOP, "top")
            .put(SlabOccupancyType.class, SlabOccupancyType.BOTTOM, "bottom")
            .put(SlabOccupancyType.class, SlabOccupancyType.DOUBLE, "double")
            // Stairs shape
            .put(StairsShape.class, StairsShape.STRAIGHT, "straight")
            .put(StairsShape.class, StairsShape.INNER_LEFT, "inner_left")
            .put(StairsShape.class, StairsShape.INNER_RIGHT, "inner_right")
            .put(StairsShape.class, StairsShape.OUTER_LEFT, "outer_left")
            .put(StairsShape.class, StairsShape.OUTER_RIGHT, "outer_right")
            // Structure-block mode
            .put(StructureBlockMode.class, StructureBlockMode.SAVE, "save")
            .put(StructureBlockMode.class, StructureBlockMode.LOAD, "load")
            .put(StructureBlockMode.class, StructureBlockMode.CORNER, "corner")
            .put(StructureBlockMode.class, StructureBlockMode.DATA, "data")
            // Bamboo leaves type
            .put(BambooLeavesType.class, BambooLeavesType.NONE, "none")
            .put(BambooLeavesType.class, BambooLeavesType.SMALL, "small")
            .put(BambooLeavesType.class, BambooLeavesType.LARGE, "large")
            // Tilt
            .put(Tilt.class, Tilt.NONE, "none")
            .put(Tilt.class, Tilt.UNSTABLE, "unstable")
            .put(Tilt.class, Tilt.PARTIAL, "partial")
            .put(Tilt.class, Tilt.FULL, "full")
            // Dripstone thickness
            .put(DripstoneThickness.class, DripstoneThickness.TIP_MERGE, "tip_merge")
            .put(DripstoneThickness.class, DripstoneThickness.TIP, "tip")
            .put(DripstoneThickness.class, DripstoneThickness.FRUSTUM, "frustum")
            .put(DripstoneThickness.class, DripstoneThickness.MIDDLE, "middle")
            .put(DripstoneThickness.class, DripstoneThickness.BASE, "base")
            // Sculk sensor phase
            .put(SculkSensorPhase.class, SculkSensorPhase.INACTIVE, "inactive")
            .put(SculkSensorPhase.class, SculkSensorPhase.ACTIVE, "active")
            .put(SculkSensorPhase.class, SculkSensorPhase.COOLDOWN, "cooldown")
            // Trial spawner state
            .put(TrialSpawnerState.class, TrialSpawnerState.INACTIVE, "inactive")
            .put(TrialSpawnerState.class, TrialSpawnerState.WAITING_FOR_PLAYERS, "waiting_for_players")
            .put(TrialSpawnerState.class, TrialSpawnerState.ACTIVE, "active")
            .put(TrialSpawnerState.class, TrialSpawnerState.AWAITING_REWARD_EJECTION, "waiting_for_reward_ejection")
            .put(TrialSpawnerState.class, TrialSpawnerState.EJECTING_REWARD, "ejecting_reward")
            .put(TrialSpawnerState.class, TrialSpawnerState.COOLDOWN, "cooldown")
            // Vault state
            .put(VaultState.class, VaultState.INACTIVE, "inactive")
            .put(VaultState.class, VaultState.ACTIVE, "active")
            .put(VaultState.class, VaultState.UNLOCKING, "unlocking")
            .put(VaultState.class, VaultState.EJECTING, "ejecting")
            // Creaking heart state
            .put(CreakingHeartState.class, CreakingHeartState.UPROOTED, "uprooted")
            .put(CreakingHeartState.class, CreakingHeartState.DORMANT, "dormant")
            .put(CreakingHeartState.class, CreakingHeartState.AWAKE, "awake")
            // Test-block mode
            .put(TestBlockMode.class, TestBlockMode.START, "start")
            .put(TestBlockMode.class, TestBlockMode.LOG, "log")
            .put(TestBlockMode.class, TestBlockMode.FAIL, "fail")
            .put(TestBlockMode.class, TestBlockMode.ACCEPT, "accept")
            .build();

    private EnumPropertyValueEquivalents() {}

    /**
     * Gets an equivalent class of a Minecraft enum associated with
     * the specified {@linkplain JsonEnumStatePropertyValueType enum-state-property value type}.
     *
     * @param type the enum-state-property value type
     * @return the equivalent class
     * @throws IllegalArgumentException if no equivalent class was registered for
     *                                  the specified enum-state-property value type
     * @since 1.0
     */
    public static Class<?> equivalentClass(JsonEnumStatePropertyValueType type) {
        Class<?> equivalentClass = VALUE_TYPE_EQUIVALENTS.get(type);
        if (equivalentClass == null) {
            throw new IllegalArgumentException(
                    "Could not find an equivalent class for the specified enum-state-property value type: " + type
            );
        }
        return equivalentClass;
    }

    /**
     * Gets a {@linkplain Map map} associating string representations with constants of the specified class
     * (which is an equivalent to a Minecraft enum) that have these string representations.
     *
     * @param enumEquivalentClass the Minecraft enum equivalent class
     * @return the string representation map
     * @param <T> the type of the constants that the string representations are associated with
     * @throws IllegalArgumentException if no string representations have been registered
     *                                  for the specified Minecraft-enum-equivalent class
     * @since 1.0
     */
    public static <T> Map<String, T> stringRepresentations(Class<T> enumEquivalentClass) {
        // noinspection unchecked ; the value is retrieved from the map that was safely created with a builder
        Map<String, T> representations = (Map<String, T>) STRING_REPRESENTATIONS.get(enumEquivalentClass);
        if (representations == null) {
            throw new IllegalArgumentException(String.format(
                    "Could not find string representations for %s enum-equivalent class",
                    enumEquivalentClass.getName()
            ));
        }
        return representations;
    }

    /**
     * A builder of a {@linkplain Map map} associating Minecraft-enum-equivalent {@linkplain Class classes}
     * with {@linkplain Map maps} associating string representations with constants that these classes provide
     * and that have these string representations.
     *
     * @since 1.0
     * @see Map
     * @see Class
     */
    private static final class StringRepresentationsBuilder {

        private final Map<Class<?>, Map<String, ?>> stringRepresentations = new HashMap<>();

        /**
         * Registers a string representation of the specified enum-like constant.
         *
         * @param clazz the class of the specified enum-like constant
         * @param value the enum-like constant that the string representation should be registered for
         * @param stringRepresentation the string representation that the specified enum-like constant should have
         * @return this builder
         * @param <T> the type of specified enum-like constant whose string representation is being registered
         * @throws IllegalArgumentException if the specified enum-like constant is
         *                                  not an instance of the specified class
         * @since 1.0
         */
        private <T> StringRepresentationsBuilder put(Class<T> clazz, T value, String stringRepresentation) {
            if (!clazz.isInstance(value)) {
                throw new IllegalArgumentException(String.format(
                        "The specified value is not an instance of %s class",
                        clazz.getName()
                ));
            }

            // noinspection unchecked ; the map is not created anywhere else and the value has been class-checked
            Map<String, T> stringRepresentations = (Map<String, T>) this.stringRepresentations.computeIfAbsent(
                    clazz,
                    ignored -> new HashMap<>()
            );

            stringRepresentations.put(stringRepresentation, value);
            return this;
        }

        /**
         * Builds the string representation {@linkplain Map map}.
         *
         * @return the created string representation map
         * @since 1.0
         */
        private Map<Class<?>, Map<String, ?>> build() {
            return Map.copyOf(this.stringRepresentations);
        }
    }
}