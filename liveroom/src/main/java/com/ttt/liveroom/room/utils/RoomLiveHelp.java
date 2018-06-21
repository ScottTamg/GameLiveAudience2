package com.ttt.liveroom.room.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.wushuangtech.bean.VideoCompositingLayout;
import com.wushuangtech.library.Constants;
import com.wushuangtech.videocore.MyVideoApi;
import com.wushuangtech.wstechapi.TTTRtcEngine;
import com.wushuangtech.wstechapi.internal.TTTRtcEngineImpl;
import com.wushuangtech.wstechapi.model.VideoCanvas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import project.android.imageprocessing.entity.Effect;

import static com.ttt.liveroom.room.utils.LocalConstans.CALL_BACK_ON_ENTER_ROOM;
import static com.ttt.liveroom.room.utils.LocalConstans.CALL_BACK_ON_ERROR;
import static com.ttt.liveroom.room.utils.LocalConstans.CALL_BACK_ON_USER_JOIN;
import static com.ttt.liveroom.room.utils.LocalConstans.CALL_BACK_ON_USER_OFFLINE;
import static com.wushuangtech.library.Constants.CLIENT_ROLE_BROADCASTER;
import static com.wushuangtech.library.Constants.ERROR_ENTER_ROOM_BAD_VERSION;
import static com.wushuangtech.library.Constants.ERROR_ENTER_ROOM_FAILED;
import static com.wushuangtech.library.Constants.ERROR_ENTER_ROOM_TIMEOUT;
import static com.wushuangtech.library.Constants.ERROR_ENTER_ROOM_UNKNOW;
import static com.wushuangtech.library.Constants.ERROR_ENTER_ROOM_VERIFY_FAILED;
import static com.wushuangtech.library.Constants.ERROR_KICK_BY_HOST;
import static com.wushuangtech.library.Constants.ERROR_KICK_BY_MASTER_EXIT;
import static com.wushuangtech.library.Constants.ERROR_KICK_BY_NEWCHAIRENTER;
import static com.wushuangtech.library.Constants.ERROR_KICK_BY_NOAUDIODATA;
import static com.wushuangtech.library.Constants.ERROR_KICK_BY_NOVIDEODATA;
import static com.wushuangtech.library.Constants.ERROR_KICK_BY_PUSHRTMPFAILED;
import static com.wushuangtech.library.Constants.ERROR_KICK_BY_RELOGIN;
import static com.wushuangtech.library.Constants.ERROR_KICK_BY_SERVEROVERLOAD;

/**
 * @author mrliu
 * Created by mrliu on 2018/5/10
 * 此类用于：用于直播的辅助类
 */
public class RoomLiveHelp {

    private RoomLiveInterface mLiveInstance;
    private Activity mContext;
    private MyLocalBroadcastReceiver mLocalBroadcast;
    private TTTRtcEngine mTTTEngine;
    private int DISCONNECT = 100;
    private EnterUserInfo mRemoteUserInfo;
    public boolean isKickout = false;
    private String mAnchorId;
    private boolean isEnableVideo = false;

    public RoomLiveHelp(RoomLiveInterface liveInterface, Activity activity, String anchorId) {
        this.mLiveInstance = liveInterface;
        mContext = activity;
        mAnchorId = anchorId;

        if ("1".equals(com.ttt.liveroom.net.Constants.IS_LIVE)) {
            isEnableVideo = true;
        } else {
            isEnableVideo = false;
        }
//        initBroadcast();
    }

    /**
     * 注册广播信息
     */
    public void initBroadcast() {
        IntentFilter filter = new IntentFilter();
        if (mLocalBroadcast == null) {
            mLocalBroadcast = new MyLocalBroadcastReceiver();
            filter.addAction(MyTTTRtcEngineEventHandler.TAG);
        } else {
            mContext.unregisterReceiver(mLocalBroadcast);
        }
        mContext.registerReceiver(mLocalBroadcast, filter);
    }

    private class MyLocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MyTTTRtcEngineEventHandler.TAG)) {
                JniObjs mJniObjs = intent.getParcelableExtra(
                        MyTTTRtcEngineEventHandler.MSG_TAG);

                switch (mJniObjs.mJniType) {
                    case CALL_BACK_ON_ENTER_ROOM:
                        TTTSDK.mMyTTTRtcEngineEventHandler.setIsSaveCallBack(false);
                        mLiveInstance.enterRoomSuccess();
                        break;
                    case CALL_BACK_ON_ERROR:
                        final int errorType = mJniObjs.mErrorType;
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (errorType == ERROR_ENTER_ROOM_TIMEOUT || errorType == ERROR_ENTER_ROOM_FAILED ||
                                        errorType == ERROR_ENTER_ROOM_VERIFY_FAILED || errorType == ERROR_ENTER_ROOM_BAD_VERSION ||
                                        errorType == ERROR_ENTER_ROOM_UNKNOW) {
                                    mLiveInstance.enterRoomFailue(errorType);
                                } else if (errorType == ERROR_KICK_BY_HOST || errorType == ERROR_KICK_BY_PUSHRTMPFAILED ||
                                        errorType == ERROR_KICK_BY_SERVEROVERLOAD || errorType == ERROR_KICK_BY_MASTER_EXIT ||
                                        errorType == ERROR_KICK_BY_RELOGIN || errorType == ERROR_KICK_BY_NEWCHAIRENTER ||
                                        errorType == ERROR_KICK_BY_NOAUDIODATA || errorType == ERROR_KICK_BY_NOVIDEODATA) {
                                    mLiveInstance.onDisconnected(errorType);
                                } else if (errorType == DISCONNECT) {
                                    mLiveInstance.onDisconnected(errorType);
                                }
                            }
                        });
                        break;
                    case CALL_BACK_ON_USER_JOIN:
                        long uid = mJniObjs.mUid;
                        int identity = mJniObjs.mIdentity;
                        mRemoteUserInfo = new EnterUserInfo(uid, identity);
                        if (mAnchorId.equals(String.valueOf(uid))) {
                            mLiveInstance.onHostEnter(uid, mRemoteUserInfo);
                        } else {
                            mLiveInstance.onMemberEnter(uid, mRemoteUserInfo);
                        }
                        break;
                    case CALL_BACK_ON_USER_OFFLINE:
                        mLiveInstance.onMemberExit(mJniObjs.mUid);
                        break;
                    case LocalConstans.CALL_BACK_ON_SEI:
                        String sei = mJniObjs.mSEI;
                        List<EnterUserInfo> mInfos = new ArrayList<>();
                        try {
                            JSONObject jsonObject = new JSONObject(sei);
                            String mid = (String) jsonObject.get("mid");
                            LocalConfig.mBroadcasterID = Integer.valueOf(mid);
                            JSONArray jsonArray = jsonObject.getJSONArray("pos");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonobject2 = (JSONObject) jsonArray.get(i);
                                String devid = jsonobject2.getString("id");
                                float x = Float.valueOf(jsonobject2.getString("x"));
                                float y = Float.valueOf(jsonobject2.getString("y"));
                                long userId;
                                int index = devid.indexOf(":");
                                if (index > 0) {
                                    userId = Long.parseLong(devid.substring(0, index));
                                } else {
                                    userId = Long.parseLong(devid);
                                }
                                if (mAnchorId.equals(String.valueOf(userId))) {
                                    continue;
                                }
                                LocalConfig.mBroadcasterID = Integer.valueOf(mid);
                                int role = 2;
                                if (LocalConfig.mBroadcasterID == userId) {
                                    role = 1;
                                }
                                EnterUserInfo temp = new EnterUserInfo(userId, role, x, y);
                                mInfos.add(temp);
                            }
                            mLiveInstance.onUpdateLiveView(mInfos);//只有在申请连麦时才会走,观众不走
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        }
    }

    /***************************************直播模块开始***************************************/

    //主播预览视频
    public void startPreview(ViewGroup rootView) {
        if (mTTTEngine == null) {
            TTTSDK.init(mContext.getApplicationContext(), "a967ac491e3acf92eed5e1b5ba641ab7");
        }
        mTTTEngine = TTTRtcEngine.getInstance();
        if (isEnableVideo) {
            mTTTEngine.enableVideo();
        }
        SurfaceView localSurfaceView = mTTTEngine.CreateRendererView(mContext);
        MyVideoApi.VideoConfig videoConfig = MyVideoApi.getInstance().getVideoConfig();
        videoConfig.videoWidth = 360;
        videoConfig.videoHeight = 640;
        MyVideoApi.getInstance().setVideoConfig(videoConfig);
        localSurfaceView.getHolder().addCallback(new RemoteSurfaceViewCb());
        mTTTEngine.setupLocalVideo(new VideoCanvas(0, Constants.RENDER_MODE_HIDDEN,
                localSurfaceView), mContext.getRequestedOrientation());
        rootView.addView(localSurfaceView, 0);
        mTTTEngine.startPreview();
        mTTTEngine.openFaceBeavty(true);
    }

    //初始化engine并进入直播 liveType:直播类型 role：进入的角色 roomId:直播的房间id, userId:进入房间的userid
    public void enterRoom(int liveType, int role, final int roomId, final long userId) {
        mTTTEngine = TTTRtcEngine.getInstance();
        // 设置频道属性
        mTTTEngine.setChannelProfile(liveType);
        // 启用视频模式
        if (isEnableVideo) {
            mTTTEngine.enableVideo();
        }
        // 重置直播房间内的参数
        mTTTEngine.setClientRole(role, null);
        TTTRtcEngineImpl.getInstance().setVideoProfile(Constants.VIDEO_PROFILE_360P, false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int result = mTTTEngine.joinChannel("", String.valueOf(roomId), userId);
            }
        }).start();
    }

    //打开本地视频 rootView：父控件 zorderMediaOverlay：显示层级
    public void openLocalVideo(ViewGroup rootView, boolean zorderMediaOverlay) {
        SurfaceView localSurfaceView;
        MyVideoApi.VideoConfig videoConfig = MyVideoApi.getInstance().getVideoConfig();
        videoConfig.videoWidth = 360;
        videoConfig.videoHeight = 640;
        MyVideoApi.getInstance().setVideoConfig(videoConfig);
        localSurfaceView = mTTTEngine.CreateRendererView(mContext);
        localSurfaceView.setZOrderMediaOverlay(zorderMediaOverlay);
        localSurfaceView.getHolder().addCallback(new RemoteSurfaceViewCb());
        mTTTEngine.setupLocalVideo(new VideoCanvas(0, Constants.RENDER_MODE_HIDDEN,
                localSurfaceView), mContext.getRequestedOrientation());
        rootView.addView(localSurfaceView, 0);
    }

    // 打开远端视频 rootView:父控件 zorderMediaOverlay：显示层级 info：副播的详细信息
    public void openRemoteVideo(ViewGroup rootView, EnterUserInfo info, boolean zorderMediaOverlay) {
        SurfaceView mSurfaceView;
        long id = info.getId();
        mSurfaceView = mTTTEngine.CreateRendererView(mContext);
        mSurfaceView.getHolder().addCallback(new RemoteSurfaceViewCb());
        mTTTEngine.setupRemoteVideo(new VideoCanvas(info.getId(), Constants.
                RENDER_MODE_HIDDEN, mSurfaceView));
        rootView.setVisibility(View.VISIBLE);
        if (zorderMediaOverlay) {
            mSurfaceView.bringToFront();
        }
        mSurfaceView.setZOrderMediaOverlay(zorderMediaOverlay);
        rootView.addView(mSurfaceView, 0);
    }

    //主播设置副播的小窗口位置 userId：副播的id
    public void resetRemoteLayout(VideoCompositingLayout.Region[] regions) {
        VideoCompositingLayout layout = new VideoCompositingLayout();
        layout.regions = regions;
        mTTTEngine.setVideoCompositingLayout(layout);
    }

    /**
     * 主播设置主播窗口位置
     */
    public void removeRemoteLayout() {
        VideoCompositingLayout layout = new VideoCompositingLayout();
        List<VideoCompositingLayout.Region> tempList = new ArrayList<>();
        VideoCompositingLayout.Region[] mRegions = new VideoCompositingLayout.Region[tempList.size()];
        layout.regions = mRegions;
        mTTTEngine.setVideoCompositingLayout(layout);
    }

    //踢出房间成员 userId被T的userid
    public boolean kickRoomMember(long userId) {
        if (null != mTTTEngine) {
            boolean status = mTTTEngine.kickChannelUser(userId);
            return status;
        }
        return false;
    }

    //切换摄像头成功与否
    public boolean switchCamera() {
        if (mTTTEngine != null) {
            int status = mTTTEngine.switchCamera();
            return status == 0 ? true : false;
        }
        return false;
    }

    //控制本地视频
    public void controlLocalVideo(boolean flag) {
        mTTTEngine.muteLocalVideoStream(flag);
    }

    //控制本地声音
    public void controlLocalVoice(boolean flag) {
        mTTTEngine.muteLocalAudioStream(flag);
    }

    //离开房间
    public void exitRoom() {
        if (null != mTTTEngine) {
            mTTTEngine.stopAudioMixing();
            mTTTEngine.leaveChannel();
        }
    }

    public class RemoteSurfaceViewCb implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (LocalConfig.mRole == CLIENT_ROLE_BROADCASTER) {
                VideoCompositingLayout layout = new VideoCompositingLayout();
                List<VideoCompositingLayout.Region> tempList = new ArrayList<>();
                VideoCompositingLayout.Region[] mRegions = new VideoCompositingLayout.Region[tempList.size()];
                layout.regions = mRegions;
                mTTTEngine.setVideoCompositingLayout(layout);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (LocalConfig.mRole == CLIENT_ROLE_BROADCASTER) {
                VideoCompositingLayout layout = new VideoCompositingLayout();
                List<VideoCompositingLayout.Region> tempList = new ArrayList<>();
                VideoCompositingLayout.Region[] mRegions = new VideoCompositingLayout.Region[tempList.size()];
                layout.regions = mRegions;
                mTTTEngine.setVideoCompositingLayout(layout);
            }
        }
    }

    //退出帮助
    public void exitHelp() {
        if (mContext != null) {
            if (null != mTTTEngine) {
                mTTTEngine.stopAudioMixing();
                mTTTEngine.leaveChannel();
            }
            mContext.unregisterReceiver(mLocalBroadcast);
            Log.e("RoomLiveHelp", "exitHelp");
        }
    }

    public void setBlurLevel(int blurLevel) {
        mTTTEngine.setBlurLevel(blurLevel);
    }

    public void setColorLevel(float colorLevel) {
        mTTTEngine.setColorLevel(colorLevel);
    }

    public void setCheekThinning(float cheekThinning) {
        mTTTEngine.setCheekThinning(cheekThinning);
    }

    public void setEyeEnlarging(float eyeEnlarging) {
        mTTTEngine.setEyeEnlarging(eyeEnlarging);
    }

    public void onEffectSelected(Effect effect) {
        mTTTEngine.onEffectSelected(effect);
    }
}
