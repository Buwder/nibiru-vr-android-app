package com.zskx.pemsystem.util;

import java.text.SimpleDateFormat;
import java.util.Date;


public class StringsUtil {
	public static final int Circle = 1;
	public static final int Single = 2;
	public static final int Sheffle = 3;
	public static final int Order = 4;
	
	public static final String  Music_Playing_list = "Music_Playing_list";
	public static final String  Music_Playing_position = "Music_Playing_position";
	
	
	//截取固定长度字符串
	public static String subString(String str,int length)
	{
		
		if(str.length()>length)
		{
			if(length-3>0)
			{
				str=str.substring(0, length-3)+"...";
			}
			
		}
		return str;
	}
	
	//格式化日期
	public static String formatDate(Date date)
	{
		if(date!=null)
		{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd");
		    return sdf.format(date);
		}else
		{
			return "";
		}
		
	}
	
}
