package net.hypejet.jet.server.entity.acquisition.respawn;

import net.hypejet.concurrency.primitive.booleans.WriteBooleanAcquisition;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.entity.player.JetPlayer;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerWorldEventPlayPacket;
import net.hypejet.jet.world.event.events.EnableRespawnScreenWorldEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Represents {@linkplain WriteBooleanAcquisition a write boolean acquisition}, whose value defines whether respawn
 * screen is enabled for {@linkplain JetPlayer a player}.
 *
 * @since 1.0
 * @see WriteBooleanAcquisition
 * @see JetPlayer
 */
public final class WriteRespawnScreenEnabledAcquisition implements WriteBooleanAcquisition {

    private final JetPlayer player;
    private final WriteBooleanAcquisition acquisition;

    /**
     * Constructs the {@linkplain WriteRespawnScreenEnabledAcquisition write respawn screen enabled acquisition}.
     *
     * @param player a player that the respawn screen state is handled for
     * @param acquisition an acquisition that the write respawn screen enabled acquisition should wrap
     * @since 1.0
     */
    public WriteRespawnScreenEnabledAcquisition(@NotNull JetPlayer player,
                                                @NotNull WriteBooleanAcquisition acquisition) {
        this.player = NullabilityUtil.requireNonNull(player, "player");
        this.acquisition = NullabilityUtil.requireNonNull(acquisition, "acquisition");
    }

    @Override
    public boolean get() {
        return this.acquisition.get();
    }

    @Override
    public void set(boolean value) {
        this.acquisition.set(value);
        this.player.sendPacket(new ServerWorldEventPlayPacket(new EnableRespawnScreenWorldEvent(value)));
    }

    @Override
    public boolean isUnlocked() {
        return this.acquisition.isUnlocked();
    }

    @Override
    public void close() {
        this.acquisition.close();
    }

    @Override
    public void ensurePermittedAndLocked() {
        this.acquisition.ensurePermittedAndLocked();
    }

    @Override
    public @NotNull AcquisitionType acquisitionType() {
        return this.acquisition.acquisitionType();
    }
}