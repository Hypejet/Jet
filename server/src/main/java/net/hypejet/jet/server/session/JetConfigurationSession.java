package net.hypejet.jet.server.session;

import net.hypejet.jet.event.events.player.configuration.PlayerConfigurationStartEvent;
import net.hypejet.jet.protocol.packet.server.configuration.ServerFinishConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerKeepAliveConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerRegistryDataConfigurationPacket;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.keepalive.KeepAliveHandler;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.session.handler.SessionHandler;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;

/**
 * Represents a {@linkplain Session session} and a {@linkplain SessionHandler session handler}, which handles
 * a {@linkplain net.hypejet.jet.protocol.ProtocolState#CONFIGURATION configuration protocol state}.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.protocol.ProtocolState#CONFIGURATION
 * @see Session
 * @see SessionHandler
 */
public final class JetConfigurationSession implements Session<JetConfigurationSession>, SessionHandler,
        Thread.UncaughtExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(JetConfigurationSession.class);

    private final JetPlayer player;
    private final KeepAliveHandler keepAliveHandler;

    private final CountDownLatch acknowledgeLatch = new CountDownLatch(1);
    private boolean finished;

    /**
     * Constructs the {@linkplain JetConfigurationSession configuration session}.
     *
     * @param player a player that is being configured
     * @since 1.0
     */
    public JetConfigurationSession(@NonNull JetPlayer player) {
        this.player = player;
        this.keepAliveHandler = new KeepAliveHandler(player, this, ServerKeepAliveConfigurationPacket::new);

        Thread.ofVirtual()
                .uncaughtExceptionHandler(this)
                .start(() -> {
                    SocketPlayerConnection connection = this.player.connection();

                    connection.server().eventNode().call(new PlayerConfigurationStartEvent(this.player));
                    this.keepAliveHandler.shutdown();

                    try {
                        this.keepAliveHandler.awaitTermination();
                    } catch (InterruptedException exception) {
                        throw new RuntimeException(exception);
                    }

                    // I just want to make the server joinable for now, don't hate me for what's below, OKKK???????

                    CompoundBinaryTag binaryTag = CompoundBinaryTag.builder()
                            .putBoolean("has_skylight", true)
                            .putBoolean("has_ceiling", true)
                            .putBoolean("ultrawarm", false)
                            .putBoolean("natural", true)
                            .putDouble("coordinate_scale", 1)
                            .putBoolean("bed_works", true)
                            .putBoolean("respawn_anchor_works", true)
                            .putInt("min_y", -64)
                            .putInt("height", 384)
                            .putInt("logical_height", 384)
                            .putString("infiniburn", "#minecraft:infiniburn_overworld")
                            .putString("effects", "minecraft:overworld")
                            .putFloat("ambient_light", 0)
                            .putBoolean("piglin_safe", true)
                            .putBoolean("has_raids", true)
                            .putInt("monster_spawn_light_level", 0)
                            .putInt("monster_spawn_block_light_limit", 0)
                            .build();

                    connection.sendPacket(new ServerRegistryDataConfigurationPacket(
                            Key.key("dimension_type"),
                            List.of(new ServerRegistryDataConfigurationPacket.Entry(
                                    Key.key("overworld"),
                                    binaryTag
                            ))
                    ));

                    connection.sendPacket(new ServerRegistryDataConfigurationPacket(
                            Key.key("wolf_variant"),
                            List.of(new ServerRegistryDataConfigurationPacket.Entry(
                                    Key.key("striped"),
                                    CompoundBinaryTag.builder()
                                            .putString("wild_texture", "minecraft:entity/wolf/wolf_striped")
                                            .putString("tame_texture", "minecraft:entity/wolf/wolf_striped_tame")
                                            .putString("angry_texture", "minecraft:entity/wolf/wolf_striped_angry")
                                            .putString("biomes", "#minecraft:is_badlands")
                                            .build()
                            ))
                    ));

                    connection.sendPacket(new ServerRegistryDataConfigurationPacket(
                            Key.key("painting_variant"),
                            List.of(new ServerRegistryDataConfigurationPacket.Entry(
                                    Key.key("alban"),
                                    CompoundBinaryTag.builder()
                                            .putString("asset_id", "minecraft:alban")
                                            .putInt("width", 1)
                                            .putInt("height", 1)
                                            .build()
                            ))
                    ));

                    connection.sendPacket(new ServerRegistryDataConfigurationPacket(
                            Key.key("damage_type"),
                            List.of(
                                    new ServerRegistryDataConfigurationPacket.Entry(
                                            Key.key("in_fire"),
                                            CompoundBinaryTag.builder()
                                                    .putString("message_id", "death.attack.onFire")
                                                    .putString("scaling", "never")
                                                    .putFloat("exhaustion", 0)
                                                    .build()
                                    ),
                                    new ServerRegistryDataConfigurationPacket.Entry(
                                            Key.key("campfire"),
                                            CompoundBinaryTag.builder()
                                                    .putString("message_id", "death.attack.onFire")
                                                    .putString("scaling", "never")
                                                    .putFloat("exhaustion", 0)
                                                    .build()
                                    ),
                                    new ServerRegistryDataConfigurationPacket.Entry(
                                            Key.key("lightning_bolt"),
                                            CompoundBinaryTag.builder()
                                                    .putString("message_id", "death.attack.onFire")
                                                    .putString("scaling", "never")
                                                    .putFloat("exhaustion", 0)
                                                    .build()
                                    ),
                                    new ServerRegistryDataConfigurationPacket.Entry(
                                            Key.key("on_fire"),
                                            CompoundBinaryTag.builder()
                                                    .putString("message_id", "death.attack.onFire")
                                                    .putString("scaling", "never")
                                                    .putFloat("exhaustion", 0)
                                                    .build()
                                    ),
                                    new ServerRegistryDataConfigurationPacket.Entry(
                                            Key.key("lava"),
                                            CompoundBinaryTag.builder()
                                                    .putString("message_id", "death.attack.onFire")
                                                    .putString("scaling", "never")
                                                    .putFloat("exhaustion", 0)
                                                    .build()
                                    ),
                                    new ServerRegistryDataConfigurationPacket.Entry(
                                            Key.key("hot_floor"),
                                            CompoundBinaryTag.builder()
                                                    .putString("message_id", "death.attack.onFire")
                                                    .putString("scaling", "never")
                                                    .putFloat("exhaustion", 0)
                                                    .build()
                                    ),
                                    new ServerRegistryDataConfigurationPacket.Entry(
                                            Key.key("in_wall"),
                                            CompoundBinaryTag.builder()
                                                    .putString("message_id", "death.attack.onFire")
                                                    .putString("scaling", "never")
                                                    .putFloat("exhaustion", 0)
                                                    .build()
                                    ),
                                    new ServerRegistryDataConfigurationPacket.Entry(
                                            Key.key("cramming"),
                                            CompoundBinaryTag.builder()
                                                    .putString("message_id", "death.attack.onFire")
                                                    .putString("scaling", "never")
                                                    .putFloat("exhaustion", 0)
                                                    .build()
                                    ),
                                    new ServerRegistryDataConfigurationPacket.Entry(
                                            Key.key("drown"),
                                            CompoundBinaryTag.builder()
                                                    .putString("message_id", "death.attack.onFire")
                                                    .putString("scaling", "never")
                                                    .putFloat("exhaustion", 0)
                                                    .build()
                                    ),
                                    new ServerRegistryDataConfigurationPacket.Entry(
                                            Key.key("starve"),
                                            CompoundBinaryTag.builder()
                                                    .putString("message_id", "death.attack.onFire")
                                                    .putString("scaling", "never")
                                                    .putFloat("exhaustion", 0)
                                                    .build()
                                    ),
                                    new ServerRegistryDataConfigurationPacket.Entry(
                                            Key.key("cactus"),
                                            CompoundBinaryTag.builder()
                                                    .putString("message_id", "death.attack.onFire")
                                                    .putString("scaling", "never")
                                                    .putFloat("exhaustion", 0)
                                                    .build()
                                    ),
                                    new ServerRegistryDataConfigurationPacket.Entry(
                                            Key.key("fall"),
                                            CompoundBinaryTag.builder()
                                                    .putString("message_id", "death.attack.onFire")
                                                    .putString("scaling", "never")
                                                    .putFloat("exhaustion", 0)
                                                    .build()
                                    ),
                                    new ServerRegistryDataConfigurationPacket.Entry(
                                            Key.key("fly_into_wall"),
                                            CompoundBinaryTag.builder()
                                                    .putString("message_id", "death.attack.onFire")
                                                    .putString("scaling", "never")
                                                    .putFloat("exhaustion", 0)
                                                    .build()
                                    ),
                                    new ServerRegistryDataConfigurationPacket.Entry(
                                            Key.key("out_of_world"),
                                            CompoundBinaryTag.builder()
                                                    .putString("message_id", "death.attack.onFire")
                                                    .putString("scaling", "never")
                                                    .putFloat("exhaustion", 0)
                                                    .build()
                                    ),
                                    new ServerRegistryDataConfigurationPacket.Entry(
                                            Key.key("generic"),
                                            CompoundBinaryTag.builder()
                                                    .putString("message_id", "death.attack.onFire")
                                                    .putString("scaling", "never")
                                                    .putFloat("exhaustion", 0)
                                                    .build()
                                    ),
                                    new ServerRegistryDataConfigurationPacket.Entry(
                                            Key.key("magic"),
                                            CompoundBinaryTag.builder()
                                                    .putString("message_id", "death.attack.onFire")
                                                    .putString("scaling", "never")
                                                    .putFloat("exhaustion", 0)
                                                    .build()
                                    ),
                                    new ServerRegistryDataConfigurationPacket.Entry(
                                            Key.key("wither"),
                                            CompoundBinaryTag.builder()
                                                    .putString("message_id", "death.attack.onFire")
                                                    .putString("scaling", "never")
                                                    .putFloat("exhaustion", 0)
                                                    .build()
                                    ),
                                    new ServerRegistryDataConfigurationPacket.Entry(
                                            Key.key("dragon_breath"),
                                            CompoundBinaryTag.builder()
                                                    .putString("message_id", "death.attack.onFire")
                                                    .putString("scaling", "never")
                                                    .putFloat("exhaustion", 0)
                                                    .build()
                                    ),
                                    new ServerRegistryDataConfigurationPacket.Entry(
                                            Key.key("dry_out"),
                                            CompoundBinaryTag.builder()
                                                    .putString("message_id", "death.attack.onFire")
                                                    .putString("scaling", "never")
                                                    .putFloat("exhaustion", 0)
                                                    .build()
                                    ),
                                    new ServerRegistryDataConfigurationPacket.Entry(
                                            Key.key("sweet_berry_bush"),
                                            CompoundBinaryTag.builder()
                                                    .putString("message_id", "death.attack.onFire")
                                                    .putString("scaling", "never")
                                                    .putFloat("exhaustion", 0)
                                                    .build()
                                    ),
                                    new ServerRegistryDataConfigurationPacket.Entry(
                                            Key.key("freeze"),
                                            CompoundBinaryTag.builder()
                                                    .putString("message_id", "death.attack.onFire")
                                                    .putString("scaling", "never")
                                                    .putFloat("exhaustion", 0)
                                                    .build()
                                    ),
                                    new ServerRegistryDataConfigurationPacket.Entry(
                                            Key.key("stalagmite"),
                                            CompoundBinaryTag.builder()
                                                    .putString("message_id", "death.attack.onFire")
                                                    .putString("scaling", "never")
                                                    .putFloat("exhaustion", 0)
                                                    .build()
                                    ),
                                    new ServerRegistryDataConfigurationPacket.Entry(
                                            Key.key("outside_border"),
                                            CompoundBinaryTag.builder()
                                                    .putString("message_id", "death.attack.onFire")
                                                    .putString("scaling", "never")
                                                    .putFloat("exhaustion", 0)
                                                    .build()
                                    ),
                                    new ServerRegistryDataConfigurationPacket.Entry(
                                            Key.key("generic_kill"),
                                            CompoundBinaryTag.builder()
                                                    .putString("message_id", "death.attack.onFire")
                                                    .putString("scaling", "never")
                                                    .putFloat("exhaustion", 0)
                                                    .build()
                                    )
                            )
                    ));

                    connection.sendPacket(new ServerRegistryDataConfigurationPacket(
                            Key.key("worldgen/biome"),
                            List.of(new ServerRegistryDataConfigurationPacket.Entry(
                                    Key.key("plains"),
                                    CompoundBinaryTag.builder()
                                            .putBoolean("has_precipitation", false)
                                            .putFloat("temperature", 2f)
                                            .putFloat("downfall", 1f)
                                            .put("effects", CompoundBinaryTag.builder()
                                                    .putInt("fog_color", 8364543)
                                                    .putInt("water_color", 8364543)
                                                    .putInt("water_fog_color", 8364543)
                                                    .putInt("sky_color", 8364543)
                                                    .build())
                                            .build()
                            ))
                    ));

                    // End of the registry fuckery

                    this.player.sendServerBrand(this.player.server().brandName());

                    this.finished = true;
                    connection.sendPacket(new ServerFinishConfigurationPacket());

                    try {
                        if (!this.acknowledgeLatch.await(TIME_OUT_DURATION, TIME_OUT_UNIT)) {
                            throw new TimeoutException("The configuration was not acknowledged on time");
                        }
                    } catch (InterruptedException | TimeoutException exception) {
                        throw new RuntimeException(exception);
                    }
                });
    }

    @Override
    public @NonNull JetConfigurationSession sessionHandler() {
        return this;
    }

    @Override
    public void onConnectionClose(@Nullable Throwable cause) {
        if (this.keepAliveHandler.isAlive()) {
            this.keepAliveHandler.shutdownNow();
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Objects.requireNonNull(e, "The throwable must not be null");
        this.keepAliveHandler.shutdownNow();
        this.player.disconnect(Component.text("An error occurred during the configuration", NamedTextColor.RED));
        LOGGER.error("An error occurred during the configuration of a player", e);
    }

    /**
     * Handles a client response for a configuration finish.
     *
     * @since 1.0
     */
    public void handleFinishAcknowledge() {
        if (!this.finished)
            throw new IllegalArgumentException("The configuration state was not finished");
        this.acknowledgeLatch.countDown();
    }

    /**
     * Gets a {@linkplain KeepAliveHandler keep alive handler} of this session.
     *
     * @return the keep alive handler
     * @since 1.0
     */
    public @NonNull KeepAliveHandler keepAliveHandler() {
        return this.keepAliveHandler;
    }

    /**
     * Casts a {@linkplain Session session} to the {@linkplain JetConfigurationSession configuration session}
     * or throws an exception.
     *
     * @param session the session to cast
     * @return the cast session
     * @throws IllegalStateException if session is not a configuration session
     * @since 1.0
     */
    public static @NonNull JetConfigurationSession asConfigurationSession(@Nullable Session<?> session) {
        if (session instanceof JetConfigurationSession configurationSession) return configurationSession;
        throw new IllegalStateException("The session is not a configuration session");
    }
}