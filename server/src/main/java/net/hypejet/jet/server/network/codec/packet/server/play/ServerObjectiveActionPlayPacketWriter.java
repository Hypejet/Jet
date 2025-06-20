package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.hypejet.jet.scoreboard.objective.ScoreboardObjective;
import net.hypejet.jet.scoreboard.score.render.RenderType;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.component.ComponentNetworkWriter;
import net.hypejet.jet.server.network.codec.game.scoreboard.score.number.NumberFormatNetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerObjectiveActionPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerObjectiveActionPlayPacket.Action;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}
 * of {@linkplain ServerObjectiveActionPlayPacket a server objective action play packet}.
 *
 * @since 1.0
 * @see ServerObjectiveActionPlayPacket
 * @see NetworkWriter
 */
public final class ServerObjectiveActionPlayPacketWriter implements NetworkWriter<ServerObjectiveActionPlayPacket> {

    /**
     * An instance
     * of the {@linkplain ServerObjectiveActionPlayPacketWriter server objective action play packet writer}.
     *
     * @since 1.0
     */
    public static final ServerObjectiveActionPlayPacketWriter INSTANCE = new ServerObjectiveActionPlayPacketWriter();

    private static final Object2ByteMap<Class<? extends Action>> ACTION_IDENTIFIERS = new Object2ByteOpenHashMap<>();
    private static final Object2IntMap<RenderType> RENDER_TYPE_IDENTIFIERS = new Object2IntOpenHashMap<>();

    static {
        ACTION_IDENTIFIERS.put(Action.Create.class, (byte) 0);
        ACTION_IDENTIFIERS.put(Action.Remove.class, (byte) 1);
        ACTION_IDENTIFIERS.put(Action.Update.class, (byte) 2);

        RENDER_TYPE_IDENTIFIERS.put(RenderType.INTEGER, 0);
        RENDER_TYPE_IDENTIFIERS.put(RenderType.HEARTS, 1);
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerObjectiveActionPlayPacket object) {
        StringNetworkCodec.INSTANCE.write(buf, object.objectiveName());

        Action action = object.action();
        Class<? extends Action> actionClass = action.getClass();

        if (!ACTION_IDENTIFIERS.containsKey(actionClass))
            throw new IllegalArgumentException(String.format("Unknown action: %s", actionClass.getSimpleName()));
        buf.writeByte(ACTION_IDENTIFIERS.getByte(actionClass));

        ScoreboardObjective objectiveData = switch (action) {
            case Action.Create(ScoreboardObjective objective) -> objective;
            case Action.Update(ScoreboardObjective objective) -> objective;
            case Action.Remove ignored -> null;
        };

        if (objectiveData == null) return;
        ComponentNetworkWriter.INSTANCE.write(buf, objectiveData.displayName());

        RenderType renderType = objectiveData.renderType();
        if (!RENDER_TYPE_IDENTIFIERS.containsKey(renderType))
            throw new IllegalArgumentException(String.format("Unknown render type: %s", renderType));

        VarIntNetworkCodec.INSTANCE.write(buf, RENDER_TYPE_IDENTIFIERS.getInt(renderType));
        NetworkUtil.writeOptional(objectiveData.numberFormat(), NumberFormatNetworkWriter.INSTANCE, buf);
    }
}