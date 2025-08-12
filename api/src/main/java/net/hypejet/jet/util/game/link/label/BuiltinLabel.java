package net.hypejet.jet.util.game.link.label;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents {@linkplain ServerLinkLabel a server link label} that displays a text that is built-in the client.
 *
 * <p>This is not an enum, since it depends on Minecraft. Adding an enum entry could break enum switch cases for
 * example./p>
 *
 * @since 1.0
 * @see ServerLinkLabel
 */
public final class BuiltinLabel implements ServerLinkLabel {
    /**
     * A built-in label, which is displayed on a connection error screen, included as a comment on the disconnection
     * report.
     *
     * @since 1.0
     */
    public static final BuiltinLabel BUG_REPORT = new BuiltinLabel("bug report");

    /**
     * A built-in label, which displays a community guidelines label text.
     *
     * @since 1.0
     */
    public static final BuiltinLabel COMMUNITY_GUIDELINES = new BuiltinLabel("community guidelines");

    /**
     * A built-in label, which displays a support label text.
     *
     * @since 1.0
     */
    public static final BuiltinLabel SUPPORT = new BuiltinLabel("support");

    /**
     * A built-in label, which displays a status label text.
     *
     * @since 1.0
     */
    public static final BuiltinLabel STATUS = new BuiltinLabel("status");

    /**
     * A built-in label, which displays a feedback label text.
     *
     * @since 1.0
     */
    public static final BuiltinLabel FEEDBACK = new BuiltinLabel("feedback");

    /**
     * A built-in label, which displays a community label text.
     *
     * @since 1.0
     */
    public static final BuiltinLabel COMMUNITY = new BuiltinLabel("community");

    /**
     * A built-in label, which displays a website label text.
     *
     * @since 1.0
     */
    public static final BuiltinLabel WEBSITE = new BuiltinLabel("website");

    /**
     * A built-in label, which displays a forums label text.
     *
     * @since 1.0
     */
    public static final BuiltinLabel FORUMS = new BuiltinLabel("forums");

    /**
     * A built-in label, which displays a news label text.
     *
     * @since 1.0
     */
    public static final BuiltinLabel NEWS = new BuiltinLabel("news");

    /**
     * A built-in label, which displays an announcements label text.
     *
     * @since 1.0
     */
    public static final BuiltinLabel ANNOUNCEMENTS = new BuiltinLabel("announcements");

    private final String name;

    private BuiltinLabel(@NonNull String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    /**
     * Gets a readable lower-case name of this built-in label.
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
        return "BuiltinLabel{" +
                "name='" + this.name + '\'' +
                '}';
    }
}