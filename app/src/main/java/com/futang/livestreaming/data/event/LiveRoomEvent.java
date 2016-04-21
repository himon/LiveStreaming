package com.futang.livestreaming.data.event;

/**
 * Created by lc on 16/4/21.
 */
public class LiveRoomEvent {

    private String msg;

    private String type;

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
