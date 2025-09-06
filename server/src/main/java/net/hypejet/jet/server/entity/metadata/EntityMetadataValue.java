package net.hypejet.jet.server.entity.metadata;

import net.hypejet.jet.entity.pose.Pose;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * A value that is bound to a specific index in {@linkplain JetEntityMetadata entity metadata}.
 * 
 * @since 1.0
 * @see JetEntityMetadata
 */
@NullMarked
// TODO: Implement all possible values
public sealed interface EntityMetadataValue {
    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing a {@code byte}.
     *
     * @param value the byte that this entity metadata value represents
     * @since 1.0
     */
    record Byte(byte value) implements EntityMetadataValue {}

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing an {@code int}.
     *
     * @param value the int that this entity metadata value represents
     * @since 1.0
     */
    record Int(int value) implements EntityMetadataValue {}

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing an {@code boolean}.
     *
     * @param value the boolean that this entity metadata value represents
     * @since 1.0
     */
    record Boolean(boolean value) implements EntityMetadataValue {}

    /**
     * An {@linkplain EntityMetadataValue entity metadata value}
     * representing an optional {@linkplain Component component}.
     *
     * @param value the component that this entity metadata value represents,
     *              {@code null} if the component is unspecified
     * @since 1.0
     */
    record OptionalComponentValue(@Nullable Component value) implements EntityMetadataValue {}

    /**
     * An {@linkplain EntityMetadataValue entity metadata value} representing a {@linkplain Pose pose}.
     *
     * @param value the pose that this entity metadata value represents
     * @since 1.0
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
}