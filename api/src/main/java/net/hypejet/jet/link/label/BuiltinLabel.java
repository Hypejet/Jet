package net.hypejet.jet.link.label;

/**
 * Represents a {@linkplain ServerLinkLabel server link label} that displays a text that is built-in the client.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerLinkLabel
 */
public enum BuiltinLabel implements ServerLinkLabel {
    /**
     * A built-in label, which is displayed on a connection error screen, included as a comment on the disconnection
     * report.
     *
     * @since 1.0
     */
    BUG_REPORT,
    /**
     * A built-in label, which displays a community guidelines label text.
     *
     * @since 1.0
     */
    COMMUNITY_GUIDELINES,
    /**
     * A built-in label, which displays a support label text.
     *
     * @since 1.0
     */
    SUPPORT,
    /**
     * A built-in label, which displays a status label text.
     *
     * @since 1.0
     */
    STATUS,
    /**
     * A built-in label, which displays a feedback label text.
     *
     * @since 1.0
     */
    FEEDBACK,
    /**
     * A built-in label, which displays a community label text.
     *
     * @since 1.0
     */
    COMMUNITY,
    /**
     * A built-in label, which displays a website label text.
     *
     * @since 1.0
     */
    WEBSITE,
    /**
     * A built-in label, which displays a forums label text.
     *
     * @since 1.0
     */
    FORUMS,
    /**
     * A built-in label, which displays a news label text.
     *
     * @since 1.0
     */
    NEWS,
    /**
     * A built-in label, which displays an announcements label text.
     *
     * @since 1.0
     */
    ANNOUNCEMENTS
}