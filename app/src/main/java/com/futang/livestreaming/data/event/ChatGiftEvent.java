package com.futang.livestreaming.data.event;

/**
 * Created by lc on 16/4/25.
 */
public class ChatGiftEvent {

    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ChatGiftEvent(int index) {
        this.index = index;
    }
}
