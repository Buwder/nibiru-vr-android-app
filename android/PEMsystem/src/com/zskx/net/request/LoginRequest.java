package com.zskx.net.request;

import com.zskx.net.NetConfiguration;
import com.zskx.net.response.user.User;

public class LoginRequest extends AbstractRequest<User> {

	protected static String Method = "login";

	public LoginRequest(GetResponseListener<User> listener, String loginName,
			String userPwd) {
		super(Method, listener);
		addProperty("loginName", loginName);
		addProperty("password", userPwd);
		addProperty("clientOS_type", "android_phone");
		addProperty("clientOS", NetConfiguration.CLIENT_OS);
		setDebug(false);
	}

}
