package net.hypejet.jet.server;

/**
 * Represents a main class, which provides an instruction for when the program starts executing.
 *
 * @since 1.0
 * @author Codestech
 */
public final class JetServerEntrypoint {

    private JetServerEntrypoint() {}

    /**
     * Runs the {@linkplain JetMinecraftServer Jet Minecraft server}.
     *
     * @param args the program arguments
     * @since 1.0
     */
    public static void main(String[] args) {
        JetMinecraftServer server = new JetMinecraftServer();
        Runtime.getRuntime().addShutdownHook(Thread.ofVirtual().unstarted(server::shutdown));
    }
}