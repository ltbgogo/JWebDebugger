package com.abc.wx.jWebDebugger;

import java.net.InetSocketAddress;

import lombok.extern.log4j.Log4j;

import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

@Log4j
public class Main {
	 
    public static void main(String[] args) throws Exception {
    	args = new String[] {"6666", "proxy3.bj.petrochina", "8080"};
 
        // Create TCP/IP acceptor.
        NioSocketAcceptor acceptor = new NioSocketAcceptor();
 
        // Start proxy.
        ClientToProxyIoHandler clientToProxyIoHandler = new ClientToProxyIoHandler(new InetSocketAddress(args[1], Integer.parseInt(args[2]))); 
        acceptor.setHandler(clientToProxyIoHandler);
        acceptor.bind(new InetSocketAddress(Integer.parseInt(args[0])));
 
        log.info("Listening on port " + Integer.parseInt(args[0]));
    }
 
}