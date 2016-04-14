package com.futang.livestreaming.data.entity;

/**
 * Created by Administrator on 2016/4/13.
 */
public class CreateRoomEntity extends BaseEntity{

    /**
     * id : 0
     * roomId : 111
     * liveType : 1
     * userId : 1
     * liveName : 111111
     * viewNum : 0
     * liveMoney : 0
     * likeNum : 0
     * startTime : 2016-04-13T11:44:03.3071056+08:00
     * isCompany : 0
     * picture : http://112.74.21.82/livePic/0343023de9ea4ca7a8fd296e14574cce.jpg
     */

    private BodyBean body;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class BodyBean {
        private int id;
        private String roomId;
        private int liveType;
        private int userId;
        private String liveName;
        private int viewNum;
        private int liveMoney;
        private int likeNum;
        private String startTime;
        private int isCompany;
        private String picture;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public int getLiveType() {
            return liveType;
        }

        public void setLiveType(int liveType) {
            this.liveType = liveType;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getLiveName() {
            return liveName;
        }

        public void setLiveName(String liveName) {
            this.liveName = liveName;
        }

        public int getViewNum() {
            return viewNum;
        }

        public void setViewNum(int viewNum) {
            this.viewNum = viewNum;
        }

        public int getLiveMoney() {
            return liveMoney;
        }

        public void setLiveMoney(int liveMoney) {
            this.liveMoney = liveMoney;
        }

        public int getLikeNum() {
            return likeNum;
        }

        public void setLikeNum(int likeNum) {
            this.likeNum = likeNum;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public int getIsCompany() {
            return isCompany;
        }

        public void setIsCompany(int isCompany) {
            this.isCompany = isCompany;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }
    }
}
