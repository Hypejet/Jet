package net.hypejet.jet.server.command.argument.writers;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType.StringType;
import io.netty.buffer.ByteBuf;
import net.hypejet.jet.data.codecs.util.mapper.Mapper;
import net.hypejet.jet.server.command.argument.ArgumentWriter;
import net.hypejet.jet.server.network.codec.mapper.MapperNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

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

    private static final MapperNetworkCodec<StringType, Integer> STRING_TYPE_CODEC = new MapperNetworkCodec<>(
            Mapper.builder(StringType.class, Integer.class)
                    .register(StringType.SINGLE_WORD, 0)
                    .register(StringType.QUOTABLE_PHRASE, 1)
                    .register(StringType.GREEDY_PHRASE, 2)
                    .build(),
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