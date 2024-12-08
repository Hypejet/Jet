package net.hypejet.jet.server.network.packet.handler.play;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.StringRange;
import net.hypejet.jet.command.tooltip.ComponentTooltip;
import net.hypejet.jet.server.network.packet.packets.client.play.ClientCommandSuggestionsRequestPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerCommandSuggestionsResponsePlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerCommandSuggestionsResponsePlayPacket.Suggestion;
import net.hypejet.jet.server.command.JetCommandManager;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.packet.handler.ClientPacketHandler;
import net.hypejet.jet.server.network.session.Session;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents {@linkplain ClientPacketHandler a client packet handler}, which handles
 * {@linkplain ClientCommandSuggestionsRequestPlayPacket a client command suggestions request play packet}.
 *
 * @author Codestech
 * @see ClientCommandSuggestionsRequestPlayPacket
 * @see ClientPacketHandler
 * @since 1.0
 */
public final class ClientCommandSuggestionsRequestPlayPacketHandler
        extends ClientPacketHandler<ClientCommandSuggestionsRequestPlayPacket> {
    /**
     * Constructs the {@linkplain ClientCommandSuggestionsRequestPlayPacketHandler client command suggestions request
     * play packet handler}.
     *
     * @since 1.0
     */
    public ClientCommandSuggestionsRequestPlayPacketHandler() {
        super(ClientCommandSuggestionsRequestPlayPacket.class);
    }

    @Override
    public void handle(@NonNull ClientCommandSuggestionsRequestPlayPacket packet, @NonNull Session session) {
        JetPlayer player = session.connection().playerOrThrow();
        JetCommandManager commandManager = player.server().commandManager();

        commandManager.suggest(packet.text(), player).thenAccept(suggestions -> {
            StringRange range = suggestions.getRange();
            List<Suggestion> suggestionList = new ArrayList<>();

            suggestions.getList().forEach(suggestion -> {
                Message tooltip = suggestion.getTooltip();
                Component convertedTooltip = null;

                if (tooltip != null) {
                    convertedTooltip = tooltip instanceof ComponentTooltip(Component component)
                            ? component : Component.text(tooltip.getString());
                }

                suggestionList.add(new Suggestion(suggestion.getText(), convertedTooltip));
            });

            player.sendPacket(new ServerCommandSuggestionsResponsePlayPacket(
                    packet.transactionId(), range.getStart(), range.getLength(), List.copyOf(suggestionList)
            ));
        });
    }
}