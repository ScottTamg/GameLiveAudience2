package com.ttt.liveroom.room;

import com.ttt.liveroom.bean.BaseResponse;
import com.ttt.liveroom.bean.PublishRoomIdBean;
import com.ttt.liveroom.bean.UserInfo;
import com.ttt.liveroom.bean.WebSocketInfoBean;
import com.ttt.liveroom.bean.room.ComplainOptionBean;
import com.ttt.liveroom.bean.room.LiveRoomEndInfo;
import com.ttt.liveroom.bean.room.RoomAdminInfo;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by mrliu on 2018/1/8.
 * 此类用于:
 */

public interface PublishApi {

    /**
     * 主播开始直播
     */
    @FormUrlEncoded
    @POST("/live/start-live")
    Observable<BaseResponse<PublishRoomIdBean>> setLiveStatus(@Field("userId") String userId,
                                                              @Field("remark") String remark,
                                                              @Field("imgSrc") String imgSrc,
                                                              @Field("title") String title,
                                                              @Field("roomId") String roomId,
                                                              @Field("latitude") String latitude,
                                                              @Field("longitude") String longitude,
                                                              @Field("type")String type);

    /**
     * 主播开始关闭直播
     *
     * @param liveId
     * @param userId
     * @param observerUserId
     * @return
     */
    @FormUrlEncoded
    @POST("/live/termination-live")
    Observable<BaseResponse<LiveRoomEndInfo>> closeLive(@Field("streamId") String liveId, @Field("userId") String userId, @Field("observerUserId") String observerUserId);

    /**
     * @param roomId
     * @return
     */
    @GET("server/location")
    Observable<BaseResponse<WebSocketInfoBean>> getWebSocket(@Query("roomId") String roomId);

    @FormUrlEncoded
    @POST("/follow/attention")
    Observable<BaseResponse<Object>> starUser(@Field("userId") String userId,
                                              @Field("userIdFollow") String userIdFollow);

    @FormUrlEncoded
    @POST("/follow/cancel-attention")
    Observable<BaseResponse<Object>> unStarUser(
            @Field("userId") String userId,
            @Field("userIdFollow") String userIdFollow);

    @POST("/OpenAPI/v1/Room/getAdmin")
    Observable<BaseResponse<List<RoomAdminInfo>>> getAdmin(@Query("uid") String uid);


    @GET("/user/profile")
    Observable<BaseResponse<UserInfo>> getUserInfo(@Query("userId") String id, @Query("observerUserId") String caller_uid);

    /**
     * 拉黑
     *
     * @param userId
     * @param addBlackUserId
     * @return
     */
    @FormUrlEncoded
    @POST("/blacklist/blacklist")
    Observable<BaseResponse<Object>> addBlackList(@Field("userId") String userId, @Field("blacklistUserId") String addBlackUserId);

    /**
     * 举报
     */
    @FormUrlEncoded
    @POST("/report/report")
    Observable<BaseResponse<Object>> complain(@Field("userId") String userId, @Field("reportedUserId") String reportedUserId,
                                              @Field("roomId") String roomId, @Field("content") String content);

    /**
     * 获取举报选项
     */
    @GET("/report/options")
    Observable<BaseResponse<ComplainOptionBean>> getComplainOption(@Query("page") String page, @Query("size") String size);

}
