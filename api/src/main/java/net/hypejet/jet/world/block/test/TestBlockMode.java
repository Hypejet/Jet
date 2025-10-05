package net.hypejet.jet.world.block.test;

import org.jspecify.annotations.NullMarked;

/**
 * The mode of a test block.
 *
 * <p>This is not an enum, since it depends on Minecraft.
 * Adding an enum entry could break enum switch cases for example./p>
 *
 * @since 1.0
 */
@NullMarked
public final class TestBlockMode {
    /**
     * A {@linkplain TestBlockMode test block mode} indicating that
     * the test block should trigger a redstone pulse when the test starts.
     *
     * @since 1.0
     */
    public static final TestBlockMode START = new TestBlockMode("start");

    /**
     * A {@linkplain TestBlockMode test block mode} indicating that the test
     * block should log a message to the log file when powered by redstone.
     *
     * @since 1.0
     */
    public static final TestBlockMode LOG = new TestBlockMode("log");

    /**
     * A {@linkplain TestBlockMode test block mode} indicating that
     * the test block should fail the test when powered by redstone.
     *
     * @since 1.0
     */
    public static final TestBlockMode FAIL = new TestBlockMode("fail");

    /**
     * A {@linkplain TestBlockMode test block mode} indicating that the test
     * block should mark the test as successfully completed when powered by redstone.
     *
     * @since 1.0
     */
    public static final TestBlockMode ACCEPT = new TestBlockMode("accept");

    private final String name;

    private TestBlockMode(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TestBlockMode{" +
                "name='" + this.name + '\'' +
                '}';
    }
}