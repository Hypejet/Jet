package net.hypejet.jet.server.test.network.protocol.codecs.registry;

import net.hypejet.jet.protocol.packet.server.configuration.ServerRegistryDataConfigurationPacket;
import net.hypejet.jet.server.network.protocol.codecs.registry.RegistryDataEntryNetworkWriter;
import net.hypejet.jet.server.test.network.protocol.codecs.NetworkCodecTestUtil;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.junit.jupiter.api.Test;

/**
 * Represents a test of reading and writing of a {@linkplain RegistryDataEntryNetworkWriter registry data entry network
 * codec}.
 *
 * @since 1.0
 * @author Codestech
 * @see RegistryDataEntryNetworkWriter
 */
public final class MinecraftRegistryDataEntryNetworkWriterTest {
    @Test
    public void test() {
        NetworkCodecTestUtil.test(
                RegistryDataEntryNetworkWriter.instance(),
                new ServerRegistryDataConfigurationPacket.Entry(
                        Key.key("some-registry"),
                        CompoundBinaryTag.builder()
                                .putBoolean("some-bool", true)
                                .putBoolean("another-one", false)
                                .putFloat("a-float-number", 2324312.4353f)
                                .putInt("an-integer", 2134124)
                                .build()
                )
        );
    }
}