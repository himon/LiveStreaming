package com.futang.livestreaming.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/4/18.
 */
public class GiftEntity extends BaseEntity{

    /**
     * id : 10
     * giftName : 一生一世
     * giftMoney : 1314
     * pictureUrl : http://112.74.21.82/GiftPic/4146a1df56844008bb5300ba3737c44a.png
     * type : 0
     * isCompany : 0
     */

    private List<BodyBean> body;

    public List<BodyBean> getBody() {
        return body;
    }

    public void setBody(List<BodyBean> body) {
        this.body = body;
    }

    public static class BodyBean implements Parcelable {
        private int id;
        private String giftName;
        private int giftMoney;
        private String pictureUrl;
        private int type;
        private int isCompany;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGiftName() {
            return giftName;
        }

        public void setGiftName(String giftName) {
            this.giftName = giftName;
        }

        public int getGiftMoney() {
            return giftMoney;
        }

        public void setGiftMoney(int giftMoney) {
            this.giftMoney = giftMoney;
        }

        public String getPictureUrl() {
            return pictureUrl;
        }

        public void setPictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getIsCompany() {
            return isCompany;
        }

        public void setIsCompany(int isCompany) {
            this.isCompany = isCompany;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.giftName);
            dest.writeInt(this.giftMoney);
            dest.writeString(this.pictureUrl);
            dest.writeInt(this.type);
            dest.writeInt(this.isCompany);
        }

        public BodyBean() {
        }

        protected BodyBean(Parcel in) {
            this.id = in.readInt();
            this.giftName = in.readString();
            this.giftMoney = in.readInt();
            this.pictureUrl = in.readString();
            this.type = in.readInt();
            this.isCompany = in.readInt();
        }

        public static final Parcelable.Creator<BodyBean> CREATOR = new Parcelable.Creator<BodyBean>() {
            @Override
            public BodyBean createFromParcel(Parcel source) {
                return new BodyBean(source);
            }

            @Override
            public BodyBean[] newArray(int size) {
                return new BodyBean[size];
            }
        };
    }
}
