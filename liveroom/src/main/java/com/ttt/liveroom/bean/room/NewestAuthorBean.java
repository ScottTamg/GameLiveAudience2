package com.ttt.liveroom.bean.room;

import java.util.List;

/**
 * Created by 刘景 on 2017/01/12.
 */

public class NewestAuthorBean {
    /**
     * totalCount : 4
     * page : 1
     * size : 10
     * pageCount : 1
     * list : [{"userId":100003,"avatar":"","nickName":"邓斌","level":1,"description":"","latitude":"39.914965","longitude":"116.404585","distance":"0m"},{"userId":100005,"avatar":"http://www.baidu.com/100.jpg","nickName":"nickname","level":1,"description":"","latitude":"40.085829","longitude":"116.424694","distance":"19.08Km"},{"userId":100004,"avatar":"http://www.baidu.com/100.jpg","nickName":"nickname","level":1,"description":"","latitude":"40.085829","longitude":"116.374694","distance":"19.17Km"},{"userId":100002,"avatar":"http://www.baidu.com/100.jpg","nickName":"nickname","level":1,"description":"","latitude":"40.085829","longitude":"116.354694","distance":"19.47Km"}]
     */

    private int totalCount;
    private int page;
    private int size;
    private int pageCount;
    private List<ListBean> list;

    public int getTotal_cnt() {
        return totalCount;
    }

    public void setTotal_cnt(int total_cnt) {
        this.totalCount = totalCount;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPage_cnt() {
        return pageCount;
    }

    public void setPage_cnt(int page_cnt) {
        this.pageCount = page_cnt;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * userId : 100003
         * avatar :
         * nickName : 邓斌
         * level : 1
         * description :
         * latitude : 39.914965
         * longitude : 116.404585
         * distance : 0m
         */

        private String userId;
        private String avatar;
        private String nickName;
        private int level;
        private String description;
        private String imgSrc;
        private String title;
        private String isLive ;
        private String pullRtmp;
        private String roomId;
        private String viewerNum;
        private String id ;
        private String startTime ;
        private String type;

        public String getViewerNum() {
            return viewerNum;
        }

        public void setViewerNum(String viewerNum) {
            this.viewerNum = viewerNum;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }


        public String getImgSrc() {
            return imgSrc;
        }

        public void setImgSrc(String imgSrc) {
            this.imgSrc = imgSrc;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIsLive() {
            return isLive;
        }

        public void setIsLive(String isLive) {
            this.isLive = isLive;
        }

        public String getPullRtmp() {
            return pullRtmp;
        }

        public void setPullRtmp(String pullRtmp) {
            this.pullRtmp = pullRtmp;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "ListBean{" +
                    "userId=" + userId +
                    ", avatar='" + avatar + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", level=" + level +
                    ", description='" + description + '\'' +
                    ", viewerNum='" + viewerNum + '\'' +
                    ", id='" + id + '\'' +
                    ", startTime='" + startTime + '\'' +
                    ", imgSrc='" + imgSrc + '\'' +
                    ", title='" + title + '\'' +
                    ", roomId='" + roomId + '\'' +
                    ", isLive=" + isLive +
                    ", pullRtmp=" + pullRtmp +
                    ", type=" + type +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NewestAuthorBean{" +
                "totalCount=" + totalCount +
                ", page=" + page +
                ", size=" + size +
                ", pageCount=" + pageCount +
                ", list=" + list +
                '}';
    }

}
