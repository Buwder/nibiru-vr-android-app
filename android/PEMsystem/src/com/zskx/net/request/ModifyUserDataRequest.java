package com.zskx.net.request;

public class ModifyUserDataRequest extends AbstractRequest<String> {

	public ModifyUserDataRequest(GetResponseListener<String> listener,
			String sessionId, String userName, String sex, String userAge,
			String Marriage) {
		super("ModifyUserData", listener);
		addProperty("sessionId", sessionId);
		addProperty("userName", userName);
		addProperty("sex", sex);
		addProperty("userAge", userAge);
		addProperty("Marriage", Marriage);

	}

}
