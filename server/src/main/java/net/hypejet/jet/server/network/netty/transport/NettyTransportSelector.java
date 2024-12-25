package net.hypejet.jet.server.network.netty.transport;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a something that selects {@linkplain NettyTransportType a netty transport type}.
 *
 * @since 1.0
 */
public enum NettyTransportSelector {
    /**
     * Represents a transport selector, which always selects {@linkplain NettyTransportType#NIO an NIO transport type}.
     *
     * @since 1.0
     */
    NIO(NettyTransportType.NIO),

    /**
     * Represents a transport selector, which always selects {@linkplain NettyTransportType#EPOLL an epoll transport
     * type}.
     *
     * @since 1.0
     */
    EPOLL(NettyTransportType.EPOLL),

    /**
     * Represents a transport selector, which always selects {@linkplain NettyTransportType#KQUEUE an KQueue transport
     * type}.
     *
     * @since 1.0
     */
    KQUEUE(NettyTransportType.KQUEUE),

    /**
     * Represents a transport selector, which selects {@linkplain NettyTransportType a netty transport type} using
     * {@link NettyTransportType#select()}.
     *
     * @since 1.0
     */
    AUTO(NettyTransportType.select());

    private final NettyTransportType transportType;

    /**
     * Constructs the {@linkplain NettyTransportType netty transport selector}.
     *
     * @param transportType the transport type selected
     * @since 1.0
     */
    NettyTransportSelector(@NonNull NettyTransportType transportType) {
        this.transportType = transportType;
    }

    /**
     * Gets the {@linkplain NettyTransportType netty transport type} selected by this selector.
     *
     * @return the transport type
     * @since 1.0
     */
    public @NonNull NettyTransportType getTransportType() {
        return this.transportType;
    }
}