package net.hypejet.jet.server.command.argument.codecs;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType.StringType;
import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.command.argument.ArgumentCodec;
import net.hypejet.jet.server.network.protocol.codecs.enums.EnumVarIntCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ArgumentCodec argument codec}, which reads and writes a {@linkplain StringArgumentType
 * string argument type}.
 *
 * @since 1.0
 * @author Codestech
 * @see StringArgumentType
 * @see ArgumentCodec
 */
public final class StringArgumentCodec extends ArgumentCodec<StringArgumentType> {

    private static final EnumVarIntCodec<StringType> STRING_TYPE_CODEC = EnumVarIntCodec.builder(StringType.class)
            .add(StringType.SINGLE_WORD, 0)
            .add(StringType.QUOTABLE_PHRASE, 1)
            .add(StringType.GREEDY_PHRASE, 2)
            .build();

    /**
     * Constructs the {@linkplain StringArgumentCodec string argument codec}.
     *
     * @since 1.0
     */
    public StringArgumentCodec() {
        super(5, StringArgumentType.class);
    }

    @Override
    public @NonNull StringArgumentType read(@NonNull ByteBuf buf) {
        return switch (STRING_TYPE_CODEC.read(buf)) {
            case SINGLE_WORD -> StringArgumentType.word();
            case QUOTABLE_PHRASE -> StringArgumentType.string();
            case GREEDY_PHRASE -> StringArgumentType.greedyString();
        };
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull StringArgumentType object) {
        STRING_TYPE_CODEC.write(buf, object.getType());
    }
}