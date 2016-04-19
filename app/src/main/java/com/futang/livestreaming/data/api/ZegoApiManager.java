package com.futang.livestreaming.data.api;


import android.content.Context;

import com.zego.zegoavkit.ZegoAVApi;
import com.zego.zegoavkit.ZegoAvConfig;
import com.zego.zegoavkit.ZegoUser;
import com.zego.zegorealtimeavkit.ZegoVideoTalk;

/**
 * des: zego api管理器.
 */
public class ZegoApiManager {

    private static ZegoApiManager sInstance = null;

    private ZegoVideoTalk mZegoVideoTalk = null;

    private ZegoUser mZegoUser;

    private ZegoAvConfig mZegoAVConfig;

    private ZegoApiManager() {

        mZegoVideoTalk = new ZegoVideoTalk();

        // 初始化用户信息
        mZegoUser = new ZegoUser();
        long ms = System.currentTimeMillis();
        mZegoUser.name = "User" + ms;
        mZegoUser.id = ms + "";

        // 初始化配置信息
        mZegoAVConfig = new ZegoAvConfig();
        mZegoAVConfig.setResolution(640, 480);

    }

    public static ZegoApiManager getInstance() {
        if (sInstance == null) {
            synchronized (ZegoApiManager.class) {
                if (sInstance == null) {
                    sInstance = new ZegoApiManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 初始化sdk.
     */
    public void initSDK(Context context) {
        // 设置日志level
        mZegoVideoTalk.setLogLevel(context, ZegoAVApi.LOG_LEVEL_DEBUG, null);

        // 即构分配的key与id
        byte[] signKey = {
                (byte) 0x91, (byte) 0x93, (byte) 0xcc, (byte) 0x66, (byte) 0x2a, (byte) 0x1c, (byte) 0xe, (byte) 0xc1,
                (byte) 0x35, (byte) 0xec, (byte) 0x71, (byte) 0xfb, (byte) 0x7, (byte) 0x19, (byte) 0x4b, (byte) 0x38,
                (byte) 0x15, (byte) 0xf1, (byte) 0x43, (byte) 0xf5, (byte) 0x7c, (byte) 0xd2, (byte) 0xb5, (byte) 0x9a,
                (byte) 0xe3, (byte) 0xdd, (byte) 0xdb, (byte) 0xe0, (byte) 0xf1, (byte) 0x74, (byte) 0x36, (byte) 0xd
        };
        int appID = 1;

        // 初始化sdk
        mZegoVideoTalk.initSDK(appID, signKey, context);

        mZegoVideoTalk.setZegoUser(mZegoUser);
        mZegoVideoTalk.setAVConfig(mZegoAVConfig);

    }


    /**
     * 释放sdk.
     */
    public void releaseSDK() {
        mZegoVideoTalk.unInitSDK();
        mZegoVideoTalk = null;
        sInstance = null;
    }

    public ZegoVideoTalk getZegoVideoTalk() {
        return mZegoVideoTalk;
    }

    public ZegoUser getZegoUser() {
        return mZegoUser;
    }

    public ZegoAvConfig getZegoAVConfig() {
        return mZegoAVConfig;
    }

    public void setUser(ZegoUser user){
        mZegoVideoTalk.setZegoUser(user);
    }

    public void setConfig(ZegoAvConfig config){
        mZegoVideoTalk.setAVConfig(config);
    }
}
