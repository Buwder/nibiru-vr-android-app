package com.zskx.net.request;

import com.zskx.net.response.user.User;

public class ChangePasswordRequest extends AbstractRequest<User> {

	public ChangePasswordRequest(GetResponseListener<User> listener,
			String sessionId, String newPassword, String insurePassword,
			String clientOS, String clientType) {
		super("changePassword", listener);
		addProperty("sessionId", sessionId);
		addProperty("newPassword", newPassword);
		addProperty("insurePassword", insurePassword);

		addProperty("clientOS", clientOS);
		addProperty("clientType", clientType);
	}

}
