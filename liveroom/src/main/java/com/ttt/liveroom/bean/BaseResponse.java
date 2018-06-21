package com.ttt.liveroom.bean;

/**
 * Created by Iverson on 2016/12/23 下午8:59
 * 此类用于：返回数据的基类
 */

public class BaseResponse<DataType> {
    public static final boolean RESULT_CODE_SUCCESS = true;
    public static final boolean RESULT_CODE_TOKEN_EXPIRED = false;

    /**
     * 通用返回值属性
     */
    private String code;
    /**
     * 通用返回信息。
     */
    private String message;
    /**
     * 具体的内容。
     */
    private DataType data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataType getData() {
        return data;
    }

    public void setData(DataType data) {
        this.data = data;
    }

    public String getMsg() {
        return message;
    }

    public void setMsg(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", msg='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
