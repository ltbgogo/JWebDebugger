package com.abc.wx.jWebDebugger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.abc.wx.jWebDebugger.SessionAttachment.KEY_ATTACHMENT;
 
/**
 * Base class of {@link org.apache.mina.core.service.IoHandler} classes which handle proxied connections.
 */
@Log4j
public abstract class AbstractProxyIoHandler extends IoHandlerAdapter {
    
    public static final String OTHER_IO_SESSION = "OTHER_IO_SESSION";
    public static final String SESSION_NAME = "SESSION_NAME";
      
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        session.suspendRead();
        session.suspendWrite();
    }
 
    @Override
    public void sessionClosed(IoSession session) throws Exception {
    	SessionAttachment attachment = (SessionAttachment) session.getAttribute(KEY_ATTACHMENT);
        if (attachment != null) {
        	session.setAttribute(KEY_ATTACHMENT, null);
        	attachment.getTheOtherIoSession().close(false);
        	attachment.destroy();
        }
    }
 
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        IoBuffer rb = (IoBuffer) message;
        IoBuffer wb = IoBuffer.allocate(rb.remaining());
        rb.mark();
        wb.put(rb);
        wb.flip();
        SessionAttachment attachment = (SessionAttachment) session.getAttribute(KEY_ATTACHMENT);
        attachment.getTheOtherIoSession().write(wb);
        rb.reset();
        try {
        	File file = FileUtils.getFile("D:\\test\\wx", attachment.getSessionName() + ".txt");
        	FileUtils.write
//        	@Cleanup
//        	InputStream input = rb.asInputStream();
//        	@Cleanup
//        	OutputStream output = new FileOutputStream(, true);
//        	IOUtils.copy(input, output);
        } catch (Exception e) {
        	log.error(e.getMessage(), e);
        }
//        log.info(rb.getString(Charset.forName("iso8859-1").newDecoder()));
    }
}



