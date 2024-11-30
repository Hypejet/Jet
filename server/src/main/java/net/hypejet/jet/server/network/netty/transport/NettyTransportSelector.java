package net.hypejet.jet.server.network.netty.transport;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a something that selects {@linkplain NettyTransportType a netty transport type}.
 *
 * @since 1.0
 * @author Codestech
 */
public enum NettyTransportSelector {
    /**
     * Represents a transport selector, which always selects a {@linkplain NettyTransportType#NIO NIO transport type}.
     *
     * @since 1.0
     */
    NIO(NettyTransportType.NIO),

    /**
     * Represents a transport selector, which always selects a {@linkplain NettyTransportType#EPOLL epoll transport
     * type}.
     *
     * @since 1.0
     */
    EPOLL(NettyTransportType.EPOLL),

    /**
     * Represents a transport selector, which always selects a {@linkplain NettyTransportType#KQUEUE kqueue transport
     * type}.
     *
     * @since 1.0
     */
    KQUEUE(NettyTransportType.KQUEUE),

    /**
     * Represents a transport selector, which selects a {@linkplain NettyTransportType netty transport type} using
     * {@link NettyTransportType#select()}.
     *
     * @since 1.0
     */
    AUTO(NettyTransportType.select());

    private final NettyTransportType transportType;

    /**
     * Constructs {@linkplain NettyTransportType a netty transport selector}.
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