package com.futang.livestreaming.data.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/4/14.
 */
public class RoomEntity extends BaseEntity {


    /**
     * id : 1
     * roomId : 11111111
     * liveType : 1
     * userId : 1
     * liveName : 111111
     * viewNum : 0
     * liveMoney : 0
     * likeNum : 0
     * startTime : 2016-04-13T13:38:05.0000000+08:00
     * isCompany : 0
     * picture : http://112.74.21.82/livePic/cd46e635190644bda93298132197e318.jpg
     * userName : 继续测试
     * userPicture : http://112.74.21.82/avatar/23bf966367cf4ab5950d5ef51b8d4862.jpg
     */

    private List<BodyBean> body;

    public List<BodyBean> getBody() {
        return body;
    }

    public void setBody(List<BodyBean> body) {
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
        private String userName;
        private String userPicture;

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

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPicture() {
            return userPicture;
        }

        public void setUserPicture(String userPicture) {
            this.userPicture = userPicture;
        }
    }
}
