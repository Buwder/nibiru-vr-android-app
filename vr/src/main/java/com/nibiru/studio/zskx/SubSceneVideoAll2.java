package com.nibiru.studio.zskx;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Surface;

import com.nibiru.service.NibiruKeyEvent;

import java.io.File;
import java.io.IOException;

import x.core.ui.XBaseScene;
import x.core.ui.XSpaceRect;
import x.core.ui.XToast;
import x.core.ui.surface.XSurfaceArea;
import x.core.ui.surface.XSurfaceConfig;
import x.core.util.MediaPlayerDecodeOpt;
import x.core.util.XLog;
import x.core.util.XVec3;
import android.media.AudioManager;
import android.app.Service;

/**
 * 演示视频播放功能实现
 * 包括2D/3D模式，平面/180/360/球幕等视频类型，示例中使用Android MediaPlayer作为解码框架，开发者可根据需要决定解码框架
 * @author Steven
 */

public class SubSceneVideoAll2 extends XBaseScene {

    private static final String TAG = SubSceneVideoAll2.class.getSimpleName();

    public static final String PLAY_MODE = "PLAY_MODE";

    String[] mModeArray = {"2DNormal", "2D180", "2D360", "2DSphere", "3DNormal", "3D180", "3D360", "3DSphere"};

    public static final int NORMAL_2D   = 0;
    public static final int HALF_2D     = 1;
    public static final int CIRCLE_2D   = 2;
    public static final int SPHERE_2D   = 3;
    public static final int NORMAL_3D   = 4;
    public static final int HALF_3D     = 5;
    public static final int CIRCLE_3D   = 6;
    public static final int SPHERE_3D   = 7;

    public int mCurrentPlayMode = NORMAL_2D;
    private AudioManager audioManager = null; // Audio管理器


    MediaPlayer mPlayer;
    XSurfaceArea m_VideoArea;

    boolean isNeedOpt = false;

    String videoPath = null;
    boolean hasPrepared = false;


    @Override
    public void onCreate() {
        //获取由SubSceneVideoPlayer传入的播放模式的参数
        loadExtra();
        disableGlobalSkybox();
        //初始化SurfaceArea和MediaPlayer
        initPlayWork();
        Intent intent = getIntent();
        String p = intent.getStringExtra("param");
        init(p);
    }

    private void loadExtra() {
        Intent intent = getIntent();
        if (intent != null) {
            int playMode = intent.getIntExtra(PLAY_MODE, NORMAL_2D);
            setPlayMode(playMode);
        }else{
            setPlayMode(NORMAL_2D);
        }
    }
    /**
     * 初始化播放器、音量数据等相关工作
     */
    private void initPlayWork() {
        audioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
    }
    public void setPlayMode(int mode) {
        mCurrentPlayMode = mode;
    }

    //根据播放模式获取初始化参数
    public XSurfaceArea.Parameters getVideoParameters(int mode){
        //设置SurfaceArea的中心点坐标
        XVec3 center = new XVec3(0f, 0f, -4f);

        //设置SurfaceArea的空间朝向，可选
        XSpaceRect pose = new XSpaceRect();
        pose.setFront(0, 0, 1);
        pose.setUp(0, 1, 0);

        //设置全景模式下的SurfaceArea的中心点坐标，
        XVec3 center1 = new XVec3(0f, 0f, 0f);

        isNeedOpt = false;
        mode = NORMAL_2D;
        switch(mode){
            case NORMAL_2D:{
                return new XSurfaceArea.Builder(this, XSurfaceConfig.Mode.MODE_2D, XSurfaceConfig.Format.TYPE_PLANE)
                        .setCenter(center)
                        //设置宽度
                        .setWidth(8)
                        //设置姿态朝向，可选
                        // .setPosition(pose)
                        //设置是否支持选中，默认关闭
                        .setEnableSelection(true)
                        //直接构造参数实体对象
                        .buildParameters();

            }
            case HALF_2D:{
                isNeedOpt = true;
                return new XSurfaceArea.Builder(this, XSurfaceConfig.Mode.MODE_2D, XSurfaceConfig.Format.TYPE_SEMI_PANORAMA)
                        //设置球心的中心点，与平面模式不同
                        .setCenter(center1)
                        //设置球半径
                        .setRadius(5)
                        //设置球面网格，数字越大会越精细，但是性能开销也越大
                        .setSphereMesh(100, 50)
                        .buildParameters();

            }
            case CIRCLE_2D:{
                isNeedOpt = true;
                return new XSurfaceArea.Builder(this, XSurfaceConfig.Mode.MODE_2D, XSurfaceConfig.Format.TYPE_PANORAMA)
                        //设置球心坐标和半径
                        .setCenter(center1)
                        .setRadius(5)
                        //设置球面网格，数字越大会越精细，但是性能开销也越大
                        .setSphereMesh(50, 30)
                        .buildParameters();

            }
            case SPHERE_2D:{
                isNeedOpt = true;
                return new XSurfaceArea.Builder(this, XSurfaceConfig.Mode.MODE_2D, XSurfaceConfig.Format.TYPE_SPHERE)
                        .setCenter(center1)
                        .setRadius(5)
                        //设置球幕的显示角度范围，这里设置垂直方向-45度到+45度，水平方向-90度到+90度
                        .setSphereArea( -45, 45, -90, 90 )
                        .buildParameters();

                /*
                如果视频是在经纬度覆盖范围都超过90度，例如从北极点朝下拍摄或者朝正前方拍摄，覆盖超过90度的范围，则采用下面的API
                 */

//                return new XSurfaceArea.Builder(this, XSurfaceConfig.Mode.MODE_2D, XSurfaceConfig.Format.TYPE_SPHERE)
//                            .setCenter(center1)
//                            .setRadius(5)
//                            //覆盖角度范围，这里用110度
//                            //The overlapping angle range, here use 110 degree
//                            .setSubSphereAngle(110.0f)
//                            //是否为正前方开始覆盖
//                            //Whether to overlap the front
//                            .setSubSphereFront(true)
//                        //设置球面网格，数字越大会越精细，但是性能开销也越大
//                        //Set sphere mesh, the larger and more accurate the number is , the greater consumption the performance costs
//                        .setSphereMesh(50, 25)
//                            .buildParameters();



                //如果拍摄原点不在顶部或者正前方，可以调用旋转接口
//                    m_VideoArea.rotateBy(-90f, 0, 0);

            }
            case NORMAL_3D:{
                return new XSurfaceArea.Builder(this, XSurfaceConfig.Mode.MODE_3D, XSurfaceConfig.Format.TYPE_PLANE)
                        .setCenter(center)
                        .setWidth(8)
                        .setPosition(pose)
                        //设置是否支持选中，默认关闭
                        //Set whether to support selection, it's disabled by default
                        .setEnableSelection(true)
                        //设置为左右布局的3D视频，如果是上下布局的3D视频，设置为LAYOUT_VERTICAL
                        //Set as left-right 3D video, if it's top-bottom 3D video, set as LAYOUT_VERTICAL
                        .setVideoLayout(XSurfaceConfig.Layout.LAYOUT_HORIZONTAL)
                        .buildParameters();

            }
            case HALF_3D:{
                isNeedOpt = true;
                return new XSurfaceArea.Builder(this, XSurfaceConfig.Mode.MODE_3D, XSurfaceConfig.Format.TYPE_SEMI_PANORAMA)
                        //设置球心和半径
                        //Set sphere center and radius
                        .setCenter(center1)
                        .setRadius(5)
                        //设置为左右布局的3D视频，如果是上下布局的3D视频，设置为LAYOUT_VERTICAL
                        //Set as left-right 3D video,, if it's top-bottom 3D video, set as LAYOUT_VERTICAL
                        .setVideoLayout(XSurfaceConfig.Layout.LAYOUT_HORIZONTAL)
                        .buildParameters();

            }
            case CIRCLE_3D:{
                isNeedOpt = true;
                return new XSurfaceArea.Builder(this, XSurfaceConfig.Mode.MODE_3D, XSurfaceConfig.Format.TYPE_PANORAMA)
                        .setCenter(center1)
                        .setRadius(5)
                        //设置为上下布局的3D视频，如果是左右布局的3D视频，设置为LAYOUT_HORIZONTAL
                        .setVideoLayout(XSurfaceConfig.Layout.LAYOUT_VERTICAL)
                        .buildParameters();

            }
            case SPHERE_3D:{
                isNeedOpt = true;
                return new XSurfaceArea.Builder(this, XSurfaceConfig.Mode.MODE_3D, XSurfaceConfig.Format.TYPE_SPHERE)
                        .setCenter(center1)
                        .setRadius(5)
                        //设置球幕的显示角度范围，这里设置垂直方向-45度到+45度，水平方向-90度到+90度
                        //Set the angle degree range of displaying the dome, vertical: -45 degree to +45 degree, horizontal: -90 degree to +90 degree
                        .setSphereArea( -45, 45, -90, 90 )
                        //设置为上下布局的3D视频，如果是左右布局的3D视频，设置为LAYOUT_HORIZONTAL
                        //Set as top-bottom 3D video, if it's left-right 3D video, set as LAYOUT_HORIZONTAL
                        .setVideoLayout(XSurfaceConfig.Layout.LAYOUT_VERTICAL)
                        .buildParameters();

            }
            default:
                return null;
        }

    }

    public void init(String num) {

        //通过scene对象和视频参数初始化XSurfaceArea
        m_VideoArea = new XSurfaceArea.Builder(this, getVideoParameters(mCurrentPlayMode)).build();

        //默认开启自动隐藏天空盒模式，也就是PLANE平面模式下显示天空盒，而其他模式都隐藏天空盒，如果不需要此功能可关闭
        m_VideoArea.setAutoHideSkybox(true);


        if( mCurrentPlayMode < 0 || mCurrentPlayMode >= mModeArray.length ){
            return;
        }

        videoPath = "/sdcard/Android/data/.com.nibiru.video/"+num;
        hasPrepared = false;

        if( m_VideoArea != null && videoPath != null ) {

            //如果文件不存在弹出Toast提示
            if( !new File(videoPath).exists() ){
                XToast.makeToast(this, "视频路径未找到: "+videoPath, 5000).show(true);
                return;
            }

            //添加SurfaceArea控件
            addActor(m_VideoArea);

            //获取SurfaceArea的纹理
            Surface surface = new Surface(m_VideoArea.getSurfaceTexture());

            //初始化Android Media Player
            mPlayer = new MediaPlayer();

            //重要！设置MediaPlayer显示的纹理，这里的纹理必须来自于SurfaceArea，否则将无法显示视频内容
            mPlayer.setSurface(surface);

            //设置视频尺寸监听，更新尺寸后会自适应画面大小
            mPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                @Override
                public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height) {
                    if( m_VideoArea != null ){
                        m_VideoArea.notifySurfaceSizeChanged(width, height);
                    }
                }
            });
            lockTrackerToFront();
            //在某些平台上对全景视频有专门优化，设置需要开启视频解码优化，平面模式无需开启
            if( isNeedOpt ){
                MediaPlayerDecodeOpt.startVideoDecodeOpt(mPlayer);
            }
            //使用MediaPlayer播放视频，具体可参考Android Media Player的用法，这里仅做示例
            try {
                //videoPath = videoPath+".mp4";
                mPlayer.setDataSource(videoPath);

                mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        hasPrepared = true;
//                        if( isRunning() ) {
//                            //准备完成后开始播放
//                            XLog.logInfo("================ prepare finished, start play");
                              startPlay();
                        //}
                    }
                });
                //隐藏选中点
                hideGaze();
                //异步执行准备
                mPlayer.prepareAsync();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //showCurrentPlayMode();
    }

    //开始播放
    void startPlay(){
        if (mPlayer != null && hasPrepared ) {
            if( isNeedOpt ){
                MediaPlayerDecodeOpt.startVideoDecodeOpt(mPlayer);
            }

            //开启视频播放模式，开启后Studio将根据视频帧率进行渲染帧率调节，节约功耗
            enableVideoMode();

            //为显示效果，可隐藏选中点
            hideGaze();

            //开始播放
            mPlayer.start();
        }
    }
    //获取视频播放位置并存储到缓存里面
    void posPlayer(String num){
        if(mPlayer != null && hasPrepared){
            int times = mPlayer.getCurrentPosition();
            SharedPreferences sp = getSharedPreferences(num, 0);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(num, String.valueOf(times));
            editor.commit();
        }
    }
    //设置指定播放位置
    void  setTime(int times){
        if(mPlayer != null && hasPrepared){
            mPlayer.seekTo(times);
            //mPlayer.start();
            startPlay();
        }
    }
    //暂停播放
    void pausePlay(){
        if (mPlayer != null && hasPrepared ) {
            //禁用视频播放模式，恢复正常帧率渲染
            disableVideoMode();
            //显示选中点
            showGaze();
            mPlayer.pause();
        }
    }

    //停止播放
    void stopPlay(){
        if (mPlayer != null && hasPrepared ) {
            disableVideoMode();
            showGaze();
            mPlayer.stop();

            //关闭视频优化功能
            if( isNeedOpt ){
                MediaPlayerDecodeOpt.stopVideoDecodeOpt(mPlayer);
            }
        }
    }
    @Override
    public void onPause() {
        Intent intent = getIntent();
        String p = intent.getStringExtra("param");

        pausePlay();
        posPlayer(p);
        unlockTracker();
    }

    @Override
    public void onResume() {
        Intent intent = getIntent();
        String p = intent.getStringExtra("param");
        //缓存中获取播放记录
        SharedPreferences sp = getSharedPreferences(p, 0);
        String time = sp.getString(p,null);
        if(null==time || time.equals("")){
            startPlay();
        }else {
            setTime(Integer.parseInt(time));
        }
    }

    //在Scene销毁时停止播放并释放资源
    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() called");
        if (mPlayer != null && hasPrepared ) {
            stopPlay();
        }

        if(mPlayer != null ) {
            mPlayer.release();
        }
        hasPrepared = false;
        finish();
    }

    boolean isUpdateParameters = false;

    Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            isUpdateParameters = false;
        }
    };
    public long exitTime = 0;
    public long exitTimes = 0;
    @Override
    public boolean onKeyDown(int keyCode) {
        if(keyCode == 154){
            if((System.currentTimeMillis() - exitTime) > 2000){
                exitTime = System.currentTimeMillis();
            }else{
                backWard();
            }
        }else if(keyCode == 155){
            if((System.currentTimeMillis() - exitTimes) > 2000){
                exitTimes = System.currentTimeMillis();
            }else{
                forWard();
            }
        }else if( keyCode == NibiruKeyEvent.KEYCODE_ENTER ){
            //暂停/恢复播放
            if( mPlayer != null && mPlayer.isPlaying() ){
                pausePlay();
            }else if( mPlayer != null && !mPlayer.isPlaying() ){
                startPlay();
            }
        }
        //上下方向键循环更新当前播放模式，如机器没有上下方向键，可用改为其他按键触发
        else if( keyCode == NibiruKeyEvent.KEYCODE_UP ){
            if(!isUpdateParameters) {
                isUpdateParameters = true;
                --mCurrentPlayMode;
                if (mCurrentPlayMode < 0) {
                    mCurrentPlayMode = mModeArray.length - 1;
                }

                XSurfaceArea.Parameters param = getVideoParameters(mCurrentPlayMode);

                //更新播放模式参数
                if (m_VideoArea != null) {
                    m_VideoArea.updateParameters(param, updateRunnable);
                }

                //showCurrentPlayMode();
            }

        }else if( keyCode == NibiruKeyEvent.KEYCODE_DOWN ){
            if(!isUpdateParameters) {
                isUpdateParameters = true;
                ++mCurrentPlayMode;
                if (mCurrentPlayMode >= mModeArray.length) {
                    mCurrentPlayMode = 0;
                }

                XSurfaceArea.Parameters param = getVideoParameters(mCurrentPlayMode);

                //更新播放模式参数
                if (m_VideoArea != null) {
                    m_VideoArea.updateParameters(param, updateRunnable);
                }
            }
            //showCurrentPlayMode();
        }else if(keyCode == 67){
            onPause();
            onDestroy();
        }else if(keyCode == 156){
            //音量+
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
        }else if(keyCode == 157){
            //音量-
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
        }else if(keyCode == 62){
            //复位
            mPlayer.seekTo(0);
            //mPlayer.start();
            startPlay();
        }
        return super.onKeyDown(keyCode);
    }
    /***************************************************
     * 设置快进10秒方法
     */
    public void forWard(){
        if(mPlayer != null){
            int position = mPlayer.getCurrentPosition();
            mPlayer.seekTo(position + 10000);
        }
    }
    /*****************************************************
     * 设置后退10秒的方法
     */
    public void backWard(){
        if(mPlayer != null){
            int position = mPlayer.getCurrentPosition();
            if(position > 10000){
                position-=10000;
            }else{
                position = 0;
            }
            mPlayer.seekTo(position);
        }
    }

}
