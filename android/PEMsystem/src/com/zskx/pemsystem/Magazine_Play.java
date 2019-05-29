package com.zskx.pemsystem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * 
 * @author guokai
 *
 */
public class Magazine_Play extends Activity {
	private static String TAG = "Magazine_Play";
	private String magazineUrl;

	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 // 在标题栏上显示进度  
        getWindow().requestFeature(Window.FEATURE_PROGRESS);  
		
        webView = new WebView(this);
        setContentView(webView);
        final Activity activity = this;  
        
		//setContentView(R.layout.magazine_play);
		Intent intent = getIntent();
		magazineUrl = intent.getStringExtra("magazineUrl");
		Log.i(TAG, magazineUrl);
	//	webView = (WebView) this.findViewById(R.id.magazine_play_webview);
		
		setWebView(activity);

	}
	
	
	private void setWebView(final Activity activity) {
		int window_width = getWindowManager().getDefaultDisplay().getWidth();
		
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setPluginsEnabled(true);
		webView.getSettings().setPluginState(PluginState.ON);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setSupportZoom(true); //支持内容缩放
		webView.getSettings().setBuiltInZoomControls(true); // 设置是否显示缩放按钮
		webView.getSettings().setUseWideViewPort(true); //无限缩放
		webView.setInitialScale(50); //初始缩放值
		webView.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
		webView.getSettings().setUseWideViewPort(true);
//		webView.addJavascriptInterface(new CallJava(), "CallJava");
		
		/** 
         * WebChromeClient类:用来辅助WebView处理JavaScript的对话框,网站图标,网站Title,加载进度等 
         * 通过setWebChromeClient调协WebChromeClient类 
         */  
		webView.setWebChromeClient(new WebChromeClient(){

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				activity.setProgress(newProgress * 100);
			}
		});  
		
		/**
         * WebViewClient类: 用来辅助WebView处理各种通知,请求等事件的类 
         * 通过setWebViewClient设置WebViewClient类 
         */  
		webView.setWebViewClient(new WebViewClient() {  
            // 页面加载失败  
            public void onReceivedError(WebView view, int errorCode,  
                    String description, String failingUrl) {  
                Toast.makeText(activity, "异常:! " + description,  
                        Toast.LENGTH_LONG).show();  
            }  
  
        }); 
		webView.loadUrl(magazineUrl);
	}
	
	/**
	 * 反射调用webView的私有方法
	 * @param methodName
	 */
	private void callWebViewMethod(String methodName){
		if(webView != null){
			try {
				/*Method method = webView.getClass().getDeclaredMethod(methodName, Void.class);
				method.setAccessible(true);
				method.invoke(webView,null);*/
			
				Method method=WebView.class.getMethod(methodName, Void.class);
    			method.invoke(webView, null);
				System.out.println("invoke::" + methodName);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void onResume() {
		/** * 设置为横屏 */
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		callWebViewMethod("onResume");
		webView.resumeTimers();  
		super.onResume();
	}

	@Override
	protected void onPause() {
		callWebViewMethod("onPause");
		webView.pauseTimers();  
		super.onPause();
	}


	@Override
	protected void onDestroy() {
		webView.destroy();
		super.onDestroy();
	}

}
