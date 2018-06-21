package com.ttt.liveroom.websocket;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ttt.liveroom.bean.websocket.BlackListRes;
import com.ttt.liveroom.bean.websocket.DisConnectLmMsg;
import com.ttt.liveroom.bean.websocket.ErrorMsg;
import com.ttt.liveroom.bean.websocket.GagResResponse;
import com.ttt.liveroom.bean.websocket.LmAgreeOrRefuseRes;
import com.ttt.liveroom.bean.websocket.ResponseMicBean;
import com.ttt.liveroom.bean.websocket.SendGiftMsg;
import com.ttt.liveroom.bean.websocket.SystemWelcome;
import com.ttt.liveroom.bean.websocket.UserPublicMsg;
import com.ttt.liveroom.bean.websocket.WsGiftMsg;
import com.ttt.liveroom.bean.websocket.WsLoginMsg;
import com.ttt.liveroom.bean.websocket.WsLoginOutMsg;
import com.ttt.liveroom.bean.websocket.WsLoginRequest;
import com.ttt.liveroom.bean.websocket.WsLogoutRequest;
import com.ttt.liveroom.bean.websocket.WsPongMsg;
import com.ttt.liveroom.bean.websocket.WsRequest;
import com.ttt.liveroom.net.Constants;
import com.ttt.liveroom.util.CustomToast;
import com.ttt.liveroom.util.L;
import com.ttt.liveroom.util.troubleshoot.NetworkDiagnosisService;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.URI;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.WebSocket;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author liujing
 * Created by 刘景 on 2017/05/11.
 */
public class WebSocketService extends Service {

    private static final String LOG_TAG = WebSocketService.class.getSimpleName();

    private static final int PONG_INTERVAL_SECONDS = 10;
    private static final int SELF_CHECK_INTERVAL_SECONDS = 20;

    /**
     * 连续尝试连接次数，超过限制时触发手动诊断和上报过程。
     */
    private int connectionAttemptCount = 0;

    /**
     * 最高允许的连续失败次数。
     * 达到这个次数将立即触发上报操作。
     */
    private static final int ATTEMPT_TOLERANCE = 2;

    public static Intent createIntent(Context context) {
        return new Intent(context, WebSocketService.class);
    }

    private WebSocketClient webSocket;
    /**
     * 为避免用户空闲太久导致WebSocket连接被服务器断开，需要定期向服务器发送Pong请求。
     * <p>Pong请求不需要服务器回复，相应地，服务器下发的Ping请求也不需要处理。</p>
     *
     * @see #PONG_INTERVAL_SECONDS
     */
    private ScheduledExecutorService pongService;
    /**
     * 有时候会因为一些数据传输异常【如重复登录房间、或数据出现错误等】导致被服务器强行断开连接。
     * 为了避免这种情况下用户毫无察觉地不可用，在WebSocket初始化后创建一个定时自检的Service。
     * <p>
     * 为什么不能单纯依靠OnClose方法来完成重连？
     * 因为OnClose方法里的重连可能连接失败！失败后就再也没有OnClose了！
     */
    private Subscription selfCheckSubscription;

    /**
     * 标记是否正在连接中，用于自检服务避免重复发起连接。
     */
    private boolean isAttemptConnecting;

    /**
     * 标识准备关闭Service。
     * 一旦这个标记为true,则onclose方法里不能再发起重连操作
     */
    private boolean preparedShutdown = false;

    /**
     * 标记是否需要自动重连。
     */
    private boolean shouldAutoReconnect;
    /**
     * 标记是否需要自动重新登录。
     * 这个标记主要用于被管理员踢出房间之后，要维持Ws连接但不能再次自动登录该房间。
     */
    private boolean shouldAutoRelogin;

//    private final Object lockForErrorHandle = new Object();

    /**
     * 由于断线后重连需要重新登录而不希望退出房间，所以这里缓存最新的登录请求。
     * 注意,即使在发起登录的时候没有登录成功，也要保存这个请求。
     * 下面两个时间点需要清除这个缓存的请求：1）发起登出请求的时候；2）准备关闭Service的时候。
     */
    private WsLoginRequest cachedLoginRequest;

    private HashMap<String, WsListener<?>> activeListener = new HashMap<>();
    private Gson gson = new Gson();

    @Override
    public void onCreate() {
        super.onCreate();
        L.i(LOG_TAG, "----- onCreate -----");
        initSocketWrapper("InitialConnect", true);
        startSelfCheckService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Use this to force restart service
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        L.v(LOG_TAG, "----- onBind -----");
        return new ServiceBinder();
    }

    @Override
    public void onDestroy() {
        L.i(LOG_TAG, "----- onDestroy -----");
        super.onDestroy();
    }

    /**
     * 准备关闭Service。这个方法调用会关闭所有自检和Pong服务，调用者应当尽快解除对Service的连接并调用stopService。
     */
    public void prepareShutdown() {
        L.i(LOG_TAG, "----- prepareShutdown -----");
        preparedShutdown = true;

        if (cachedLoginRequest != null) {
            cachedLoginRequest = null;
        }
        stopSelfCheckService();
        stopPongDaemonService();
        if (webSocket != null && webSocket.isOpen()) {
            webSocket.close();
        }
        if (activeListener.size() > 0) {
            L.w(LOG_TAG, "Force clear active listeners, count=%d", activeListener.size());
            activeListener.clear();
        }
    }

    private boolean checkSocketAvailable() {
        if (webSocket == null || (!webSocket.isOpen())) {
            L.e(LOG_TAG, "WebSocket not ready, ignore this operation!");
            return false;
        }
        return true;
    }

    /**
     * Register listener for specified type.
     *
     * @param event    Event name. see {@link SocketConstants}
     * @param listener see {@link WsListener}
     */
    public void registerListener(@NonNull String event, @NonNull WsListener listener) {
        activeListener.put(event, listener);
    }

    /**
     * Remove all listeners.
     */
    public void removeAllListeners() {
        L.i(LOG_TAG, "Removing all listeners, count=%d. ", activeListener.size());
        activeListener.clear();
    }

    /**
     * Send request to server.
     *
     * @param request see {@link WsRequest}
     */
    public void sendRequest(@NonNull WsRequest request) {
        if (request instanceof WsLoginRequest) {
            //update value
            cachedLoginRequest = (WsLoginRequest) request;
        } else if (request instanceof WsLogoutRequest) {
            //clear value
            cachedLoginRequest = null;
        }

        if (!checkSocketAvailable()) {
            return;
        }
        Gson gson = new Gson();
        String msg = gson.toJson(request);
        Log.e("sendRequest", msg);
        webSocket.send(msg);
    }

    private void initSocketWrapper(String forReason) {
        initSocketWrapper(forReason, false);
    }

    private void initSocketWrapper(final String forReason, final boolean isFirstConnect) {
        Observable.just(forReason)
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        //如果正在连接则屏蔽该次消息
                        if (isAttemptConnecting) {

                            return Boolean.FALSE;
                        }
                        return Boolean.TRUE;
                    }
                })
                //强制跳转主线程做通知操作
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (webSocket != null && !isFirstConnect && !isAttemptConnecting) {
                            notifyUiWsStatus("服务器连接中断，正在重连……");
                        }
                    }
                })
                //跳转IO线程做操作
                .observeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {

                        initSocket();
                    }
                });
    }

    /**
     * Never call this method directly!
     * call {@link #initSocketWrapper(String)} instead.
     */
    private void initSocket() {
        //0.12.0 开始在onClose里也会自动发起重连，因此将attemptConnecting状态的维护放在这个地方。
        if (isAttemptConnecting) {
            return;
        }
        isAttemptConnecting = true;
        L.v(LOG_TAG, "Set isAttemptConnecting flag to true");
        Observable.create(new Observable.OnSubscribe<WebSocket>() {
            @Override
            public void call(final Subscriber<? super WebSocket> subscriber) {
                try {
                    connectionAttemptCount++;
                    isAttemptConnecting = false;
                    L.d(LOG_TAG, "Connection attempt:%d", connectionAttemptCount);
                    L.e(LOG_TAG, Constants.SOCKET_URL);

                    webSocket = new WebSocketClient(new URI(Constants.SOCKET_URL)) {
                        @Override
                        public void onOpen(ServerHandshake handshakedata) {
                            L.i(LOG_TAG, "onCompleted");
                        }

                        @Override
                        public void onMessage(String message) {
                            Log.v(LOG_TAG, "received msg:==" + message);
                            dispatchMessage(message);
                        }

                        @Override
                        public void onClose(int code, String reason, boolean remote) {
                            L.i(LOG_TAG, "ClosedCallback: WebSocket closed.");
                            //不做任何操作，等待自检服务重启Socket，或者自然死掉
                            //Update: 0.12.0 需要自动重连，不等待服务。
                            if ((!preparedShutdown) && (shouldAutoReconnect)) {
                                initSocketWrapper("onClose");
                            }
                        }

                        @Override
                        public void onError(Exception ex) {
                            subscriber.onError(ex != null ? ex : new ConnectException
                                    ("Cannot connect ws service!"));
                        }
                    };
                    webSocket.connect();
                } catch (Exception e) {
                    Log.e("websocket", "Cannot connect ws service");
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WebSocket>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        L.w(LOG_TAG, "WebSocket init failed!");
                        e.printStackTrace();
                        //判断是否需要执行诊断服务
                        if (connectionAttemptCount >= ATTEMPT_TOLERANCE) {
                            L.e(LOG_TAG, "Continuous connection error occurred for %d times!",
                                    connectionAttemptCount);
                            L.i(LOG_TAG, "Force starting diagnosis service");
                            startService(new Intent(WebSocketService.this,
                                    NetworkDiagnosisService.class));
                            //重置标记
                            connectionAttemptCount = 0;
                        }

                    }

                    @Override
                    public void onNext(WebSocket webSocket) {

                        if (pongService == null) {
                            startPongDaemonService();
                        }
                        //如果缓存的登录请求不为空，则应该是房间内异常断线后的重连，自动执行重新登录
                        if (/*shouldAutoRelogin&&*/cachedLoginRequest != null) {

                            sendRequest(cachedLoginRequest);
                        }
                    }
                });
    }

    private void notifyUiWsStatus(String msg) {
        Observable.just(msg)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        CustomToast.makeCustomText(getApplicationContext(), s, Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    /**
     * 启动自检服务。自检服务会立即执行，之后按周期执行。
     */
    private void startSelfCheckService() {
//        //为安全起见先解除之前的订阅
//        stopSelfCheckService();
        //订阅新的自检服务
        selfCheckSubscription = Observable.interval(SELF_CHECK_INTERVAL_SECONDS, SELF_CHECK_INTERVAL_SECONDS, TimeUnit.SECONDS)
                .filter(new Func1<Long, Boolean>() {
                    @Override
                    public Boolean call(Long aLong) {
                        if (!shouldAutoReconnect) {
                            L.w(LOG_TAG, "Auto reconnect has been disabled, maybe kicked?");
                        }
                        return shouldAutoReconnect;
                    }
                })
                .map(new Func1<Long, Boolean>() {
                    @Override
                    public Boolean call(Long aLong) {
                        return (webSocket != null) && (webSocket.isOpen());
                    }
                })
                .subscribeOn(Schedulers.computation())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        L.i(LOG_TAG, "Self check task has been scheduled per %d seconds.",
                                SELF_CHECK_INTERVAL_SECONDS);
                        shouldAutoReconnect = true;
                        L.i(LOG_TAG, "Auto reconnect feature has been enabled.");
                    }
                })
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean websocketAlive) {
                        if (websocketAlive) {
                            L.v(false, LOG_TAG, "WebSocket self check: is alive.");
                            return;
                        }
                        initSocketWrapper("SelfCheckService");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        L.e(LOG_TAG, "Error while executing self check!", throwable);
                    }
                });
    }

    /**
     * 停止自检服务
     */
    private void stopSelfCheckService() {
        if (selfCheckSubscription != null && (!selfCheckSubscription.isUnsubscribed())) {
            selfCheckSubscription.unsubscribe();
            L.i(LOG_TAG, "Self check service has been unSubscribed.");
        }
    }

    private void startPongDaemonService() {
        pongService = Executors.newSingleThreadScheduledExecutor();
        pongService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                if (webSocket != null && webSocket.isOpen()) {
                    //sendRequest(WsObjectPool.newPongRequest());
                } else {
                    L.v(LOG_TAG, "WebSocket is not ready, cancel sending pong msg.");
                }
            }
        }, PONG_INTERVAL_SECONDS, PONG_INTERVAL_SECONDS, TimeUnit.SECONDS);
        L.i(LOG_TAG, "Pong service has been scheduled at %s seconds delay.", PONG_INTERVAL_SECONDS);
    }

    /**
     * 停止心跳
     */
    private void stopPongDaemonService() {
        if (pongService != null && (!pongService.isShutdown())) {
            pongService.shutdownNow();
            L.i(LOG_TAG, "Shutdown pong service now.");
        }
    }

    //    这里是得到信息
    private void dispatchMessage(String msg) {
        Log.e("dispatchMessage", msg);
        String type = null;
        try {
            JSONObject json = new JSONObject(msg);
            type = json.optString(SocketConstants.FIELD_TYPE);
        } catch (JSONException e) {
            L.e(LOG_TAG, "Message is not well-formed data!");
        }
        if (TextUtils.isEmpty(type)) {
            L.e(LOG_TAG, "Cannot parse type from msg!");
            return;
        }
        try {
            switch (type) {
                case SocketConstants.EVENT_LOGIN_RSP:
                    notifyListener(msg, SocketConstants.EVENT_LOGIN_RSP, WsLoginMsg.class);
                    break;
                case SocketConstants.EVENT_PUB_MSG_RSP:
                    notifyListener(msg, SocketConstants.EVENT_PUB_MSG_RSP, UserPublicMsg.class);
                    break;
                case SocketConstants.EVENT_SYSWElCOME:
                    notifyListener(msg, SocketConstants.EVENT_SYSWElCOME, SystemWelcome.class);//系统欢迎
                    break;
                case SocketConstants.EVENT_PONG_RSP:
                    notifyListener(msg, SocketConstants.EVENT_PONG_RSP, WsPongMsg.class);//心跳包
                    break;
                case SocketConstants.EVENT_SEND_GIFT_RSP:
                    notifyListener(msg, SocketConstants.EVENT_SEND_GIFT_RSP, WsGiftMsg.class);
                    break;
                case SocketConstants.EVENT_NOTIFY_GIFT_RSP:
                    notifyListener(msg, SocketConstants.EVENT_NOTIFY_GIFT_RSP, SendGiftMsg.class);
                    break;
                case SocketConstants.EVENT_LOGOUT_RSP:
                    notifyListener(msg, SocketConstants.EVENT_LOGOUT_RSP, WsLoginOutMsg.class);
                    break;
                case SocketConstants.APPLY_MIC_RESPONSE:
                    notifyListener(msg, SocketConstants.APPLY_MIC_RESPONSE, ResponseMicBean.class);//主播获取观众的请求连麦集合
                    break;
//                case SocketConstants.APPLY_MIC_RESPONSE_RSP:
//                    notifyListener(msg, SocketConstants.APPLY_MIC_RESPONSE_RSP, RsqMicBean.class);//观众获取主播是否同意连麦的响应
//                    break;
                case SocketConstants.DISCONNECT_LM_REQUEST_RSP:
                    notifyListener(msg, SocketConstants.DISCONNECT_LM_REQUEST_RSP, DisConnectLmMsg.class);
                    break;
                case SocketConstants.GAG_RES_RESPONSE:
                    notifyListener(msg, SocketConstants.GAG_RES_RESPONSE, GagResResponse.class);
                    break;
                case SocketConstants.BLACK_LIST_RES:
                    notifyListener(msg, SocketConstants.BLACK_LIST_RES, BlackListRes.class);
                    break;
                case SocketConstants.LM_AGREE_OR_REFUSE_RES:
                    notifyListener(msg, SocketConstants.LM_AGREE_OR_REFUSE_RES, LmAgreeOrRefuseRes.class);
                    break;
                default:
                    if (type.startsWith(SocketConstants.EVENT_ERROR)) {
                        notifyListener(msg, SocketConstants.EVENT_ERROR, ErrorMsg.class);
                        boolean shouldShutDown = shouldShutdownOnError(type);
                        L.i(LOG_TAG, "should shutdown for this error type?%s", shouldShutDown);
                        if (shouldShutDown) {
                            shouldAutoReconnect = false;
                            L.i(LOG_TAG, "Auto reconnect feature has been disabled.");
                        }
                        shouldAutoRelogin = canAutoReloginOnError(type);
                        L.i(LOG_TAG, "should relogin for this error type?%s", shouldAutoRelogin);
                    } else {
                        L.e(LOG_TAG, "Unsupported msg type:%s", type);
                    }
            }
        } catch (Exception e) {
            L.e(LOG_TAG, "Unexpected exception while dispatching msg.", e);
        }
    }

    private boolean shouldShutdownOnError(String type) {

        return false;
    }

    private boolean canAutoReloginOnError(String type) {
//        if (SocketConstants.ERROR_KICKED.equalsIgnoreCase(type)){
//            return false;
//        }
        return true;
    }

    /**
     * Notify active listener to handle data, if no listener matches, just discard.
     */
    @SuppressWarnings("unchecked")
    private <T> void notifyListener(String msg, final String type, final Class<T> clzData) {
        //transfer this to main thread
        Observable.just(msg)
                .map(new Func1<String, T>() {
                    @Override
                    public T call(String s) {
                        if (type == SocketConstants.EVENT_SEND_GIFT) {

                        }
                        return gson.fromJson(s, clzData);
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {
                    @Override
                    public void onCompleted() {
                        //Empty...
                    }

                    @Override
                    public void onError(Throwable e) {
                        //Auto upload this error
                        L.e(LOG_TAG, "Ws Service has catch an error!", e);
                    }

                    @Override
                    public void onNext(T data) {
                        WsListener<T> listener = (WsListener<T>) activeListener.get(type);
                        if (listener == null) {
                            L.e(LOG_TAG, "No listener handle type %s, discard this.", type);
                            return;
                        }
                        L.d(LOG_TAG, "Msg entity:%s.", data);
                        listener.handleData(data);
                    }
                });
    }

    public class ServiceBinder extends Binder {
        public WebSocketService getService() {
            return WebSocketService.this;
        }
    }


}
