package com.ttt.liveroom.net.nohttp;

import com.yolanda.nohttp.rest.Response;

/**
 * Created by 刘景 on 2017/06/06.
 */

public interface HttpListener<T> {
    void onSucceed(int what, Response<T> response);

    void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis);
}
