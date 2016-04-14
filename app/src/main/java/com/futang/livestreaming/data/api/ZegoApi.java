package com.futang.livestreaming.data.api;

import android.content.Context;

import com.zego.zegoavkit.ZegoAVApi;
import com.zego.zegoavkit.ZegoAVConfig;

public class ZegoApi {
    static public synchronized ZegoAVApi getAPI() {
        if (_api == null) {
            _api = new ZegoAVApi();
        }

        return _api;
    }

    static public synchronized void releaseApi() {
        if (_api != null) {
            _api.unInitSDK();
            _api = null;
        }
    }

    static public synchronized void setupAVKit(Context ctx) {
        getAPI().setLogLevel(ctx, ZegoAVApi.LOG_LEVEL_DEBUG, null);

        byte[] signKey = {
                (byte)0x91,(byte)0x93,(byte)0xcc,(byte)0x66,(byte)0x2a,(byte)0x1c,(byte)0xe,(byte)0xc1,
                (byte)0x35,(byte)0xec,(byte)0x71,(byte)0xfb,(byte)0x7,(byte)0x19,(byte)0x4b,(byte)0x38,
                (byte)0x15,(byte)0xf1,(byte)0x43,(byte)0xf5,(byte)0x7c,(byte)0xd2,(byte)0xb5,(byte)0x9a,
                (byte)0xe3,(byte)0xdd,(byte)0xdb,(byte)0xe0,(byte)0xf1,(byte)0x74,(byte)0x36,(byte)0xd
        };
        int appID = 1;

        getAPI().initSDK(appID, signKey, ctx);

        ZegoAVConfig config = new ZegoAVConfig();
        config.setResolution(ZegoAVConfig.VideoResolution.R_640x480);
        getAPI().setAVConfig(config);
    }

    static ZegoAVApi _api = null;
}
