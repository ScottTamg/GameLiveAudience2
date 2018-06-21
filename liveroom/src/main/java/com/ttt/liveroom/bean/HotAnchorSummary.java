package com.ttt.liveroom.bean;

import java.util.List;

/**
 * Created by 刘景 on 2017/01/12.
 * 主播简介,用于热门主播,附近,关注主播
 */

public class HotAnchorSummary {

    /**
     * totalCount : 151
     * page : 1
     * size : 10
     * pageCount : 16
     * list : [{"id":"300043","userId":"400001","roomId":"400001","startTime":"2018.01.16 11:27","imgSrc":"http://3tlive.oss-cn-beijing.aliyuncs.com/publishliveIMG_20180116_112727.png","title":"你呢","isLive":"1","avatar":"","nickName":"186****0467","level":1,"description":""},{"id":"400037","userId":"400001","roomId":"400001","startTime":"2018.01.16 11:14","imgSrc":"http://3tlive.oss-cn-beijing.aliyuncs.com/publishliveIMG_20180116_111442.png","title":"m'q","isLive":"1","avatar":"","nickName":"186****0467","level":1,"description":""},{"id":"400036","userId":"400001","roomId":"400001","startTime":"2018.01.16 11:12","imgSrc":"http://3tlive.oss-cn-beijing.aliyuncs.com/publishliveIMG_20180116_111212.png","title":"开播","isLive":"1","avatar":"","nickName":"186****0467","level":1,"description":""},{"id":"400035","userId":"400001","roomId":"400001","startTime":"2018.01.16 11:05","imgSrc":"http://3tlive.oss-cn-beijing.aliyuncs.com/publishliveIMG_20180116_110534.png","title":"共同","isLive":"1","avatar":"","nickName":"186****0467","level":1,"description":""},{"id":"400034","userId":"400001","roomId":"400001","startTime":"2018.01.16 11:01","imgSrc":"http://3tlive.oss-cn-beijing.aliyuncs.com/publishliveIMG_20180116_110153.png","title":"经济","isLive":"1","avatar":"","nickName":"186****0467","level":1,"description":""},{"id":"300042","userId":"1","roomId":"34","startTime":"2018.01.16 10:42","imgSrc":"","title":"","isLive":"1","avatar":"","nickName":"","level":0,"description":""},{"id":"300041","userId":"1","roomId":"34","startTime":"2018.01.16 10:36","imgSrc":"","title":"","isLive":"1","avatar":"","nickName":"","level":0,"description":""},{"id":"400033","userId":"1","roomId":"34","startTime":"2018.01.16 10:24","imgSrc":"","title":"","isLive":"1","avatar":"","nickName":"","level":0,"description":""},{"id":"300040","userId":"1","roomId":"34","startTime":"2018.01.16 10:19","imgSrc":"","title":"","isLive":"1","avatar":"","nickName":"","level":0,"description":""},{"id":"400032","userId":"3456","roomId":"3456","startTime":"2018.01.15 21:01","imgSrc":"","title":"","isLive":"1","avatar":"","nickName":"","level":0,"description":""}]
     */
    private int totalCount;
    private int page;
    private int size;
    private int pageCount;
    private List<ListBean> list;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
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

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }


    @Override
    public String toString() {
        return "HotAnchorSummary{" +
                "totalCount=" + totalCount +
                ", page=" + page +
                ", size=" + size +
                ", pageCount=" + pageCount +
                ", list=" + list +
                '}';
    }

    public static class ListBean {
        /**
         * id : 300043
         * userId : 400001
         * roomId : 400001
         * startTime : 2018.01.16 11:27
         * imgSrc : http://3tlive.oss-cn-beijing.aliyuncs.com/publishliveIMG_20180116_112727.png
         * title : 你呢
         * isLive : 1
         * avatar :
         * nickName : 186****0467
         * level : 1
         * description :
         */

        private String id;
        private String userId;
        private String roomId;
        private String startTime;
        private String imgSrc;
        private String title;
        private String isLive;
        private String avatar;
        private String nickName;
        private int level;
        private String description;
        private String pullRtmp;
        private String viewerNum;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
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


        public String getPullRtmp() {
            return pullRtmp;
        }

        public void setPullRtmp(String pullRtmp) {
            this.pullRtmp = pullRtmp;
        }

        public String getViewerNum() {
            return viewerNum;
        }

        public void setViewerNum(String viewerNum) {
            this.viewerNum = viewerNum;
        }

        @Override
        public String toString() {
            return "ListBean{" +
                    "id='" + id + '\'' +
                    ", userId='" + userId + '\'' +
                    ", roomId='" + roomId + '\'' +
                    ", startTime='" + startTime + '\'' +
                    ", imgSrc='" + imgSrc + '\'' +
                    ", title='" + title + '\'' +
                    ", isLive='" + isLive + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", level=" + level +
                    ", description='" + description + '\'' +
                    ", pullRtmp='" + pullRtmp + '\'' +
                    ", viewerNum='" + viewerNum + '\'' +
                    '}';
        }
    }
}
