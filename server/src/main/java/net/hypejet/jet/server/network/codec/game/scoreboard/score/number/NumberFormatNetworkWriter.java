package net.hypejet.jet.server.network.codec.game.scoreboard.score.number;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.hypejet.jet.scoreboard.score.number.BlankNumberFormat;
import net.hypejet.jet.scoreboard.score.number.FixedNumberFormat;
import net.hypejet.jet.scoreboard.score.number.NumberFormat;
import net.hypejet.jet.scoreboard.score.number.StyledNumberFormat;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.component.ComponentNetworkWriter;
import net.hypejet.jet.server.network.codec.game.component.StyleNetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer} of {@linkplain NumberFormat a number format}.
 *
 * @since 1.0
 * @see NumberFormat
 * @see NetworkWriter
 */
public final class NumberFormatNetworkWriter implements NetworkWriter<NumberFormat> {

    /**
     * An instance of the {@linkplain NumberFormatNetworkWriter number format network writer}.
     *
     * @since 1.0
     */
    public static final NumberFormatNetworkWriter INSTANCE = new NumberFormatNetworkWriter();

    private static final Object2IntMap<Class<? extends NumberFormat>> IDENTIFIERS = new Object2IntOpenHashMap<>();

    static {
        IDENTIFIERS.put(BlankNumberFormat.class, 0);
        IDENTIFIERS.put(StyledNumberFormat.class, 1);
        IDENTIFIERS.put(FixedNumberFormat.class, 2);
    }

    private NumberFormatNetworkWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf,
                      @NonNull JetRegistryManager registryManager,
                      @NonNull NumberFormat object) {
        Class<? extends NumberFormat> formatClass = object.getClass();
        if (!IDENTIFIERS.containsKey(formatClass)) {
            throw new IllegalArgumentException(String.format(
                    "Unknown number format with class name of %s",
                    formatClass.getSimpleName()
            ));
        }

        int identifier = IDENTIFIERS.getInt(formatClass);
        VarIntNetworkCodec.INSTANCE.write(buf, registryManager, identifier);

        switch (object) {
            case BlankNumberFormat ignored -> {}
            case StyledNumberFormat format ->
                    StyleNetworkWriter.INSTANCE.write(buf, registryManager, format.style());
            case FixedNumberFormat format ->
                    ComponentNetworkWriter.INSTANCE.write(buf, registryManager, format.placeholder());
            default -> throw new IllegalStateException(String.format("Unknown number format: %s", object));
        }
    }
}