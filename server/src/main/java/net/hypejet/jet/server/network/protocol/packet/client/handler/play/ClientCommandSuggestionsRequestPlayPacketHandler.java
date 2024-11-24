package net.hypejet.jet.server.network.protocol.packet.client.handler.play;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.StringRange;
import io.netty.buffer.ByteBuf;
import net.hypejet.jet.command.tooltip.ComponentTooltip;
import net.hypejet.jet.protocol.packet.client.play.ClientCommandSuggestionsRequestPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerCommandSuggestionsResponsePlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerCommandSuggestionsResponsePlayPacket.Suggestion;
import net.hypejet.jet.server.command.JetCommandManager;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketHandler;
import net.hypejet.jet.server.network.session.task.PlayTask;
import net.hypejet.jet.server.network.session.task.SessionTask;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which reads and handles
 * {@linkplain ClientCommandSuggestionsRequestPlayPacket a command suggestions request play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientCommandSuggestionsRequestPlayPacket
 * @see ClientPacketHandler
 */
public final class ClientCommandSuggestionsRequestPlayPacketHandler
        implements ClientPacketHandler<ClientCommandSuggestionsRequestPlayPacket> {

    private static final StringNetworkCodec TEXT_CODEC = StringNetworkCodec.create(32_500);

    @Override
    public @NonNull ClientCommandSuggestionsRequestPlayPacket read(@NonNull ByteBuf buf) {
        return new ClientCommandSuggestionsRequestPlayPacket(
                VarIntNetworkCodec.instance().read(buf),
                TEXT_CODEC.read(buf)
        );
    }

    @Override
    public void handle(@NonNull ClientCommandSuggestionsRequestPlayPacket packet,
                       @NonNull SessionTask sessionTask) {
        if (!(sessionTask instanceof PlayTask playTask))
            throw new IllegalArgumentException("The session task must be a play task");

        JetPlayer player = playTask.player();
        JetCommandManager commandManager = player.server().commandManager();

        commandManager.suggest(packet.text(), player).thenAccept(suggestions -> {
            StringRange range = suggestions.getRange();
            List<Suggestion> suggestionList = new ArrayList<>();

            suggestions.getList().forEach(suggestion -> {
                Message tooltip = suggestion.getTooltip();
                Component convertedTooltip = null;

                if (tooltip != null) {
                    convertedTooltip = tooltip instanceof ComponentTooltip componentTooltip
                            ? componentTooltip.component()
                            : Component.text(tooltip.getString());
                }

                suggestionList.add(new Suggestion(suggestion.getText(), convertedTooltip));
            });

            player.sendPacket(new ServerCommandSuggestionsResponsePlayPacket(packet.transactionId(),
                    range.getStart(), range.getLength(), List.copyOf(suggestionList)));
        });
    }
}