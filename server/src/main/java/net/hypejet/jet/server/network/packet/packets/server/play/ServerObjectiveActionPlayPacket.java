package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.scoreboard.score.number.NumberFormat;
import net.hypejet.jet.scoreboard.score.render.RenderType;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents {@linkplain ServerPacket a server packet} which does {@linkplain Action an action} specified
 * to a scoreboard objective with name specified.
 *
 * @param objectiveName the name of the scoreboard objective
 * @param action the action that should be done to the scoreboard objective
 * @since 1.0
 */
public record ServerObjectiveActionPlayPacket(@NonNull String objectiveName, @NonNull Action action)
        implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerObjectiveActionPlayPacket server objective action play packet}.
     *
     * @param objectiveName the name of the scoreboard objective
     * @param action the action that should be done to the scoreboard objective
     * @since 1.0
     */
    public ServerObjectiveActionPlayPacket {
        NullabilityUtil.requireNonNull(objectiveName, "objective name");
        NullabilityUtil.requireNonNull(action, "action");
    }

    /**
     * Represents an action that should be done for a Minecraft scoreboard objective.
     *
     * @since 1.0
     */
    public sealed interface Action {
        /**
         * Represents {@linkplain Action an action} creating a scoreboard objective.
         *
         * @param data a data that the scoreboard objective should have
         * @since 1.0
         */
        record Create(@NonNull ObjectiveData data) implements Action {
            /**
             * Constructs the {@linkplain Create create action}.
             *
             * @param data a data that the scoreboard objective should have
             * @since 1.0
             */
            public Create {
                NullabilityUtil.requireNonNull(data, "data");
            }
        }

        /**
         * Represents {@linkplain Action an action} updating a scoreboard objective.
         *
         * @param data a new data that the scoreboard objective should have
         * @since 1.0
         */
        record Update(@NonNull ObjectiveData data) implements Action {
            /**
             * Constructs the {@linkplain Update update action}.
             *
             * @param data a new data that the scoreboard objective should have
             * @since 1.0
             */
            public Update {
                NullabilityUtil.requireNonNull(data, "data");
            }
        }

        /**
         * Represents {@linkplain Action an action} removing a scoreboard objective.
         *
         * @since 1.0
         */
        final class Remove implements Action {
            /**
             * An instance of the {@linkplain Remove remove action}.
             *
             * @since 1.0
             */
            public static final Remove INSTANCE = new Remove();

            private Remove() {}
        }
    }

    /**
     * Represents a packet data of {@linkplain ??? a scoreboard objective}.
     *
     * @param displayName a display name that the scoreboard objective should have
     * @param renderType a render type that the scoreboard objective should have
     * @param numberFormat a number format that the scoreboard objective should have
     * @since 1.0
     * @see ???
     */
    public record ObjectiveData(@NonNull Component displayName, @NonNull RenderType renderType,
                                @Nullable NumberFormat numberFormat) {
        /**
         * Constructs the {@linkplain ObjectiveData objective data}.
         *
         * @param displayName a display name that the scoreboard objective should have
         * @param renderType a render type that the scoreboard objective should have
         * @param numberFormat a number format that the scoreboard objective should have
         * @since 1.0
         */
        public ObjectiveData {
            NullabilityUtil.requireNonNull(displayName, "display name");
            NullabilityUtil.requireNonNull(renderType, "render type");
        }
    }
}