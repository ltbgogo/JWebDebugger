package com.abc.wx.jWebDebugger;

import java.net.SocketAddress;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
 
/**
 * Handles the client to proxy part of the proxied connection.
 */
public class ClientToProxyIoHandler extends AbstractProxyIoHandler {
	
    private final ServerToProxyIoHandler connectorHandler = new ServerToProxyIoHandler();
    private final IoConnector connector;
    private final SocketAddress proxyServerAddress;
 
    public ClientToProxyIoHandler(SocketAddress proxyServerAddress) {
		// Create TCP/IP connector.
		this.connector = new NioSocketConnector();
		this.connector.setConnectTimeoutMillis(30 * 1000L);
		//like a handler chain
		this.connector.setHandler(connectorHandler);
        this.proxyServerAddress = proxyServerAddress;
    }
 
    @Override
    public void sessionOpened(final IoSession session) throws Exception {
        connector.connect(proxyServerAddress).addListener(new IoFutureListener<ConnectFuture>() {
            public void operationComplete(ConnectFuture future) {
                try {
                    future.getSession().setAttribute(OTHER_IO_SESSION, session);
                    session.setAttribute(OTHER_IO_SESSION, future.getSession());
                    future.getSession().resumeRead();
                    future.getSession().resumeWrite();
                } catch (RuntimeIoException e) {
                    // Connect failed
                    session.close(true);
                } finally {
                    session.resumeRead();
                    session.resumeWrite();
                }
            }
        });
    }
}