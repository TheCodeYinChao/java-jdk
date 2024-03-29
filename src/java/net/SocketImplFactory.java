/*
 * Copyright (c) 1995, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package java.net;

/**
 * This interface defines a factory for socket implementations. It
 * is used by the classes {@code Socket} and
 * {@code ServerSocket} to create actual socket
 * implementations.
 *  创建 socket 和 serversocket 的实现类的工厂
 * @author  Arthur van Hoff
 * @see     Socket
 * @see     ServerSocket
 * @since   JDK1.0
 */
public
interface SocketImplFactory {
    /**
     * Creates a new {@code SocketImpl} instance.
     *
     * @return  a new instance of {@code SocketImpl}.
     * @see     SocketImpl
     */
    SocketImpl createSocketImpl();
}
