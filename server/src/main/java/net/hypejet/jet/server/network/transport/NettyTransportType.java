package net.hypejet.jet.server.network.transport;

import io.netty.channel.MultithreadEventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Supplier;

/**
 * Represents a transport type, which should be used by netty.
 *
 * @since 1.0
 * @author Codestech
 */
public enum NettyTransportType {
    /**
     * Represents a transport type providing a {@linkplain NioEventLoopGroup NIO event loop group} and creating
     * {@linkplain NioServerSocketChannel NIO server socket channels}.
     *
     * @since 1.0
     */
    NIO(NioEventLoopGroup::new, NioServerSocketChannel.class, true),

    /**
     * Represents a transport type providing a {@linkplain EpollEventLoopGroup epoll event loop group} and creating
     * {@linkplain EpollServerSocketChannel epoll server socket channels}.
     *
     * @since 1.0
     */
    EPOLL(EpollEventLoopGroup::new, EpollServerSocketChannel.class, Epoll.isAvailable()),

    /**
     * Represents a transport type providing a {@linkplain KQueueEventLoopGroup kqueue event loop group} and creating
     * {@linkplain KQueueServerSocketChannel kqueue server socket channels}.
     *
     * @since 1.0
     */
    KQUEUE(KQueueEventLoopGroup::new, KQueueServerSocketChannel.class, KQueue.isAvailable());

    private final Supplier<MultithreadEventLoopGroup> eventLoopSupplier;
    private final Class<? extends ServerSocketChannel> socketChannel;

    private final boolean available;

    /**
     * Constructs a {@linkplain NettyTransportType netty transport type}.
     *
     * @param eventLoopSupplier a supplier producing a {@linkplain MultithreadEventLoopGroup multi-thread event loop
     *                          group}
     * @param socketChannel a class of the type of {@linkplain ServerSocketChannel}, which netty should create
     * @since 1.0
     */
    NettyTransportType(@NonNull Supplier<MultithreadEventLoopGroup> eventLoopSupplier,
                       @NonNull Class<? extends ServerSocketChannel> socketChannel, boolean available) {
        this.eventLoopSupplier = eventLoopSupplier;
        this.socketChannel = socketChannel;
        this.available = available;
    }

    /**
     * Creates a {@linkplain MultithreadEventLoopGroup multi-threaded event loop group}, which supports this transport.
     *
     * @return the event loop group
     * @since 1.0
     */
    public @NonNull MultithreadEventLoopGroup createEventLoop() {
        return this.eventLoopSupplier.get();
    }

    /**
     * Gets a class of a type of {@linkplain ServerSocketChannel server socket channel} that this transport supports.
     *
     * @return the class
     * @since 1.0
     */
    public @NonNull Class<? extends ServerSocketChannel> getSocketChannel() {
        return socketChannel;
    }

    /**
     * Checks whether this transport is available.
     *
     * @return true if the transport is available, false otherwise
     * @since 1.0
     */
    public boolean isAvailable() {
        return this.available;
    }

    /**
     * Gets the "best" {@linkplain NettyTransportType netty transport type} available.
     *
     * @return the transport type
     * @since 1.0
     */
    public static @NonNull NettyTransportType select() {
        if (Epoll.isAvailable()) return NettyTransportType.EPOLL;
        if (KQueue.isAvailable()) return NettyTransportType.KQUEUE;
        return NettyTransportType.NIO;
    }
}