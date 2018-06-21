package com.ttt.liveroom.websocket;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.ttt.liveroom.base.DataManager;
import com.ttt.liveroom.bean.LoginInfo;
import com.ttt.liveroom.bean.UserInfo;
import com.ttt.liveroom.bean.websocket.ApplyMicBean;
import com.ttt.liveroom.bean.websocket.BlackListReq;
import com.ttt.liveroom.bean.websocket.CloseCallSecondaryReq;
import com.ttt.liveroom.bean.websocket.DisConnectLmRequest;
import com.ttt.liveroom.bean.websocket.GagReqRequest;
import com.ttt.liveroom.bean.websocket.LmAgreeOrRefuseReq;
import com.ttt.liveroom.bean.websocket.WsLoginRequest;
import com.ttt.liveroom.bean.websocket.WsLogoutRequest;
import com.ttt.liveroom.bean.websocket.WsPongRequest;
import com.ttt.liveroom.bean.websocket.WsPublicMsgRequest;
import com.ttt.liveroom.bean.websocket.WsRequest;
import com.ttt.liveroom.bean.websocket.WsSendGiftRequest;
import com.ttt.liveroom.util.L;

/**
 * Created by 刘景 on 2017/05/11.
 */

public class WsObjectPool {

    private static final String LOG_TAG = WsObjectPool.class.getSimpleName();
    private static SparseArray<WsRequest> requestArray;
    private static final int REQ_LOGIN = 1;
    private static final int REQ_LOGOUT = 2;
    private static final int REQ_SEND_PUB_MSG = 3;
    private static final int APPLY_MIC_REQUEST = 4;
    private static final int APPLY_MIC_RESPONE = 5;
    private static final int REQ_HEARTBEAT = 6;
    private static final int REQ_SENDGIFT = 7;
    private static final int REQ_DISLM = 8;
    private static final int REQ_GAG = 9;
    private static final int REQ_BLACK_LIST = 10;
    private static final int REQ_LM_AGREE = 11;
    private static final int REQ_CLOSE_CALL_SECONDARY = 12;

    private static String nickname;
    private static String userId;
    private static String token;
    private static String ucuid;

    /**
     * 释放所有资源，清空数据。
     */

    public static void release() {
        nickname = null;
        token = null;
        userId = null;
        if (requestArray != null) {
            requestArray.clear();
            requestArray = null;
        }
    }

    /**
     * 为用户初始化对象池。
     */

    public static void init(Context context, LoginInfo loginInfo) {
        String username = loginInfo.getNickname();
        String userId = loginInfo.getUserId();
        String token = loginInfo.getToken();

        WsObjectPool.token = token;
        if (userId == null) {
            String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            WsObjectPool.userId = androidId;
        } else {
            WsObjectPool.userId = userId;
        }
        WsObjectPool.nickname = username;

        requestArray = new SparseArray<>();

        //用户登录
        WsLoginRequest loginRequest = new WsLoginRequest();
        loginRequest.setMessageType(SocketConstants.EVENT_LOGIN);
        loginRequest.setData(new WsLoginRequest.WsLoginRequestData());
        requestArray.put(REQ_LOGIN, loginRequest);

        //用户登出
        WsLogoutRequest logoutRequest = new WsLogoutRequest();
        logoutRequest.setMessageType(SocketConstants.EVENT_LOGOUT);
        logoutRequest.setData(new WsLogoutRequest.WsLogoutRequestData());
        requestArray.put(REQ_LOGOUT, logoutRequest);

        //发送弹幕公共消息
        WsPublicMsgRequest pubMsgRequest = new WsPublicMsgRequest();
        pubMsgRequest.setMessageType(SocketConstants.EVENT_PUB_MSG);
        pubMsgRequest.setData(new WsPublicMsgRequest.WsPublicMsgRequestData());
        requestArray.put(REQ_SEND_PUB_MSG, pubMsgRequest);

        //直播心跳
        WsPongRequest pongRequest = new WsPongRequest();
        pongRequest.setMessageType(SocketConstants.EVENT_PONG);
        pongRequest.setData(new WsPongRequest.WsPongRequestData());
        requestArray.put(REQ_HEARTBEAT, pongRequest);

        //申请连麦
        ApplyMicBean applyMicBeanRequest = new ApplyMicBean();
        applyMicBeanRequest.setMessageType(SocketConstants.APPLY_MIC_REQUEST);
        applyMicBeanRequest.setData(new ApplyMicBean.ApplyMicBeanData());
        requestArray.put(APPLY_MIC_REQUEST, applyMicBeanRequest);

        //连麦响应
//        ApplyMicRsqBean applyMicBeanResponse = new ApplyMicRsqBean();
//        applyMicBeanResponse.setMessageType(SocketConstants.APPLY_MIC_REQUEST_REQ);
//        applyMicBeanResponse.setData(new ApplyMicRsqBean.ApplyMicRsqBeanData());
//        requestArray.put(APPLY_MIC_RESPONE, applyMicBeanResponse);

        //发送礼物
        WsSendGiftRequest sendGiftRequest = new WsSendGiftRequest();
        sendGiftRequest.setMessageType(SocketConstants.EVENT_SEND_GIFT);
        sendGiftRequest.setData(new WsSendGiftRequest.WsSendGiftRequestData());
        requestArray.put(REQ_SENDGIFT, sendGiftRequest);

        //断开连麦
        DisConnectLmRequest disConnectLmRequest = new DisConnectLmRequest();
        disConnectLmRequest.setMessageType(SocketConstants.DISCONNECT_LM_REQUEST);
        disConnectLmRequest.setData(new DisConnectLmRequest.DisConnectLmRequestData());
        requestArray.put(REQ_DISLM, disConnectLmRequest);

        //禁言
        GagReqRequest gagReqRequest = new GagReqRequest();
        gagReqRequest.setMessageType(SocketConstants.GAG_REQ_REQUEST);
        gagReqRequest.setData(new GagReqRequest.Data());
        requestArray.put(REQ_GAG, gagReqRequest);

        //拉黑
        BlackListReq blackListReq = new BlackListReq();
        blackListReq.setMessageType(SocketConstants.BLACK_LIST_REQ);
        blackListReq.setData(new BlackListReq.Data());
        requestArray.put(REQ_BLACK_LIST, blackListReq);

        //连麦响应新
        LmAgreeOrRefuseReq lmAgreeOrRefuseReq = new LmAgreeOrRefuseReq();
        lmAgreeOrRefuseReq.setMessageType(SocketConstants.LM_AGREE_OR_REFUSE_REQ);
        lmAgreeOrRefuseReq.setData(new LmAgreeOrRefuseReq.Data());
        requestArray.put(REQ_LM_AGREE, lmAgreeOrRefuseReq);

        //副播断开连麦
        CloseCallSecondaryReq closeCallSecondaryReq = new CloseCallSecondaryReq();
        closeCallSecondaryReq.setMessageType(SocketConstants.CLOSE_CALL_SECONDARY_REQ);
        closeCallSecondaryReq.setData(new CloseCallSecondaryReq.Data());
        requestArray.put(REQ_CLOSE_CALL_SECONDARY, closeCallSecondaryReq);
    }

    private static void checkInitOrThrow(Context context) {
        if (!tryRestorePoolFromLocal(context)) {
            throw new IllegalStateException("Pool not initialized correctly and cannot be " +
                    "restored!");
        }
    }

    /**
     * 在检测到对象池未初始化时执行的最后的恢复操作，如果能从本地恢复则可以避免抛出异常。
     *
     * @return 如果成功从本地存储的登录信息中恢复则返回true，否则返回false。
     */
    private static boolean tryRestorePoolFromLocal(Context context) {
        LoginInfo loginInfo = DataManager.getInstance().getLoginInfo();
        L.d(LOG_TAG, "Trying to restore ws object pool: login info=%s.", loginInfo);
        if (loginInfo != null) {
            init(context, loginInfo);
            L.i(LOG_TAG, "Ws object pool has been restored successfully.");
        } else {
            loginInfo = new LoginInfo();
            init(context, loginInfo);
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    //Assume type safe
    private static <T> T get(int index) {
        return (T) requestArray.get(index);
    }

    public static WsLoginRequest newLoginRequest(Context context, String roomId, String userId, String avatar, String nickname, String level, String role, String income) {
        checkInitOrThrow(context);
        UserInfo userInfo = DataManager.getInstance().getmUserInfo();
        String username = userInfo.getNickName();
        String userAvatar = userInfo.getAvatar();
        String balance = userInfo.getBalance();
        WsLoginRequest request = get(REQ_LOGIN);
        request.getData().setRoomId(roomId);
        request.getData().setUserId(userInfo.getId());
        request.getData().setNickName(TextUtils.isEmpty(username) ? "昵称" : username);
        request.getData().setAvatar(userAvatar);
        request.getData().setMessage(SocketConstants.EVENT_LOGIN);
        request.getData().setRole(Integer.parseInt(role));
        request.getData().setLevel(Integer.parseInt(userInfo.getLevel()));
        request.getData().setMasterUserId(Integer.parseInt(userId));
        request.getData().setMasterAvatar(TextUtils.isEmpty(avatar) ? "http://3tdoc.oss-cn-beijing.aliyuncs.com/wechat/avatar/8.jpg" : avatar);
        request.getData().setMasterNickName(TextUtils.isEmpty(nickname) ? "主播昵称" : nickname);
        request.getData().setMasterLevel(TextUtils.isEmpty(level) ? 1 : Integer.parseInt(level));
        request.getData().setBalance(balance);
        request.getData().setIncome(income);
        return request;
    }

    public static WsLogoutRequest newLogoutRequest(Context context, String anchorId, String id, String avatar, String nickName, String level, String websocketRoleHost, String roomId) {
        checkInitOrThrow(context);
        WsLogoutRequest request = get(REQ_LOGOUT);
        request.getData().setRoomId(Integer.parseInt(anchorId));
        request.getData().setUserId(Integer.parseInt(id));
        request.getData().setAvatar(avatar);
        request.getData().setNickName(TextUtils.isEmpty(nickName) ? "昵称" : nickName);
        request.getData().setLevel(TextUtils.isEmpty(level) ? "1" : level);
        request.getData().setIsMaster(Integer.parseInt(websocketRoleHost));
        request.getData().setMessage(roomId);
        return request;
    }

    public static WsPublicMsgRequest newPublicMsgRequest(Context context, String anchorId, String content, String flymsg) {
        checkInitOrThrow(context);

        UserInfo userInfo = DataManager.getInstance().getmUserInfo();
        String userId = userInfo.getId();
        String nickName = userInfo.getNickName();
        String avatar = userInfo.getAvatar();
        Log.e("WsPublicMsgRequest", userId + "==userId ," + nickName + "==nickName ," +avatar + "==avatar");
        WsPublicMsgRequest request = get(REQ_SEND_PUB_MSG);
        request.getData().setRoomId(anchorId);
        request.getData().setUserId(userId);
        request.getData().setNickName(TextUtils.isEmpty(nickName) ? "昵称" : nickName);
        request.getData().setAvatar(TextUtils.isEmpty(avatar) ? "http://3tdoc.oss-cn-beijing.aliyuncs.com/wechat/avatar/8.jpg" : avatar);
        request.getData().setMessage(content);
        request.getData().setFly(flymsg);
        return request;
    }

    /**
     * 申请连麦
     *
     * @param hostId   房间号,主播ID
     * @param id       申请连麦人的id
     * @param nickname @return
     */
    public static ApplyMicBean newApplyMicRequest(Context context, String liveId, String hostId, String id, String nickname, String avatar) {
        checkInitOrThrow(context);
        ApplyMicBean request = get(APPLY_MIC_REQUEST);
        request.getData().setRoomId(liveId);
        request.getData().setAdminUserId(hostId);
        request.getData().setUserId(id);
        request.getData().setNickName(nickname);
        request.getData().setAvatar(avatar);
        return request;
    }

    /**
     * 响应连麦
     *
     * @param anchorId
     * @param userId   对方的userid
     * @param response 是否同意连麦  1：同意  0 不同意
     * @return
     */
//    @Deprecated
//    public static ApplyMicRsqBean newApplyMicResposnse(Context context, String liveId, String anchorId, String userId, String response) {
//        checkInitOrThrow(context);
//        ApplyMicRsqBean request = get(APPLY_MIC_RESPONE);
//        request.getData().setRoomId(liveId);
//        request.getData().setUserId(userId);
//        request.getData().setType(response);
//        return request;
//    }

    /**
     * 直播心跳
     *
     * @param roomId
     * @param userId
     * @param isMaster
     * @return
     */
    public static WsPongRequest newPongRequest(Context context, String roomId, String userId,
                                               String isMaster, String streamId) {
        checkInitOrThrow(context);
        WsPongRequest request = get(REQ_HEARTBEAT);
        request.getData().setRoomId(roomId);
        request.getData().setUserId(userId);
        request.getData().setIsMaster(isMaster);
        request.getData().setStreamId(streamId);
        return request;
    }

    /**
     * 发送礼物
     *
     * @param anchorId
     * @param id
     * @param price
     * @param selectedGiftId
     * @param finalCombo
     * @param nickName
     * @param avatar
     * @param level
     * @param giftName
     * @param giftSrc
     * @return
     */
    public static WsSendGiftRequest newSendGiftRequest(Context context, String roomId, String anchorId, String id, String price, String selectedGiftId, int finalCombo,
                                                       String nickName, String avatar, String level, String giftName, String giftSrc) {
        checkInitOrThrow(context);
        WsSendGiftRequest request = get(REQ_SENDGIFT);
        request.getData().setRoomId(roomId);
        request.getData().setUserId(id);
        request.getData().setUserIdTo(anchorId);
        request.getData().setGiftId(selectedGiftId);
        request.getData().setPrice(price);
        request.getData().setNum(finalCombo);
        request.getData().setNickName(nickName);
        request.getData().setAvatar(avatar);
        request.getData().setLevel(level);
        request.getData().setMessage(SocketConstants.EVENT_SEND_GIFT);
        request.getData().setGiftName(giftName);
        request.getData().setGiftImg(giftSrc);
        return request;
    }

    /**
     * 断开连麦
     *
     * @param roomId      房间ID
     * @param adminUserId 主播ID
     * @param userId      用户ID
     * @return
     */
    public static DisConnectLmRequest disConnectLmRequest(Context context, String roomId, String adminUserId, String userId) {
        checkInitOrThrow(context);
        DisConnectLmRequest request = get(REQ_DISLM);
        request.getData().setRoomId(roomId);
        request.getData().setAdminUserId(adminUserId);
        request.getData().setUserId(userId);
        return request;
    }


    /**
     * 禁言
     *
     * @param context
     * @param roomId
     * @param adminUserId
     * @param userId
     * @param avatar
     * @param nickname
     * @param level
     * @param expiry
     * @param message
     * @return
     */
    public static GagReqRequest newGagReqRequest(Context context, String roomId, String adminUserId,
                                                 String userId, String avatar, String nickname,
                                                 String level, String expiry, String message) {
        checkInitOrThrow(context);
        GagReqRequest request = get(REQ_GAG);
        request.getData().setRoomId(roomId);
        request.getData().setAdminUserId(adminUserId);
        request.getData().setUserId(userId);
        request.getData().setAvatar(avatar);
        request.getData().setNickName(nickname);
        request.getData().setLevel(level);
        request.getData().setExpiry(expiry);
        request.getData().setMessage(message);
        return request;
    }

    /**
     * 拉黑
     *
     * @param context
     * @param blackListUserId
     * @param roomId
     * @param userId
     * @return
     */
    public static BlackListReq newBlackListRequest(Context context, String blackListUserId,
                                                   String roomId, String userId) {
        checkInitOrThrow(context);
        BlackListReq request = get(REQ_BLACK_LIST);
        request.getData().setBlacklistUserId(blackListUserId);
        request.getData().setRoomId(roomId);
        request.getData().setUserId(userId);
        return request;
    }

    /**
     * 连麦响应
     *
     * @param context
     * @param adminUserId
     * @param roomId
     * @param userId
     * @param type
     * @return
     */
    public static LmAgreeOrRefuseReq newLmAgreeOrRefuseRequest(Context context, String adminUserId,
                                                               String roomId, String userId,
                                                               String type) {
        checkInitOrThrow(context);
        LmAgreeOrRefuseReq request = get(REQ_LM_AGREE);
        request.getData().setAdminUserId(adminUserId);
        request.getData().setRoomId(roomId);
        request.getData().setType(type);
        request.getData().setUserId(userId);
        return request;
    }

    /**
     * 副播断开连麦
     *
     * @param context
     * @param adminUserId
     * @param roomId
     * @param userId
     * @return
     */
    public static CloseCallSecondaryReq newCloseCallSecondaryRequest(Context context, String adminUserId,
                                                                     String roomId, String userId) {
        checkInitOrThrow(context);
        CloseCallSecondaryReq request = get(REQ_CLOSE_CALL_SECONDARY);
        request.getData().setAdminUserId(adminUserId);
        request.getData().setRoomId(roomId);
        request.getData().setUserId(userId);
        return request;
    }
}
