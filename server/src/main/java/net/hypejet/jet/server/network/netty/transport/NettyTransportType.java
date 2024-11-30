package net.hypejet.jet.server.network.netty.transport;

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
     * Represents a transport type providing {@linkplain NioEventLoopGroup an NIO event loop group} and creating
     * {@linkplain NioServerSocketChannel NIO server socket channels}.
     *
     * @since 1.0
     */
    NIO(NioEventLoopGroup::new, NioServerSocketChannel.class, true),

    /**
     * Represents a transport type providing {@linkplain EpollEventLoopGroup an epoll event loop group} and creating
     * {@linkplain EpollServerSocketChannel epoll server socket channels}.
     *
     * @since 1.0
     */
    EPOLL(EpollEventLoopGroup::new, EpollServerSocketChannel.class, Epoll.isAvailable()),

    /**
     * Represents a transport type providing {@linkplain KQueueEventLoopGroup an KQueue event loop group} and creating
     * {@linkplain KQueueServerSocketChannel kqueue server socket channels}.
     *
     * @since 1.0
     */
    KQUEUE(KQueueEventLoopGroup::new, KQueueServerSocketChannel.class, KQueue.isAvailable());

    private final Supplier<MultithreadEventLoopGroup> eventLoopSupplier;
    private final Class<? extends ServerSocketChannel> socketChannel;

    private final boolean available;

    /**
     * Constructs {@linkplain NettyTransportType a netty transport type}.
     *
     * @param eventLoopSupplier a supplier creating a multi-thread event loop group
     * @param socketChannel a class of the type of server socket channel, which netty should create
     * @since 1.0
     */
    NettyTransportType(@NonNull Supplier<MultithreadEventLoopGroup> eventLoopSupplier,
                       @NonNull Class<? extends ServerSocketChannel> socketChannel, boolean available) {
        this.eventLoopSupplier = eventLoopSupplier;
        this.socketChannel = socketChannel;
        this.available = available;
    }

    /**
     * Creates {@linkplain MultithreadEventLoopGroup a multi-threaded event loop group}, which supports this transport.
     *
     * @return the event loop group
     * @since 1.0
     */
    public @NonNull MultithreadEventLoopGroup createEventLoop() {
        return this.eventLoopSupplier.get();
    }

    /**
     * Gets a class of a type of {@linkplain ServerSocketChannel a server socket channel} that this transport supports.
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
     * @return {@code true} if the transport is available, {@code false} otherwise
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