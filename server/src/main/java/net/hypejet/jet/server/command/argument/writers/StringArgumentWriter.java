package net.hypejet.jet.server.command.argument.writers;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType.StringType;
import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.command.argument.ArgumentWriter;
import net.hypejet.jet.server.network.codec.index.IndexNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.util.index.IndexUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

/**
 * Represents {@linkplain ArgumentWriter an argument writer}, which writes
 * {@linkplain StringArgumentType a string argument type}.
 *
 * @since 1.0
 * @see StringArgumentType
 * @see ArgumentWriter
 */
public final class StringArgumentWriter extends ArgumentWriter<StringArgumentType> {

    /**
     * An instance of the {@linkplain StringArgumentWriter string argument writer}.
     *
     * @since 1.0
     */
    public static final StringArgumentWriter INSTANCE = new StringArgumentWriter();

    private static final IndexNetworkCodec<StringType, Integer> STRING_TYPE_CODEC = new IndexNetworkCodec<>(
            IndexUtil.fromMap(Map.of(
                    0, StringType.SINGLE_WORD,
                    1, StringType.QUOTABLE_PHRASE,
                    2, StringType.GREEDY_PHRASE
            )),
            VarIntNetworkCodec.INSTANCE
    );

    private StringArgumentWriter() {
        super(5, StringArgumentType.class);
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull StringArgumentType object) {
        STRING_TYPE_CODEC.write(buf, object.getType());
    }
}