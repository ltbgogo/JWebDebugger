package com.abc.wx.jWebDebugger;

import java.nio.charset.Charset;

import lombok.extern.log4j.Log4j;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
/**
 * Base class of {@link org.apache.mina.core.service.IoHandler} classes which handle proxied connections.
 */
@Log4j
public abstract class AbstractProxyIoHandler extends IoHandlerAdapter {
    
    public static final String OTHER_IO_SESSION = "OTHER_IO_SESSION";
      
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        session.suspendRead();
        session.suspendWrite();
    }
 
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        if (session.getAttribute(OTHER_IO_SESSION) != null) {
            IoSession sess = (IoSession) session.getAttribute(OTHER_IO_SESSION);
            sess.setAttribute(OTHER_IO_SESSION, null);
            sess.close(false);
            session.setAttribute(OTHER_IO_SESSION, null);
        }
    }
 
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        IoBuffer rb = (IoBuffer) message;
        IoBuffer wb = IoBuffer.allocate(rb.remaining());
        rb.mark();
        wb.put(rb);
        wb.flip();
        ((IoSession) session.getAttribute(OTHER_IO_SESSION)).write(wb);
        rb.reset();
        try {
        	FileUtils.copyInputStreamToFile(rb.asInputStream(), FileUtils.getFile("D:\\test\\wx", System.nanoTime() + ".txt"));
        } catch (Exception e) {
        	log.error(e.getMessage(), e);
        }
//        log.info(rb.getString(Charset.forName("iso8859-1").newDecoder()));
    }
}



