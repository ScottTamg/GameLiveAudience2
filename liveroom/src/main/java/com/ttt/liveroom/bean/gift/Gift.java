package com.ttt.liveroom.bean.gift;

/**
 * 礼物实体类，标记礼物的内容、图片等。
 */
public class Gift {
    /**
     * id : 100001
     * name : 守护之心
     * imgSrc : http://3tdoc.oss-cn-beijing.aliyuncs.com/wechat/avatar/1.jpg
     * price : 10
     * created : 1509605685
     * updated : 1509605685
     */

    private String id;
    private String name;
    private String imgSrc;
    private String imgGif;
    private String price;
    private String level;
    private String isFire;
    private String created;
    private String updated;
    private String isDelete;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getImgGif() {
        return imgGif;
    }

    public void setImgGif(String imgGif) {
        this.imgGif = imgGif;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIsFire() {
        return isFire;
    }

    public void setIsFire(String isFire) {
        this.isFire = isFire;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String toString() {
        return "Gift{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imgSrc='" + imgSrc + '\'' +
                ", imgGif='" + imgGif + '\'' +
                ", price='" + price + '\'' +
                ", level='" + level + '\'' +
                ", isFire='" + isFire + '\'' +
                ", created='" + created + '\'' +
                ", updated='" + updated + '\'' +
                ", isDelete='" + isDelete + '\'' +
                '}';
    }
}