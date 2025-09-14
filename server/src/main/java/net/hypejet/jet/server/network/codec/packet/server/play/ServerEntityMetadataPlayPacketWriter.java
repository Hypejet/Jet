package net.hypejet.jet.server.network.codec.packet.server.play;

import com.google.common.collect.Sets;
import io.netty.buffer.ByteBuf;
import io.netty.util.collection.IntObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.hypejet.jet.entity.armadillo.ArmadilloState;
import net.hypejet.jet.entity.pose.Pose;
import net.hypejet.jet.entity.sniffer.SnifferState;
import net.hypejet.jet.server.entity.metadata.EntityMetadataValue;
import net.hypejet.jet.server.entity.metadata.JetEntityMetadata;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.codec.game.component.ComponentNetworkWriter;
import net.hypejet.jet.server.network.codec.game.miscellaneous.BinaryTagNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.coordinate.BlockPositionNetworkCodec;
import net.hypejet.jet.server.network.codec.game.world.coordinate.floats.FloatQuaternionNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.coordinate.floats.FloatVectorNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.coordinate.rotations.RotationsNetworkWriter;
import net.hypejet.jet.server.network.codec.index.IndexNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarLongNetworkCodec;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.codec.other.UUIDNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerEntityMetadataPlayPacket;
import net.hypejet.jet.server.util.NetworkUtil;
import net.hypejet.jet.server.util.collection.Object2IntMapBuilder;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.hypejet.jet.world.direction.Direction;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@linkplain NetworkWriter network writer}
 * of {@linkplain ServerEntityMetadataPlayPacket server entity metadata play packets}.
 *
 * @since 1.0
 * @see NetworkWriter
 * @see ServerEntityMetadataPlayPacket
 */
public final class ServerEntityMetadataPlayPacketWriter implements NetworkWriter<ServerEntityMetadataPlayPacket> {

    private static final CollectionNetworkWriter<IntObjectMap.PrimitiveEntry<EntityMetadataValue>>
            UPDATES_WRITER = new CollectionNetworkWriter<>(false, new MetadataUpdateNetworkWriter());

    /**
     * An instance of the {@linkplain ServerEntityMetadataPlayPacketWriter server entity metadata play packet writer}.
     *
     * @since 1.0
     */
    public static final ServerEntityMetadataPlayPacketWriter INSTANCE = new ServerEntityMetadataPlayPacketWriter();

    private static final Object2IntMap<Class<?>> DATA_TYPE_IDS = new Object2IntMapBuilder<Class<?>>()
            .put(EntityMetadataValue.Byte.class, 0)
            .put(EntityMetadataValue.Int.class, 1)
            .put(EntityMetadataValue.Long.class, 2)
            .put(EntityMetadataValue.Float.class, 3)
            .put(EntityMetadataValue.StringValue.class, 4)
            .put(EntityMetadataValue.ComponentValue.class, 5)
            .put(EntityMetadataValue.OptionalComponentValue.class, 6)
            .put(EntityMetadataValue.ItemStackValue.class, 7)
            .put(EntityMetadataValue.Boolean.class, 8)
            .put(EntityMetadataValue.RotationsValue.class, 9)
            .put(EntityMetadataValue.BlockPositionValue.class, 10)
            .put(EntityMetadataValue.OptionalBlockPositionValue.class, 11)
            .put(EntityMetadataValue.DirectionValue.class, 12)
            .put(EntityMetadataValue.OptionalLivingEntityReference.class, 13)
            .put(EntityMetadataValue.BlockStateValue.class, 14)
            .put(EntityMetadataValue.OptionalBlockStateValue.class, 15)
            .put(EntityMetadataValue.CompoundBinaryTagValue.class, 16)
            .put(EntityMetadataValue.Particle.class, 17)
            .put(EntityMetadataValue.ParticleList.class, 18)
            .put(EntityMetadataValue.VillagerData.class, 19)
            .put(EntityMetadataValue.OptionalUnsignedInt.class, 20)
            .put(EntityMetadataValue.PoseValue.class, 21)
            .put(EntityMetadataValue.CatVariantValue.class, 22)
            .put(EntityMetadataValue.CowVariantValue.class, 23)
            .put(EntityMetadataValue.WolfVariantValue.class, 24)
            .put(EntityMetadataValue.WolfSoundVariantValue.class, 25)
            .put(EntityMetadataValue.FrogVariantValue.class, 26)
            .put(EntityMetadataValue.PigVariantValue.class, 27)
            .put(EntityMetadataValue.ChickenVariantValue.class, 28)
            // ID 29 is reserved for optional global pos type, however it is not used anywhere
            .put(EntityMetadataValue.PaintingVariantValue.class, 30)
            .put(EntityMetadataValue.SnifferStateValue.class, 31)
            .put(EntityMetadataValue.ArmadilloStateValue.class, 32)
            .put(EntityMetadataValue.VectorValue.class, 33)
            .put(EntityMetadataValue.QuaternionValue.class, 34)
            .build();

    private static final NetworkWriter<ArmadilloState> ARMADILLO_STATE_WRITER = new IndexNetworkCodec<>(
            IndexUtil.fromMap(Map.of(
                    0, ArmadilloState.IDLE,
                    1, ArmadilloState.ROLLING,
                    2, ArmadilloState.SCARED,
                    3, ArmadilloState.UNROLLING
            )),
            VarIntNetworkCodec.INSTANCE
    );

    private static final NetworkWriter<SnifferState> SNIFFER_STATE_WRITER = new IndexNetworkCodec<>(
            IndexUtil.fromMap(Map.of(
                    0, SnifferState.IDLING,
                    1, SnifferState.FEELING_HAPPY,
                    2, SnifferState.SCENTING,
                    3, SnifferState.SNIFFING,
                    4, SnifferState.SEARCHING,
                    5, SnifferState.DIGGING,
                    6, SnifferState.RISING
            )),
            VarIntNetworkCodec.INSTANCE
    );

    private static final NetworkWriter<Pose> POSE_WRITER = new IndexNetworkCodec<>(
            IndexUtil.fromMap(Map.ofEntries(
                    Map.entry(0, Pose.STANDING),
                    Map.entry(1, Pose.GLIDING),
                    Map.entry(2, Pose.SLEEPING),
                    Map.entry(3, Pose.SWIMMING),
                    Map.entry(4, Pose.SPIN_ATTACK),
                    Map.entry(5, Pose.CROUCHING),
                    Map.entry(6, Pose.LONG_JUMPING),
                    Map.entry(7, Pose.DYING),
                    Map.entry(8, Pose.CROAKING),
                    Map.entry(9, Pose.USING_TONGUE),
                    Map.entry(10, Pose.SITTING),
                    Map.entry(11, Pose.ROARING),
                    Map.entry(12, Pose.SNIFFING),
                    Map.entry(13, Pose.EMERGING),
                    Map.entry(14, Pose.DIGGING),
                    Map.entry(15, Pose.SLIDING),
                    Map.entry(16, Pose.SHOOTING),
                    Map.entry(17, Pose.INHALING)
            )),
            VarIntNetworkCodec.INSTANCE
    );

    private static final NetworkWriter<Direction> DIRECTION_WRITER = new IndexNetworkCodec<>(
            IndexUtil.fromMap(Map.of(
                    0, Direction.DOWN,
                    1, Direction.UP,
                    2, Direction.NORTH,
                    3, Direction.SOUTH,
                    4, Direction.WEST,
                    5, Direction.EAST
            )),
            VarIntNetworkCodec.INSTANCE
    );

    private static final Map<Class<?>, NetworkWriter<?>> VALUE_WRITERS = new ValueWriterMapBuilder()
            .put(EntityMetadataValue.Byte.class, (buf, object) -> buf.writeByte(object.value()))
            .put(EntityMetadataValue.Float.class, (buf, object) -> buf.writeFloat(object.value()))
            .put(EntityMetadataValue.Boolean.class, (buf, object) -> buf.writeBoolean(object.value()))
            .put(EntityMetadataValue.PoseValue.class, (buf, object) -> POSE_WRITER.write(buf, object.value()))
            .put(
                    EntityMetadataValue.Int.class,
                    (buf, object) -> VarIntNetworkCodec.INSTANCE.write(buf, object.value())
            )
            .put(
                    EntityMetadataValue.Long.class,
                    (buf, object) -> VarLongNetworkCodec.INSTANCE.write(buf, object.value())
            )
            .put(
                    EntityMetadataValue.StringValue.class,
                    (buf, object) -> StringNetworkCodec.INSTANCE.write(buf, object.value())
            )
            .put(
                    EntityMetadataValue.ComponentValue.class,
                    (buf, object) -> ComponentNetworkWriter.INSTANCE.write(buf, object.value())
            )
            .put(
                    EntityMetadataValue.OptionalComponentValue.class,
                    (buf, object) -> NetworkUtil.writeOptional(object.value(), ComponentNetworkWriter.INSTANCE, buf)
            )
            .put(
                    EntityMetadataValue.RotationsValue.class,
                    (buf, object) -> RotationsNetworkWriter.INSTANCE.write(buf, object.value())
            )
            .put(
                    EntityMetadataValue.BlockPositionValue.class,
                    (buf, object) -> BlockPositionNetworkCodec.INSTANCE.write(buf, object.value())
            )
            .put(
                    EntityMetadataValue.OptionalBlockPositionValue.class,
                    (buf, object) -> NetworkUtil.writeOptional(object.value(), BlockPositionNetworkCodec.INSTANCE, buf)
            )
            .put(
                    EntityMetadataValue.DirectionValue.class,
                    (buf, object) -> DIRECTION_WRITER.write(buf, object.value())
            )
            .put(
                    EntityMetadataValue.OptionalLivingEntityReference.class,
                    (buf, object) -> NetworkUtil.writeOptional(object.value(), UUIDNetworkCodec.INSTANCE, buf)
            )
            .put(
                    EntityMetadataValue.CompoundBinaryTagValue.class,
                    (buf, object) -> BinaryTagNetworkWriter.INSTANCE.write(buf, object.value())
            )
            .put(
                    EntityMetadataValue.ArmadilloStateValue.class,
                    (buf, object) -> ARMADILLO_STATE_WRITER.write(buf, object.value())
            )
            .put(
                    EntityMetadataValue.SnifferStateValue.class,
                    (buf, object) -> SNIFFER_STATE_WRITER.write(buf, object.value())
            )
            .put(
                    EntityMetadataValue.VectorValue.class,
                    (buf, object) -> FloatVectorNetworkWriter.INSTANCE.write(buf, object.value())
            )
            .put(
                    EntityMetadataValue.QuaternionValue.class,
                    (buf, object) -> FloatQuaternionNetworkWriter.INSTANCE.write(buf, object.value())
            )
            .put(
                    EntityMetadataValue.OptionalUnsignedInt.class,
                    (buf, object) -> {
                        Integer value = object.value();
                        VarIntNetworkCodec.INSTANCE.write(buf, value == null ? 0 : value + 1);
                    }
            )
            .build();

    private ServerEntityMetadataPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerEntityMetadataPlayPacket object) {
        UPDATES_WRITER.write(buf, Sets.newHashSet(object.updates().entries()));
        buf.writeByte(255); // Mark an end of metadata updates
    }

    /**
     * A {@linkplain NetworkWriter network writer}
     * of {@linkplain IntObjectMap.PrimitiveEntry int-object map primitive entries}
     * associating {@linkplain JetEntityMetadata entity metadata} indices
     * with {@linkplain EntityMetadataValue entity metadata values} bound to them.
     *
     * @since 1.0
     * @see JetEntityMetadata
     * @see NetworkWriter
     */
    private static final class MetadataUpdateNetworkWriter
            implements NetworkWriter<IntObjectMap.PrimitiveEntry<EntityMetadataValue>> {
        @Override
        public void write(@NonNull ByteBuf buf, IntObjectMap.@NonNull PrimitiveEntry<EntityMetadataValue> object) {
            buf.writeByte(object.key());

            EntityMetadataValue metadataValue = object.value();
            Class<? extends EntityMetadataValue> valueClass = metadataValue.getClass();

            if (!DATA_TYPE_IDS.containsKey(valueClass)) {
                throw new IllegalArgumentException(String.format(
                        "No data type ID was specified for entity metadata value with class: "
                                + valueClass.getSimpleName()
                ));
            }
            buf.writeInt(DATA_TYPE_IDS.getInt(valueClass));

            NetworkWriter<?> writer = VALUE_WRITERS.get(valueClass);
            if (writer == null) {
                throw new IllegalArgumentException(String.format(
                        "No network writer was specified for entity metadata value with class: "
                                + valueClass.getSimpleName()
                ));
            }

            writeValue(writer, metadataValue, buf);
        }

        private static <V> void writeValue(NetworkWriter<V> writer, EntityMetadataValue value, ByteBuf buf) {
            // noinspection unchecked ; it is expected that the writer has been correctly retrieved from the map
            writer.write(buf, (V) value);
        }
    }

    /**
     * A builder of a {@linkplain Map map} associating {@linkplain EntityMetadataValue entity metadata value}
     * {@linkplain Class classes} with {@linkplain NetworkWriter network writers} that can write these values.
     *
     * @since 1.0
     * @see EntityMetadataValue
     * @see NetworkWriter
     * @see Class
     * @see Map
     */
    @NullMarked // TODO: Move the annotation to the master class
    private static final class ValueWriterMapBuilder {

        private final Map<Class<? extends EntityMetadataValue>, NetworkWriter<? extends EntityMetadataValue>> values;

        private ValueWriterMapBuilder() {
            this.values = new HashMap<>();
        }

        /**
         * Associates the specified {@linkplain EntityMetadataValue entity metadata value} {@linkplain Class class}
         * with a {@linkplain NetworkWriter network writer} that can write values with that class.
         *
         * @param valueClass the entity metadata value class that should
         *                   be associated with the specified network writer
         * @param writer the network writer that the entity metadata value class should be associated with
         * @return this builder
         * @param <V> the type of the entity metadata value whose class
         *            is being associated with the specified network writer
         * @since 1.0
         */
        private <V extends EntityMetadataValue> ValueWriterMapBuilder put(Class<V> valueClass,
                                                                          NetworkWriter<V> writer) {
            this.values.put(valueClass, writer);
            return this;
        }

        /**
         * Builds the {@linkplain Map map}.
         *
         * @return the created map
         * @since 1.0
         */
        private Map<Class<?>, NetworkWriter<?>> build() {
            return Map.copyOf(this.values);
        }
    }
}