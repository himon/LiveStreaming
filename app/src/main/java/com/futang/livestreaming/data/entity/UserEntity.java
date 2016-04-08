package com.futang.livestreaming.data.entity;

/**
 * Created by Administrator on 2016/4/7.
 */
public class UserEntity extends BaseEntity{

    /**
     * id : 13
     * loginId : 18923546789
     * qQ :
     * weixin :
     * token : 99ffdc71a51ff9a37a4e4b0098540bd0
     * loginPwd :
     * userName :
     * loginType : 0
     * age : 20
     * sex :
     * address :
     * phone : 18923546789
     * type : 0
     * money : 0
     * time : 2016-04-07T13:40:11.0000000+08:00
     * picture :
     * signature :
     * isDelete : 0
     * recommendUser : 13289647634
     * isCompany : 0
     * remark :
     */

    private BodyBean body;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class BodyBean {
        private String id;
        private String loginId;
        private String qQ;
        private String weixin;
        private String token;
        private String loginPwd;
        private String userName;
        private String loginType;
        private int age;
        private String sex;
        private String address;
        private String phone;
        private int type;
        private int money;
        private String time;
        private String picture;
        private String signature;
        private int isDelete;
        private String recommendUser;
        private int isCompany;
        private String remark;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLoginId() {
            return loginId;
        }

        public void setLoginId(String loginId) {
            this.loginId = loginId;
        }

        public String getQQ() {
            return qQ;
        }

        public void setQQ(String qQ) {
            this.qQ = qQ;
        }

        public String getWeixin() {
            return weixin;
        }

        public void setWeixin(String weixin) {
            this.weixin = weixin;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getLoginPwd() {
            return loginPwd;
        }

        public void setLoginPwd(String loginPwd) {
            this.loginPwd = loginPwd;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getLoginType() {
            return loginType;
        }

        public void setLoginType(String loginType) {
            this.loginType = loginType;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public int getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(int isDelete) {
            this.isDelete = isDelete;
        }

        public String getRecommendUser() {
            return recommendUser;
        }

        public void setRecommendUser(String recommendUser) {
            this.recommendUser = recommendUser;
        }

        public int getIsCompany() {
            return isCompany;
        }

        public void setIsCompany(int isCompany) {
            this.isCompany = isCompany;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
