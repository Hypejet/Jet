package net.hypejet.jet.server;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.hypejet.jet.command.CommandSource;
import net.hypejet.jet.event.command.CommandExecutionFailureEvent;
import net.hypejet.jet.event.events.login.LoginSessionInitializeEvent;
import net.hypejet.jet.protocol.packet.client.login.ClientLoginRequestLoginPacket;
import net.hypejet.jet.server.command.JetCommandManager;
import net.hypejet.jet.session.LoginSession;
import net.hypejet.jet.session.handler.LoginSessionHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.UUID;

/**
 * Represents a main class, which provides an instruction for when the program starts executing.
 *
 * @since 1.0
 * @author Codestech
 */
public final class JetServerEntrypoint {

    private JetServerEntrypoint() {}

    /**
     * Runs the {@linkplain JetMinecraftServer Jet Minecraft server}.
     *
     * @param args the program arguments
     * @since 1.0
     */
    public static void main(String[] args) {
        JetMinecraftServer server = new JetMinecraftServer();

        server.eventNode().addListener(event -> event.setSessionHandler(new LoginSessionHandler() {
            @Override
            public void onLoginRequest(@NonNull ClientLoginRequestLoginPacket packet, @NonNull LoginSession session) {
                session.finish(packet.username(), UUID.randomUUID(), List.of());
            }
        }), LoginSessionInitializeEvent.class);
        server.eventNode().addListener(System.out::println, Object.class);

        LiteralCommandNode<CommandSource> node1 = LiteralArgumentBuilder.<CommandSource>literal("test")
                .executes(ctx -> {
                    ctx.getSource().sendMessage(Component.text("Hi!"));
                    return Command.SINGLE_SUCCESS;
                })
                .build();

        node1.addChild(RequiredArgumentBuilder.<CommandSource, Integer>argument("int", IntegerArgumentType.integer())
                .executes(ctx -> {
                    ctx.getSource().sendMessage(Component.text(ctx.getArgument("int", int.class)));
                    return Command.SINGLE_SUCCESS;
                })
                .redirect(node1, ctx -> {
                    ctx.getCommand().run(ctx);
                    return ctx.getSource();
                })
                .build());

        JetCommandManager commandManager = server.commandManager();
        commandManager.register(node1);

        server.eventNode().addListener(
                event -> event.source().sendMessage(Component.text(event.exception().getMessage(), NamedTextColor.RED)),
                CommandExecutionFailureEvent.class
        );

        Runtime.getRuntime().addShutdownHook(Thread.ofVirtual().unstarted(server::shutdown));
    }
}