package com.zskx.sax;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.zskx.net.response.Host;


/**
 * 解析IP地址列表
 * @author wqp
 *
 */

public class NetIPSaxHandler extends DefaultHandler{

	private List<Host> hostArray=null;
	
	private Host host;
	@Override
	public void startDocument() throws SAXException {
		
		hostArray=new ArrayList<Host>();
	
	}

	

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		String city;
		String ip;
		
		if(Host.TAG.equals(localName))
		{
			city=attributes.getValue(Host.CITY);
			ip=attributes.getValue(Host.IP);
			host=new Host(ip, city);
		}
		
		
	
		

	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		
		if(Host.TAG.equals(localName))
		{
			hostArray.add(host);
		}
		
		
	}
	
	@Override
	public void endDocument() throws SAXException {
	
		host=null;
	}
	
	
	public List<Host> getHosts()
	{
		return hostArray;
	}

	
	
	

	
	
}

