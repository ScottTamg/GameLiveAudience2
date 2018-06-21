package com.ttt.liveroom.room.play;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import static com.ttt.liveroom.room.RoomActivity.TYPE_VIEW_LIVE;

/**
 * @author txw
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    private Bundle mBundleArgs;
    private String mAnchorId;
    private String hostAvatar;
    private String hostNickName;
    private String hostLevel;
    private String mPullRtmp;
    private String mLiveId;
    private String mStreamId;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addData(Bundle bundleArgs, String anchorId, String hostAvatar, String hostNickName,
                        String hostLevel, String pullRtmp, String liveId, String streamId) {
        this.mBundleArgs = bundleArgs;
        this.mAnchorId = anchorId;
        this.hostAvatar = hostAvatar;
        this.hostNickName = hostNickName;
        this.hostLevel = hostLevel;
        this.mPullRtmp = pullRtmp;
        this.mLiveId = liveId;
        this.mStreamId = streamId;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        PlayFragment fragment = PlayFragment.newInstance(mBundleArgs, mAnchorId, hostAvatar,
                hostNickName, hostLevel, mPullRtmp, mStreamId, mLiveId, false);
        fragment.mRoomType = TYPE_VIEW_LIVE;
        fragment.setAnchorId(mAnchorId);
        return fragment;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 10;
    }
}
