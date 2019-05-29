package com.zskx.net.request;

public class LogOutRequest extends AbstractRequest<String> {

	public LogOutRequest(GetResponseListener<String> listener, String sessionId) {
		super("LoginOut", listener);
		addProperty("sessionId", sessionId);
	}

}
