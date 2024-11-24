package net.hypejet.jet.server.network.protocol.packet.server.writer.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.play.ServerCommandSuggestionsResponsePlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerCommandSuggestionsResponsePlayPacket.Suggestion;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.collection.CollectionNetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.component.ComponentNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerCommandSuggestionsResponsePlayPacket a command suggestions response play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerCommandSuggestionsResponsePlayPacket
 * @see NetworkWriter
 */
public final class ServerCommandSuggestionsResponsePlayPacketWriter
        implements NetworkWriter<ServerCommandSuggestionsResponsePlayPacket> {

    private static final CollectionNetworkWriter<Suggestion> SUGGESTIONS_WRITER =
            new CollectionNetworkWriter<>(new SuggestionWriter());

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerCommandSuggestionsResponsePlayPacket object) {
        VarIntNetworkCodec.instance().write(buf, object.transactionId());
        VarIntNetworkCodec.instance().write(buf, object.start());
        VarIntNetworkCodec.instance().write(buf, object.length());
        SUGGESTIONS_WRITER.write(buf, object.suggestions());
    }

    /**
     * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain Suggestion a suggestion}.
     *
     * @since 1.0
     * @author Codestech
     * @see Suggestion
     * @see NetworkWriter
     */
    private static final class SuggestionWriter implements NetworkWriter<Suggestion> {
        @Override
        public void write(@NonNull ByteBuf buf, @NonNull Suggestion object) {
            StringNetworkCodec.instance().write(buf, object.text());
            NetworkUtil.writeOptional(object.tooltip(), ComponentNetworkCodec.instance(), buf);
        }
    }
}