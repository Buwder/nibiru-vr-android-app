package com.nibiru.studio.zskx;

import android.content.Intent;
import android.util.Log;

import com.nibiru.service.NibiruKeyEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import x.core.listener.IXActorEventListener;
import x.core.ui.XActor;
import x.core.ui.XBaseScene;
import x.core.ui.XImage;
import x.core.ui.XImageText;
import x.core.ui.XLabel.XAlign;
import x.core.ui.XLabel.XArrangementMode;
import x.core.util.NibiruStudioUtils;

public class MxList extends XBaseScene implements IXActorEventListener{
    private XImage xImage1;
    XImageText mImageText,mImageText1,mImageText2;

    @Override
    public void onCreate() {
        Update();
        init();
        AutoFun();
    }

    @Override
    public void onResume() {
        resetHeadTracker();
        init();
    }

    @Override
    public void onPause() {
        finish();
    }

    @Override
    public void onDestroy() {
        //finishAll();
    }

    public void init() {

        //初始化来自assets路径下的图片，支持PNG/JPG格式
//        xImage1 = new XImage("ic_image_focused.png");

        xImage1 = new XImage("icon/logo.png");

        xImage1.setCenterPosition(0f, 1.8f, -4f);
        xImage1.setSize(2f, 0.4f);
        xImage1.setRenderOrder(6);

        xImage1.setEnableGazeAnimation(true);
        //设置控件朝向眼睛
        //xImage1.towardOrigin();
        addActor(xImage1);
//        xImage1.setRotation(0, 0, -90);


        //设置选中和未选中图片，图片来自于assets
        mImageText= new XImageText("meunbg/1.png", "meunbg/1.png");


        mImageText.setCenterPosition(-2f, 0f, -4f);
        mImageText.setSize(1.5F, 1.5F);
        mImageText.setSizeOfImage(1.5f, 1.5f);
        mImageText.setRenderOrder(7);

        //支持设置选中/选中文本的资源ID，便于国际化
        mImageText.setTitle("心理治疗系统", "心理治疗系统");
        mImageText.setSelectedArrangementMode(XArrangementMode.SingleRowMove);

        //设置未选中时左对齐，超出部分显示省略号
        mImageText.setUnselectedArrangementMode(XArrangementMode.SingleRowClip);
        mImageText.setUnselectedAlign(XAlign.Center);

        mImageText.setSizeOfTitle(1.5f, 0.2f);
        mImageText.setTitlePosition(0, -0.55f);

        mImageText.setEventListener(new IXActorEventListener() {
            @Override
            public void onGazeEnter(XActor actor) { }
            @Override
            public void onGazeExit(XActor actor) { }
            @Override
            public boolean onGazeTrigger(XActor actor) {

                Intent intent = new Intent( getApplicationContext(), GridViewVideo.class);
                intent.putExtra("param", 1);
                //打开带有返回值的Scene，参数为intent和requestCode
                startSceneForResult(intent, 600);

                return false;
            }

            @Override
            public void onAnimation(XActor actor, boolean isSelected) {
                //mMaskLayer.setEnabled(true);
            }
        });

        addActor(mImageText);



        //设置选中和未选中图片，图片来自于assets
        mImageText1= new XImageText("meunbg/2.png", "meunbg/2.png");


        mImageText1.setCenterPosition(0f, 0f, -4f);
        mImageText1.setSize(1.5F, 1.5F);
        mImageText1.setSizeOfImage(1.5f, 1.5f);
        mImageText1.setRenderOrder(7);



        //支持设置选中/选中文本的资源ID，便于国际化
        mImageText1.setTitle("音乐治疗系统", "音乐治疗系统");

        //设置选中时开启跑马灯
        mImageText1.setSelectedArrangementMode(XArrangementMode.SingleRowMove);

        //设置未选中时左对齐，超出部分显示省略号
        mImageText1.setUnselectedArrangementMode(XArrangementMode.SingleRowClip);
        mImageText1.setUnselectedAlign(XAlign.Center);

        mImageText1.setSizeOfTitle(2.5f, 0.2f);
        mImageText1.setTitlePosition(0, -0.55f);

        mImageText1.setEventListener(new IXActorEventListener() {
            @Override
            public void onGazeEnter(XActor actor) { }
            @Override
            public void onGazeExit(XActor actor) { }
            @Override
            public boolean onGazeTrigger(XActor actor) {

                Intent intent = new Intent( getApplicationContext(), GirdViewMusic.class);
                intent.putExtra("param", 2);
                //打开带有返回值的Scene，参数为intent和requestCode
                startSceneForResult(intent, 600);

                return false;
            }

            @Override
            public void onAnimation(XActor actor, boolean isSelected) {
                //mMaskLayer.setEnabled(true);
            }
        });

        addActor(mImageText1);


        //设置选中和未选中图片，图片来自于assets
        mImageText2= new XImageText("meunbg/3.png", "meunbg/3.png");

        mImageText2.setCenterPosition(2f, 0f, -4f);
        mImageText2.setSize(1.5F, 1.5F);
        mImageText2.setSizeOfImage(1.5f, 1.5f);
        mImageText2.setRenderOrder(7);

        //支持设置选中/选中文本的资源ID，便于国际化
        mImageText2.setTitle("脑功能评估系统", "脑功能评估系统");

        mImageText2.setSelectedArrangementMode(XArrangementMode.SingleRowMove);

        //设置未选中时左对齐，超出部分显示省略号
        mImageText2.setUnselectedArrangementMode(XArrangementMode.SingleRowClip);
        mImageText2.setUnselectedAlign(XAlign.Center);

        mImageText2.setSizeOfTitle(1.5f, 0.2f);
        mImageText2.setTitlePosition(0, -0.55f);

        mImageText2.setEventListener(new IXActorEventListener() {
            @Override
            public void onGazeEnter(XActor actor) { }
            @Override
            public void onGazeExit(XActor actor) { }
            @Override
            public boolean onGazeTrigger(XActor actor) {

//                try {
//                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//                    if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
//                        Toast.makeText(getApplicationContext(),"请打开蓝牙！",Toast.LENGTH_SHORT).show();
//                        return false;
//                    }
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                    Log.i(TAG, "error:" + e.getMessage());
//                }
//                PackageManager packageManager = getPackageManager();
//                Intent intent = packageManager.getLaunchIntentForPackage("com.zskx.naobo");
//                startActivity(intent);

                return false;
            }

            @Override
            public void onAnimation(XActor actor, boolean isSelected) {
                //mMaskLayer.setEnabled(true);
            }
        });;

        addActor(mImageText2);
        //lockTrackerToFront();
        resetHeadTracker();
    }

    @Override
    public boolean onGazeTrigger(XActor actor) {
        //获取Utils实例
        NibiruStudioUtils utils = getNibiruStudioUtils();

       if( actor == mImageText ){

           Intent intent = new Intent( this, GridViewVideo.class);
           intent.putExtra("param", "1");

           //打开带有返回值的Scene，参数为intent和requestCode
           startSceneForResult(intent, 600);
        }
        if( actor == mImageText1 ){

            Intent intent = new Intent( this, GirdViewMusic.class);
            intent.putExtra("param", "2");

            //打开带有返回值的Scene，参数为intent和requestCode
            startSceneForResult(intent, 600);
        }
        if( actor == mImageText2 ){
//            try {
//                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//                if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
//                    Toast.makeText(
//                            this,
//                            "请打开蓝牙！",
//                            Toast.LENGTH_SHORT).show();
////                   finish();
//                }
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//                Log.i(TAG, "error:" + e.getMessage());
//            }
//            PackageManager packageManager = getPackageManager();
//            Intent intent = packageManager.getLaunchIntentForPackage("com.zskx.naobo");
//            startActivity(intent);
            return  false;
        }
        return false;
    }

    public long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode) {
        if(keyCode == 61){
            if((System.currentTimeMillis() - exitTime) > 1000){
                exitTime = System.currentTimeMillis();
            }else{
                Intent intent = new Intent( getApplicationContext(), GridViewVideo.class);
                intent.putExtra("param", "video");
                //打开带有返回值的Scene，参数为intent和requestCode
                startSceneForResult(intent, 600);
            }
        }else if( keyCode == NibiruKeyEvent.KEYCODE_BACK ) {
           Intent intent = new Intent(this, MxList.class);
           //打开带有返回值的Scene，参数为intent和requestCode
           startSceneForResult(intent, 600);
       }else if(keyCode == 65){//180
           Intent intent = new Intent( getApplicationContext(), GirdViewMusic.class);
           intent.putExtra("param", "music");
           //打开带有返回值的Scene，参数为intent和requestCode
           startSceneForResult(intent, 600);
       }else if(keyCode == 210){
//           try {
//               mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//               if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
//                   Toast.makeText(
//                           this,
//                           "请打开蓝牙！",
//                           Toast.LENGTH_SHORT).show();
////                   finish();
//               }
//           } catch (Exception e) {
//               // TODO Auto-generated catch block
//               e.printStackTrace();
//               Log.i(TAG, "error:" + e.getMessage());
//           }
//           PackageManager packageManager = getPackageManager();
//           Intent intent = packageManager.getLaunchIntentForPackage("com.zskx.naobo");
//           startActivity(intent);
            return  false;

       }else if(keyCode == 62){
           //复位
           resetHeadTracker();
       }
       return true;
    }
    @Override
    public void onGazeEnter(XActor actor) {

//        //当选中点移入时显示半透明层
//        if( actor == mImageText ){
//            mMaskLayer.setEnabled(false);
//        }
//        if( actor == mImageText1 ){
//            mMaskLayer1.setEnabled(false);
//        }
//        if( actor == mImageText2 ){
//            mMaskLayer2.setEnabled(false);
//        }
    }

    @Override
    public void onGazeExit(XActor actor) {

//        //当选中点移出时隐藏半透明层
//        if( actor == mImageText ){
//            mMaskLayer.setEnabled(false);
//        }
//        if( actor == mImageText1 ){
//            mMaskLayer1.setEnabled(false);
//        }
//        if( actor == mImageText2 ){
//            mMaskLayer2.setEnabled(false);
//        }

    }

    private String sendRequestWithHttpURLConnection(int vision) throws JSONException {
        //开启线程来发起网络请求

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String line = null;
        String result = "";
        String local = getFileFromSD("/sdcard/Android/data/config.json");
        JSONObject jsonObject = new JSONObject(local);
        try {
            URL url = new URL(jsonObject.getString("mxUrl")+vision);
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
    public void Update(){
        int vision = Tools.getVersion(this);
        try {
            String res = sendRequestWithHttpURLConnection(vision);
            String code = null;
            JSONObject jsonObject = new JSONObject(res);
            code = jsonObject.getString("code");
            if(code.equals("0")){
                //创建 UpdataAppManger更新版本
                UpdataAppManger  updatamanger = new UpdataAppManger(this);
                updatamanger.downloadAPK(jsonObject.getString("url"), "冥想放松训练系统");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAnimation(XActor actor, boolean isSelected) {

    }
    private String getFileFromSD(String path) {
        String result = "";

        try {
            FileInputStream f = new FileInputStream(path);
            BufferedReader bis = new BufferedReader(new InputStreamReader(f));
            String line = "";
            while ((line = bis.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }
    //开启线程来发起网络请求请求视频播放状态
    public static String sendRequestWithHttpURLConnection() throws JSONException {
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

    public void AutoFun(){
        final long timeInterval = 1000;
        Runnable runnable = new Runnable() {

            public void run() {
                while (true) {
                    try {
                        String date = sendRequestWithHttpURLConnection();
                        play(date);
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
    public void play(String date) throws JSONException {
        JSONObject jsonObject = new JSONObject(date);
        //NO视频编号 status 0播放 1暂停 page 0播放页 1返回
        String page = jsonObject.getString("page");
        String NO = jsonObject.getString("NO");
        String status = jsonObject.getString("status");
        if(page.equals("0")){
            if(status.equals("0")){
                Intent intent = new Intent(getApplicationContext(), SubSceneVideoAll2.class);
                intent.putExtra("param", NO);
                //打开带有返回值的Scene，参数为intent和requestCode
                startSceneForResult(intent, 600);
            }
        }else{
            Intent intent = new Intent(getApplicationContext(), MxList.class);
            //打开带有返回值的Scene，参数为intent和requestCode
            this.startSceneForResult(intent, 600);
            return;
        }
    }
}
