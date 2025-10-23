package net.hypejet.jet.server.registry.codecs.world.coordinate.source;

import net.hypejet.jet.server.registry.codecs.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.KeyBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.util.uuid.UUIDBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.coordinate.BlockPositionBinaryTagCodec;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.hypejet.jet.world.coordinate.source.BlockPositionSource;
import net.hypejet.jet.world.coordinate.source.EntityPositionSource;
import net.hypejet.jet.world.coordinate.source.PositionSource;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.util.Index;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.Map;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary-tag codec} of {@linkplain PositionSource position sources}.
 *
 * @since 1.0
 * @see PositionSource
 * @see BinaryTagCodec
 */
@NullMarked
public final class PositionSourceBinaryTagCodec implements BinaryTagCodec<PositionSource> {

    private static final String TYPE_FIELD = "type";

    private static final Map<Class<? extends PositionSource>, AdditionalCodec<?>> CODECS = new CodecMapBuilder()
            .put(BlockPositionSource.class, new BlockAdditionalCodec())
            .put(EntityPositionSource.class, new EntityAdditionalCodec())
            .build();

    private static final Index<Class<? extends PositionSource>, Key> POSITION_SOURCE_TYPES = IndexUtil.fromMap(Map.of(
            Key.key("block"), BlockPositionSource.class,
            Key.key("entity"), EntityPositionSource.class
    ));

    /**
     * An instance of the {@linkplain PositionSourceBinaryTagCodec position source binary-tag codec}.
     *
     * @since 1.0
     */
    public static final PositionSourceBinaryTagCodec INSTANCE = new PositionSourceBinaryTagCodec();

    private PositionSourceBinaryTagCodec() {}

    @Override
    public PositionSource decode(BinaryTag binaryTag) {
        if (!(binaryTag instanceof CompoundBinaryTag compound)) {
            throw new IllegalArgumentException(
                    "The encoded tag must be of compound type to decode it into a position source"
            );
        }
        Key typeKey = KeyBinaryTagCodec.INSTANCE.decode(requiredTag(TYPE_FIELD, compound));
        return CODECS.get(POSITION_SOURCE_TYPES.keyOrThrow(typeKey)).read(compound);
    }

    @Override
    public BinaryTag encode(PositionSource value) {
        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder();

        Class<? extends PositionSource> positionSourceClass = value.getClass();
        Key typeKey = POSITION_SOURCE_TYPES.valueOrThrow(positionSourceClass);
        builder.put(TYPE_FIELD, KeyBinaryTagCodec.INSTANCE.encode(typeKey));

        writeAdditional(codec(positionSourceClass), value, builder);
        return builder.build();
    }

    private static <P extends PositionSource> void writeAdditional(AdditionalCodec<P> codec,
                                                                   PositionSource positionSource,
                                                                   CompoundBinaryTag.Builder builder) {
        // noinspection unchecked ; the additional codec is expected to be safely retreived from the codec map
        codec.write((P) positionSource, builder);
    }

    private static AdditionalCodec<?> codec(Class<? extends PositionSource> positionSourceClass) {
        AdditionalCodec<?> codec = CODECS.get(positionSourceClass);
        if (codec == null) {
            throw new IllegalArgumentException(String.format(
                    "Could not find an additional codec for position source with %s class",
                    positionSourceClass.getName()
            ));
        }
        return codec;
    }

    /**
     * A {@linkplain AdditionalCodec additional codec} of {@linkplain BlockPositionSource block position sources}.
     *
     * @since 1.0
     * @see BlockPositionSource
     * @see AdditionalCodec
     */
    private static final class BlockAdditionalCodec implements AdditionalCodec<BlockPositionSource> {

        private static final String POS_FIELD = "pos";

        @Override
        public BlockPositionSource read(CompoundBinaryTag compound) {
            return new BlockPositionSource(
                    BlockPositionBinaryTagCodec.INSTANCE.decode(requiredTag(POS_FIELD, compound))
            );
        }

        @Override
        public void write(BlockPositionSource source, CompoundBinaryTag.Builder builder) {
            builder.put(POS_FIELD, BlockPositionBinaryTagCodec.INSTANCE.encode(source.blockPosition()));
        }
    }

    /**
     * An {@linkplain AdditionalCodec additional codec} of {@linkplain EntityPositionSource entity position sources}.
     *
     * @since 1.0
     * @see EntityPositionSource
     * @see AdditionalCodec
     */
    private static final class EntityAdditionalCodec implements AdditionalCodec<EntityPositionSource> {

        private static final String SOURCE_ENTITY_FIELD = "source_entity";
        private static final String Y_OFFSET_FIELD = "y_offset";

        @Override
        public EntityPositionSource read(CompoundBinaryTag compound) {
            return new EntityPositionSource(
                    UUIDBinaryTagCodec.INSTANCE.decode(requiredTag(SOURCE_ENTITY_FIELD, compound)),
                    compound.getFloat(Y_OFFSET_FIELD)
            );
        }

        @Override
        public void write(EntityPositionSource source, CompoundBinaryTag.Builder builder) {
            builder.put(SOURCE_ENTITY_FIELD, UUIDBinaryTagCodec.INSTANCE.encode(source.uniqueId()));
            builder.putFloat(Y_OFFSET_FIELD, source.yOffset());
        }
    }

    /**
     * Something handling {@linkplain CompoundBinaryTag compound-binary-tag}
     * serialization of additional fields of {@linkplain PositionSource position sources}.
     *
     * @param <P> the type of position sources whose additional fields are written by this additional codec
     * @since 1.0
     * @see PositionSource
     * @see CompoundBinaryTag
     */
    private interface AdditionalCodec<P extends PositionSource> {
        /**
         * Creates the {@linkplain PositionSource position source} by reading its additional
         * fields from the specified {@linkplain CompoundBinaryTag compound binary tag}.
         *
         * @param compound the compound binary tag to read the additional fields from
         * @return the created position source
         * @since 1.0
         */
        P read(CompoundBinaryTag compound);

        /**
         * Writes additional fields of the specified {@linkplain PositionSource position source}
         * to the specified {@linkplain CompoundBinaryTag.Builder compound binary tag builder}.
         *
         * @param source the position source whose additional fields should be written
         * @param builder the compound binary tag builder to write the additional fields to
         * @since 1.0
         */
        void write(P source, CompoundBinaryTag.Builder builder);
    }

    /**
     * A builder of {@linkplain Map map} associating {@linkplain PositionSource position source}
     * {@linkplain Class classes} with {@linkplain AdditionalCodec additional codecs} writing
     * additional fields of these {@linkplain PositionSource position sources}.
     *
     * @since 1.0
     * @see PositionSource
     * @see AdditionalCodec
     * @see Class
     * @see Map
     */
    private static final class CodecMapBuilder {

        private final Map<Class<? extends PositionSource>, AdditionalCodec<?>> codecs = new HashMap<>();

        /**
         * Associates the specified {@linkplain PositionSource position source} {@linkplain Class class}
         * with the specified {@linkplain AdditionalCodec additional codec}.
         *
         * @param positionSourceClass the position source class that should be associated with the specified codec
         * @param codec the additional codec that the specified position source class should be associated with
         * @return this codec map builder
         * @param <P> the type of position source whose serialization is handled by the specified additional codec
         * @since 1.0
         */
        private <P extends PositionSource> CodecMapBuilder put(Class<P> positionSourceClass,
                                                               AdditionalCodec<P> codec) {
            this.codecs.put(positionSourceClass, codec);
            return this;
        }

        /**
         * Builds the {@linkplain Map map}.
         *
         * @return the created map
         * @since 1.0
         */
        private Map<Class<? extends PositionSource>, AdditionalCodec<?>> build() {
            return Map.copyOf(this.codecs);
        }
    }
}