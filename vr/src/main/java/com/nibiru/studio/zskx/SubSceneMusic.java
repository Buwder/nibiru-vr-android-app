package com.nibiru.studio.zskx;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Surface;

import com.nibiru.service.NibiruKeyEvent;

import java.io.File;
import java.io.IOException;

import x.core.listener.IXActorAnimationListener;
import x.core.ui.XActor;
import x.core.ui.XBaseScene;
import x.core.ui.XImage;
import x.core.ui.XImageText;
import x.core.ui.XLabel;
import x.core.ui.XSpaceRect;
import x.core.ui.XToast;
import x.core.ui.XUI;
import x.core.ui.animation.XAnimation;
import x.core.ui.animation.XRotateAnimation;
import x.core.ui.surface.XSurfaceArea;
import x.core.ui.surface.XSurfaceConfig;
import x.core.util.MediaPlayerDecodeOpt;
import x.core.util.XLog;
import x.core.util.XVec3;

/**
 * 演示视频播放功能实现
 * 包括2D/3D模式，平面/180/360/球幕等视频类型，示例中使用Android MediaPlayer作为解码框架，开发者可根据需要决定解码框架
 * @author Steven
 */

public class SubSceneMusic extends XBaseScene {
    XAnimation mRotAnimation = null;
    private static final String TAG = SubSceneMusic.class.getSimpleName();

    public static final String PLAY_MODE = "PLAY_MODE";

    String[] mModeArray = {"2DNormal", "2D180", "2D360", "2DSphere", "3DNormal", "3D180", "3D360", "3DSphere"};
    XImage xImage1,xImage2,mMaskLayer;
    XImageText mImageText;
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
        //disableGlobalSkybox();
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
        mode = NORMAL_3D;
        switch(mode){
            case NORMAL_3D:{
                return new XSurfaceArea.Builder(this, XSurfaceConfig.Mode.MODE_3D, XSurfaceConfig.Format.TYPE_PLANE)
                .setCenter(center)
                .setWidth(8)
                .setPosition(pose)
                //设置是否支持选中，默认关闭
                .setEnableSelection(true)
                //设置为左右布局的3D视频，如果是上下布局的3D视频，设置为LAYOUT_VERTICAL
                .setVideoLayout(XSurfaceConfig.Layout.LAYOUT_HORIZONTAL)
                .buildParameters();

            }
            default:
            return null;
        }

    }

    public void init(String num) {

        loadFont("MengGongFang.ttf", XUI.Location.ASSETS);
        xImage1 = new XImage("bg/m_5.png");

        xImage1.setCenterPosition(0f, 1.2f, -4f);
        xImage1.setSize(2f, 2f);
        xImage1.setRenderOrder(6);

        xImage1.setEnableGazeAnimation(true);
        //设置控件朝向眼睛
        //xImage1.towardOrigin();
        addActor(xImage1);
        rotate(xImage1);
//
//        xImage2 = new XImage("bg/mxbg");
//
//        xImage2.setCenterPosition(0f, -1.6f, -4f);
//        xImage2.setSize(4f, 2f);
//        xImage2.setRenderOrder(6);
//
//        xImage2.setEnableGazeAnimation(true);
//        addActor(xImage2);



//        XLabel titleLabel = new XLabel("冥想介绍：音乐是热情洋溢的自由艺术，是室外的艺术，像自然那样无边无际，像风，像天空，像海洋。重要的不在于旋律的开始，而是把它继续下去");
//        //设置 Label 的排版方式为，居中，并且不开启跑马灯
//        titleLabel.setAlignment(XLabel.XAlign.Left);
//        titleLabel.setArrangementMode(XLabel.XArrangementMode.SingleRowMove);
//        //设置 Label 的位置坐标和尺寸
//        titleLabel.setCenterPosition(0, 0f, -4f);
//        titleLabel.setSize(4f, 0.2f);
//        titleLabel.setTextStyle(XLabel.TextStyle.Underline,0,1000);
//        titleLabel.setTextColor(Color.YELLOW);
//        //将 Label 加入到场景中
//        addActor(titleLabel);

        //设置选中和未选中图片，图片来自于assets
        mImageText= new XImageText("bg/mxbg.jpg", "bg/mxbg.jpg");


        mImageText.setCenterPosition(0f, -1.1f, -4f);
        mImageText.setSize(0.2F, 1.5F);
        mImageText.setSizeOfImage(4f, 2f);
        mImageText.setRenderOrder(7);

        //支持设置选中/选中文本的资源ID，便于国际化
        mImageText.setTitle("冥想音乐", "冥想音乐");
        mImageText.setSelectedArrangementMode(XLabel.XArrangementMode.SingleRowMove);
        mImageText.setEnabled(true);
        //设置未选中时左对齐，超出部分显示省略号
        mImageText.setUnselectedArrangementMode(XLabel.XArrangementMode.SingleRowClip);
        mImageText.setUnselectedAlign(XLabel.XAlign.Center);

        mImageText.setSizeOfTitle(1.5f, 0.2f);
        mImageText.setTitlePosition(0, -0.55f);

        addActor(mImageText);

        //在ImageText基础上添加半透明层区分选中状态
        mMaskLayer = new XImage("blackxx.png");
        mMaskLayer.setSize(4f, 2f);

        mMaskLayer.setEnabled(true);
        mImageText.addLayer(mMaskLayer, 0, 0);


        //通过scene对象和视频参数初始化XSurfaceArea
        m_VideoArea = new XSurfaceArea.Builder(this, getVideoParameters(mCurrentPlayMode)).build();

        //默认开启自动隐藏天空盒模式，也就是PLANE平面模式下显示天空盒，而其他模式都隐藏天空盒，如果不需要此功能可关闭
        m_VideoArea.setAutoHideSkybox(true);


        if( mCurrentPlayMode < 0 || mCurrentPlayMode >= mModeArray.length ){
            return;
        }

        videoPath = "/sdcard/Android/data/.com.nibiru.music/"+num;
        hasPrepared = false;

        if( m_VideoArea != null && videoPath != null ) {

            //如果文件不存在弹出Toast提示
            if( !new File(videoPath).exists() ){
                XToast.makeToast(this, "音乐路径未找到: "+videoPath, 5000).show(true);
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

    void rotate(XActor actor) {
        if (mRotAnimation == null) {
            //设置旋转动画，第一个参数为旋转角度，第二个参数为旋转时间，单位毫秒，第三个参数设置重复执行次数，LOOP代表一致循环
            mRotAnimation = new XRotateAnimation(360, 3000, XAnimation.REPEAT_LOOP);

            //设置指定旋转中心的旋转动画，第一个参数为旋转中心，第二个参数为旋转角度，第三个参数为旋转时间，单位毫秒，第四个参数设置重复执行次数，LOOP代表一致循环
//            float[] rotCenter = new float[]{0, 0, -4};
//            mRotAnimation = new XRotateAnimation(rotCenter, 360, 500, XAnimation.REPEAT_LOOP);

            //设置动画监听，监听包括开始，进行，结束，重复，取消几个回调
            mRotAnimation.setAnimationListener(new IXActorAnimationListener() {
                @Override
                public void onAnimationStart(XAnimation animation) {
                    Log.d(XLog.TAG, "[Anim] ============== START: ");
                }

                @Override
                public void onAnimationProcess(XAnimation animation, float process) {
//                    Log.d(XLog.TAG, "[Anim] ============== PROCESS: Pro: " + process);
                }

                @Override
                public void onAnimationEnd(XAnimation animation) {
//                    Log.d(XLog.TAG, "[Anim] ============== END: ");
                }

                @Override
                public void onAnimationRepeat(XAnimation animation, int rest) {
//                    Log.d(XLog.TAG, "[Anim] ============== !!REPEAT!!: rest count: " + rest);
                }

                @Override
                public void onAnimationCancel(XAnimation animation) {
//                    Log.d(XLog.TAG, "[Anim] ============== CANCEL: ");
                }
            });

            //开始执行动画
            actor.startAnimation(mRotAnimation);
        } else {
            //取消动画
            if (actor != null && mRotAnimation != null) {
                actor.cancelAnimation(mRotAnimation);
                mRotAnimation = null;
            }

        }
    }
    void rotates(XActor actor) {
        if (mRotAnimation == null) {
            //设置旋转动画，第一个参数为旋转角度，第二个参数为旋转时间，单位毫秒，第三个参数设置重复执行次数，LOOP代表一致循环
            mRotAnimation = new XRotateAnimation(0, 0, XAnimation.REPEAT_LOOP);

            //设置指定旋转中心的旋转动画，第一个参数为旋转中心，第二个参数为旋转角度，第三个参数为旋转时间，单位毫秒，第四个参数设置重复执行次数，LOOP代表一致循环
//            float[] rotCenter = new float[]{0, 0, -4};
//            mRotAnimation = new XRotateAnimation(rotCenter, 360, 500, XAnimation.REPEAT_LOOP);

            //设置动画监听，监听包括开始，进行，结束，重复，取消几个回调
            mRotAnimation.setAnimationListener(new IXActorAnimationListener() {
                @Override
                public void onAnimationStart(XAnimation animation) {
                    Log.d(XLog.TAG, "[Anim] ============== START: ");
                }

                @Override
                public void onAnimationProcess(XAnimation animation, float process) {
//                    Log.d(XLog.TAG, "[Anim] ============== PROCESS: Pro: " + process);
                }

                @Override
                public void onAnimationEnd(XAnimation animation) {
//                    Log.d(XLog.TAG, "[Anim] ============== END: ");
                }

                @Override
                public void onAnimationRepeat(XAnimation animation, int rest) {
//                    Log.d(XLog.TAG, "[Anim] ============== !!REPEAT!!: rest count: " + rest);
                }

                @Override
                public void onAnimationCancel(XAnimation animation) {
//                    Log.d(XLog.TAG, "[Anim] ============== CANCEL: ");
                }
            });

            //开始执行动画
            actor.startAnimation(mRotAnimation);
        } else {
            //取消动画
            if (actor != null && mRotAnimation != null) {
                actor.cancelAnimation(mRotAnimation);
                mRotAnimation = null;
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

    @Override
    public boolean onKeyDown(int keyCode) {

        if( keyCode == NibiruKeyEvent.KEYCODE_ENTER ){
            //暂停/恢复播放
            if( mPlayer != null && mPlayer.isPlaying() ){
                pausePlay();
                rotates(xImage1);
            }else if( mPlayer != null && !mPlayer.isPlaying() ){
                startPlay();
                rotate(xImage1);
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
        }else if(keyCode == 157){
            //音量+
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
        }else if(keyCode == 156){
            //音量-
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
        }
        return super.onKeyDown(keyCode);
    }

}
