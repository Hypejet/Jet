package net.hypejet.jet.server.network.codec.game.scoreboard.position;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.hypejet.jet.scoreboard.position.BelowNameScoreboardPosition;
import net.hypejet.jet.scoreboard.position.PlayerListScoreboardPosition;
import net.hypejet.jet.scoreboard.position.ScoreboardPosition;
import net.hypejet.jet.scoreboard.position.SidebarScoreboardPosition;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.kyori.adventure.text.format.NamedTextColor;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer} of {@linkplain ScoreboardPosition a scoreboard position}.
 *
 * @since 1.0
 * @see ScoreboardPosition
 * @see NetworkWriter
 */
public final class ScoreboardPositionNetworkWriter implements NetworkWriter<ScoreboardPosition> {

    /**
     * An instance of the {@linkplain ScoreboardPositionNetworkWriter scoreboard position network writer}.
     *
     * @since 1.0
     * @see ScoreboardPositionNetworkWriter
     */
    public static final ScoreboardPositionNetworkWriter INSTANCE = new ScoreboardPositionNetworkWriter();

    private static final Object2IntMap<NamedTextColor> COLOR_IDENTIFIERS = new Object2IntOpenHashMap<>();

    static {
        COLOR_IDENTIFIERS.put(NamedTextColor.BLACK, 0);
        COLOR_IDENTIFIERS.put(NamedTextColor.DARK_BLUE, 1);
        COLOR_IDENTIFIERS.put(NamedTextColor.DARK_GREEN, 2);
        COLOR_IDENTIFIERS.put(NamedTextColor.DARK_AQUA, 3);
        COLOR_IDENTIFIERS.put(NamedTextColor.DARK_RED, 4);
        COLOR_IDENTIFIERS.put(NamedTextColor.DARK_PURPLE, 5);
        COLOR_IDENTIFIERS.put(NamedTextColor.GOLD, 6);
        COLOR_IDENTIFIERS.put(NamedTextColor.GRAY, 7);
        COLOR_IDENTIFIERS.put(NamedTextColor.DARK_GRAY, 8);
        COLOR_IDENTIFIERS.put(NamedTextColor.BLUE, 9);
        COLOR_IDENTIFIERS.put(NamedTextColor.GREEN, 10);
        COLOR_IDENTIFIERS.put(NamedTextColor.AQUA, 11);
        COLOR_IDENTIFIERS.put(NamedTextColor.RED, 12);
        COLOR_IDENTIFIERS.put(NamedTextColor.LIGHT_PURPLE, 13);
        COLOR_IDENTIFIERS.put(NamedTextColor.YELLOW, 14);
        COLOR_IDENTIFIERS.put(NamedTextColor.WHITE, 15);
    }

    private ScoreboardPositionNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ScoreboardPosition object) {
        int identifier = switch (object) {
            case PlayerListScoreboardPosition ignored -> 0;
            case BelowNameScoreboardPosition ignored -> 2;
            case SidebarScoreboardPosition position -> {
                NamedTextColor color = position.color();
                if (color == null) yield 1;

                if (!COLOR_IDENTIFIERS.containsKey(color))
                    throw new IllegalArgumentException(String.format("Unknown team color: %s", color));
                yield 3 + COLOR_IDENTIFIERS.getInt(color);
            }
            default -> throw new IllegalStateException(String.format("Unknown scoreboard position: %s", object));
        };
        VarIntNetworkCodec.INSTANCE.write(buf, identifier);
    }
}