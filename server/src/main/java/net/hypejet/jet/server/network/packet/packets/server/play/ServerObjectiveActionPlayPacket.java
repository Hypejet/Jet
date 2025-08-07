package net.hypejet.jet.server.network.packet.packets.server.play;

import java.util.Objects;
import net.hypejet.jet.scoreboard.objective.ScoreboardObjective;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ServerPacket a server packet} which does {@linkplain Action an action} specified
 * to {@linkplain ScoreboardObjective a scoreboard objective} with name specified.
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
        Objects.requireNonNull(objectiveName, "objective name");
        Objects.requireNonNull(action, "action");
    }

    /**
     * Represents an action that should be done to {@linkplain ScoreboardObjective a scoreboard objective}.
     *
     * @since 1.0
     */
    public sealed interface Action {
        /**
         * Represents {@linkplain Action an action} creating {@linkplain ScoreboardObjective a scoreboard objective}.
         *
         * @param objective a data that the scoreboard objective should have
         * @since 1.0
         */
        record Create(@NonNull ScoreboardObjective objective) implements Action {
            /**
             * Constructs the {@linkplain Create create action}.
             *
             * @param objective a data that the scoreboard objective should have
             * @since 1.0
             */
            public Create {
                Objects.requireNonNull(objective, "objective");
            }
        }

        /**
         * Represents {@linkplain Action an action} updating {@linkplain ScoreboardObjective a scoreboard objective}.
         *
         * @param objective a new data that the scoreboard objective should have
         * @since 1.0
         */
        record Update(@NonNull ScoreboardObjective objective) implements Action {
            /**
             * Constructs the {@linkplain Update update action}.
             *
             * @param objective a new data that the scoreboard objective should have
             * @since 1.0
             */
            public Update {
                Objects.requireNonNull(objective, "objective");
            }
        }

        /**
         * Represents {@linkplain Action an action} removing {@linkplain ScoreboardObjective a scoreboard objective}.
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
}