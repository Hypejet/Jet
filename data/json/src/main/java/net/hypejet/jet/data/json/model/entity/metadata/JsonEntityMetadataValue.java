package net.hypejet.jet.data.json.model.entity.metadata;

import net.hypejet.jet.data.json.model.item.JsonItemComponent;
import net.hypejet.jet.data.json.model.position.JsonBlockPosition;
import net.hypejet.jet.data.json.model.position.JsonGlobalPosition;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * A value of {@linkplain JsonEntityMetadataEntry entity metadata entry}.
 *
 * @since 1.0
 * @see JsonEntityMetadataEntry
 */
@NullMarked
public sealed interface JsonEntityMetadataValue {
    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} containing a {@code byte}.
     *
     * @param value the byte value
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record Byte(byte value) implements JsonEntityMetadataValue {}

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} containing an {@code int}.
     *
     * @param value the int value
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record Int(int value) implements JsonEntityMetadataValue {}

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} containing a {@code long}.
     *
     * @param value the long value
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record Long(long value) implements JsonEntityMetadataValue {}

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} containing a {@code float}.
     *
     * @param value the float value
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record Float(float value) implements JsonEntityMetadataValue {}

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} containing a {@code boolean}.
     *
     * @param value the boolean value
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record Boolean(boolean value) implements JsonEntityMetadataValue {}

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} containing a {@linkplain String string}.
     *
     * @param value the string value
     * @since 1.0
     * @see String
     * @see JsonEntityMetadataValue
     */
    record StringValue(String value) implements JsonEntityMetadataValue {
        /**
         * Constructs the {@linkplain StringValue string entity metadata value}.
         *
         * @param value the string value
         * @since 1.0
         */
        public StringValue {
            Objects.requireNonNull(value, "value");
        }
    }

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} containing a text component.
     *
     * @param value the serialized text component
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record Component(BinaryTagHolder value) implements JsonEntityMetadataValue {
        /**
         * Constructs the {@linkplain Component component entity metadata value}.
         *
         * @param value the serialized text component
         * @since 1.0
         */
        public Component {
            Objects.requireNonNull(value, "value");
        }
    }

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} containing an optional text component.
     *
     * @param value the serialized text component, {@code null} if the text component is unspecified
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record OptionalComponent(@Nullable BinaryTagHolder value) implements JsonEntityMetadataValue {}

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} containing an item stack.
     *
     * @param count the count of the item stack
     * @param itemKey the key of the item used by the item stack
     * @param components a map associating keys with item components that the item stack has
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record ItemStack(int count, Key itemKey, Map<Key, JsonItemComponent> components)
            implements JsonEntityMetadataValue {
        /**
         * Constructs the {@linkplain ItemStack item stack entity metadata value}.
         *
         * @param count the count that the item stack should have
         * @param itemKey the key of the item that should be used by the item stack
         * @param components a map associating keys with item components that the item stack should have
         * @since 1.0
         */
        public ItemStack {
            Objects.requireNonNull(itemKey, "item key");
            components = Map.copyOf(Objects.requireNonNull(components, "components"));
        }
    }

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} containing a block state.
     *
     * @param index an index of the block state
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record BlockState(int index) implements JsonEntityMetadataValue {}

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} containing an optional block state.
     *
     * @param index an index of the block state, {@code null} if the block state is unspecified
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record OptionalBlockState(@Nullable Integer index) implements JsonEntityMetadataValue {}

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} containing a particle and its properties.
     *
     * @param value the serialized particle with properties
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record Particle(BinaryTagHolder value) implements JsonEntityMetadataValue {
        /**
         * Constructs the {@linkplain Particle particle entity metadata value}.
         *
         * @param value the serialized particle with properties
         * @since 1.0
         */
        public Particle {
            Objects.requireNonNull(value, "value");
        }
    }

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value}
     * containing a {@linkplain List list} of particles with their properties.
     *
     * @param particles a list of serialized particles with their properties
     * @since 1.0
     * @see JsonEntityMetadataValue
     * @see List
     */
    record ParticleList(List<BinaryTagHolder> particles) implements JsonEntityMetadataValue {
        /**
         * Constructs the {@linkplain ParticleList particle list metadata value}.
         *
         * @param particles a list of serialized particles with their properties
         * @since 1.0
         */
        public ParticleList {
            particles = List.copyOf(Objects.requireNonNull(particles, "particles"));
        }
    }

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} containing rotations around each vector axis.
     *
     * @param x a rotation around the {@code X} vector axis
     * @param y a rotation around the {@code Y} vector axis
     * @param z a rotation around the {@code Z} vector axis
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record Rotations(float x, float y, float z) implements JsonEntityMetadataValue {}

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value}
     * containing a {@linkplain JsonBlockPosition block position}.
     *
     * @param value the block position
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record BlockPosition(JsonBlockPosition value) implements JsonEntityMetadataValue {
        /**
         * Constructs the {@linkplain BlockPosition block position entity metadata value}.
         *
         * @param value the block position
         * @since 1.0
         */
        public BlockPosition {
            Objects.requireNonNull(value, "value");
        }
    }

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value}
     * containing an optional {@linkplain JsonBlockPosition block position}.
     *
     * @param value the block position, {@code null} if it is unspecified
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record OptionalBlockPosition(@Nullable JsonBlockPosition value) implements JsonEntityMetadataValue {}

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} representing a direction.
     *
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    enum Direction implements JsonEntityMetadataValue {
        /**
         * The downward direction.
         *
         * @since 1.0
         */
        DOWN,
        /**
         * The upward direction.
         *
         * @since 1.0
         */
        UP,
        /**
         * The northern direction.
         *
         * @since 1.0
         */
        NORTH,
        /**
         * The southern direction.
         *
         * @since 1.0
         */
        SOUTH,
        /**
         * The western direction.
         *
         * @since 1.0
         */
        WEST,
        /**
         * The eastern direction.
         *
         * @since 1.0
         */
        EAST
    }

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value}
     * containing an optional reference to a living entity.
     *
     * @param uniqueId a unique id of the referenced living entity, {@code null} if no living entity is referenced
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record OptionalLivingEntityReference(@Nullable UUID uniqueId) implements JsonEntityMetadataValue {}

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value}
     * containing an optional {@linkplain JsonGlobalPosition global position}.
     *
     * @param position the global position, {@code null} if it is unspecified
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record OptionalGlobalPosition(@Nullable JsonGlobalPosition position) implements JsonEntityMetadataValue {}

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} containing a compound binary tag.
     *
     * @param tagHolder the serialized compound binary tag
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record CompoundBinaryTag(BinaryTagHolder tagHolder) implements JsonEntityMetadataValue {
        /**
         * Constructs the {@linkplain CompoundBinaryTag compound binary tag entity metadata value}.
         *
         * @param tagHolder the serialized compound binary tag
         * @since 1.0
         */
        public CompoundBinaryTag {
            Objects.requireNonNull(tagHolder, "tag holder");
        }
    }

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} containing data of a villager entity.
     *
     * @param typeKey the key of type of the villager
     * @param professionKey the key of profession of the villager
     * @param level the level of the villager
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record VillagerData(Key typeKey, Key professionKey, int level) implements JsonEntityMetadataValue {
        /**
         * Constructs the {@linkplain VillagerData villager data entity metadata value}.
         *
         * @param typeKey the key of type of the villager
         * @param professionKey the key of profession of the villager
         * @param level the level of the villager
         * @since 1.0
         */
        public VillagerData {
            Objects.requireNonNull(typeKey, "type key");
            Objects.requireNonNull(professionKey, "profession key");
        }
    }

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} containing an optional positive {@code int}.
     *
     * @param value the positive int, {@code null} if it is unspecified
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record OptionalUnsignedInt(@Nullable Integer value) implements JsonEntityMetadataValue {
        /**
         * Constructs the {@linkplain OptionalUnsignedInt optional unsigned int entity metadata value}.
         *
         * @param value the positive int, {@code null} if it should be unspecified
         * @since 1.0
         */
        public OptionalUnsignedInt {
            if (value != null && value < 0)
                throw new IllegalArgumentException("The value must be positive");
        }
    }

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} representing an entity pose.
     *
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    enum Pose implements JsonEntityMetadataValue {
        /**
         * The standing pose.
         *
         * @since 1.0
         */
        STANDING,
        /**
         * The elytra gliding pose.
         *
         * @since 1.0
         */
        FALL_FLYING,
        /**
         * The sleeping pose.
         *
         * @since 1.0
         */
        SLEEPING,
        /**
         * The swimming pose.
         *
         * @since 1.0
         */
        SWIMMING,
        /**
         * The pose used while charging from a trident with the riptide enchantment.
         *
         * @since 1.0
         */
        SPIN_ATTACK,
        /**
         * The crouching pose.
         *
         * @since 1.0
         */
        CROUCHING,
        /**
         * The long jumping pose.
         *
         * @since 1.0
         */
        LONG_JUMPING,
        /**
         * The dying pose.
         *
         * @since 1.0
         */
        DYING,
        /**
         * The croaking pose.
         *
         * @since 1.0
         */
        CROAKING,
        /**
         * The using-tongue pose.
         *
         * @since 1.0
         */
        USING_TONGUE,
        /**
         * The sitting pose.
         *
         * @since 1.0
         */
        SITTING,
        /**
         * The roaring pose.
         *
         * @since 1.0
         */
        ROARING,
        /**
         * The sniffing pose.
         *
         * @since 1.0
         */
        SNIFFING,
        /**
         * The emerging pose.
         *
         * @since 1.0
         */
        EMERGING,
        /**
         * The digging pose.
         *
         * @since 1.0
         */
        DIGGING,
        /**
         * The sliding pose.
         *
         * @since 1.0
         */
        SLIDING,
        /**
         * The shooting pose.
         *
         * @since 1.0
         */
        SHOOTING,
        /**
         * The inhaling pose.
         *
         * @since 1.0
         */
        INHALING
    }

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} containing a cat variant.
     *
     * @param key the key of the cat variant
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record CatVariant(Key key) implements JsonEntityMetadataValue {
        /**
         * Constructs the {@linkplain CatVariant cat variant entity metadata value}.
         *
         * @param key the key of the cat variant
         * @since 1.0
         */
        public CatVariant {
            Objects.requireNonNull(key, "key");
        }
    }

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} containing a chicken variant.
     *
     * @param key the key of the chicken variant
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record ChickenVariant(Key key) implements JsonEntityMetadataValue {
        /**
         * Constructs the {@linkplain ChickenVariant chicken variant entity metadata value}.
         *
         * @param key the key of the chicken variant
         * @since 1.0
         */
        public ChickenVariant {
            Objects.requireNonNull(key, "key");
        }
    }

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} containing a cow variant.
     *
     * @param key the key of the cow variant
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record CowVariant(Key key) implements JsonEntityMetadataValue {
        /**
         * Constructs the {@linkplain CowVariant cow variant entity metadata value}.
         *
         * @param key the key of the cow variant
         * @since 1.0
         */
        public CowVariant {
            Objects.requireNonNull(key, "key");
        }
    }

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} containing a wolf variant.
     *
     * @param key the key of the wolf variant
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record WolfVariant(Key key) implements JsonEntityMetadataValue {
        /**
         * Constructs the {@linkplain WolfVariant wolf variant entity metadata value}.
         *
         * @param key the key of the wolf variant
         * @since 1.0
         */
        public WolfVariant {
            Objects.requireNonNull(key, "key");
        }
    }

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} containing a wolf sound variant.
     *
     * @param key the key of the wolf sound variant
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record WolfSoundVariant(Key key) implements JsonEntityMetadataValue {
        /**
         * Constructs the {@linkplain WolfSoundVariant wolf sound variant entity metadata value}.
         *
         * @param key the key of the wolf sound variant
         * @since 1.0
         */
        public WolfSoundVariant {
            Objects.requireNonNull(key, "key");
        }
    }

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} containing a frog variant.
     *
     * @param key the key of the frog variant
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record FrogVariant(Key key) implements JsonEntityMetadataValue {
        /**
         * Constructs the {@linkplain FrogVariant frog variant entity metadata value}.
         *
         * @param key the key of the frog variant
         * @since 1.0
         */
        public FrogVariant {
            Objects.requireNonNull(key, "key");
        }
    }

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} containing a pig variant.
     *
     * @param key the key of the pig variant
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record PigVariant(Key key) implements JsonEntityMetadataValue {
        /**
         * Constructs the {@linkplain PigVariant pig variant entity metadata value}.
         *
         * @param key the key of the pig variant
         * @since 1.0
         */
        public PigVariant {
            Objects.requireNonNull(key, "key");
        }
    }

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} containing a painting variant.
     *
     * @param key the key of the painting variant
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record PaintingVariant(Key key) implements JsonEntityMetadataValue {
        /**
         * Constructs the {@linkplain PaintingVariant painting variant entity metadata value}.
         *
         * @param key the key of the painting variant
         * @since 1.0
         */
        public PaintingVariant {
            Objects.requireNonNull(key, "key");
        }
    }

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} representing an armadillo state.
     *
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    enum ArmadilloState implements JsonEntityMetadataValue {
        /**
         * The idle state.
         *
         * @since 1.0
         */
        IDLE,
        /**
         * The rolling state.
         *
         * @since 1.0
         */
        ROLLING,
        /**
         * The scared state.
         *
         * @since 1.0
         */
        SCARED,
        /**
         * The unrolling state.
         *
         * @since 1.0
         */
        UNROLLING
    }

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} representing a sniffer state
     *
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    enum SnifferState implements JsonEntityMetadataValue {
        /**
         * THe idling state.
         *
         * @since 1.0
         */
        IDLING,
        /**
         * The feeling happy state.
         *
         * @since 1.0
         */
        FEELING_HAPPY,
        /**
         * The scenting state.
         *
         * @since 1.0
         */
        SCENTING,
        /**
         * The sniffing state.
         *
         * @since 1.0
         */
        SNIFFING,
        /**
         * The searching state.
         *
         * @since 1.0
         */
        SEARCHING,
        /**
         * The digging state.
         *
         * @since 1.0
         */
        DIGGING,
        /**
         * The rising state.
         *
         * @since 1.0
         */
        RISING
    }

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} containing a vector.
     *
     * @param x the {@code X} axis value of the vector
     * @param y the {@code Y} axis value of the vector
     * @param z the {@code Z} axis value of the vector
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record Vector(float x, float y, float z) implements JsonEntityMetadataValue {}

    /**
     * An {@linkplain JsonEntityMetadataValue entity metadata value} containing a quaternion.
     *
     * @param w the rotation angle scalar of the quaternion
     * @param x the {@code X} axis value of the vector part of the quaternion
     * @param y the {@code Y} axis value of the vector part of the quaternion
     * @param z the {@code Z} axis value of the vector part of the quaternion
     * @since 1.0
     * @see JsonEntityMetadataValue
     */
    record Quaternion(float w, float x, float y, float z) implements JsonEntityMetadataValue {}
}