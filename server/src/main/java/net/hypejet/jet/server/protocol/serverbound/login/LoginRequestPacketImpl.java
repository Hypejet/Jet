package net.hypejet.jet.server.protocol.serverbound.login;

import net.hypejet.jet.protocol.packet.serverbound.login.LoginRequestPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public record LoginRequestPacketImpl(@NonNull String username, @NonNull UUID uniqueId) implements LoginRequestPacket {}