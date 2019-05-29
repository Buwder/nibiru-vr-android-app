package com.zskx.pemsystem.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import android.content.Context;

import com.zskx.net.response.HospitalVO;
import com.zskx.net.response.user.User;
import com.zskx.pemsystem.BMapApiDemoApp;

public class AppConfiguration {

	// public static User user;
	private final static String USR = "usr";

	private final static String NetNameSpace = "net";
	private final static String HOSPITAL = "hospital";

	/**
	 * 保存 usr
	 * 
	 * @param context
	 * @param user
	 */
	public static void saveUser(Context context, User user) {
		FileOutputStream fos;
		ObjectOutputStream bos;
		try {
			fos = context.openFileOutput(USR, Context.MODE_PRIVATE);
			bos = new ObjectOutputStream(fos);
			bos.writeObject(user);

			bos.flush();
			fos.flush();

			bos.close();
			fos.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	/**
	 * 保存 网络配置
	 * 
	 * @param context
	 * @param user
	 */
	public static void saveNet( String net) {
		FileOutputStream fos;
		ObjectOutputStream bos;
		try {
			fos = BMapApiDemoApp.getInstance().openFileOutput(NetNameSpace, Context.MODE_PRIVATE);
			bos = new ObjectOutputStream(fos);
			bos.writeObject(net);

			bos.flush();
			fos.flush();

			bos.close();
			fos.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	/**
	 * 返回 网络地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getNet() {
		FileInputStream fis;
		ObjectInputStream bis;

		try {
			fis = BMapApiDemoApp.getInstance().openFileInput(NetNameSpace);
			bis = new ObjectInputStream(fis);
			String net = (String) bis.readObject();

			bis.close();
			fis.close();

			return net;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * 返回 usr
	 * 
	 * @param context
	 * @return
	 */
	public static User getUser(Context context) {
		FileInputStream fis;
		ObjectInputStream bis;
		try {
			fis = context.openFileInput(USR);
			bis = new ObjectInputStream(fis);
			User user = (User) bis.readObject();

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
	
	/**
	 * 保存 HospitalList
	 * 
	 * @param context
	 * @param HospitalList
	 */
	public static void saveHospitalList(Context context, User user) {
		FileOutputStream fos;
		ObjectOutputStream bos;
		try {
			fos = context.openFileOutput(HOSPITAL, Context.MODE_PRIVATE);
			bos = new ObjectOutputStream(fos);
			bos.writeObject(user);

			bos.flush();
			fos.flush();

			bos.close();
			fos.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	
	
	/**
	 * 返回 HospitalList
	 * 
	 * @param context
	 * @return
	 */
	public static ArrayList<HospitalVO> getHospitalList(Context context) {
		FileInputStream fis;
		ObjectInputStream bis;
		try {
			fis = context.openFileInput(HOSPITAL);
			bis = new ObjectInputStream(fis);
			ArrayList<HospitalVO> user = (ArrayList<HospitalVO>) bis.readObject();

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
