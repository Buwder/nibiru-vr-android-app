package com.nibiru.studio.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nibiru.studio.zskx.MxList;
import com.nibiru.studio.zskx.SubSceneVideoAll2;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import x.core.ui.XBaseScene;
import x.core.util.NibiruStudioUtils;

public class autoPlay extends XBaseScene {

    @Override
    public void onCreate() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }

    public static String sendRequestWithHttpURLConnection() throws JSONException {
        //开启线程来发起网络请求

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String line = null;
        String result = "";
        try {
            URL url = new URL("http://192.168.0.164:3000/getVideoData");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(60000);
            connection.setReadTimeout(60000);
            InputStream in = connection.getInputStream();
            //下面对获取到的输入流进行读取
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null){
                if(line != "null") {
                    result += line;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null){
                connection.disconnect();
            }
        }
        return result;
    }

    public void main(final Context applicationContext){
        final autoPlay auto = new autoPlay();
        final long timeInterval = 1000;
        Runnable runnable = new Runnable() {
            private Object Context = applicationContext;

            public void run() {
                while (true) {
                    try {
                        String date = sendRequestWithHttpURLConnection();
                        auto.play(date,Context);
                        Thread.sleep(timeInterval);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }catch (JSONException e){
                        Log.e("Exception when run main",e.toString());
                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public void play(String date, Object context) throws JSONException {
        JSONObject jsonObject = new JSONObject(date);
        //NO视频编号 status 0播放 1暂停 page 0播放页 1返回
        String page = jsonObject.getString("page");
        String NO = jsonObject.getString("NO");
        String status = jsonObject.getString("status");
        if(page.equals("0")){
            if(status.equals("0")){
                Intent intent = new Intent((Context) context, SubSceneVideoAll2.class);
                intent.putExtra("param", NO);
                //打开带有返回值的Scene，参数为intent和requestCode
                startSceneForResult(intent, 600);
            }
        }else{
            Intent intent = new Intent((Context) context, MxList.class);

            //打开带有返回值的Scene，参数为intent和requestCode
            this.startSceneForResult(intent, 600);
            return;
        }
    }
}
