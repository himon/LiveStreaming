package com.futang.livestreaming.data.entity;

import java.util.List;

/**
 * Created by lc on 16/4/25.
 */
public class WatcherPicEntity extends BaseEntity {


    private List<String> body;

    public void setBody(List<String> body) {
        this.body = body;
    }

    public List<String> getBody() {
        return body;
    }
}
