package com.zskx.pemsystem.util;

import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.content.Context;
import android.widget.Toast;

import com.zskx.net.response.Host;
import com.zskx.sax.NetIPSaxHandler;

public class NetUtil {

	/**
	 * 返回 IP地址列表
	 * @param context
	 * @return List<Host>
	 */
	public static List<Host> getHosts(Context context) {
		InputStream is = null;

		try {

			NetIPSaxHandler saxHandler = new NetIPSaxHandler();
			is = context.getAssets().open("IP_NET/ip_hosts.xml");

			SAXParserFactory factory = SAXParserFactory.newInstance();

			SAXParser parser = factory.newSAXParser();
			parser.parse(is, saxHandler);

			return saxHandler.getHosts();

		} catch (Exception e) {

			Toast.makeText(context, "网络地址解析失败", Toast.LENGTH_LONG).show();
		}

		return null;
	}

}
