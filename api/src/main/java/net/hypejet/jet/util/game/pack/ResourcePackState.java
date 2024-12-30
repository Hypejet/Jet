package net.hypejet.jet.util.game.pack;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a state of resource pack loading.
 *
 * <p>This is not an enum, since it depends on Minecraft. Adding an enum entry could break enum switch cases for
 * example./p>
 *
 * @since 1.0
 */
public final class ResourcePackState {
    /**
     * A state used when a client states that the resource pack was loaded successfully.
     *
     * @since 1.0
     */
    public static final ResourcePackState SUCCESS = new ResourcePackState("success");

    /**
     * A state used when a client states that the resource pack was loaded successfully.
     *
     * @since 1.0
     */
    public static final ResourcePackState DECLINED = new ResourcePackState("declined");

    /**
     * A state used when a client states that it could not download the resource pack.
     *
     * @since 1.0
     */
    public static final ResourcePackState FAILED_TO_DOWNLOAD = new ResourcePackState("failed to download");

    /**
     * A state used when a client states that the client accepted the resource pack.
     *
     * @since 1.0
     */
    public static final ResourcePackState ACCEPTED = new ResourcePackState("accepted");

    /**
     * A state used when a client states that the resource pack was downloaded successfully.
     *
     * @since 1.0
     */
    public static final ResourcePackState DOWNLOADED = new ResourcePackState("downloaded");

    /**
     * A state used when a client states that it could not find an url resource pack.
     *
     * @since 1.0
     */
    public static final ResourcePackState INVALID_URL = new ResourcePackState("invalid URL");

    /**
     * A state used when a client states that the resource pack was could not be reloaded.
     *
     * @since 1.0
     */
    public static final ResourcePackState FAILED_TO_RELOAD = new ResourcePackState("failed to reload");

    /**
     * A state used when a client states that the resource pack was discarded.
     *
     * @since 1.0
     */
    public static final ResourcePackState DISCARDED = new ResourcePackState("discarded");

    private final String name;

    private ResourcePackState(@NonNull String name) {
        this.name = NullabilityUtil.requireNonNull(name, "name");
    }

    /**
     * Gets a readable lower-case name of this resource pack state.
     *
     * @return the name
     * @since 1.0
     */
    public @NonNull String name() {
        return this.name;
    }

    /* Methods #equals and #hashCode are not implemented, since this class is intended to be identity-compared only
       since all instances are defined in constants of this class. */

    @Override
    public String toString() {
        return "ResourcePackState{" +
                "name='" + this.name + '\'' +
                '}';
    }
}
