package net.hypejet.jet.server.util.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.color.ANSIConstants;
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase;

/**
 * Represents {@linkplain ForegroundCompositeConverterBase a foreground composite converter base} setting logging
 * message colors based on a message level.
 *
 * @since 1.0
 */
public final class LogbackHighlightingMessage extends ForegroundCompositeConverterBase<ILoggingEvent> {
    @Override
    protected String getForegroundColorCode(ILoggingEvent event) {
        return switch (event.getLevel().toInt()) {
            case Level.WARN_INT -> ANSIConstants.YELLOW_FG;
            case Level.ERROR_INT -> ANSIConstants.RED_FG;
            default -> ANSIConstants.DEFAULT_FG;
        };
    }
}