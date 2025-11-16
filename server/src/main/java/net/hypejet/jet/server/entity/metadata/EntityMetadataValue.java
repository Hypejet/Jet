package net.hypejet.jet.server.entity.metadata;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.armadillo.ArmadilloState;
import net.hypejet.jet.entity.pose.Pose;
import net.hypejet.jet.entity.sniffer.SnifferState;
import net.hypejet.jet.entity.variant.cat.CatVariant;
import net.hypejet.jet.entity.variant.chicken.ChickenVariant;
import net.hypejet.jet.entity.variant.cow.CowVariant;
import net.hypejet.jet.entity.variant.frog.FrogVariant;
import net.hypejet.jet.entity.variant.painting.PaintingVariant;
import net.hypejet.jet.entity.variant.pig.PigVariant;
import net.hypejet.jet.entity.variant.wolf.WolfSoundVariant;
import net.hypejet.jet.entity.variant.wolf.WolfVariant;
import net.hypejet.jet.entity.villager.VillagerProfession;
import net.hypejet.jet.entity.villager.VillagerType;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.inventory.item.JetItemStack;
import net.hypejet.jet.server.world.block.state.JetBlockState;
import net.hypejet.jet.util.range.RangeUtil;
import net.hypejet.jet.world.coordinate.BlockPosition;
import net.hypejet.jet.world.coordinate.floats.FloatQuaternion;
import net.hypejet.jet.world.coordinate.floats.FloatVector;
import net.hypejet.jet.world.coordinate.rotation.Rotations;
import net.hypejet.jet.world.direction.Direction;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A single value of an {@linkplain EntityMetadata entity metadata}.
 * 
 * @since 1.0
 * @see EntityMetadata
 */
@NullMarked
// TODO: Implement all possible values
public sealed interface EntityMetadataValue {
    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing a {@code byte}.
     *
     * @param value the byte that this entity metadata value represents
     * @since 1.0
     * @see EntityMetadataValue
     */
    record Byte(byte value) implements EntityMetadataValue {}

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing an {@code int}.
     *
     * @param value the int that this entity metadata value represents
     * @since 1.0
     * @see EntityMetadataValue
     */
    record Int(int value) implements EntityMetadataValue {}

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing a {@code long}.
     *
     * @param value the long that this entity metadata value represents
     * @since 1.0
     * @see EntityMetadataValue
     */
    record Long(long value) implements EntityMetadataValue {}

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing a {@code float}.
     *
     * @param value the float that this entity metadata value represents
     * @since 1.0
     * @see EntityMetadataValue
     */
    record Float(float value) implements EntityMetadataValue {}

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing an {@code boolean}.
     *
     * @param value the boolean that this entity metadata value represents
     * @since 1.0
     * @see EntityMetadataValue
     */
    record Boolean(boolean value) implements EntityMetadataValue {}

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing a {@linkplain String string}.
     *
     * @param value the string that this entity metadata value represents
     * @since 1.0
     * @see String
     * @see EntityMetadataValue
     */
    record StringValue(String value) implements EntityMetadataValue {
        /**
         * Constructs the {@linkplain StringValue string entity metadata value}.
         *
         * @param value the string that the constructed string entity metadata value should represent
         * @since 1.0
         */
        public StringValue {
            Objects.requireNonNull(value, "value");
        }
    }

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing a {@linkplain Component component}.
     *
     * @param value the component that this entity metadata value represents
     * @since 1.0
     * @see Component
     * @see EntityMetadataValue
     */
    record ComponentValue(Component value) implements EntityMetadataValue {
        /**
         * Constructs the {@linkplain ComponentValue component entity metadata value}.
         *
         * @param value the component that the constructed entity metadata value should represent
         * @since 1.0
         */
        public ComponentValue {
            Objects.requireNonNull(value, "value");
        }
    }

    /**
     * An {@linkplain EntityMetadataValue entity metadata value}
     * representing an optional {@linkplain Component component}.
     *
     * @param value the component that this entity metadata value represents,
     *              {@code null} if the component is unspecified
     * @since 1.0
     * @see Component
     * @see EntityMetadataValue
     */
    record OptionalComponentValue(@Nullable Component value) implements EntityMetadataValue {}

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing an {@linkplain JetItemStack item stack}.
     *
     * @param value the item stack that this entity metadata value represents
     * @since 1.0
     * @see JetItemStack
     * @see EntityMetadataValue
     */
    record ItemStackValue(JetItemStack value) implements EntityMetadataValue {
        /**
         * Constructs the {@linkplain ItemStackValue item stack entity metadata value}.
         *
         * @param value the item stack that the constructed item stack entity metadata value should represent
         * @since 1.0
         */
        public ItemStackValue {
            Objects.requireNonNull(value, "value");
        }
    }

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing a {@linkplain JetBlockState block state}.
     *
     * @param value the block state that this entity metadata value represents
     * @since 1.0
     * @see JetBlockState
     * @see EntityMetadataValue
     */
    record BlockStateValue(JetBlockState value) implements EntityMetadataValue {
        /**
         * Constructs the {@linkplain BlockStateValue block state entity metadata value}.
         *
         * @param value the block state that the constructed block state value should represent
         * @since 1.0
         */
        public BlockStateValue {
            Objects.requireNonNull(value, "value");
        }
    }

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing
     * an optional {@linkplain JetBlockState block state}.
     *
     * @param value the block state that this entity metadata value represents,
     *              {@code null} if the block state is unspecified
     * @since 1.0
     * @see JetBlockState
     * @see EntityMetadataValue
     */
    record OptionalBlockStateValue(@Nullable JetBlockState value) implements EntityMetadataValue {}

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing a particle.
     *
     * @param value the particle that this entity metadata value represents, serialized as a binary tag
     * @since 1.0
     * @see EntityMetadataValue
     */
    // TODO: Replace the value with particle type
    record Particle(BinaryTag value) implements EntityMetadataValue {
        /**
         * Constructs the {@linkplain Particle particle entity metadata value}.
         *
         * @param value the particle that the constructed entity metadata value
         *              should represent, serialized as a binary tag
         * @since 1.0
         */
        public Particle {
            Objects.requireNonNull(value, "value");
        }
    }

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing a {@linkplain List list} of particles.
     *
     * @param value the list of serialized particles that this entity metadata value represents
     * @since 1.0
     * @see List
     * @see EntityMetadataValue
     */
    // TODO: Replace the value with particle type
    record ParticleList(List<BinaryTag> value) implements EntityMetadataValue {
        /**
         * Constructs the {@linkplain ParticleList particle list entity metadata value}.
         *
         * @param value the list of serialized particles that the constructed entity metadata value should represent
         * @since 1.0
         */
        public ParticleList {
            value = List.copyOf(Objects.requireNonNull(value, "value"));
        }
    }

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing {@linkplain Rotations rotations}.
     *
     * @param value the rotations that this entity metadata value represents
     * @since 1.0
     * @see Rotations
     * @see EntityMetadataValue
     */
    record RotationsValue(Rotations value) implements EntityMetadataValue {
        /**
         * Constructs the {@linkplain RotationsValue rotations entity metadata value}.
         *
         * @param value the rotations that the constructed entity metadata value should represent
         * @since 1.0
         */
        public RotationsValue {
            Objects.requireNonNull(value, "value");
        }
    }

    /**
     * An {@linkplain EntityMetadataValue entity metadata value}
     * representing {@linkplain BlockPosition block position}.
     *
     * @param value the block position that this entity metadata value represents
     * @version 1.0
     * @see BlockPosition
     * @see EntityMetadataValue
     */
    record BlockPositionValue(BlockPosition value) implements EntityMetadataValue {
        /**
         * Constructs the {@linkplain BlockPositionValue block position entity metadata value}.
         *
         * @param value the block position that the constructed entity metadata value should represent
         * @since 1.0
         */
        public BlockPositionValue {
            Objects.requireNonNull(value, "value");
        }
    }

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing
     * an optional {@linkplain BlockPosition block position}.
     *
     * @param value the block position that this entity metadata value represents,
     *              {@code null} if the block position is unspecified
     * @since 1.0
     * @see BlockPosition
     * @see EntityMetadataValue
     */
    record OptionalBlockPositionValue(@Nullable BlockPosition value) implements EntityMetadataValue {}

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing a {@linkplain Direction direction}.
     *
     * @param value the direction that this entity metadata value represents
     * @since 1.0
     * @see Direction
     * @see EntityMetadataValue
     */
    record DirectionValue(Direction value) implements EntityMetadataValue {
        /**
         * Constructs the {@linkplain DirectionValue direction entity metadata value}.
         *
         * @param value the direction that the constructed entity metadata value should represent
         * @since 1.0
         */
        public DirectionValue {
            Objects.requireNonNull(value, "value");
        }
    }

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing
     * an optional reference to a living {@linkplain Entity entity}.
     *
     * @param value the unique id of the entity that this entity metadata value references to,
     *              {@code null} if this entity metadata value does not reference to any living entity
     * @since 1.0
     * @see Entity
     * @see EntityMetadataValue
     */
    record OptionalLivingEntityReference(@Nullable UUID value) implements EntityMetadataValue {}

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing
     * a {@linkplain CompoundBinaryTag compound binary tag}.
     *
     * @param value the compound binary tag that this entity metadata value represents
     * @since 1.0
     * @see CompoundBinaryTag
     * @see EntityMetadataValue
     */
    record CompoundBinaryTagValue(CompoundBinaryTag value) implements EntityMetadataValue {
        /**
         * Constructs the {@linkplain CompoundBinaryTagValue compound binary tag entity metadata value}.
         *
         * @param value the compound binary tag that the constructed entity metadata value should represent
         * @since 1.0
         */
        public CompoundBinaryTagValue {
            Objects.requireNonNull(value, "value");
        }
    }

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing
     * a data of villager {@linkplain Entity entity}.
     *
     * @param type the villager type of the villager entity
     * @param profession the villager profession of the villager entity
     * @param level the level of the villager entity
     * @since 1.0
     * @see Entity
     * @see EntityMetadataValue
     */
    record VillagerData(Holder<VillagerType> type, Holder<VillagerProfession> profession, int level)
            implements EntityMetadataValue {
        /**
         * Constructs the {@linkplain VillagerData villager data entity metadata value}.
         *
         * @param type the villager type of the villager data
         *             that the constructed entity metadata value should represent
         * @param profession the villager profession of the villager data
         *                   that the constructed entity metadata value should represent
         * @param level the level of the villager data that the constructed entity metadata value should represent
         * @since 1.0
         */
        public VillagerData {
            Objects.requireNonNull(type, "type");
            Objects.requireNonNull(profession, "profession");
        }
    }

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing
     * an optional non-negative {@linkplain Integer integer}.
     *
     * @param value the integer value that this entity metadata value represents,
     *              {@code null} if the integer is unspecified
     * @since 1.0
     * @see Integer
     * @see EntityMetadataValue
     */
    record OptionalUnsignedInt(@Range(from = 0, to = Integer.MAX_VALUE) @Nullable Integer value)
            implements EntityMetadataValue {
        /**
         * Constructs the {@linkplain OptionalUnsignedInt optional unsigned int entity metadata value}.
         *
         * @param value the integer value that the constructed entity metadata value
         *              should represent, {@code null} if the integer should be unspecified
         * @throws IllegalArgumentException if the specified int value is negative
         * @since 1.0
         */
        public OptionalUnsignedInt {
            if (value != null)
                RangeUtil.ensureNotNegative(value);
        }
    }

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing a {@linkplain Pose pose}.
     *
     * @param value the pose that this entity metadata value represents
     * @since 1.0
     * @see Pose
     * @see EntityMetadataValue
     */
    record PoseValue(Pose value) implements EntityMetadataValue {
        /**
         * Constructs the {@linkplain PoseValue pose entity metadata value}.
         *
         * @param value the pose that the constructed entity metadata value should represent
         * @since 1.0
         */
        public PoseValue {
            Objects.requireNonNull(value, "value");
        }
    }

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing
     * a {@linkplain Holder.Reference holder referencing} to a {@linkplain CatVariant cat variant}.
     *
     * @param value the cat variant reference that this entity metadata value represents
     * @since 1.0
     * @see CatVariant
     * @see Holder.Reference
     * @see EntityMetadataValue
     */
    record CatVariantValue(Holder.Reference<CatVariant> value) implements EntityMetadataValue {
        /**
         * Constructs the {@linkplain CatVariantValue cat variant entity metadata value}.
         *
         * @param value the cat variant reference that the constructed entity metadata value should represent
         * @since 1.0
         */
        public CatVariantValue {
            Objects.requireNonNull(value, "value");
        }
    }

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing
     * a {@linkplain Holder.Reference holder referencing} to a {@linkplain ChickenVariant chicken variant}.
     *
     * @param value the chicken variant reference that this entity metadata value represents
     * @since 1.0
     * @see ChickenVariant
     * @see Holder.Reference
     * @see EntityMetadataValue
     */
    record ChickenVariantValue(Holder.Reference<ChickenVariant> value) implements EntityMetadataValue {
        /**
         * Constructs the {@linkplain ChickenVariantValue chicken variant entity metadata value}.
         *
         * @param value the chicken variant reference that the constructed entity metadata value should represent
         * @since 1.0
         */
        public ChickenVariantValue {
            Objects.requireNonNull(value, "value");
        }
    }

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing
     * a {@linkplain Holder.Reference holder referencing} to a {@linkplain CowVariant cow variant}.
     *
     * @param value the cow variant reference that this entity metadata value represents
     * @since 1.0
     * @see CowVariant
     * @see Holder.Reference
     * @see EntityMetadataValue
     */
    record CowVariantValue(Holder.Reference<CowVariant> value) implements EntityMetadataValue {
        /**
         * Constructs the {@linkplain CatVariantValue cow variant entity metadata value}.
         *
         * @param value the cow variant reference that the constructed entity metadata value should represent
         * @since 1.0
         */
        public CowVariantValue {
            Objects.requireNonNull(value, "value");
        }
    }

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing
     * a {@linkplain Holder.Reference holder referencing} to a {@linkplain WolfVariant wolf variant}.
     *
     * @param value the wolf variant reference that this entity metadata value represents
     * @since 1.0
     * @see WolfVariant
     * @see Holder.Reference
     * @see EntityMetadataValue
     */
    record WolfVariantValue(Holder.Reference<WolfVariant> value) implements EntityMetadataValue {
        /**
         * Constructs the {@linkplain WolfVariantValue wolf variant entity metadata value}.
         *
         * @param value the wolf variant reference that the constructed entity metadata value should represent
         * @since 1.0
         */
        public WolfVariantValue {
            Objects.requireNonNull(value, "value");
        }
    }

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing
     * a {@linkplain Holder.Reference holder referencing} to a {@linkplain WolfSoundVariant wolf sound variant}.
     *
     * @param value the wolf sound variant reference that this entity metadata value represents
     * @since 1.0
     * @see WolfSoundVariant
     * @see Holder.Reference
     * @see EntityMetadataValue
     */
    record WolfSoundVariantValue(Holder.Reference<WolfVariant> value) implements EntityMetadataValue {
        /**
         * Constructs the {@linkplain WolfVariantValue wolf sound variant entity metadata value}.
         *
         * @param value the wolf sound variant reference that the constructed entity metadata value should represent
         * @since 1.0
         */
        public WolfSoundVariantValue {
            Objects.requireNonNull(value, "value");
        }
    }

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing
     * a {@linkplain Holder.Reference holder referencing} to a {@linkplain FrogVariant frog variant}.
     *
     * @param value the frog variant reference that this entity metadata value represents
     * @since 1.0
     * @see FrogVariant
     * @see Holder.Reference
     * @see EntityMetadataValue
     */
    record FrogVariantValue(Holder.Reference<FrogVariant> value) implements EntityMetadataValue {
        /**
         * Constructs the {@linkplain FrogVariantValue frog variant entity metadata value}.
         *
         * @param value the frog variant reference that the constructed entity metadata value should represent
         * @since 1.0
         */
        public FrogVariantValue {
            Objects.requireNonNull(value, "value");
        }
    }

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing
     * a {@linkplain Holder.Reference holder referencing} to a {@linkplain PigVariant pig variant}.
     *
     * @param value the pig variant reference that this entity metadata value represents
     * @since 1.0
     * @see PigVariant
     * @see Holder.Reference
     * @see EntityMetadataValue
     */
    record PigVariantValue(Holder.Reference<PigVariant> value) implements EntityMetadataValue {
        /**
         * Constructs the {@linkplain PigVariantValue pig variant entity metadata value}.
         *
         * @param value the pig variant reference that the constructed entity metadata value should represent
         * @since 1.0
         */
        public PigVariantValue {
            Objects.requireNonNull(value, "value");
        }
    }

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing
     * a {@linkplain Holder.Reference holder referencing} to a {@linkplain PaintingVariant painting variant}.
     *
     * @param value the painting variant reference that this entity metadata value represents
     * @since 1.0
     * @see PaintingVariant
     * @see Holder.Reference
     * @see EntityMetadataValue
     */
    record PaintingVariantValue(Holder.Reference<PaintingVariant> value) implements EntityMetadataValue {
        /**
         * Constructs the {@linkplain PaintingVariantValue painting variant entity metadata value}.
         *
         * @param value the painting variant reference that the constructed entity metadata value should represent
         * @since 1.0
         */
        public PaintingVariantValue {
            Objects.requireNonNull(value, "value");
        }
    }

    /**
     * An {@linkplain EntityMetadataValue entity metadata value}
     * representing an {@linkplain ArmadilloState armadillo state}.
     *
     * @param value the armadillo state that this entity metadata value represents
     * @since 1.0
     * @see ArmadilloState
     * @see EntityMetadataValue
     */
    record ArmadilloStateValue(ArmadilloState value) implements EntityMetadataValue {
        /**
         * Constructs the {@linkplain ArmadilloStateValue armadillo state entity metadata value}.
         *
         * @param value the armadillo state that the constructed entity metadata value should represent
         * @since 1.0
         */
        public ArmadilloStateValue {
            Objects.requireNonNull(value, "value");
        }
    }

    /**
     * An {@linkplain EntityMetadataValue entity metadata value}
     * representing a {@linkplain SnifferState sniffer state}.
     *
     * @param value the sniffer state that this entity metadata value represents
     * @since 1.0
     * @see SnifferState
     * @see EntityMetadataValue
     */
    record SnifferStateValue(SnifferState value) implements EntityMetadataValue {
        /**
         * Constructs the {@linkplain SnifferStateValue sniffer state entity metadata value}.
         *
         * @param value the sniffer state that the constructed entity metadata value should represent
         * @since 1.0
         */
        public SnifferStateValue {
            Objects.requireNonNull(value, "value");
        }
    }

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing a {@linkplain FloatVector float vector}.
     *
     * @param value the float vector that this entity metadata value represents
     * @since 1.0
     * @see FloatVector
     * @see EntityMetadataValue
     */
    record VectorValue(FloatVector value) implements EntityMetadataValue {
        /**
         * Constructs the {@linkplain VectorValue vector entity metadata value}.
         *
         * @param value the float vector that the constructed entity metadata value should represent
         * @since 1.0
         */
        public VectorValue {
            Objects.requireNonNull(value, "value");
        }
    }

    /**
     * An {@linkplain EntityMetadataValue entity metadata value}
     * representing a {@linkplain FloatQuaternion float quaternion}.
     *
     * @param value the float quaternion that this entity metadata value represents
     * @since 1.0
     * @see FloatQuaternion
     * @see EntityMetadataValue
     */
    record QuaternionValue(FloatQuaternion value) implements EntityMetadataValue {
        /**
         * Constructs the {@linkplain QuaternionValue quaternion entity metadata value}.
         *
         * @param value the float quaternion that the constructed entity metadata value should represent
         * @since 1.0
         */
        public QuaternionValue {
            Objects.requireNonNull(value, "value");
        }
    }
}