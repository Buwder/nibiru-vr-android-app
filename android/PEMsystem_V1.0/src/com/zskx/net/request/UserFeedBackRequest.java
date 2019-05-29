package com.zskx.net.request;

public class UserFeedBackRequest extends AbstractRequest<String> {

	public UserFeedBackRequest(GetResponseListener<String> listener,
			String title, String content) {
		super("userFeedBack", listener);

		addProperty("title", title);
		addProperty("content", content);
	}

}
