package net.hypejet.jet.server.network.protocol.packet.server.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.play.ServerCommandSuggestionsResponsePlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerCommandSuggestionsResponsePlayPacket.Suggestion;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.CollectionNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.component.ComponentNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.StringNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ServerCommandSuggestionsResponsePlayPacket command suggestions response play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerCommandSuggestionsResponsePlayPacket
 * @see PacketCodec
 */
public final class ServerCommandSuggestionsResponsePlayPacketCodec
        extends PacketCodec<ServerCommandSuggestionsResponsePlayPacket> {

    private static final CollectionNetworkCodec<Suggestion> SUGGESTIONS_CODEC = CollectionNetworkCodec
            .create(new SuggestionCodec());

    /**
     * Constructs the {@linkplain ServerCommandSuggestionsResponsePlayPacketCodec command suggestions response
     * play packet codec}.
     *
     * @since 1.0
     */
    public ServerCommandSuggestionsResponsePlayPacketCodec() {
        super(ServerPacketIdentifiers.PLAY_SUGGESTIONS_RESPONSE, ServerCommandSuggestionsResponsePlayPacket.class);
    }

    @Override
    public @NonNull ServerCommandSuggestionsResponsePlayPacket read(@NonNull ByteBuf buf) {
        return new ServerCommandSuggestionsResponsePlayPacket(VarIntNetworkCodec.instance().read(buf),
                VarIntNetworkCodec.instance().read(buf), VarIntNetworkCodec.instance().read(buf),
                SUGGESTIONS_CODEC.read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerCommandSuggestionsResponsePlayPacket object) {
        VarIntNetworkCodec.instance().write(buf, object.transactionId());
        VarIntNetworkCodec.instance().write(buf, object.start());
        VarIntNetworkCodec.instance().write(buf, object.length());
        SUGGESTIONS_CODEC.write(buf, object.suggestions());
    }

    /**
     * Represents a {@linkplain NetworkCodec network codec}, which reads and writes
     * a {@linkplain Suggestion suggestion}.
     *
     * @since 1.0
     * @author Codestech
     * @see Suggestion
     * @see NetworkCodec
     */
    private static final class SuggestionCodec implements NetworkCodec<Suggestion> {
        @Override
        public @NonNull Suggestion read(@NonNull ByteBuf buf) {
            return new Suggestion(StringNetworkCodec.instance().read(buf),
                    NetworkUtil.readOptional(ComponentNetworkCodec.instance(), buf));
        }

        @Override
        public void write(@NonNull ByteBuf buf, @NonNull Suggestion object) {
            StringNetworkCodec.instance().write(buf, object.text());
            NetworkUtil.writeOptional(object.tooltip(), ComponentNetworkCodec.instance(), buf);
        }
    }
}