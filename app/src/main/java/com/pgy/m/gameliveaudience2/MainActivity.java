package com.pgy.m.gameliveaudience2;

import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ttt.liveroom.Config;
import com.ttt.liveroom.RoomInterface;
import com.ttt.liveroom.RoomManager;
import com.ttt.liveroom.base.DataManager;
import com.ttt.liveroom.bean.LoginInfo;
import com.ttt.liveroom.bean.room.NewestAuthorBean;
import com.ttt.liveroom.net.Constants;
import com.ttt.liveroom.room.RoomActivity;
import com.ttt.liveroom.room.play.LiveListDialog;
import com.ttt.liveroom.room.play.PlayFragment;
import com.ttt.liveroom.room.play.SmallScreenHelp;
import com.ttt.liveroom.util.SPUtils;

public class MainActivity extends AppCompatActivity implements RoomInterface {
    private static final int OVERLAY_PERMISSION_REQ_CODE = 100;

    private TextView mTvAudienceStEnter;
    private SmallScreenHelp mSmallInstance;
    private LiveListDialog dialog;
    private LoginInfo mLoginInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLoginInfo();

        RoomManager.getInstance().setRoomInstance(this);
        mSmallInstance = SmallScreenHelp.init(this);
        mTvAudienceStEnter = findViewById(R.id.audience_enter);
        mTvAudienceStEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLiveListDialog();
            }
        });

        initPermission();
    }

    private void initLoginInfo() {
        mLoginInfo = new LoginInfo();
        mLoginInfo.setAvatar("http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJwdDTU5w155tzbMqHj6JSy23FtzKHjSDl909OetCVZeknbsgYvHicKJsn1C63icmgOXlGEcNZAry6A/0");
        mLoginInfo.setNickname("用户10005");
        mLoginInfo.setUserId("10005");
        mLoginInfo.setUserName("用户10005");
        mLoginInfo.setTotalBalance(100000);
        mLoginInfo.setLevel("1");
        DataManager.getInstance().saveLoginInfo(mLoginInfo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSmallInstance.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSmallInstance.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSmallInstance.release();
    }

    private void showLiveListDialog() {
        //显示直播列表
        dialog = new LiveListDialog(this, mLoginInfo.getUserId());
        dialog.setLivelistDialogListener(new LiveListDialog.LiveListDialogListener() {
            @Override
            public void onItemClick(NewestAuthorBean.ListBean bean) {
                String hostInfo = new Gson().toJson(bean);
                SPUtils.save(Constants.KEYROOMID, bean.getRoomId());
                Config.LIVE_PULL_URL = bean.getPullRtmp();
                Config.LIVE_DATA = bean;
                startActivity(RoomActivity.createIntent(MainActivity.this,
                        RoomActivity.TYPE_VIEW_LIVE,
                        bean.getId(),
                        String.valueOf(bean.getUserId()),
                        bean.getAvatar(),
                        bean.getNickName(),
                        String.valueOf(bean.getLevel()),
                        bean.getTitle(),
                        bean.getPullRtmp(),
                        false,
                        bean.getRoomId(),
                        bean.getType(),
                        PlayFragment.createArgs(hostInfo)));
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "权限授予失败，无法开启悬浮窗", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "权限授予成功！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public LoginInfo getRoomInfo() {
        return mLoginInfo;
    }

    @Override
    public void sendGift(int num, String giftName, String price, String totalMoney, String sendUserId, String hostId) {
        Log.e("sendGift", "num:" + num + "   giftName:" + giftName + "   totalMoney:" + totalMoney + "   sendUserId:" + sendUserId + "   hostId:" + hostId);
    }

    @Override
    public void changeSmall() {
        mSmallInstance.starChangeSmall();
    }
}
