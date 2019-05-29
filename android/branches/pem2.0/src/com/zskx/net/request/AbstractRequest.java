package com.zskx.net.request;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

import com.zskx.net.NetConfiguration;
import com.zskx.net.response.ResponseEntity;
import com.zskx.pemsystem.util.AppConfiguration;
import com.zskx.pemsystem.util.MyPost;

/**
 * 网络请求基类
 * 
 * @author demo
 * 
 * @param <T>
 */
public abstract class AbstractRequest<T> {

	private static String TAG = "AbstractRequest<T>";

	/** webservice请求 */
	private SoapObject request;
	/** 请求的集装箱 */
	private SoapSerializationEnvelope envelope;
	/** 访问服务器的数据返回监听器 */
	protected GetResponseListener<T> listener;
	/** 是否接受返回数据，用于取消请求 */
	private boolean isAccessData = true;
	/** 默认调试模式是关闭状态 */
	private boolean DEBUG = false;

	private int restTime = 1000;

	public boolean isAccessData() {
		return isAccessData;
	}

	public void setAccessData(boolean isAccessData) {
		this.isAccessData = isAccessData;
	}

	public void setDebug(boolean isDebug) {
		DEBUG = isDebug;
	}

	public boolean getDebug() {
		return DEBUG;
	}

	/**
	 * 设置服务器ip地址
	 * 
	 * @param ip
	 */
	public void setServerIp(String ip) {
		NetConfiguration.NAME_SPACE = "http://" + ip
				+ "/services/RemoteService";

		SoapObject so = new SoapObject(NetConfiguration.NAME_SPACE, name);

		for (String s : paramList) {
			so.addProperty(s, request.getProperty(s));
		}
		request = so;
		AppConfiguration.saveNet(NetConfiguration.NAME_SPACE);
	}

	private String name;

	public AbstractRequest(String s, GetResponseListener<T> listener) {
		System.out.println("getMethod:" + s);
		name = s;
		if(NetConfiguration.NAME_SPACE.equals("")){
			NetConfiguration.NAME_SPACE = AppConfiguration.getNet();
		}
		request = new SoapObject(NetConfiguration.NAME_SPACE, s);
		// 设定版本为VER.11
		envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		System.out.println("NAME_SPACE:" + NetConfiguration.NAME_SPACE);
		this.listener = listener;

	}

	/*
	 * public AbstractRequest(GetResponseListener<T> listener, int time) {
	 * this(listener); restTime = time; }
	 */

	private List<String> paramList = new ArrayList<String>();

	/**
	 * 添加请求属性
	 * 
	 * @param paramName
	 * @param paramValue
	 */
	protected void addProperty(String paramName, Object paramValue) {
		request.addProperty(paramName, paramValue);
		paramList.add(paramName);
	}

	/**
	 * 开启线程发送请求
	 */
	public final void sendByThread() {
		Thread thread = new Thread(new Runnable() {

			public void run() {
				send();
			}

		});
		thread.start();
	}

	/**
	 * 将请求发送到服务器并接受数据
	 */
	private final void send() {
		// 将请求放入集装箱
		envelope.bodyOut = request;
		HttpTransportSE tx = new HttpTransportSE(NetConfiguration.NAME_SPACE+"?wsdl",
				10000);
		// 开启调试模式，打印发送过程的详细信息
		tx.debug = true;
		String resultString;

		try {

			// 如果是测试模式则会延迟1秒作为网络访问的过程。
			if (!DEBUG) {
				System.out.println(getClass().getSimpleName()+"namespace:"+request.getNamespace()+"--->webservice:"+tx.getHost());
				
				System.out.println(getClass().getSimpleName()
						+ "----请求内容------" + envelope.bodyOut.toString());
				tx.call(null, envelope);
				Object result = envelope.getResponse();

				resultString = result.toString();
				System.out.println(getClass().getSimpleName() + "----返回数据-----"
						+ resultString);
			} else {
				try {
					Thread.sleep(restTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				resultString = "";
			}
			// 解析json数据
			filterJsonDataToBean(resultString);

		} catch (Exception e) {
			if (listener != null) {
				final ResponseEntity<T> response = new ResponseEntity<T>();
				response.setCode("001");
				response.setMsg("网络访问失败，请检测网络！");
				MyPost.post(new Runnable() {

					@Override
					public void run() {
						listener.onError(response);

					}
				});

			}
			e.printStackTrace();
		}
	}

	/**
	 * 取消请求
	 */
	public void cancel() {
		isAccessData = false;
	}

	/**
	 * 过滤json数据为实例
	 * 
	 * @param resultString
	 * @return
	 */
	public ResponseEntity<T> filterJsonDataToBean(String resultString)
			throws Exception {
		// 创建一个实体类用于装载数据
		final ResponseEntity<T> response = new ResponseEntity<T>();

		if (!DEBUG) {
			JSONObject json = new JSONObject(resultString);
			response.setCode(json.getString("code"));
			response.setMsg(json.getString("msg"));
			if (json.isNull("count")) {
				Log.e(TAG,
						"name=count is NULL ! we must get complete data ,so throw Exception!");
			}
			if (json.isNull("totalCount")) {
				Log.e(TAG,
						"name=totalCount is NULL ! we must get complete data ,so  throw Exception!");
			}
			if (json.isNull("content")) {
				Log.e(TAG,
						"name=content is NULL !  we must get complete data ,so  throw Exception!");
			}
			response.setCount(json.getInt("count"));
			response.setTotalCount(json.getInt("totalCount"));
			ArrayList<T> list = new ArrayList<T>();
			JSONArray jsonArray = json.getJSONArray("content");

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonTemp = (JSONObject) jsonArray.opt(i);
				T t = createTInstance(this.getClass());
				fillT(t, jsonTemp);
				list.add(t);
			}
			response.setContent(list);

		}
		if (isAccessData && listener != null) {
			if ("200".equals(response.getCode()) || DEBUG) {
				MyPost.post(new Runnable() {

					@Override
					public void run() {
						listener.onSuccess(response);

					}
				});

			} else {
				MyPost.post(new Runnable() {

					@Override
					public void run() {
						listener.onError(response);

					}
				});

			}
		}

		return response;
	}

	/**
	 * 通过子类得到子类的实例
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected T createTInstance(Class<?> clazz) throws Exception {
		Class<T> result = null;
		T newT = null;

		result = (Class<T>) ((ParameterizedType) clazz.getGenericSuperclass())
				.getActualTypeArguments()[0];
		newT = (T) result.newInstance();

		return newT;
	}

	/**
	 * 填充生成的bean
	 * 
	 * @param <T>
	 * @param t
	 * @param json
	 * @return
	 */
	@SuppressWarnings("hiding")
	private <T> T fillT(T t, JSONObject json) throws Exception {
		Class<? extends Object> result = t.getClass();

		Field[] fields = result.getDeclaredFields();
		for (Field field : fields) {
			if (Modifier.isFinal(field.getModifiers())
					&& Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			field.setAccessible(true);
			String name = field.getName();
			Class<?> type = field.getType();
			if (json.isNull(name)) {
				Log.e(TAG,
						"name="
								+ name
								+ " is NULL ! we must get complete data ,so  throw Exception!");
			}
			if (type.equals(String.class)) {
				field.set(t, json.getString(name));
			} else if (type.equals(int.class) || type.equals(Integer.class)) {
				field.setInt(t, json.getInt(name));
			} else if (type.equals(Date.class)) {
				SimpleDateFormat format = new SimpleDateFormat();
				format.applyPattern("yy-MM-dd hh:mm:ss");
				Date date = format.parse(json.getString(name));
				field.set(t, date);
			} else if (type.equals(ArrayList.class)) {
				List<String> l = new ArrayList<String>();
				JSONArray jsonArrayTemp = json.getJSONArray(name);
				for (int i = 0; i < jsonArrayTemp.length(); i++) {
					l.add(jsonArrayTemp.opt(i).toString());
				}
				field.set(t, l);
			}
		}

		return t;
	}

	/**
	 * 网络获取数据监听器
	 * 
	 * @author demo
	 * 
	 * @param <T>
	 */
	public static interface GetResponseListener<T> {

		/**
		 * 成功返回数据
		 * 
		 * @param result
		 */
		public void onSuccess(ResponseEntity<T> result);

		/**
		 * 获取数据失败
		 * 
		 * @param code
		 * @param msg
		 */
		public void onError(ResponseEntity<T> result);
	}
}
