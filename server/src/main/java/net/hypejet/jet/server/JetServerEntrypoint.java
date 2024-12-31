package net.hypejet.jet.server;

/**
 * Represents a main class, which provides an instruction for when the application starts executing.
 *
 * @since 1.0
 */
public final class JetServerEntrypoint {

    private JetServerEntrypoint() {}

    /**
     * Runs the {@linkplain JetMinecraftServer Minecraft server}.
     *
     * @param args arguments that the application should start with
     * @since 1.0
     */
    public static void main(String[] args) {
        JetMinecraftServer server = new JetMinecraftServer();
        Runtime.getRuntime().addShutdownHook(Thread.ofVirtual().unstarted(server::shutdown));
    }
}