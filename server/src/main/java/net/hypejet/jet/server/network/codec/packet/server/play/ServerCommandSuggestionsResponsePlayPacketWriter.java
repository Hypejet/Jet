package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.codec.game.component.ComponentNetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerCommandSuggestionsResponsePlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerCommandSuggestionsResponsePlayPacket.Suggestion;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerCommandSuggestionsResponsePlayPacket a command suggestions response play packet}.
 *
 * @since 1.0
 * @see ServerCommandSuggestionsResponsePlayPacket
 * @see NetworkWriter
 */
public final class ServerCommandSuggestionsResponsePlayPacketWriter
        implements NetworkWriter<ServerCommandSuggestionsResponsePlayPacket> {

    /**
     * An instance of the {@linkplain ServerCommandSuggestionsResponsePlayPacketWriter server command suggestions
     * response play packet writer}.
     *
     * @since 1.0
     */
    public static final ServerCommandSuggestionsResponsePlayPacketWriter
            INSTANCE = new ServerCommandSuggestionsResponsePlayPacketWriter();

    private static final CollectionNetworkWriter<Suggestion> SUGGESTIONS_WRITER =
            new CollectionNetworkWriter<>(new SuggestionWriter());

    private ServerCommandSuggestionsResponsePlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerCommandSuggestionsResponsePlayPacket object) {
        VarIntNetworkCodec.INSTANCE.write(buf, object.transactionId());
        VarIntNetworkCodec.INSTANCE.write(buf, object.start());
        VarIntNetworkCodec.INSTANCE.write(buf, object.length());
        SUGGESTIONS_WRITER.write(buf, object.suggestions());
    }

    /**
     * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain Suggestion a suggestion}.
     *
     * @since 1.0
     * @see Suggestion
     * @see NetworkWriter
     */
    private static final class SuggestionWriter implements NetworkWriter<Suggestion> {
        @Override
        public void write(@NonNull ByteBuf buf, @NonNull Suggestion object) {
            StringNetworkCodec.INSTANCE.write(buf, object.text());
            NetworkUtil.writeOptional(object.tooltip(), ComponentNetworkWriter.INSTANCE, buf);
        }
    }
}