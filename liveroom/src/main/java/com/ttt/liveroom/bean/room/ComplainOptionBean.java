package com.ttt.liveroom.bean.room;

import java.util.List;

/**
 * Created by mrliu on 2018/6/5.
 * 此类用于:
 */

public class ComplainOptionBean {


    /**
     * totalCount : 4
     * page : 1
     * size : 10
     * pageCount : 1
     * list : [{"id":"200002","content":"投诉2","created":"1525949816"},{"id":"200004","content":"投诉4","created":"1525949316"},{"id":"200003","content":"投诉3","created":"1525949118"},{"id":"200001","content":"投诉1","created":"1525949116"}]
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

    public static class ListBean {
        /**
         * id : 200002
         * content : 投诉2
         * created : 1525949816
         */

        private String id;
        private String content;
        private String created;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }
    }
}
