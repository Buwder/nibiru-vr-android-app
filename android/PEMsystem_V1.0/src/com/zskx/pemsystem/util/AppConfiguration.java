package com.zskx.pemsystem.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import android.content.Context;

import com.zskx.net.response.user.User;

public  class AppConfiguration {
	
	//public static User user;
	private final static String USR="usr";
	
	/**
	 * 保存 usr
	 * @param context
	 * @param user
	 */
	public static void saveUser(Context context,User user)
	{
		FileOutputStream fos;
		ObjectOutputStream bos;
		try {
			fos=context.openFileOutput(USR,Context.MODE_PRIVATE);
			bos=new ObjectOutputStream(fos);
			bos.writeObject(user);
			
			bos.flush();
			fos.flush();
			
			bos.close();
			fos.close();
			
		} catch (FileNotFoundException e) {
		
			
			e.printStackTrace();
		}catch (IOException e) {
		
			
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 返回 usr
	 * @param context
	 * @return
	 */
	public static User getUser(Context context)
	{
		FileInputStream fis;
		ObjectInputStream bis;
		try {
			 fis=context.openFileInput(USR);
			 bis=new ObjectInputStream(fis);
			 User user=(User) bis.readObject();
			 
			 bis.close();
			 fis.close();
			 
			 return user;
			 
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			return null;
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}
	

}
