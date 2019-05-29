package com.nibiru.studio.zskx;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.util.Log;
import android.view.KeyEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import x.core.adapter.XItemAdapter;
import x.core.listener.IXActorEventListener;
import x.core.listener.IXActorKeyEventListener;
import x.core.ui.XActor;
import x.core.ui.XBaseScene;
import x.core.ui.XImage;
import x.core.ui.XImageText;
import x.core.ui.XLabel;
import x.core.ui.XPanel;
import x.core.ui.group.XActorGridView;
import x.core.ui.group.XActorPageView;
import x.core.ui.group.XItem;
import x.core.util.XVec3;
import android.media.AudioManager;
import android.app.Service;
import android.widget.Toast;


public class GridViewVideo extends XBaseScene implements IXActorEventListener {
    private MyAdapter myAdapter;
    private List<String> datas = new ArrayList<String>();
    private List<String> nums = new ArrayList<String>();
    private int contentView;
    private BluetoothAdapter mBluetoothAdapter;
    private AudioManager audioManager = null; // Audio管理器

    @Override
    public void onCreate() {
        init();
        initData();
        initPlayWork();
        initBottomActor();
    }

    private void initData() {
        String [] dataArr = new String [10];
        dataArr[0] = "Day1 生命冥想";
        dataArr[1] = "Day2 脉轮冥想";
        dataArr[2] = "Day3 大爱冥想";
        dataArr[3] = "Day4 自然冥想";
        dataArr[4] = "Day5 正能量冥想";
        dataArr[5] = "Day6 水的冥想";
        dataArr[6] = "Day7 白云冥想";
        dataArr[7] = "Day8 月光冥想";
        dataArr[8] = "Day9 荷花冥想";
        dataArr[9] = "Day10 向日葵冥想";
        for (int j = 0; j < dataArr.length; j++) {
            datas.add(dataArr[j]);
        }

        String [] numArr = new String [10];
        numArr[0] = "1";
        numArr[1] = "2";
        numArr[2] = "3";
        numArr[3] = "4";
        numArr[4] = "5";
        numArr[5] = "6";
        numArr[6] = "7";
        numArr[7] = "8";
        numArr[8] = "9";
        numArr[9] = "10";
        for (int i = 0; i < numArr.length; i++) {
            nums.add(numArr[i]);
        }
        myAdapter.notifyDataSetChanged();
    }

    private XPanel mDialog_bg;
    private boolean isDialogShow;
    public XLabel dialogMsg;
    public XImageText confirm;
    public XImageText cancel;

    public void resetDialog(boolean isShowing) {
        if (mDialog_bg != null) {
            mDialog_bg.setEnabled(isShowing);
        }
        if (dialogMsg != null) {
            dialogMsg.setEnabled(isShowing);
        }
        if (confirm != null) {
            confirm.setEnabled(isShowing);
        }
        if (cancel != null) {
            cancel.setEnabled(isShowing);
        }
    }

    @Override
    public void onResume() {
//        mFollowUtil.onResume();
        resetHeadTracker();
        updatePageNum();
    }

    @Override
    public void onPause() {
        //finish();
    }

    @Override
    public void onDestroy() {

    }
    /**
     * 初始化播放器、音量数据等相关工作
     */
    private void initPlayWork() {
        audioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
    }
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }


    XActorGridView actorGridView;

    public void init() {
        XImage xImage1;

        xImage1 = new XImage("icon/logo.png");

        xImage1.setCenterPosition(0f, 2.1f, -4f);
        xImage1.setSize(2f, 0.4f);
        xImage1.setRenderOrder(6);

        xImage1.setEnableGazeAnimation(true);
        //设置控件朝向眼睛
        //xImage1.towardOrigin();
        addActor(xImage1);
        int type = getIntent().getIntExtra("type", 0);

        XActorPageView.PageViewDefaultType t = XActorPageView.PageViewDefaultType.values()[0];

        //构造一个GridView，指定类型、页宽、页高、行数和列数
        actorGridView = new XActorGridView(t, 5.5f, 5f, 2, 2);

        List<XVec3> temp = new ArrayList<>();
        temp.add(new XVec3(-19f, 0, -6));
        temp.add(new XVec3(0, 0, -8f));
        temp.add(new XVec3(19f, 0, -6));
        actorGridView.setControlPoints(temp);
        myAdapter = new MyAdapter();
        //设置适配器
        actorGridView.setAdapter(myAdapter);

        //设置显示页的数量和布局方式
        actorGridView.setVisiblePageLayout(XActorPageView.VisiblePage.LEFT_MIDDLE_RIGHT);

        //设置背景图片
        actorGridView.setPageBackGroundName("blackxx.png");

        //关闭循环翻页
        actorGridView.setCirculation(true);

        //关闭点击两侧页面触发翻页的功能（默认开启）
        actorGridView.setEnableSidePageTriggerMove(true);

        //设置页切换动画更新，在开始动画和结束动画时回调
        actorGridView.setPageAnimationListener(new XActorPageView.IXPageAnimationListener() {
            @Override
            public void onBegin() {
                Log.d("test", "begin switch Page");
            }

            //onEnd回调参数为当前处于正中间的页对象
            @Override
            public void onEnd(XActorPageView.XPageState state) {

                //获取当前显示页的索引范围
                int[] items = new int[0];
                try {
                    items = actorGridView.getCenterPageItemPosRange();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                if (items != null) {
                    Log.d("test", "Current page item position range: " + Arrays.toString(items));
                }

                updatePageNum();
            }
        });

        actorGridView.setKeyEventListener(new IXActorKeyEventListener() {
            public boolean onKeyDown(int i) {
                if (i == KeyEvent.KEYCODE_DPAD_LEFT) {
                    actorGridView.moveLeft();
                    return true;
                } else if (i == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    actorGridView.moveRight();
                    return true;
                } else if(i == KeyEvent.KEYCODE_BACK){
                    //返回
                    Intent intent = new Intent( getApplicationContext(), MxList.class);
                    //打开带有返回值的Scene，参数为intent和requestCode
                    startSceneForResult(intent, 600);
                        //return false;
                } else if(i == 67){
                    //返回
                    Intent intent = new Intent( getApplicationContext(), MxList.class);
                    //打开带有返回值的Scene，参数为intent和requestCode
                    startSceneForResult(intent, 600);
                    //return false;
                }else if(i == 145){
                    Intent intent = new Intent( getApplicationContext(), SubSceneVideoAll2.class);
                    intent.putExtra("param", "1");
                    //打开带有返回值的Scene，参数为intent和requestCode
                    startSceneForResult(intent, 600);
                }else if(i == 146){
                    Intent intent = new Intent( getApplicationContext(), SubSceneVideoAll2.class);
                    intent.putExtra("param", "2");
                    //打开带有返回值的Scene，参数为intent和requestCode
                    startSceneForResult(intent, 600);
                }else if(i == 147){
                    Intent intent = new Intent( getApplicationContext(), SubSceneVideoAll2.class);
                    intent.putExtra("param", "3");
                    //打开带有返回值的Scene，参数为intent和requestCode
                    startSceneForResult(intent, 600);
                }else if(i == 148){
                    Intent intent = new Intent( getApplicationContext(), SubSceneVideoAll2.class);
                    intent.putExtra("param", "4");
                    //打开带有返回值的Scene，参数为intent和requestCode
                    startSceneForResult(intent, 600);
                }else if(i == 149){
                    Intent intent = new Intent( getApplicationContext(), SubSceneVideoAll2.class);
                    intent.putExtra("param", "5");
                    //打开带有返回值的Scene，参数为intent和requestCode
                    startSceneForResult(intent, 600);
                }else if(i == 150){
                    Intent intent = new Intent( getApplicationContext(), SubSceneVideoAll2.class);
                    intent.putExtra("param", "6");
                    //打开带有返回值的Scene，参数为intent和requestCode
                    startSceneForResult(intent, 600);
                }else if(i == 151){
                    Intent intent = new Intent( getApplicationContext(), SubSceneVideoAll2.class);
                    intent.putExtra("param", "7");
                    //打开带有返回值的Scene，参数为intent和requestCode
                    startSceneForResult(intent, 600);
                }else if(i == 152){
                    Intent intent = new Intent( getApplicationContext(), SubSceneVideoAll2.class);
                    intent.putExtra("param", "8");
                    //打开带有返回值的Scene，参数为intent和requestCode
                    startSceneForResult(intent, 600);
                }else if(i == 153){
                    Intent intent = new Intent( getApplicationContext(), SubSceneVideoAll2.class);
                    intent.putExtra("param", "9");
                    //打开带有返回值的Scene，参数为intent和requestCode
                    startSceneForResult(intent, 600);
                }else if(i == 144){
                    Intent intent = new Intent( getApplicationContext(), SubSceneVideoAll2.class);
                    intent.putExtra("param", "10");
                    //打开带有返回值的Scene，参数为intent和requestCode
                    startSceneForResult(intent, 600);
                }else if(i == 62){
                    //复位
                    resetHeadTracker();
                    //lockTrackerToFront();
                    //unlockTracker();
                }else if(i == 156){
                    //音量+
                    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                }else if(i == 157){
                    //音量-
                    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                }else if(i == 154){
                    //心理治疗
                    if(actorGridView != null){
                        actorGridView.moveLeft();
                    }
                }else if(i == 155){
                    //音乐治疗
                    if(actorGridView != null){
                        actorGridView.moveRight();
                    }
                }else if(i ==61){
                   return false;
                }else if(i == 65){//180
                    Intent intent = new Intent( getApplicationContext(), GirdViewMusic.class);
                    intent.putExtra("param", "music");
                    //打开带有返回值的Scene，参数为intent和requestCode
                    startSceneForResult(intent, 600);
                }else if(i == 210){//183
//                    try {
//                        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//                        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
//                            Toast.makeText(
//                            getApplicationContext(),
//                            "请打开蓝牙！",
//                            Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    PackageManager packageManager = getPackageManager();
//                    Intent intent = packageManager.getLaunchIntentForPackage("com.zskx.naobo");
//                    startActivity(intent);
                }

                return false;
            }

            @Override
            public boolean onKeyUp(int i) {
                return false;
            }

            public boolean KeyEvent(int i) {
                System.out.println(i+"多媒体按键");
                return false;
            }

        });

        addActor(actorGridView);
    }

    @Override
    public void onGazeEnter(XActor actor) {


    }

    @Override
    public void onGazeExit(XActor actor) {

    }

    @Override
    public boolean onGazeTrigger(XActor actor) {
        return false;
    }

    @Override
    public void onAnimation(XActor actor, boolean isSelected) {

    }

    public void setContentView(int contentView) {
        this.contentView = contentView;
    }

    private class MyAdapter extends XItemAdapter {

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getObject(int position) {
            return null;
        }

        @Override
        public XItem getXItem(final int position, XItem convertItem, XActor parent) {
            String data;
            final String numsw;
            final XImage mMaskLayer;
            final XImageText imageText;
            synchronized (GridViewVideo.class) {
                if (position < 0 || position >= datas.size()) return null;
                data = datas.get(position);
                numsw = nums.get(position);
            }

            if (convertItem == null) {
                convertItem = new XItem();

                imageText = new XImageText("bg/mx_"+numsw+".png","bg/mx_"+numsw+".png");
                imageText.setTitle(data, data);
                imageText.setSizeOfImage(2f, 2f);
                imageText.setSize(2f, 2f);
                imageText.setSizeOfTitle(2.0f, 0.25f);
                imageText.setTitlePosition(0, -0.7f);
                imageText.setCenterPosition(0, 0, -4);

//                final XImage xImage = new XImage("bg/mx_"+numsw+".png", "bg/mx_"+numsw+".png");
//
//                xImage.setSize(2f, 2f);
//                xImage.setRenderOrder(24);
//                xImage.setName("deleteSmall");

                //在ImageText基础上添加半透明层区分选中状态
                mMaskLayer = new XImage("blackxx.png");
                mMaskLayer.setSize(2f, 2f);

                mMaskLayer.setEnabled(false);
                imageText.addLayer(mMaskLayer, 0, 0,true);

                imageText.setEventListener(new IXActorEventListener() {
                    @Override
                    public void onGazeEnter(XActor actor) {
                        mMaskLayer.setEnabled(true);
                    }

                    @Override
                    public void onGazeExit(XActor actor) {
                        mMaskLayer.setEnabled(false);
                    }

                    @Override
                    public boolean onGazeTrigger(XActor actor) {
                        if( actor == mMaskLayer){
                            Intent intent = new Intent( getApplicationContext(), SubSceneVideoAll2.class);
                            intent.putExtra("param", numsw);
                            //打开带有返回值的Scene，参数为intent和requestCode
                            startSceneForResult(intent, 600);
                        }
                        return false;
                    }

                    @Override
                    public void onAnimation(XActor actor, boolean isSelected) {
                        //mMaskLayer.setEnabled(true);
                    }
                });

                mMaskLayer.setEventListener(new IXActorEventListener() {
                    @Override
                    public void onGazeEnter(XActor xActor) {
                        mMaskLayer.setEnabled(true);
                    }

                    @Override
                    public void onGazeExit(XActor xActor) {
                        mMaskLayer.setEnabled(false);
                    }

                    @Override
                    public boolean onGazeTrigger(XActor xActor) {
                        if( xActor == mMaskLayer){
                            Intent intent = new Intent(getApplicationContext(), SubSceneVideoAll2.class);
                            intent.putExtra("param", numsw);
                            //打开带有返回值的Scene，参数为intent和requestCode
                            startSceneForResult(intent, 600);
                        }
                        return false;
                    }

                    @Override
                    public void onAnimation(XActor xActor, boolean b) {
                        //mMaskLayer.setEnabled(true);
                    }
                });

                convertItem.addLayer(imageText);
            }
            //重置当前视野
            //resetHeadTracker();

            return convertItem;
        }
    }
    //初始化底部按钮，左右翻页和当前页数显示
    void initBottomActor(){
        mPageLeft = new XImage("ic_left_focused.png", "ic_left_default.png");
        mPageLeft.setCenterPosition(-0.5f, -1.9f, -4f);
        mPageLeft.setSize(0.3f, 0.3f);
        mPageLeft.setName("left");
        mPageLeft.setEventListener(new IXActorEventListener() {
            @Override
            public void onGazeEnter(XActor actor) {

            }

            @Override
            public void onGazeExit(XActor actor) {

            }

            //左翻页
            @Override
            public boolean onGazeTrigger(XActor actor) {
                if( actorGridView != null ){
                    actorGridView.moveLeft();
                }
                return false;
            }

            @Override
            public void onAnimation(XActor actor, boolean isSelected) {

            }
        });

        addActor(mPageLeft);

        mPageRight = new XImage("ic_right_focused.png", "ic_right_default.png");
        mPageRight.setCenterPosition(0.5f, -1.9f, -4f);
        mPageRight.setSize(0.3f, 0.3f);
        mPageRight.setName("right");
        mPageRight.setEventListener(new IXActorEventListener() {
            @Override
            public void onGazeEnter(XActor actor) {

            }

            @Override
            public void onGazeExit(XActor actor) {

            }

            //右翻页
            @Override
            public boolean onGazeTrigger(XActor actor) {
                if( actorGridView != null ){
                    actorGridView.moveRight();
                }
                return false;
            }

            @Override
            public void onAnimation(XActor actor, boolean isSelected) {

            }
        });

        addActor(mPageRight);

        mPageNumLabel = new XLabel("");
        mPageNumLabel.setCenterPosition(0, -1.9f, -4.0f);
        mPageNumLabel.setSize(0.6f, 0.2f);
        mPageNumLabel.setAlignment(XLabel.XAlign.Center);
        mPageNumLabel.setArrangementMode(XLabel.XArrangementMode.SingleRowNotMove);

        addActor(mPageNumLabel);
    }
    void updatePageNum(){
        if( actorGridView == null || !actorGridView.isCreated() )return;

        try {
            //获取页状态信息，如在切页动画运行中调用会抛出异常
            //Get the current page status, if it's called when the page switching animation is running, there will be error
            XActorPageView.XPageState state = actorGridView.getXPageState();

            //如果当前不是循环模式，判断是否到达第一页和最后一页
            //If it's not circulation mode, judge whether to meet the first page and the last page
            if( !state.isCircle() ){
                if( mPageLeft != null ) {
                    //如已经是第一页则隐藏上一页按钮
                    //If it's the first page, hide the back button
                    if (state.hasMeetHead()) {
                        mPageLeft.setEnabled(false);
                    } else {
                        mPageLeft.setEnabled(true);
                    }
                }

                if( mPageRight != null ) {
                    //如已经是最后一页则隐藏下一页按钮
                    //If it's the last page, hide the next button
                    if (state.hasMeetTail()) {
                        mPageRight.setEnabled(false);
                    } else {
                        mPageRight.setEnabled(true);
                    }
                }
            }
            if( mPageNumLabel != null ){
                //页计数从1开始，position根据数组索引从0开始
                //The page counts from 1, and position counts from 0 according to array index
                mPageNumLabel.setTextContent((state.getCenterPagePosition() + 1) +" / "+state.getPageCount());
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onGazeTrigger() {
        super.onGazeTrigger();
        return true;
    }

    private XLabel mTip;

    XLabel mPageNumLabel;
    XImage mPageLeft;
    XImage mPageRight;
}
