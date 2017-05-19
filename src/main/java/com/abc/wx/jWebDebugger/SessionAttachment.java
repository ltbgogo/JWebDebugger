package com.abc.wx.jWebDebugger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.apache.mina.core.session.IoSession;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SessionAttachment {
	
	public static final String KEY_ATTACHMENT = "KEY_SESSIOIN_ATTACHMENT";

	private IoSession theOtherIoSession;
	private String sessionName;
	
	public void destroy() {
		this.setTheOtherIoSession(null);
		this.setSessionName(null);
	}
}



