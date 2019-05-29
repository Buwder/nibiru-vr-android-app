package com.zskx.net.request;

import com.zskx.net.response.user.User;

public class ChangeUserInformationRequest extends AbstractRequest<User> {

	public ChangeUserInformationRequest(GetResponseListener<User> listener,
			String sessionId, String userName, String sex, String userAge,
			String moneyLevel, String clientOS, String clientType) {
		super("changeUserInformation", listener);
		addProperty("sessionId", sessionId);
		addProperty("userName", userName);
		addProperty("sex", sex);
		addProperty("userAge", userAge);
		addProperty("moneyLevel", moneyLevel);
		addProperty("clientOS", clientOS);
		addProperty("clientType", clientType);

	}

}
