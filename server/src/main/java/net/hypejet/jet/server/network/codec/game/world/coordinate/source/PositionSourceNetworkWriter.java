package net.hypejet.jet.server.network.codec.game.world.coordinate.source;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.coordinate.BlockPositionNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.world.coordinate.source.BlockPositionSource;
import net.hypejet.jet.world.coordinate.source.EntityPositionSource;
import net.hypejet.jet.world.coordinate.source.PositionSource;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A {@linkplain NetworkWriter network writer} of {@linkplain PositionSource position sources}.
 *
 * @since 1.0
 * @see NetworkWriter
 * @see PositionSource
 */
@NullMarked
public final class PositionSourceNetworkWriter implements NetworkWriter<PositionSource> {

    private static final Map<Class<? extends PositionSource>, PositionSourceType<?>> TYPES = new TypeMapBuilder()
            .put(BlockPositionSource.class, new PositionSourceType<>(new BlockAdditionalWriter(), 0))
            .put(EntityPositionSource.class, new PositionSourceType<>(new EntityAdditionalWriter(), 1))
            .build();

    /**
     * An instance of the {@linkplain PositionSourceNetworkWriter position source network writer}.
     *
     * @since 1.0
     */
    public static final PositionSourceNetworkWriter INSTANCE = new PositionSourceNetworkWriter();

    private PositionSourceNetworkWriter() {}

    @Override
    public void write(ByteBuf buf, JetRegistryManager registryManager, PositionSource object) {
        Class<? extends PositionSource> positionSourceClass = object.getClass();
        PositionSourceType<?> sourceType = TYPES.get(positionSourceClass);

        if (sourceType == null) {
            throw new IllegalArgumentException(String.format(
                    "Could not find a position source type for %s position source class",
                    positionSourceClass.getName()
            ));
        }

        VarIntNetworkCodec.INSTANCE.write(buf, registryManager, sourceType.registryIndex());
        writeAdditional(buf, registryManager, object, sourceType);
    }

    private static <P extends PositionSource> void writeAdditional(ByteBuf buf,
                                                                   JetRegistryManager registryManager,
                                                                   PositionSource positionSource,
                                                                   PositionSourceType<P> sourceType) {
        // noinspection unchecked ; the source type is expected to be safely retreived from the source type map
        sourceType.additionalWriter().write(buf, registryManager, (P) positionSource);
    }

    /**
     * A {@linkplain NetworkWriter network writer} of additional
     * data of {@linkplain BlockPositionSource block position sources}.
     *
     * @since 1.0
     * @see BlockPositionSource
     * @see NetworkWriter
     */
    private static final class BlockAdditionalWriter implements NetworkWriter<BlockPositionSource> {
        @Override
        public void write(ByteBuf buf, JetRegistryManager registryManager, BlockPositionSource object) {
            BlockPositionNetworkCodec.INSTANCE.write(buf, registryManager, object.blockPosition());
        }
    }

    /**
     * A {@linkplain NetworkWriter network writer} of additional
     * data of {@linkplain EntityPositionSource entity position sources}.
     *
     * @since 1.0
     * @see EntityPositionSource
     * @see NetworkWriter
     */
    private static final class EntityAdditionalWriter implements NetworkWriter<EntityPositionSource> {
        @Override
        public void write(ByteBuf buf, JetRegistryManager registryManager, EntityPositionSource object) {
            VarIntNetworkCodec.INSTANCE.write(buf, registryManager, JetEntity.cast(object.entity()).entityId());
            buf.writeFloat(object.yOffset());
        }
    }

    /**
     * A type of {@linkplain PositionSource position source}.
     *
     * @param additionalWriter the network writer that additional data of position
     *                         sources of this type should be written with
     * @param registryIndex the index of this position source type in the position source type registry
     * @param <P> the type of position sources that the specified network writer writes
     * @since 1.0
     * @see PositionSource
     */
    private record PositionSourceType<P extends PositionSource>(NetworkWriter<P> additionalWriter, int registryIndex) {
        /**
         * Constructs the {@linkplain PositionSourceType position source type}.
         *
         * @param additionalWriter the network writer that additional data of position
         *                         sources of the constructed type should be written with
         * @param registryIndex the index of the constructed position source type in the position source type registry
         * @since 1.0
         */
        private PositionSourceType {
            Objects.requireNonNull(additionalWriter, "additional writer");
        }
    }

    /**
     * A builder of a {@linkplain Map map} associating {@linkplain PositionSource position source}
     * {@linkplain Class classes} with {@linkplain PositionSourceType position source types}
     * of these {@linkplain PositionSource position sources}.
     *
     * @since 1.0
     * @see PositionSource
     * @see PositionSourceType
     * @see Class
     * @see Map
     */
    private static final class TypeMapBuilder {

        private final Map<Class<? extends PositionSource>, PositionSourceType<?>> types = new HashMap<>();

        /**
         * Associates the specified {@linkplain PositionSource position source} {@linkplain Class class}
         * with the specified {@linkplain PositionSourceType position source type}.
         *
         * @param sourceClass the position source class that should be associated
         *                    with the specified position source type
         * @param sourceType the position source type that the specified position
         *                   source class should be associated with
         * @return this type map builder
         * @param <P> the object type of the position source for which the position source type is being registered
         * @since 1.0
         */
        private <P extends PositionSource> TypeMapBuilder put(Class<P> sourceClass, PositionSourceType<P> sourceType) {
            this.types.put(sourceClass, sourceType);
            return this;
        }

        /**
         * Builds the {@linkplain Map map}.
         *
         * @return the created map
         * @since 1.0
         */
        private Map<Class<? extends PositionSource>, PositionSourceType<?>> build() {
            return Map.copyOf(this.types);
        }
    }
}