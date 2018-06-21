package com.ttt.liveroom.bean.websocket;

import com.google.gson.annotations.SerializedName;

/**
 * @author Muyangmin
 * @since 1.0.0
 */
public class WsLightHeartRequest extends WsRequest {

    @SerializedName("_method_")
    private String method;

    @SerializedName("color")
    private int colorIndex;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getColorIndex() {
        return colorIndex;
    }

    public void setColorIndex(int colorIndex) {
        this.colorIndex = colorIndex;
    }

    @Override
    public String toString() {
        return "WsLightHeartRequest{" +
                "method='" + method + '\'' +
                ", colorIndex=" + colorIndex +
                '}';
    }
}
