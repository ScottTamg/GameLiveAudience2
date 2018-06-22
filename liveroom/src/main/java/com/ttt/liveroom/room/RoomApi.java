package com.ttt.liveroom.room;

import com.ttt.liveroom.bean.BaseResponse;
import com.ttt.liveroom.bean.GetFriendBean;
import com.ttt.liveroom.bean.UserInfo;
import com.ttt.liveroom.bean.gift.Gift;
import com.ttt.liveroom.bean.room.ComplainOptionBean;
import com.ttt.liveroom.bean.room.LiveRoomEndInfo;
import com.ttt.liveroom.bean.room.NewestAuthorBean;
import com.ttt.liveroom.bean.room.RoomAdminInfo;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Iverson on 2017/1/5 下午2:40
 * 此类用于：关于直播的api
 */

public interface RoomApi {

    @GET("/gift/list")
    Observable<BaseResponse<List<Gift>>> getAvailableGifts(@Query("userId") String userId);

    @FormUrlEncoded
    @POST("/api/gift.php")
    Observable<BaseResponse<Object>> sendGift(@Field("token") String author, @Field("to_uid") String toUserId,
                                              @Field("gift_id") String giftId,
                                              @Field("count") String count);

    @FormUrlEncoded
    @POST("/follow/attention")
    Observable<BaseResponse<Object>> starUser(@Field("userId") String userId,
                                              @Field("userIdFollow") String userIdFollow);

    @FormUrlEncoded
    @POST("/follow/cancel-attention")
    Observable<BaseResponse<Object>> unStarUser(
            @Field("userId") String userId,
            @Field("userIdFollow") String userIdFollow);

    @GET("/user/profile")
    Observable<BaseResponse<UserInfo>> getUserInfo(@Query("userId") String id, @Query("observerUserId") String caller_uid);

    @GET("/OpenAPI/v1/Friend/getfriend")
    Observable<BaseResponse<List<GetFriendBean>>> getFriendList();


    @POST("/OpenAPI/v1/Room/getAdmin")
    Observable<BaseResponse<List<RoomAdminInfo>>> getAdmin(@Query("uid") String uid);

    @GET("/OpenAPI/v1/User/setHit")
    Observable<BaseResponse<Object>> setHit(@Query("token") String token,
                                            @Query("hituid") String title);

    @GET("/OpenAPI/v1/User/removeHit")
    Observable<BaseResponse<Object>> removeHit(@Query("token") String token,
                                               @Query("hituid") String title);

    //观看直播
    @FormUrlEncoded
    @POST("/OpenAPI/v1/anchor/watchlive")
    Observable<BaseResponse<Object>> watchLive(@Field("token") String token, @Field("uid") String uid, @Field("roomid") String roomid);

    //结束观看直播
    @FormUrlEncoded
    @POST("/OpenAPI/v1/anchor/stopwatchlive")
    Observable<BaseResponse<Object>> stopWatchLive(@Field("token") String token, @Field("uid") String uid);


    @FormUrlEncoded
    @POST("/OpenAPI/v1/room/entryOfflineroom")
    Observable<BaseResponse<LiveRoomEndInfo>> getLiveRoomInfo(@Field("roomnum") String roomId, @Field("id") String id, @Field("caller_uid") String caller_uid);

    @FormUrlEncoded
    @POST("/OpenAPI/v1/anchor/complaint")
    Observable<BaseResponse<String>> complaint(@Field("uid") String uid, @Field("roomid") String roomid, @Field("remark") String remark);

    @POST("/OpenAPI/v1/anchor/heartbeat")
    Observable<BaseResponse<Object>> keepLiveStatus(@Query("token") String token);

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

    /**
     * 获取最新主播列表
     *
     * @param page
     * @param size
     * @param userId
     * @return
     */
    @GET("/live/latest")
    Observable<BaseResponse<NewestAuthorBean>> getNewestByAuthor(@Query("page") String page, @Query("size") String size, @Query("userId") String userId);
}
