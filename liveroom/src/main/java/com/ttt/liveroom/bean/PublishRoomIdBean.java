package com.ttt.liveroom.bean;

/**
 * Created by mrliu on 2018/1/16.
 * 此类用于:
 */

public class PublishRoomIdBean {
    /**
     * liveId : 400050
     */
    private String streamId;
    private String pushRtmp;
    private String shareUrl;

    public String getLiveId() {
        return streamId;
    }

    public void setLiveId(String streamId) {
        this.streamId = streamId;
    }

    public String getPushRtmp() {
        return pushRtmp;
    }

    public void setPushRtmp(String pushRtmp) {
        this.pushRtmp = pushRtmp;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    @Override
    public String toString() {
        return "PublishRoomIdBean{" +
                "streamId=" + streamId +
                "shareUrl=" + shareUrl +
                ", pushRtmp='" + pushRtmp + '\'' +
                '}';
    }
}
