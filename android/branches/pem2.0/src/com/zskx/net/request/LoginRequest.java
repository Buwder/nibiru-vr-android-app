package com.zskx.net.request;

import com.zskx.net.NetConfiguration;
import com.zskx.net.response.user.User;

/**
 * 404 用户不存在; 405 密码错误 ; 500 过期; 501 新建用户未开始; 200 成功;
 * 
 * @author demo
 * 
 */
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
