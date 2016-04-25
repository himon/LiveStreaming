package com.futang.livestreaming.data.event;

import com.futang.livestreaming.data.entity.GiftEntity;

/**
 * Created by lc on 16/4/21.
 */
public class LiveRoomEvent {

    private String msg;

    private String type;

    private int index;

    private GiftEntity.BodyBean gift;

    public GiftEntity.BodyBean getGift() {
        return gift;
    }

    public void setGift(GiftEntity.BodyBean gift) {
        this.gift = gift;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LiveRoomEvent(String type) {
        this.type = type;
    }
}
