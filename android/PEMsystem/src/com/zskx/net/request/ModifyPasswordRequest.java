package com.zskx.net.request;

public class ModifyPasswordRequest extends AbstractRequest<String> {

	public ModifyPasswordRequest(GetResponseListener<String> listener,
			String sessionId, String nowPwd, String newPwd) {
		super("ModifyPassword", listener);
		addProperty("sessionId", sessionId);
		addProperty("nowPwd", nowPwd);
		addProperty("newPwd", newPwd);
	}

}
