package net.hypejet.jet.world.event;

/**
 * Represents an event of {@linkplain ??? a Minecraft world}.
 *
 * <p>The interface is not sealed, since it depends on Minecraft. Adding another class implementing a sealed interface
 * could break switch cases for example.</p>
 *
 * @since 1.0
 * @see ???
 */
public interface WorldEvent {}